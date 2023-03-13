package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents place of residence at event
* Data model for the place of residence at a point in time based on their home address at that time, if they are registered with a GP, including address and location information
*/
public class PlaceOfResidenceAtEvent extends IMDMBase<PlaceOfResidenceAtEvent> {


	/**
	* Place of residence at event constructor 
	*/
	public PlaceOfResidenceAtEvent() {
		super("PlaceOfResidenceAtEvent");
	}

	/**
	* Place of residence at event constructor with identifier
	*/
	public PlaceOfResidenceAtEvent(String id) {
		super("PlaceOfResidenceAtEvent", id);
	}

	/**
	* Gets the address line of this place of residence at event
	* @return addressLine
	*/
	public String getAddressLine() {
		return getProperty("addressLine");
	}


	/**
	* Changes the address line of this PlaceOfResidenceAtEvent
	* @param addressLine The new address line to set
	* @return PlaceOfResidenceAtEvent
	*/
	public PlaceOfResidenceAtEvent setAddressLine(String addressLine) {
		setProperty("addressLine", addressLine);
		return this;
	}


	/**
	* Gets the record owner of this place of residence at event
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this PlaceOfResidenceAtEvent
	* @param recordOwner The new record owner to set
	* @return PlaceOfResidenceAtEvent
	*/
	public PlaceOfResidenceAtEvent setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the patient of this place of residence at event
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this PlaceOfResidenceAtEvent
	* @param patient The new patient to set
	* @return PlaceOfResidenceAtEvent
	*/
	public PlaceOfResidenceAtEvent setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the Locality of this place of residence at event
	* @return locality
	*/
	public String getLocality() {
		return getProperty("locality");
	}


	/**
	* Changes the Locality of this PlaceOfResidenceAtEvent
	* @param locality The new Locality to set
	* @return PlaceOfResidenceAtEvent
	*/
	public PlaceOfResidenceAtEvent setLocality(String locality) {
		setProperty("locality", locality);
		return this;
	}


	/**
	* Gets the Region of this place of residence at event
	* @return region
	*/
	public String getRegion() {
		return getProperty("region");
	}


	/**
	* Changes the Region of this PlaceOfResidenceAtEvent
	* @param region The new Region to set
	* @return PlaceOfResidenceAtEvent
	*/
	public PlaceOfResidenceAtEvent setRegion(String region) {
		setProperty("region", region);
		return this;
	}


	/**
	* Gets the city of this place of residence at event
	* @return city
	*/
	public String getCity() {
		return getProperty("city");
	}


	/**
	* Changes the city of this PlaceOfResidenceAtEvent
	* @param city The new city to set
	* @return PlaceOfResidenceAtEvent
	*/
	public PlaceOfResidenceAtEvent setCity(String city) {
		setProperty("city", city);
		return this;
	}


	/**
	* Gets the country of this place of residence at event
	* @return country
	*/
	public String getCountry() {
		return getProperty("country");
	}


	/**
	* Changes the country of this PlaceOfResidenceAtEvent
	* @param country The new country to set
	* @return PlaceOfResidenceAtEvent
	*/
	public PlaceOfResidenceAtEvent setCountry(String country) {
		setProperty("country", country);
		return this;
	}


	/**
	* Gets the Post Code of this place of residence at event
	* @return postCode
	*/
	public String getPostCode() {
		return getProperty("postCode");
	}


	/**
	* Changes the Post Code of this PlaceOfResidenceAtEvent
	* @param postCode The new Post Code to set
	* @return PlaceOfResidenceAtEvent
	*/
	public PlaceOfResidenceAtEvent setPostCode(String postCode) {
		setProperty("postCode", postCode);
		return this;
	}


	/**
	* Gets the location of this place of residence at event
	* @return location
	*/
	public Location getLocation() {
		return getProperty("location");
	}


	/**
	* Changes the location of this PlaceOfResidenceAtEvent
	* @param location The new location to set
	* @return PlaceOfResidenceAtEvent
	*/
	public PlaceOfResidenceAtEvent setLocation(Location location) {
		setProperty("location", location);
		return this;
	}
}

