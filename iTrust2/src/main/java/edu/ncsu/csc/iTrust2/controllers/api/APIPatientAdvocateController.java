package edu.ncsu.csc.iTrust2.controllers.api;

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
import edu.ncsu.csc.iTrust2.models.PatientAdvocate;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.PatientAdvocateService;
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
     * User Service
     */
    @Autowired
    private UserService            userService;

    /**
     * LoggerUtil
     */
    @Autowired
    private LoggerUtil             loggerUtil;

    @Autowired
    private PatientAdvocateService patientAdvocateService;

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
        try {
            userEdit = !auth.getAuthorities().contains( admin );
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
}
