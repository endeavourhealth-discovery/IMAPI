package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents hospital discharge
* An entry describing the event of a hospital discharge. Has specialised encounter properties.<p>common data model attributes for Hospital discharge (entry)
*/
public class HospitalDischarge extends IMDMBase<HospitalDischarge> {


	/**
	* Hospital discharge constructor 
	*/
	public HospitalDischarge() {
		super("HospitalDischarge");
	}

	/**
	* Hospital discharge constructor with identifier
	*/
	public HospitalDischarge(String id) {
		super("HospitalDischarge", id);
	}

	/**
	* Gets the has discharge destination of this hospital discharge
	* @return hasDischargeDestination
	*/
	public String getHasDischargeDestination() {
		return getProperty("hasDischargeDestination");
	}


	/**
	* Changes the has discharge destination of this HospitalDischarge
	* @param hasDischargeDestination The new has discharge destination to set
	* @return HospitalDischarge
	*/
	public HospitalDischarge setHasDischargeDestination(String hasDischargeDestination) {
		setProperty("hasDischargeDestination", hasDischargeDestination);
		return this;
	}


	/**
	* Gets the record owner of this hospital discharge
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this HospitalDischarge
	* @param recordOwner The new record owner to set
	* @return HospitalDischarge
	*/
	public HospitalDischarge setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the has discharge method of this hospital discharge
	* @return hasDischargeMethod
	*/
	public String getHasDischargeMethod() {
		return getProperty("hasDischargeMethod");
	}


	/**
	* Changes the has discharge method of this HospitalDischarge
	* @param hasDischargeMethod The new has discharge method to set
	* @return HospitalDischarge
	*/
	public HospitalDischarge setHasDischargeMethod(String hasDischargeMethod) {
		setProperty("hasDischargeMethod", hasDischargeMethod);
		return this;
	}


	/**
	* Gets the text of this hospital discharge
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this HospitalDischarge
	* @param text The new text to set
	* @return HospitalDischarge
	*/
	public HospitalDischarge setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the is component of of this hospital discharge
	* @return isComponentOf
	*/
	public HospitalInpatientStay getIsComponentOf() {
		return getProperty("isComponentOf");
	}


	/**
	* Changes the is component of of this HospitalDischarge
	* @param isComponentOf The new is component of to set
	* @return HospitalDischarge
	*/
	public HospitalDischarge setIsComponentOf(HospitalInpatientStay isComponentOf) {
		setProperty("isComponentOf", isComponentOf);
		return this;
	}


	/**
	* Gets the patient of this hospital discharge
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this HospitalDischarge
	* @param patient The new patient to set
	* @return HospitalDischarge
	*/
	public HospitalDischarge setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the concept of this hospital discharge
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this HospitalDischarge
	* @param concept The new concept to set
	* @return HospitalDischarge
	*/
	public HospitalDischarge setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the original concept of this hospital discharge
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this HospitalDischarge
	* @param originalConcept The new original concept to set
	* @return HospitalDischarge
	*/
	public HospitalDischarge setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this hospital discharge
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this HospitalDischarge
	* @param effectiveDate The new effective date to set
	* @return HospitalDischarge
	*/
	public HospitalDischarge setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this hospital discharge
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this HospitalDischarge
	* @param endDate The new end date to set
	* @return HospitalDischarge
	*/
	public HospitalDischarge setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

