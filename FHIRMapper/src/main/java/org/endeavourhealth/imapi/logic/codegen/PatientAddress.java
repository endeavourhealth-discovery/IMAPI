package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents patient address
* A standard mailing/postal address for a patient
*/
public class PatientAddress extends IMDMBase<PatientAddress> {


	/**
	* Patient address constructor 
	*/
	public PatientAddress() {
		super("PatientAddress");
	}

	/**
	* Patient address constructor with identifier
	*/
	public PatientAddress(String id) {
		super("PatientAddress", id);
	}

	/**
	* Gets the address line of this patient address
	* @return addressLine
	*/
	public String getAddressLine() {
		return getProperty("addressLine");
	}


	/**
	* Changes the address line of this PatientAddress
	* @param addressLine The new address line to set
	* @return PatientAddress
	*/
	public PatientAddress setAddressLine(String addressLine) {
		setProperty("addressLine", addressLine);
		return this;
	}


	/**
	* Gets the address use of this patient address
	* @return addressUse
	*/
	public String getAddressUse() {
		return getProperty("addressUse");
	}


	/**
	* Changes the address use of this PatientAddress
	* @param addressUse The new address use to set
	* @return PatientAddress
	*/
	public PatientAddress setAddressUse(String addressUse) {
		setProperty("addressUse", addressUse);
		return this;
	}


	/**
	* Gets the Locality of this patient address
	* @return locality
	*/
	public String getLocality() {
		return getProperty("locality");
	}


	/**
	* Changes the Locality of this PatientAddress
	* @param locality The new Locality to set
	* @return PatientAddress
	*/
	public PatientAddress setLocality(String locality) {
		setProperty("locality", locality);
		return this;
	}


	/**
	* Gets the patient of this patient address
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this PatientAddress
	* @param patient The new patient to set
	* @return PatientAddress
	*/
	public PatientAddress setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the Region of this patient address
	* @return region
	*/
	public String getRegion() {
		return getProperty("region");
	}


	/**
	* Changes the Region of this PatientAddress
	* @param region The new Region to set
	* @return PatientAddress
	*/
	public PatientAddress setRegion(String region) {
		setProperty("region", region);
		return this;
	}


	/**
	* Gets the start date and time of this patient address
	* @return startDateAndTime
	*/
	public PartialDateTime getStartDateAndTime() {
		return getProperty("startDateAndTime");
	}


	/**
	* Changes the start date and time of this PatientAddress
	* @param startDateAndTime The new start date and time to set
	* @return PatientAddress
	*/
	public PatientAddress setStartDateAndTime(PartialDateTime startDateAndTime) {
		setProperty("startDateAndTime", startDateAndTime);
		return this;
	}


	/**
	* Gets the city of this patient address
	* @return city
	*/
	public String getCity() {
		return getProperty("city");
	}


	/**
	* Changes the city of this PatientAddress
	* @param city The new city to set
	* @return PatientAddress
	*/
	public PatientAddress setCity(String city) {
		setProperty("city", city);
		return this;
	}


	/**
	* Gets the end date and time of this patient address
	* @return endDateAndTime
	*/
	public PartialDateTime getEndDateAndTime() {
		return getProperty("endDateAndTime");
	}


	/**
	* Changes the end date and time of this PatientAddress
	* @param endDateAndTime The new end date and time to set
	* @return PatientAddress
	*/
	public PatientAddress setEndDateAndTime(PartialDateTime endDateAndTime) {
		setProperty("endDateAndTime", endDateAndTime);
		return this;
	}


	/**
	* Gets the country of this patient address
	* @return country
	*/
	public String getCountry() {
		return getProperty("country");
	}


	/**
	* Changes the country of this PatientAddress
	* @param country The new country to set
	* @return PatientAddress
	*/
	public PatientAddress setCountry(String country) {
		setProperty("country", country);
		return this;
	}


	/**
	* Gets the Post Code of this patient address
	* @return postCode
	*/
	public String getPostCode() {
		return getProperty("postCode");
	}


	/**
	* Changes the Post Code of this PatientAddress
	* @param postCode The new Post Code to set
	* @return PatientAddress
	*/
	public PatientAddress setPostCode(String postCode) {
		setProperty("postCode", postCode);
		return this;
	}


	/**
	* Gets the location of this patient address
	* @return location
	*/
	public Location getLocation() {
		return getProperty("location");
	}


	/**
	* Changes the location of this PatientAddress
	* @param location The new location to set
	* @return PatientAddress
	*/
	public PatientAddress setLocation(Location location) {
		setProperty("location", location);
		return this;
	}
}

