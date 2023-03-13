package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents location
* Information about a geographical location, building or entity that is related to the organisation that operates from it. This may include a region such as a postcode area or a single property
*/
public class Location extends IMDMBase<Location> {


	/**
	* Location constructor 
	*/
	public Location() {
		super("Location");
	}

	/**
	* Location constructor with identifier
	*/
	public Location(String id) {
		super("Location", id);
	}

	/**
	* Gets the address of this location
	* @return address
	*/
	public Address getAddress() {
		return getProperty("address");
	}


	/**
	* Changes the address of this Location
	* @param address The new address to set
	* @return Location
	*/
	public Location setAddress(Address address) {
		setProperty("address", address);
		return this;
	}


	/**
	* Gets the contact of this location
	* @return contact
	*/
	public String getContact() {
		return getProperty("contact");
	}


	/**
	* Changes the contact of this Location
	* @param contact The new contact to set
	* @return Location
	*/
	public Location setContact(String contact) {
		setProperty("contact", contact);
		return this;
	}


	/**
	* Gets the location Type of this location
	* @return locationType
	*/
	public String getLocationType() {
		return getProperty("locationType");
	}


	/**
	* Changes the location Type of this Location
	* @param locationType The new location Type to set
	* @return Location
	*/
	public Location setLocationType(String locationType) {
		setProperty("locationType", locationType);
		return this;
	}


	/**
	* Gets the organisation of this location
	* @return organisation
	*/
	public Organisation getOrganisation() {
		return getProperty("organisation");
	}


	/**
	* Changes the organisation of this Location
	* @param organisation The new organisation to set
	* @return Location
	*/
	public Location setOrganisation(Organisation organisation) {
		setProperty("organisation", organisation);
		return this;
	}


	/**
	* Gets the name of this location
	* @return name
	*/
	public String getName() {
		return getProperty("name");
	}


	/**
	* Changes the name of this Location
	* @param name The new name to set
	* @return Location
	*/
	public Location setName(String name) {
		setProperty("name", name);
		return this;
	}


	/**
	* Gets the UPRN of this location
	* @return uprn
	*/
	public Long getUprn() {
		return getProperty("uprn");
	}


	/**
	* Changes the UPRN of this Location
	* @param uprn The new UPRN to set
	* @return Location
	*/
	public Location setUprn(Long uprn) {
		setProperty("uprn", uprn);
		return this;
	}


	/**
	* Gets the lower Layer Super Output Area LSOA of this location
	* @return lowerLayerSuperOutputAreaLsoa
	*/
	public String getLowerLayerSuperOutputAreaLsoa() {
		return getProperty("lowerLayerSuperOutputAreaLsoa");
	}


	/**
	* Changes the lower Layer Super Output Area LSOA of this Location
	* @param lowerLayerSuperOutputAreaLsoa The new lower Layer Super Output Area LSOA to set
	* @return Location
	*/
	public Location setLowerLayerSuperOutputAreaLsoa(String lowerLayerSuperOutputAreaLsoa) {
		setProperty("lowerLayerSuperOutputAreaLsoa", lowerLayerSuperOutputAreaLsoa);
		return this;
	}


	/**
	* Gets the middle Layer Super Output Area MSOA of this location
	* @return middleLayerSuperOutputAreaMsoa
	*/
	public String getMiddleLayerSuperOutputAreaMsoa() {
		return getProperty("middleLayerSuperOutputAreaMsoa");
	}


	/**
	* Changes the middle Layer Super Output Area MSOA of this Location
	* @param middleLayerSuperOutputAreaMsoa The new middle Layer Super Output Area MSOA to set
	* @return Location
	*/
	public Location setMiddleLayerSuperOutputAreaMsoa(String middleLayerSuperOutputAreaMsoa) {
		setProperty("middleLayerSuperOutputAreaMsoa", middleLayerSuperOutputAreaMsoa);
		return this;
	}


	/**
	* Gets the ods site code of this location
	* @return odsSiteCode
	*/
	public String getOdsSiteCode() {
		return getProperty("odsSiteCode");
	}


	/**
	* Changes the ods site code of this Location
	* @param odsSiteCode The new ods site code to set
	* @return Location
	*/
	public Location setOdsSiteCode(String odsSiteCode) {
		setProperty("odsSiteCode", odsSiteCode);
		return this;
	}


	/**
	* Gets the longitude of this location
	* @return longitude
	*/
	public String getLongitude() {
		return getProperty("longitude");
	}


	/**
	* Changes the longitude of this Location
	* @param longitude The new longitude to set
	* @return Location
	*/
	public Location setLongitude(String longitude) {
		setProperty("longitude", longitude);
		return this;
	}


	/**
	* Gets the latitude of this location
	* @return latitude
	*/
	public String getLatitude() {
		return getProperty("latitude");
	}


	/**
	* Changes the latitude of this Location
	* @param latitude The new latitude to set
	* @return Location
	*/
	public Location setLatitude(String latitude) {
		setProperty("latitude", latitude);
		return this;
	}
}

