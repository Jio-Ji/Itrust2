package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.PatientAdvocateForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.PatientAdvocate;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.PatientAdvocateService;
import edu.ncsu.csc.iTrust2.services.PatientService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * Controller responsible for providing various REST API endpoints for the
 * Patient Advocate model.
 *
 * @author Qichen Wang
 *
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APIPatientAdvocateController extends APIController {
    /**
     * LoggerUtil
     */
    @Autowired
    private LoggerUtil             loggerUtil;
    /**
     * The patient advocate service
     */
    @Autowired
    private PatientAdvocateService patientAdvocateService;
    /**
     * The patient service
     */
    @Autowired
    private PatientService         patientService;
    /** User service */
    @Autowired
    private UserService<User>      userService;

    /**
     * Retrieves and returns a list of all Patient Advocates stored in the
     * system
     *
     * @return list of patient advocates
     */
    @GetMapping ( BASE_PATH + "/pas" )
    public List<PatientAdvocate> getPas () {
        final List<PatientAdvocate> pas = patientAdvocateService.findAll();
        return pas;
    }

    /**
     * Retrieves and returns a list of all Patient Advocates associated with a
     * patient
     *
     * @return list of patient advocates
     */
    @GetMapping ( BASE_PATH + "/pas/mypas" )
    public List<PatientAdvocate> getMyPas () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        final List<PatientAdvocate> pas = patientAdvocateService.findAll();
        final Patient pat = (Patient) self;
        return pat.getPatientAdvocates();
    }

    /**
     * Updates the Patient Advocate with the id provided by overwriting it with
     * the new Patient Advocate record that is provided. If the ID provided does
     * not match the ID set in the Patient Advocate provided, the update will
     * not take place
     *
     * @param id
     *            The username of the Patient Advocate to be updated
     * @param patientF
     *            The updated Patient Advocate to save
     * @return response
     */
    @PutMapping ( BASE_PATH + "/pas/{id}" )
    public ResponseEntity updatePa ( @PathVariable final String id, @RequestBody final PatientAdvocateForm patientF ) {
        boolean userEdit = false;
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final SimpleGrantedAuthority admin = new SimpleGrantedAuthority( "ROLE_ADMIN" );
        final SimpleGrantedAuthority pa = new SimpleGrantedAuthority( "ROLE_PATIENT" );

        try {
            userEdit = !auth.getAuthorities().contains( admin ) && !auth.getAuthorities().contains( pa );
            if ( !auth.getName().equals( id ) && userEdit ) {
                return new ResponseEntity( errorResponse( "You do not have permission to edit this record" ),
                        HttpStatus.UNAUTHORIZED );
            }

        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }

        try {
            final PatientAdvocate patient = (PatientAdvocate) patientAdvocateService.findByName( id );

            // Shouldn't be possible but let's check anyways
            if ( null == patient ) {
                return new ResponseEntity( errorResponse( "Patient Advocate not found" ), HttpStatus.NOT_FOUND );
            }

            patient.update( patientF );

            final User dbPatient = patientAdvocateService.findByName( id );
            if ( null == dbPatient ) {
                return new ResponseEntity( errorResponse( "No Patient Advocate found for id " + id ),
                        HttpStatus.NOT_FOUND );
            }
            patientAdvocateService.save( patient );

            if ( userEdit ) {
                loggerUtil.log( TransactionType.EDIT_DEMOGRAPHICS, LoggerUtil.currentUser(),
                        "User with username " + patient.getUsername() + "updated their demographics" );
            }
            else {
                loggerUtil.log( TransactionType.EDIT_DEMOGRAPHICS, LoggerUtil.currentUser(), patient.getUsername(),
                        "Admin edited demographics for patient advocate with username " + patient.getUsername() );
            }
            return new ResponseEntity( patient, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not update " + patientF.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Get PA and patients then add them to their own associate List
     *
     * @param patA
     *            patient advocate user name
     * @param p
     *            patient user name
     * @return response
     */
    @PutMapping ( BASE_PATH + "/pas/associate/{patA}/{p}" )
    public ResponseEntity associatePa ( @PathVariable final String patA, @PathVariable final String p ) {

        boolean userEdit = false;
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final SimpleGrantedAuthority admin = new SimpleGrantedAuthority( "ROLE_ADMIN" );
        try {
            userEdit = !auth.getAuthorities().contains( admin );
            if ( !auth.getName().equals( patA ) && userEdit ) {
                return new ResponseEntity( errorResponse( "You do not have permission to edit this record" ),
                        HttpStatus.UNAUTHORIZED );
            }

        }
        catch ( final Exception e ) {
            return new ResponseEntity( HttpStatus.UNAUTHORIZED );
        }

        final PatientAdvocate patientAdvocate = (PatientAdvocate) patientAdvocateService.findByName( patA );
        if ( null == patientAdvocate ) {
            return new ResponseEntity( errorResponse( "Patient Advocate not found" ), HttpStatus.NOT_FOUND );
        }

        final Patient patient = (Patient) patientService.findByName( p );
        if ( null == patient ) {
            return new ResponseEntity( errorResponse( "Patient not found" ), HttpStatus.NOT_FOUND );
        }

        List<Patient> pat = patientAdvocate.getPatients();
        if ( pat == null ) {
            pat = new ArrayList<Patient>();
        }
        pat.add( patient );
        patientAdvocate.setPatients( pat );
        patientAdvocateService.save( patientAdvocate );

        List<PatientAdvocate> pa;
        pa = patient.getPatientAdvocates();

        if ( pa == null ) {
            pa = new ArrayList<PatientAdvocate>();
        }

        pa.add( patientAdvocate );
        patient.setPatientAdvocates( pa );
        patientService.save( patient );

        return new ResponseEntity( HttpStatus.OK );

    }

}
