package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents emergency care commissioning data set.
* An entry recording an accident ans emergency attendance
*/
public class EmergencyCareCommissioningDataSet extends IMDMBase<EmergencyCareCommissioningDataSet> {


	/**
	* Emergency care commissioning data set constructor with identifier
	*/
	public EmergencyCareCommissioningDataSet(UUID id) {
		super("EmergencyCareCommissioningDataSet", id);
	}

	/**
	* Gets the a&e department type of this emergency care commissioning data set
	* @return aEDepartmentType
	*/
	public String getAEDepartmentType() {
		return getProperty("aEDepartmentType");
	}


	/**
	* Changes the a&e department type of this EmergencyCareCommissioningDataSet
	* @param aEDepartmentType The new a&e department type to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setAEDepartmentType(String aEDepartmentType) {
		setProperty("aEDepartmentType", aEDepartmentType);
		return this;
	}


	/**
	* Gets the record owner of this emergency care commissioning data set
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this EmergencyCareCommissioningDataSet
	* @param recordOwner The new record owner to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the has a&e attendance source of of this emergency care commissioning data set
	* @return hasAEAttendanceSourceOf
	*/
	public String getHasAEAttendanceSourceOf() {
		return getProperty("hasAEAttendanceSourceOf");
	}


	/**
	* Changes the has a&e attendance source of of this EmergencyCareCommissioningDataSet
	* @param hasAEAttendanceSourceOf The new has a&e attendance source of to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setHasAEAttendanceSourceOf(String hasAEAttendanceSourceOf) {
		setProperty("hasAEAttendanceSourceOf", hasAEAttendanceSourceOf);
		return this;
	}


	/**
	* Gets the text of this emergency care commissioning data set
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this EmergencyCareCommissioningDataSet
	* @param text The new text to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the has a&e category of attendance of of this emergency care commissioning data set
	* @return hasAECategoryOfAttendanceOf
	*/
	public String getHasAECategoryOfAttendanceOf() {
		return getProperty("hasAECategoryOfAttendanceOf");
	}


	/**
	* Changes the has a&e category of attendance of of this EmergencyCareCommissioningDataSet
	* @param hasAECategoryOfAttendanceOf The new has a&e category of attendance of to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setHasAECategoryOfAttendanceOf(String hasAECategoryOfAttendanceOf) {
		setProperty("hasAECategoryOfAttendanceOf", hasAECategoryOfAttendanceOf);
		return this;
	}


	/**
	* Gets the patient of this emergency care commissioning data set
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this EmergencyCareCommissioningDataSet
	* @param patient The new patient to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the has arrival mode of this emergency care commissioning data set
	* @return hasArrivalMode
	*/
	public String getHasArrivalMode() {
		return getProperty("hasArrivalMode");
	}


	/**
	* Changes the has arrival mode of this EmergencyCareCommissioningDataSet
	* @param hasArrivalMode The new has arrival mode to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setHasArrivalMode(String hasArrivalMode) {
		setProperty("hasArrivalMode", hasArrivalMode);
		return this;
	}


	/**
	* Gets the original concept of this emergency care commissioning data set
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this EmergencyCareCommissioningDataSet
	* @param originalConcept The new original concept to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the treatment function for service for which admitted of this emergency care commissioning data set
	* @return treatmentFunctionForServiceForWhichAdmitted
	*/
	public String getTreatmentFunctionForServiceForWhichAdmitted() {
		return getProperty("treatmentFunctionForServiceForWhichAdmitted");
	}


	/**
	* Changes the treatment function for service for which admitted of this EmergencyCareCommissioningDataSet
	* @param treatmentFunctionForServiceForWhichAdmitted The new treatment function for service for which admitted to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setTreatmentFunctionForServiceForWhichAdmitted(String treatmentFunctionForServiceForWhichAdmitted) {
		setProperty("treatmentFunctionForServiceForWhichAdmitted", treatmentFunctionForServiceForWhichAdmitted);
		return this;
	}


	/**
	* Gets the effective date of this emergency care commissioning data set
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this EmergencyCareCommissioningDataSet
	* @param effectiveDate The new effective date to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the concept of this emergency care commissioning data set
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this EmergencyCareCommissioningDataSet
	* @param concept The new concept to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the end date of this emergency care commissioning data set
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this EmergencyCareCommissioningDataSet
	* @param endDate The new end date to set
	* @return EmergencyCareCommissioningDataSet
	*/
	public EmergencyCareCommissioningDataSet setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

