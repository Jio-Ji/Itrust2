package edu.ncsu.csc.iTrust2.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

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
    private String        firstName;

    /**
     * The middle name of this patient advocate
     */
    @Length ( min = 1 )
    private String        middleName;

    /**
     * The last name of this patient advocate
     */
    @Length ( min = 1 )
    private String        lastName;

    /**
     * The preferred name of this patient advocate
     */
    @Length ( max = 20 )
    private String        preferredName;

    /**
     * The email address of this patient advocate
     */
    @Length ( max = 30 )
    private String        email;

    /**
     * The phone number of this patient advocate
     */
    @Length ( min = 12, max = 12 )
    private String        phone;

    /**
     * The address line 1 of this patient advocate
     */
    @Length ( max = 50 )
    private String        address1;

    /**
     * The address line 2 of this patient advocate
     */
    @Length ( max = 50 )
    private String        address2;

    /**
     * The city of residence of this patient advocate
     */
    @Length ( max = 15 )
    private String        city;

    /**
     * The state of residence of this patient advocate
     */
    @Enumerated ( EnumType.STRING )
    private State         state;

    /**
     * The zip code of this patient advocate
     */
    @Length ( min = 5, max = 10 )
    private String        zip;

    /**
     * Patients associated with the Patient Advocate
     */
    @OneToMany
    private List<Patient> patients;

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
                    "Attempted to create a Patient Advocate record for a non-Patient Advocate user!" );
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
        setFirstName( form.getFirstName() );
        setPreferredName( form.getPreferredName() );
        setLastName( form.getLastName() );
        setEmail( form.getEmail() );
        setPhone( form.getPhone() );
        setAddress1( form.getAddress1() );
        setAddress2( form.getAddress2() );
        setCity( form.getCity() );
        setState( State.parse( form.getState() ) );
        setZip( form.getZip() );

        setPatients( form.getPatients() );

        return this;
    }

    public String getFirstName () {
        return firstName;
    }

    public void setFirstName ( final String firstName ) {
        this.firstName = firstName;
    }

    public String getMiddleName () {
        return middleName;
    }

    public void setMiddleName ( final String middleName ) {
        this.middleName = middleName;
    }

    public String getLastName () {
        return lastName;
    }

    public void setLastName ( final String lastName ) {
        this.lastName = lastName;
    }

    public String getPreferredName () {
        return preferredName;
    }

    public void setPreferredName ( final String preferredName ) {
        this.preferredName = preferredName;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail ( final String email ) {
        this.email = email;
    }

    public String getPhone () {
        return phone;
    }

    public void setPhone ( final String phone ) {
        this.phone = phone;
    }

    public String getAddress1 () {
        return address1;
    }

    public void setAddress1 ( final String address1 ) {
        this.address1 = address1;
    }

    public String getAddress2 () {
        return address2;
    }

    public void setAddress2 ( final String address2 ) {
        this.address2 = address2;
    }

    public String getCity () {
        return city;
    }

    public void setCity ( final String city ) {
        this.city = city;
    }

    public State getState () {
        return state;
    }

    public void setState ( final State state ) {
        this.state = state;
    }

    public String getZip () {
        return zip;
    }

    public void setZip ( final String zip ) {
        this.zip = zip;
    }

    public List getPatients () {
        return patients;
    }

    public void setPatients ( final List patients ) {
        this.patients = patients;
    }

}
