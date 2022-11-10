package edu.ncsu.csc.iTrust2.forms;

import java.util.List;

import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

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
    @OneToMany
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
     * Populate the patient form from a patient object
     *
     * @param patient
     *            the patient object to set the form with
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

    public String getUsername () {
        return username;
    }

    public void setUsername ( final String username ) {
        this.username = username;
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

    public String getState () {
        return state;
    }

    public void setState ( final String state ) {
        this.state = state;
    }

    public String getZip () {
        return zip;
    }

    public void setZip ( final String zip ) {
        this.zip = zip;
    }

    public List<Patient> getPatients () {
        return patients;
    }

    public void setPatients ( final List<Patient> patients ) {
        this.patients = patients;
    }

    /**
     * @return the prescription
     */
    public boolean isPrescription () {
        return prescription;
    }

    /**
     * @param prescription
     *            the prescription to set
     */
    public void setPrescription ( final boolean prescription ) {
        this.prescription = prescription;
    }

    /**
     * @return the billing
     */
    public boolean isBilling () {
        return billing;
    }

    /**
     * @param billing
     *            the billing to set
     */
    public void setBilling ( final boolean billing ) {
        this.billing = billing;
    }

    /**
     * @return the officeVisit
     */
    public boolean isOfficeVisit () {
        return officeVisit;
    }

    /**
     * @param officeVisit
     *            the officeVisit to set
     */
    public void setOfficeVisit ( final boolean officeVisit ) {
        this.officeVisit = officeVisit;
    }
}
