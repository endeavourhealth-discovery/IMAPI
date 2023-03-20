package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents numeric measurement.
* A point in time event that holds a value qualified by units and normal ranges
*/
public class NumericMeasurement extends IMDMBase<NumericMeasurement> {


	/**
	* Numeric measurement constructor with identifier
	*/
	public NumericMeasurement(UUID id) {
		super("NumericMeasurement", id);
	}

	/**
	* Gets the concept of this numeric measurement
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this NumericMeasurement
	* @param concept The new concept to set
	* @return NumericMeasurement
	*/
	public NumericMeasurement setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the record owner of this numeric measurement
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this NumericMeasurement
	* @param recordOwner The new record owner to set
	* @return NumericMeasurement
	*/
	public NumericMeasurement setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this numeric measurement
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this NumericMeasurement
	* @param text The new text to set
	* @return NumericMeasurement
	*/
	public NumericMeasurement setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the patient of this numeric measurement
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this NumericMeasurement
	* @param patient The new patient to set
	* @return NumericMeasurement
	*/
	public NumericMeasurement setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the original concept of this numeric measurement
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this NumericMeasurement
	* @param originalConcept The new original concept to set
	* @return NumericMeasurement
	*/
	public NumericMeasurement setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this numeric measurement
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this NumericMeasurement
	* @param effectiveDate The new effective date to set
	* @return NumericMeasurement
	*/
	public NumericMeasurement setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this numeric measurement
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this NumericMeasurement
	* @param endDate The new end date to set
	* @return NumericMeasurement
	*/
	public NumericMeasurement setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

