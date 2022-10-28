package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.CPTCodeForm;
import edu.ncsu.csc.iTrust2.models.CPTCode;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.CPTCodeService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * Controller responsible for providing various REST API endpoints for the
 * CPTCode model
 *
 * @author bswalia, mhyun
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APICPTCodeController extends APIController {
    /**
     * LoggerUtil
     */
    @Autowired
    private LoggerUtil     loggerUtil;
    /**
     * Service for CPT Code
     */
    @Autowired
    private CPTCodeService service;

    /**
     * Returns a list of all CPTCodes
     *
     * @return A list of all CPTCodes
     */
    @GetMapping ( BASE_PATH + "/cptcodes" )
    public List<CPTCode> getCPTCodes () {
        // log view event & return all instances stored by CPTCodeService
        loggerUtil.log( TransactionType.CPTCODE_VIEW, LoggerUtil.currentUser() );
        return service.findAll();
    }

    /**
     * Returns a specific CPTCode based on its hibernate id
     *
     * @param id
     *            A Long that hibernate gave the CPTCode for its id
     * @return ResponseEntity with either OK and the cptcode that was found or
     *         NOT_FOUND if it could not find a CPTCode with that id
     */
    @GetMapping ( BASE_PATH + "/cptcode/{id}" )
    public ResponseEntity getCPTCode ( @PathVariable final Long id ) {
        // find the CPTCode with the given ID
        final CPTCode cptcode = service.findById( id );
        // code NOT found or found code is NOT active? return NOT FOUND status
        if ( null == cptcode || !cptcode.getisActive() ) {
            return new ResponseEntity( errorResponse( "No CPTCode found" ), HttpStatus.NOT_FOUND );
        }
        // return ResponseEntity w/ ok status
        return new ResponseEntity( cptcode, HttpStatus.OK );
    }

    /**
     * Creates a CPTCode from the form passed in
     *
     * @param form
     *            A CPTCodeForm object that contains the information for the
     *            CPTCode
     * @return A ResponseEntity with OK and the cptcode if one was created and
     *         Conflict if there was already a code with that id
     */
    @PostMapping ( BASE_PATH + "/cptcode" )
    @PreAuthorize ( "hasAnyRole('ROLE_BSM')" )
    public ResponseEntity createCPTCode ( @RequestBody final CPTCodeForm form ) {
        try {
            // attempt to build a CPTCode from the given form
            final CPTCode cptcode = service.build( form );
            // try to find a pre-existing cptcode with the int code value from
            // the newly built CPTcode
            CPTCode code = service.findActiveByCode( cptcode.getCode() );
            if ( null != code ) {
                // if one is found, return BAD REQUEST entity, created CPTCodes
                // must have new/unique codes!
                return new ResponseEntity(
                        errorResponse( "A CPT code with the provided Code already exists & is active!" ),
                        HttpStatus.BAD_REQUEST );
            }
            /* Now look for inactive versions */
            code = service.findMostRecentByCode( cptcode.getCode() );
            final int maxVersion = null == code ? 1 : 1 + code.getVersion();
            cptcode.setVersion( maxVersion );
            cptcode.setisActive( true );
            service.save( cptcode );
            // log creation event!
            loggerUtil.log( TransactionType.CPTCODE_CREATE, LoggerUtil.currentUser() );
            // return success status
            return new ResponseEntity( cptcode, HttpStatus.OK );
        }
        // error catch all - send a bad request response
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the CPTCode provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * Edits a CPTCode with the information passed in
     *
     * @param id
     *            the id of the cptCode
     * @param form
     *            The form with the new CPTCode information
     * @return A ResponseEntity with Ok and the new code if it was updated,
     *         NOT_FOUND if no code was found with the given id, and CONFLICT if
     *         there was a conflict
     */
    @PutMapping ( BASE_PATH + "/cptcode/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_BSM')" )
    public ResponseEntity editCPTCode ( @PathVariable final Long id, @RequestBody final CPTCodeForm form ) {
        try {
            // replaces the CPTCode of given id to reflect values in given
            // forms, updating the pre
            // existing cptcode with that ID to be inactive

            // check for a CPTCode with ID given
            final CPTCode cptCode = service.findById( id );
            // none found? return error response! (not found)
            if ( cptCode == null ) {
                return new ResponseEntity( errorResponse( "No CPTCode found" ), HttpStatus.NOT_FOUND );
            }
            // if a CPTCode object w/ given ID WAS found..
            // make it inactive (will be replaced w/ updated CPTCode)
            cptCode.setisActive( false );
            // create a new CPTCode object based off the given form object
            final CPTCode newCode = service.build( form );
            // update the new CPTCOde object's version number
            newCode.setVersion( cptCode.getVersion() + 1 );
            // save newly inactive and newly active cptcodes
            service.save( newCode );
            service.save( cptCode );
            // log code edit event!
            loggerUtil.log( TransactionType.CPTCODE_EDIT, LoggerUtil.currentUser() );
            // return status OK
            return new ResponseEntity( newCode, HttpStatus.OK );
        }
        // error handling ... any errors result in a bad request being returned
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the CPTCode provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * deletes a CPTCode with the code passed in indicated the code that will be
     * deleted
     *
     * @param id
     *            The CPTCode's id used to indicate the code that will be
     *            deleted
     * @return ResponseEntity with OK if the code was deleted and NOT_FOUND if
     *         no code was found with that code
     */
    @DeleteMapping ( BASE_PATH + "/cptcode/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_BSM')" )
    public ResponseEntity deleteCPTCode ( @PathVariable ( "id" ) final Long id ) {
        // deleting a CPTCode equates to setting the status as inactive
        try {
            // find CPTCode with given ID
            final CPTCode cptCode = service.findById( id );
            // not found? return NOT FOUND response
            if ( cptCode == null ) {
                return new ResponseEntity( errorResponse( "No CPTCode found" ), HttpStatus.NOT_FOUND );
            }
            // else, set it as inactive and overrwrite it in database
            cptCode.setisActive( false );
            service.save( cptCode );
            // log delete event!
            loggerUtil.log( TransactionType.CPTCODE_DELETE, LoggerUtil.currentUser() );
            return new ResponseEntity( HttpStatus.OK );
        }
        // error handling...return Bad Request Response!
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or delete the CPTCode provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }

}
