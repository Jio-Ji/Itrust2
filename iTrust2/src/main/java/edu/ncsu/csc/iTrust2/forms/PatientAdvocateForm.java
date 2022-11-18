package edu.ncsu.csc.iTrust2.forms;

import java.util.List;

import javax.persistence.ManyToMany;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.PatientAdvocate;

/**
 * Form for user to fill out to add a Patient Advocate to the system.
 *
 * @author Austin Heyward
 */
public class PatientAdvocateForm {

    /**
     * For Spring
     */
    public PatientAdvocateForm () {

    }

    /** Username of this Patient **/
    @Length ( max = 20 )
    private String        username;

    /** The first name of the patient **/
    @Length ( max = 20 )
    private String        firstName;

    /** The middle name of the patient **/
    @Length ( max = 20 )
    private String        middleName;

    /** The last name of the patient **/
    @Length ( max = 30 )
    private String        lastName;

    /** The preferred name of the patient **/
    @Length ( max = 20 )
    private String        preferredName;

    /** The email of the patient **/
    @Length ( max = 30 )
    private String        email;

    /** The phone number of the patient **/
    @Pattern ( regexp = "(^[0-9]{3}-[0-9]{3}-[0-9]{4}$)", message = "Phone number must be formatted as xxx-xxx-xxxx" )
    private String        phone;

    /** The address line 1 of the patient **/
    @Length ( max = 50 )
    private String        address1;

    /** The address line 2 of the patient **/
    @Length ( max = 50 )
    private String        address2;

    /** The city of residence of the patient **/
    @Length ( max = 15 )
    private String        city;

    /** The state of residence of the patient **/
    @Length ( min = 2, max = 2 )
    private String        state;

    /** The zipcode of the patient **/
    @Length ( min = 5, max = 10 )
    private String        zip;

    /**
     * Patients associated with the Patient Advocates
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
     * Populate the patient advocate form from a patient advocate object
     *
     * @param patientAdvocate
     *            the patient advocate object to set the form with
     */
    public PatientAdvocateForm ( final PatientAdvocate patientAdvocate ) {
        if ( null == patientAdvocate ) {
            return; /* Nothing to do here */
        }
        setFirstName( patientAdvocate.getFirstName() );
        setMiddleName( patientAdvocate.getMiddleName() );
        setLastName( patientAdvocate.getLastName() );
        setPreferredName( patientAdvocate.getPreferredName() );
        setEmail( patientAdvocate.getEmail() );
        setPhone( patientAdvocate.getPhone() );
        setAddress1( patientAdvocate.getAddress1() );
        setAddress2( patientAdvocate.getAddress2() );
        setCity( patientAdvocate.getCity() );
        if ( null != patientAdvocate.getState() ) {
            setState( patientAdvocate.getState().toString() );
        }
        setZip( patientAdvocate.getZip() );

        if ( null != patientAdvocate.getPatients() ) {
            setPatients( patientAdvocate.getPatients() );
        }
        setPrescription( patientAdvocate.isPrescription() );
        setBilling( patientAdvocate.isBilling() );
        setOfficeVisit( patientAdvocate.isOfficeVisit() );
    }

    /**
     * Get the username
     *
     * @return the username
     */
    public String getUsername () {
        return username;
    }

    /**
     * Set the user name
     *
     * @param username
     *            the username
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * Get the first name
     *
     * @return the frist name
     */
    public String getFirstName () {
        return firstName;
    }

    /**
     * Set the first name
     *
     * @param firstName
     *            the firstname
     */
    public void setFirstName ( final String firstName ) {
        this.firstName = firstName;
    }

    /**
     * Get the middle name
     *
     * @return the middle name
     */
    public String getMiddleName () {
        return middleName;
    }

    /**
     * Set the middle name
     *
     * @param middleName
     *            the middle name
     */
    public void setMiddleName ( final String middleName ) {
        this.middleName = middleName;
    }

    /**
     * Get the last name
     *
     * @return the last name
     */
    public String getLastName () {
        return lastName;
    }

    /**
     * Set the last name
     *
     * @param lastName
     *            the last name
     */
    public void setLastName ( final String lastName ) {
        this.lastName = lastName;
    }

    /**
     * Get preferred name
     *
     * @return the preferred name
     */
    public String getPreferredName () {
        return preferredName;
    }

    /**
     * Set the preferred name
     *
     * @param preferredName
     *            the preferred name
     */
    public void setPreferredName ( final String preferredName ) {
        this.preferredName = preferredName;
    }

    /**
     * Get the email
     *
     * @return the email
     */
    public String getEmail () {
        return email;
    }

    /**
     * Set the email
     *
     * @param email
     *            the email
     */
    public void setEmail ( final String email ) {
        this.email = email;
    }

    /**
     * Get the phone
     *
     * @return the phone
     */
    public String getPhone () {
        return phone;
    }

    /**
     * Set the phone
     *
     * @param phone
     *            the phone
     */
    public void setPhone ( final String phone ) {
        this.phone = phone;
    }

    /**
     * Get the address1
     *
     * @return the address1
     */
    public String getAddress1 () {
        return address1;
    }

    /**
     * Set the address1
     *
     * @param address1
     *            the address1
     */
    public void setAddress1 ( final String address1 ) {
        this.address1 = address1;
    }

    /**
     * Get the address2
     *
     * @return the address2
     */
    public String getAddress2 () {
        return address2;
    }

    /**
     * Set the address2
     *
     * @param address2
     *            the address2
     */
    public void setAddress2 ( final String address2 ) {
        this.address2 = address2;
    }

    /**
     * Get the city
     *
     * @return the city
     */
    public String getCity () {
        return city;
    }

    /**
     * Set the city
     *
     * @param city
     *            the city
     */
    public void setCity ( final String city ) {
        this.city = city;
    }

    /**
     * Get the state
     *
     * @return the state
     */
    public String getState () {
        return state;
    }

    /**
     * Set the state
     *
     * @param state
     *            the state
     */
    public void setState ( final String state ) {
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
     * Set the list of patients
     *
     * @param patients
     *            the list of patients
     */
    public void setPatients ( final List<Patient> patients ) {
        this.patients = patients;
    }

    /**
     * Get the prescription
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
     * Get the billing
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
     * Get the office visit
     *
     * @return the officeVisit
     */
    public boolean isOfficeVisit () {
        return officeVisit;
    }

    /**
     * Set the office visit
     *
     * @param officeVisit
     *            the officeVisit to set
     */
    public void setOfficeVisit ( final boolean officeVisit ) {
        this.officeVisit = officeVisit;
    }
}
