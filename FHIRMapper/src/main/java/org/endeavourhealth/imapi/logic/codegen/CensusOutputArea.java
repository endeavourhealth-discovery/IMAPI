package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents census output area
* A geopgraphical area used in a census
*/
public class CensusOutputArea extends IMDMBase<CensusOutputArea> {


	/**
	* Census output area constructor 
	*/
	public CensusOutputArea() {
		super("CensusOutputArea");
	}

	/**
	* Census output area constructor with identifier
	*/
	public CensusOutputArea(String id) {
		super("CensusOutputArea", id);
	}

	/**
	* Gets the code of this census output area
	* @return code
	*/
	public String getCode() {
		return getProperty("code");
	}


	/**
	* Changes the code of this CensusOutputArea
	* @param code The new code to set
	* @return CensusOutputArea
	*/
	public CensusOutputArea setCode(String code) {
		setProperty("code", code);
		return this;
	}


	/**
	* Gets the label of this census output area
	* @return label
	*/
	public String getLabel() {
		return getProperty("label");
	}


	/**
	* Changes the label of this CensusOutputArea
	* @param label The new label to set
	* @return CensusOutputArea
	*/
	public CensusOutputArea setLabel(String label) {
		setProperty("label", label);
		return this;
	}


	/**
	* Gets the postcode of this census output area
	* @return postcode
	*/
	public String getPostcode() {
		return getProperty("postcode");
	}


	/**
	* Changes the postcode of this CensusOutputArea
	* @param postcode The new postcode to set
	* @return CensusOutputArea
	*/
	public CensusOutputArea setPostcode(String postcode) {
		setProperty("postcode", postcode);
		return this;
	}
}

