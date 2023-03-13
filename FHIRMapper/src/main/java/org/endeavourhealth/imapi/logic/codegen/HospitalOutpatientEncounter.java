package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents hospital outpatient encounter
* An entry describing a hospital outpatient attendance.<p>common data model attributes for Hospital outpatient encounter
*/
public class HospitalOutpatientEncounter extends IMDMBase<HospitalOutpatientEncounter> {


	/**
	* Hospital outpatient encounter constructor 
	*/
	public HospitalOutpatientEncounter() {
		super("HospitalOutpatientEncounter");
	}

	/**
	* Hospital outpatient encounter constructor with identifier
	*/
	public HospitalOutpatientEncounter(String id) {
		super("HospitalOutpatientEncounter", id);
	}

	/**
	* Gets the has attendance outcome of this hospital outpatient encounter
	* @return hasAttendanceOutcome
	*/
	public String getHasAttendanceOutcome() {
		return getProperty("hasAttendanceOutcome");
	}


	/**
	* Changes the has attendance outcome of this HospitalOutpatientEncounter
	* @param hasAttendanceOutcome The new has attendance outcome to set
	* @return HospitalOutpatientEncounter
	*/
	public HospitalOutpatientEncounter setHasAttendanceOutcome(String hasAttendanceOutcome) {
		setProperty("hasAttendanceOutcome", hasAttendanceOutcome);
		return this;
	}


	/**
	* Gets the record owner of this hospital outpatient encounter
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this HospitalOutpatientEncounter
	* @param recordOwner The new record owner to set
	* @return HospitalOutpatientEncounter
	*/
	public HospitalOutpatientEncounter setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the has attendance status of this hospital outpatient encounter
	* @return hasAttendanceStatus
	*/
	public String getHasAttendanceStatus() {
		return getProperty("hasAttendanceStatus");
	}


	/**
	* Changes the has attendance status of this HospitalOutpatientEncounter
	* @param hasAttendanceStatus The new has attendance status to set
	* @return HospitalOutpatientEncounter
	*/
	public HospitalOutpatientEncounter setHasAttendanceStatus(String hasAttendanceStatus) {
		setProperty("hasAttendanceStatus", hasAttendanceStatus);
		return this;
	}


	/**
	* Gets the text of this hospital outpatient encounter
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this HospitalOutpatientEncounter
	* @param text The new text to set
	* @return HospitalOutpatientEncounter
	*/
	public HospitalOutpatientEncounter setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the treatment function type of this hospital outpatient encounter
	* @return treatmentFunctionType
	*/
	public String getTreatmentFunctionType() {
		return getProperty("treatmentFunctionType");
	}


	/**
	* Changes the treatment function type of this HospitalOutpatientEncounter
	* @param treatmentFunctionType The new treatment function type to set
	* @return HospitalOutpatientEncounter
	*/
	public HospitalOutpatientEncounter setTreatmentFunctionType(String treatmentFunctionType) {
		setProperty("treatmentFunctionType", treatmentFunctionType);
		return this;
	}


	/**
	* Gets the patient of this hospital outpatient encounter
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this HospitalOutpatientEncounter
	* @param patient The new patient to set
	* @return HospitalOutpatientEncounter
	*/
	public HospitalOutpatientEncounter setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the concept of this hospital outpatient encounter
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this HospitalOutpatientEncounter
	* @param concept The new concept to set
	* @return HospitalOutpatientEncounter
	*/
	public HospitalOutpatientEncounter setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the original concept of this hospital outpatient encounter
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this HospitalOutpatientEncounter
	* @param originalConcept The new original concept to set
	* @return HospitalOutpatientEncounter
	*/
	public HospitalOutpatientEncounter setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this hospital outpatient encounter
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this HospitalOutpatientEncounter
	* @param effectiveDate The new effective date to set
	* @return HospitalOutpatientEncounter
	*/
	public HospitalOutpatientEncounter setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this hospital outpatient encounter
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this HospitalOutpatientEncounter
	* @param endDate The new end date to set
	* @return HospitalOutpatientEncounter
	*/
	public HospitalOutpatientEncounter setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

