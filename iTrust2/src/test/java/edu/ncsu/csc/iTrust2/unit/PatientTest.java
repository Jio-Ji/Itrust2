package edu.ncsu.csc.iTrust2.unit;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.PatientAdvocate;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.VaccineType;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.PatientAdvocateService;
import edu.ncsu.csc.iTrust2.services.PatientService;
import edu.ncsu.csc.iTrust2.services.VaccineTypeService;

/**
 * Tests the patient model
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
@ActiveProfiles ( { "test" } )
public class PatientTest {

    @Autowired
    private PatientService<Patient>                 service;

    @Autowired
    private PatientAdvocateService<PatientAdvocate> paService;

    @Autowired
    private VaccineTypeService                      s;

    private static final String                     USER_1 = "demoTestUser1";

    private static final String                     USER_2 = "demoTestUser2";

    private static final String                     PW     = "123456";

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Tests creating a patient
     */
    @Test
    @Transactional
    public void testCreatePatientRecord () {

        final VaccineType vacc = new VaccineType();

        vacc.setName( "testvac" );
        vacc.setNumDoses( 2 );
        vacc.setMaxAge( 100 );
        vacc.setMinAge( 12 );
        s.save( vacc );

        Assertions.assertEquals( 0, service.count(), "There should be no Patient records in the system!" );

        final Patient p1 = new Patient( new UserForm( USER_1, PW, Role.ROLE_PATIENT, 1 ) );

        service.save( p1 );

        final List<Patient> savedRecords = service.findAll();

        Assertions.assertEquals( 1, savedRecords.size(),
                "Creating a Patient record should results in its creation in the DB" );

        Assertions.assertEquals( USER_1, savedRecords.get( 0 ).getUsername(),
                "Creating a Patient record should results in its creation in the DB" );

        p1.setFirstName( "Karl" );
        p1.setLastName( "Liebknecht" );
        p1.setDoses( "1" );
        p1.setVaccineType( vacc );
        p1.setPatientAdvocates( new ArrayList<PatientAdvocate>() );
        service.save( p1 );

        final User userRecord = service.findByName( USER_1 );

        Assertions.assertEquals( USER_1, userRecord.getUsername() );

        Assertions.assertEquals( "1", p1.getDoses() );

        Assertions.assertEquals( "testvac", p1.getVaccineType().getName() );

        Assertions.assertEquals( Patient.class, userRecord.getClass() );

        Assertions.assertEquals( 0, p1.getPatientAdvocates().size() );

        final Patient retrieved = (Patient) userRecord;

        Assertions.assertEquals( "Karl", retrieved.getFirstName() );

        try {
            final Patient p2 = new Patient( new UserForm( USER_2, PW, Role.ROLE_ADMIN, 1 ) );
            Assertions.assertNotNull( p2 ); // Otherwise we get compilation
                                            // warnings
            Assertions.fail( "Should not be able to create a Patient from a non-Patient user" );
        }
        catch ( final Exception e ) {
            // expected
        }

    }

    /**
     * Tests editing the patient advocate
     */
    @Test
    @Transactional
    public void testEditPatientAdvocate () {
        final Patient p1 = new Patient( new UserForm( USER_1, PW, Role.ROLE_PATIENT, 1 ) );
        p1.setFirstName( "Karl" );
        p1.setLastName( "Liebknecht" );
        p1.setPatientAdvocates( new ArrayList<PatientAdvocate>() );
        final PatientAdvocate pa1 = new PatientAdvocate( new UserForm( USER_2, PW, Role.ROLE_PA, 1 ) );
        pa1.setPrescription( false );
        pa1.setBilling( false );
        pa1.setOfficeVisit( false );
        paService.save( pa1 );
        p1.getPatientAdvocates().add( pa1 );
        service.save( p1 );

        List<Patient> savedRecords = service.findAll();
        Assertions.assertEquals( 1, savedRecords.get( 0 ).getPatientAdvocates().size() );
        Assertions.assertEquals( USER_2, savedRecords.get( 0 ).getPatientAdvocates().get( 0 ).getId() );

        Assertions.assertFalse( savedRecords.get( 0 ).getPatientAdvocates().get( 0 ).isPrescription() );
        Assertions.assertFalse( savedRecords.get( 0 ).getPatientAdvocates().get( 0 ).isBilling() );
        Assertions.assertFalse( savedRecords.get( 0 ).getPatientAdvocates().get( 0 ).isOfficeVisit() );

        Assertions.assertTrue( p1.editPatientAdvocate( USER_2, true, false, true ) );

        paService.save( p1.getPatientAdvocates().get( 0 ) );
        service.save( p1 );
        savedRecords = service.findAll();

        Assertions.assertTrue( p1.getPatientAdvocates().get( 0 ).isPrescription() );
        Assertions.assertFalse( p1.getPatientAdvocates().get( 0 ).isBilling() );
        Assertions.assertTrue( p1.getPatientAdvocates().get( 0 ).isOfficeVisit() );

        Assertions.assertTrue( savedRecords.get( 0 ).getPatientAdvocates().get( 0 ).isPrescription() );
        Assertions.assertFalse( savedRecords.get( 0 ).getPatientAdvocates().get( 0 ).isBilling() );
        Assertions.assertTrue( savedRecords.get( 0 ).getPatientAdvocates().get( 0 ).isOfficeVisit() );
    }
}
