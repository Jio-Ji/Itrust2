package edu.ncsu.csc.iTrust2.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.iTrust2.common.TestUtils;
import edu.ncsu.csc.iTrust2.forms.PatientAdvocateForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.PatientAdvocate;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.models.enums.State;
import edu.ncsu.csc.iTrust2.services.PatientAdvocateService;
import edu.ncsu.csc.iTrust2.services.PatientService;

/**
 * Test for API functionality for interacting with Patient Advocates
 *
 * @author Qichen Wang
 * @author Shaolong zhou
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles ( { "test" } )
public class APIPatientAdvocateTest {
    /**
     * The mvc
     */
    @Autowired
    private MockMvc                                 mvc;
    /**
     * The patient advocate service
     */
    @Autowired
    private PatientAdvocateService<PatientAdvocate> paService;
    /**
     * The patient service
     */
    @Autowired
    private PatientService<Patient>                 pService;

    /**
     * Sets up test
     */
    @BeforeEach
    public void setup () {

        paService.deleteAll();
        pService.deleteAll();
    }

    /**
     * Tests PatientAdvocateAPI
     *
     * @throws Exception
     *             if it does not run in a right way
     */
    @Test
    @WithMockUser ( username = "admin", roles = { "ADMIN" } )
    @Transactional
    public void testPatientAPI () throws Exception {

        final PatientAdvocateForm patient = new PatientAdvocateForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Some Location" );
        patient.setCity( "Viipuri" );
        patient.setEmail( "antti@itrust.fi" );
        patient.setMiddleName( "Zac" );
        patient.setFirstName( "Antti" );
        patient.setLastName( "Walhelm" );
        patient.setPhone( "123-456-7890" );
        patient.setUsername( "antti" );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        patient.setOfficeVisit( true );

        // Editing the patient before they exist should fail
        mvc.perform( put( "/api/v1/pas/antti" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isNotFound() );

        final PatientAdvocate antti = new PatientAdvocate( new UserForm( "antti", "123456", Role.ROLE_PA, 1 ) );

        paService.save( antti );

        // Should also now be able to edit existing record with new information
        mvc.perform( put( "/api/v1/pas/antti" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isOk() );

        PatientAdvocate anttiRetrieved = (PatientAdvocate) paService.findByName( "antti" );

        // Test a few fields to be reasonably confident things are working
        Assertions.assertEquals( "Walhelm", anttiRetrieved.getLastName() );
        Assertions.assertEquals( "Viipuri", anttiRetrieved.getCity() );

        // Also test a field we haven't set yet
        Assertions.assertNull( anttiRetrieved.getPreferredName() );

        patient.setPreferredName( "Antti Walhelm" );

        mvc.perform( put( "/api/v1/pas/antti" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().isOk() );

        anttiRetrieved = (PatientAdvocate) paService.findByName( "antti" );

        Assertions.assertNotNull( anttiRetrieved.getPreferredName() );

        // Editing with the wrong username should fail.
        mvc.perform( put( "/api/v1/pas/badusername" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) ).andExpect( status().is4xxClientError() );

    }

    /**
     * Test associate api method.
     *
     * @throws Exception
     *             if it does not run in a right way
     */
    @Test
    @WithMockUser ( username = "admin", roles = { "ADMIN" } )
    @Transactional
    public void testAssociateAPI () throws Exception {

        final PatientAdvocate antti = new PatientAdvocate( new UserForm( "antti", "123456", Role.ROLE_PA, 1 ) );

        final Patient antta = new Patient( new UserForm( "antta", "123456", Role.ROLE_PATIENT, 1 ) );

        paService.save( antti );
        pService.save( antta );

        // Should also now be able to edit existing record with new information
        mvc.perform(
                put( "/api/v1/pas/associate/antti/antta" ).with( csrf() ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final Patient pa1 = (Patient) pService.findByName( "antta" );

        Assertions.assertEquals( pa1.getPatientAdvocates().get( 0 ), antti );

    }
}
