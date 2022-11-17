package edu.ncsu.csc.iTrust2.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ncsu.csc.iTrust2.forms.PatientAdvocateForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.models.enums.State;

/**
 * Represents a Patient Advocate stored in the iTrust2 system.
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
    @ManyToMany
    @JsonIgnore
    private List<Patient> patients;

    /**
     * Whether or not the prescription is enabled
     */
    private boolean       prescription;

    /**
     * Whether or not the billing is enabled
     */
    private boolean       billing;

    /**
     * Whether or not the officeVisit is enabled
     */
    private boolean       officeVisit;

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
        setMiddleName( form.getMiddleName() );
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
        setPrescription( form.isPrescription() );
        setBilling( form.isBilling() );
        setOfficeVisit( form.isOfficeVisit() );
        return this;
    }

    /**
     * Get the FirstName
     *
     * @return the FirstName
     */
    public String getFirstName () {
        return firstName;
    }

    /**
     * Set the FirstName
     *
     * @param firstName
     *            the firstName
     */
    public void setFirstName ( final String firstName ) {
        this.firstName = firstName;
    }

    /**
     * Get the MiddleName
     *
     * @return the MiddleName
     */
    public String getMiddleName () {
        return middleName;
    }

    /**
     * Set the MiddleName
     *
     * @param middleName
     *            the middleName
     */
    public void setMiddleName ( final String middleName ) {
        this.middleName = middleName;
    }

    /**
     * Get the LastName
     *
     * @return the LastName
     */
    public String getLastName () {
        return lastName;
    }

    /**
     * Set the LastName
     *
     * @param lastName
     *            the lastName
     */
    public void setLastName ( final String lastName ) {
        this.lastName = lastName;
    }

    /**
     * Get the PreferredName
     *
     * @return the PreferredName
     */
    public String getPreferredName () {
        return preferredName;
    }

    /**
     * Set the PreferredName
     *
     * @param preferredName
     *            the preferredName
     */
    public void setPreferredName ( final String preferredName ) {
        this.preferredName = preferredName;
    }

    /**
     * Get the Email
     *
     * @return the Email
     */
    public String getEmail () {
        return email;
    }

    /**
     * Set the Email
     *
     * @param email
     *            the email
     */
    public void setEmail ( final String email ) {
        this.email = email;
    }

    /**
     * Get the Phone
     *
     * @return the Phone
     */
    public String getPhone () {
        return phone;
    }

    /**
     * Set the Phone
     *
     * @param phone
     *            the phone
     */
    public void setPhone ( final String phone ) {
        this.phone = phone;
    }

    /**
     * Get the Address1
     *
     * @return the Address1
     */
    public String getAddress1 () {
        return address1;
    }

    /**
     * Set the Address1
     *
     * @param address1
     *            the address1
     */
    public void setAddress1 ( final String address1 ) {
        this.address1 = address1;
    }

    /**
     * Get the Address2
     *
     * @return the Address2
     */
    public String getAddress2 () {
        return address2;
    }

    /**
     * Set the Address2
     *
     * @param address2
     *            the address2
     */
    public void setAddress2 ( final String address2 ) {
        this.address2 = address2;
    }

    /**
     * Get the City
     *
     * @return the City
     */
    public String getCity () {
        return city;
    }

    /**
     * Set the City
     *
     * @param city
     *            the city
     */
    public void setCity ( final String city ) {
        this.city = city;
    }

    /**
     * Get the State
     *
     * @return the State
     */
    public State getState () {
        return state;
    }

    /**
     * Set the State
     *
     * @param state
     *            the state
     */
    public void setState ( final State state ) {
        this.state = state;
    }

    /**
     * Get the zip
     *
     * @return the zip
     */
    public String getZip () {
        return zip;
    }

    /**
     * Set the zip
     *
     * @param zip
     *            the zip
     */
    public void setZip ( final String zip ) {
        this.zip = zip;
    }

    /**
     * Get the list of patients
     *
     * @return the list of patients
     */
    public List<Patient> getPatients () {
        return patients;
    }

    /**
     * Set the list of patients.
     *
     * @param patients
     *            list of patients
     */
    public void setPatients ( final List<Patient> patients ) {
        this.patients = patients;
    }

    /**
     * Get the boolean for prescription
     *
     * @return the prescription
     */
    public boolean isPrescription () {
        return prescription;
    }

    /**
     * Set the prescription
     *
     * @param prescription
     *            the prescription to set
     */
    public void setPrescription ( final boolean prescription ) {
        this.prescription = prescription;
    }

    /**
     * Get the boolean for billing
     *
     * @return the billing
     */
    public boolean isBilling () {
        return billing;
    }

    /**
     * Set the billing
     *
     * @param billing
     *            the billing to set
     */
    public void setBilling ( final boolean billing ) {
        this.billing = billing;
    }

    /**
     * Get the boolean for office visit
     *
     * @return the officeVisit
     */
    public boolean isOfficeVisit () {
        return officeVisit;
    }

    /**
     * Set the office visit.
     *
     * @param officeVisit
     *            the officeVisit to set
     */
    public void setOfficeVisit ( final boolean officeVisit ) {
        this.officeVisit = officeVisit;
    }

}
