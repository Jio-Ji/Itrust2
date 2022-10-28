package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.iTrust2.forms.PaymentForm;
import edu.ncsu.csc.iTrust2.forms.VaccineVisitForm;
import edu.ncsu.csc.iTrust2.models.Bill;
import edu.ncsu.csc.iTrust2.models.Payment;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.BillService;
import edu.ncsu.csc.iTrust2.services.PaymentService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * Bill API Controller
 *
 * @author bswalia and mhyun
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIBillController extends APIController {
    /**
     * LoggerUtil
     */
    @Autowired
    private LoggerUtil  loggerUtil;
    /**
     * Service for Bills
     */
    @Autowired
    private BillService billService;
    /**
     * Service for User
     */
    @Autowired
    private UserService userService;

    /**
     * Service for Payments
     */
    @Autowired
    PaymentService      paymentService;

    /**
     * Retrieves a list of all the Bills in the database
     *
     * @param username
     *            Username of the patient
     * @return list of bills
     */
    @GetMapping ( BASE_PATH + "/bills/{username}" )
    @PreAuthorize ( "hasAnyRole('ROLE_BSM')" )
    public List<Bill> getBills ( @PathVariable final String username ) {
        // get the patient with the current name then pass that to bill service
        final User patient = userService.findByName( username );
        // event log
        loggerUtil.log( TransactionType.BILLS_VIEWED, username );
        // return bills associated w retrieved patient
        return billService.findByPatient( patient );
    }

    /**
     * Retrieves a list of all OfficeVisits in the database for the current
     * patient
     *
     * @return list of office visits
     */
    @GetMapping ( BASE_PATH + "/bills/mybills" )
    @PreAuthorize ( "hasAnyRole('ROLE_PATIENT')" )
    public List<Bill> getMyBills () {
        final User self = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.BILLS_VIEWED, self );
        return billService.findByPatient( self );

    }

    /**
     * Edits a Bill with the passed in PaymentForm
     *
     * @param id
     *            The id of the Bill that is being edited
     * @param pay
     *            The Payment that was made to the Bill
     * @return ResponseEntity OK if the Bill was edited, NOT_FOUND if there was
     *         no Bill with that id, and BAD_REQUEST if there was an error
     */
    @PutMapping ( BASE_PATH + "/bills/{id}" )
    @PreAuthorize ( "hasAnyRole('ROLE_BSM')" )
    public ResponseEntity editBills ( @PathVariable final Long id, @RequestBody final PaymentForm pay ) {
        try {
            // look for the Bill w/ given ID
            final Bill b = billService.findById( id );
            // attempt to create a Payment object w/ given form
            final Payment p = new Payment( pay );

            // save payment object
            paymentService.save( p );
            // no bill found? return NOT FOUND
            if ( b == null ) {
                return new ResponseEntity( errorResponse( "No Bill found" ), HttpStatus.NOT_FOUND );
            }
            // bill found? update it with payment information
            b.updateBill( p );
            // save it
            billService.save( b );
            // log BILL PAY event
            // TODO is this the best logging label?
            loggerUtil.log( TransactionType.BILL_PAID, LoggerUtil.currentUser() );
            return new ResponseEntity( b, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            // return BAD REQUEST response for any other errors that occur
            // TODO: remove print? where does it go
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the Bill provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * Creates a Bill from an office visit form and then saves that Bill to a
     * Patient
     *
     * @param form
     *            The office visit form that contains all the necessary
     *            information to make a Bill
     * @return ResponseEntity 200 If the Bill was created, Bad_Request if there
     *         was an error
     */
    @PostMapping ( BASE_PATH + "bills/ov" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP')" )
    public ResponseEntity createBillOfficeVisit ( @RequestBody final OfficeVisitForm form ) {
        // set the HCP of the OfficeVisitForm to the current user, create a bill
        // associated with the form, then save it
        try {
            form.setHcp( LoggerUtil.currentUser() );
            final Bill b = billService.build( form );

            billService.save( b );
            // log bill creation event
            loggerUtil.log( TransactionType.BILL_CREATED_OV, LoggerUtil.currentUser() );
            return new ResponseEntity( b, HttpStatus.OK );
        }
        // any errors? return bad request.
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the Bill provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * Creates a Bill from an vaccine visit form and then saves that Bill to a
     * Patient
     *
     * @param form
     *            The vaccine visit form that contains all the necessary
     *            information to make a Bill
     * @return ResponseEntity 200 If the Bill was created, Bad_Request if there
     *         was an error
     */
    @PostMapping ( BASE_PATH + "bills/vv" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_VACCINATOR')" )
    public ResponseEntity createBillVaccineVisit ( @RequestBody final VaccineVisitForm form ) {
        // set the vaccinator of the VaccineVisitForm to the current user,
        // create a bill
        // associated with the form, then save it
        try {
            // set form vaccinator
            form.setVaccinator( LoggerUtil.currentUser() );
            final Bill b = billService.build( form );
            // save instance
            billService.save( b );
            // log bill creation!
            loggerUtil.log( TransactionType.BILL_CREATED_VV, LoggerUtil.currentUser() );
            return new ResponseEntity( b, HttpStatus.OK );
        }
        // any errors result in a bad request response
        catch ( final Exception e ) {
            e.printStackTrace();
            return new ResponseEntity(
                    errorResponse( "Could not validate or save the Bill provided due to " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

}
