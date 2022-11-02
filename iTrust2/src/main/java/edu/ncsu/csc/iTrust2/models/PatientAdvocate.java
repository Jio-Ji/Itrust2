package edu.ncsu.csc.iTrust2.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.iTrust2.forms.PatientAdvocateForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.models.enums.State;

/**
 * Represents a Patient Advocate stored in the iTrust2 system
 *
 * @author Austin Heyward
 * @author Nick Bleuzen
 *
 */
@Entity
public class PatientAdvocate extends User {

    /**
     * The first name of this patient advocate
     */
    @Length ( min = 1 )
    private String firstName;

    /**
     * The middle name of this patient advocate
     */
    @Length ( min = 1 )
    private String middleName;

    /**
     * The last name of this patient advocate
     */
    @Length ( min = 1 )
    private String lastName;

    /**
     * The preferred name of this patient advocate
     */
    @Length ( max = 20 )
    private String preferredName;

    /**
     * The email address of this patient advocate
     */
    @Length ( max = 30 )
    private String email;

    /**
     * The phone number of this patient advocate
     */
    @Length ( min = 12, max = 12 )
    private String phone;

    /**
     * The address line 1 of this patient advocate
     */
    @Length ( max = 50 )
    private String address1;

    /**
     * The address line 2 of this patient advocate
     */
    @Length ( max = 50 )
    private String address2;

    /**
     * The city of residence of this patient advocate
     */
    @Length ( max = 15 )
    private String city;

    /**
     * The state of residence of this patient advocate
     */
    @Enumerated ( EnumType.STRING )
    private State  state;

    /**
     * The zip code of this patient advocate
     */
    @Length ( min = 5, max = 10 )
    private String zip;

    /**
     * For Hibernate
     */
    public PatientAdvocate () {

    }

    /**
     * Creates a PatientAdvocate from the provided UserForm
     *
     * @param uf
     *            UserForm to build a PatientAdvocate from
     */
    public PatientAdvocate ( final UserForm uf ) {
        super( uf );
        if ( !getRoles().contains( Role.ROLE_PA ) ) {
            throw new IllegalArgumentException(
                    "Attempted to create a Patient Advocate record for a non-Patient user!" );
        }
    }

    /**
     * Updates this PatientAdvocate based on the provided UserForm
     *
     * @param form
     *            Form to update from
     * @return updated PatientAdvocate
     */
    public PatientAdvocate update ( final PatientAdvocateForm form ) {

    }
}
