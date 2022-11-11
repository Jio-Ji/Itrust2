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
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.PatientAdvocateService;
import edu.ncsu.csc.iTrust2.services.PatientService;

/**
 * Tests the patient model
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
@ActiveProfiles ( { "test" } )
public class PatientAdvocateTest {

    @Autowired
    private PatientService<Patient>                 patientService;

    @Autowired
    private PatientAdvocateService<PatientAdvocate> patientAdvocateService;

    private static final String                     USER_1 = "demoTestUser1";

    private static final String                     USER_2 = "demoTestUser2";

    private static final String                     USER_3 = "demoTestUser3";

    private static final String                     PW     = "123456";

    @BeforeEach
    public void setup () {
        patientService.deleteAll();
        patientAdvocateService.deleteAll();
    }

    /**
     * Tests creating a patient
     */
    @Test
    @Transactional
    public void testCreatePatientAdvocateRecord () {

        Assertions.assertEquals( 0, patientService.count(), "There should be no Patient records in the system!" );
        Assertions.assertEquals( 0, patientAdvocateService.count(),
                "There should be no Patient Advocate records in the system!" );

        final PatientAdvocate pa1 = new PatientAdvocate( new UserForm( USER_1, PW, Role.ROLE_PA, 1 ) );

        patientAdvocateService.save( pa1 );

        final List<PatientAdvocate> savedRecords = patientAdvocateService.findAll();

        Assertions.assertEquals( 1, savedRecords.size(),
                "Creating a Patient Advocate record should results in its creation in the DB" );

        Assertions.assertEquals( USER_1, savedRecords.get( 0 ).getUsername(),
                "Creating a Patient Advocate record should results in its creation in the DB" );

        final Patient p1 = new Patient( new UserForm( USER_2, PW, Role.ROLE_PATIENT, 1 ) );
        patientService.save( p1 );

        final List<Patient> patientList = new ArrayList<Patient>();
        Assertions.assertEquals( 0, patientList.size(),
                "A newly created Patient Advocate record should have 0 patients" );
        patientList.add( p1 );

        pa1.setFirstName( "Karl" );
        pa1.setLastName( "Liebknecht" );
        pa1.setPatients( patientList );
        patientAdvocateService.save( pa1 );

        final User userRecord = patientAdvocateService.findByName( USER_1 );

        Assertions.assertEquals( USER_1, userRecord.getUsername() );

        Assertions.assertEquals( 1, pa1.getPatients().size() );

        Assertions.assertEquals( PatientAdvocate.class, userRecord.getClass() );

        final PatientAdvocate retrieved = (PatientAdvocate) userRecord;

        Assertions.assertEquals( "Karl", retrieved.getFirstName() );

        try {
            final PatientAdvocate pa2 = new PatientAdvocate( new UserForm( USER_3, PW, Role.ROLE_ADMIN, 1 ) );
            Assertions.assertNotNull( pa2 ); // Otherwise we get compilation
                                             // warnings
            Assertions.fail( "Should not be able to create a Patient from a non-Patient user" );
        }
        catch ( final Exception e ) {
            // expected
        }

    }
}
