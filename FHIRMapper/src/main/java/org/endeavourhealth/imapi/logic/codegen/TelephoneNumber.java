package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents telephone number
* A structural definition of a telephone number
*/
public class TelephoneNumber extends IMDMBase<TelephoneNumber> {


	/**
	* Telephone number constructor 
	*/
	public TelephoneNumber() {
		super("TelephoneNumber");
	}

	/**
	* Telephone number constructor with identifier
	*/
	public TelephoneNumber(String id) {
		super("TelephoneNumber", id);
	}

	/**
	* Gets the value of this telephone number
	* @return value
	*/
	public String getValue() {
		return getProperty("value");
	}


	/**
	* Changes the value of this TelephoneNumber
	* @param value The new value to set
	* @return TelephoneNumber
	*/
	public TelephoneNumber setValue(String value) {
		setProperty("value", value);
		return this;
	}


	/**
	* Gets the use of this telephone number
	* @return use
	*/
	public String getUse() {
		return getProperty("use");
	}


	/**
	* Changes the use of this TelephoneNumber
	* @param use The new use to set
	* @return TelephoneNumber
	*/
	public TelephoneNumber setUse(String use) {
		setProperty("use", use);
		return this;
	}


	/**
	* Gets the area dialling code of this telephone number
	* @return areaDiallingCode
	*/
	public Integer getAreaDiallingCode() {
		return getProperty("areaDiallingCode");
	}


	/**
	* Changes the area dialling code of this TelephoneNumber
	* @param areaDiallingCode The new area dialling code to set
	* @return TelephoneNumber
	*/
	public TelephoneNumber setAreaDiallingCode(Integer areaDiallingCode) {
		setProperty("areaDiallingCode", areaDiallingCode);
		return this;
	}


	/**
	* Gets the country dialling code of this telephone number
	* @return countryDiallingCode
	*/
	public String getCountryDiallingCode() {
		return getProperty("countryDiallingCode");
	}


	/**
	* Changes the country dialling code of this TelephoneNumber
	* @param countryDiallingCode The new country dialling code to set
	* @return TelephoneNumber
	*/
	public TelephoneNumber setCountryDiallingCode(String countryDiallingCode) {
		setProperty("countryDiallingCode", countryDiallingCode);
		return this;
	}
}

