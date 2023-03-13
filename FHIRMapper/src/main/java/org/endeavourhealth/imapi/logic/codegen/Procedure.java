package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents procedure
* Procedure provides more information beyond a simple observation about an operation or observation relating to the outcome of the procedure.<p>Within the health data model, unlike FHIR a complex procedure description (that includes body site, laterality, method and nature of device) is represented by a Snomed expression in the observation concept.
*/
public class Procedure extends IMDMBase<Procedure> {


	/**
	* Procedure constructor 
	*/
	public Procedure() {
		super("Procedure");
	}

	/**
	* Procedure constructor with identifier
	*/
	public Procedure(String id) {
		super("Procedure", id);
	}

	/**
	* Gets the complications of this procedure
	* @return complications
	*/
	public String getComplications() {
		return getProperty("complications");
	}


	/**
	* Changes the complications of this Procedure
	* @param complications The new complications to set
	* @return Procedure
	*/
	public Procedure setComplications(String complications) {
		setProperty("complications", complications);
		return this;
	}


	/**
	* Gets the record owner of this procedure
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this Procedure
	* @param recordOwner The new record owner to set
	* @return Procedure
	*/
	public Procedure setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this procedure
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this Procedure
	* @param text The new text to set
	* @return Procedure
	*/
	public Procedure setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the devices used of this procedure
	* @return devicesUsed
	*/
	public String getDevicesUsed() {
		return getProperty("devicesUsed");
	}


	/**
	* Changes the devices used of this Procedure
	* @param devicesUsed The new devices used to set
	* @return Procedure
	*/
	public Procedure setDevicesUsed(String devicesUsed) {
		setProperty("devicesUsed", devicesUsed);
		return this;
	}


	/**
	* Gets the follow ups of this procedure
	* @return followUps
	*/
	public String getFollowUps() {
		return getProperty("followUps");
	}


	/**
	* Changes the follow ups of this Procedure
	* @param followUps The new follow ups to set
	* @return Procedure
	*/
	public Procedure setFollowUps(String followUps) {
		setProperty("followUps", followUps);
		return this;
	}


	/**
	* Gets the patient of this procedure
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this Procedure
	* @param patient The new patient to set
	* @return Procedure
	*/
	public Procedure setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the original concept of this procedure
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this Procedure
	* @param originalConcept The new original concept to set
	* @return Procedure
	*/
	public Procedure setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the linked problems of this procedure
	* @return linkedProblems
	*/
	public Condition getLinkedProblems() {
		return getProperty("linkedProblems");
	}


	/**
	* Changes the linked problems of this Procedure
	* @param linkedProblems The new linked problems to set
	* @return Procedure
	*/
	public Procedure setLinkedProblems(Condition linkedProblems) {
		setProperty("linkedProblems", linkedProblems);
		return this;
	}


	/**
	* Gets the effective date of this procedure
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this Procedure
	* @param effectiveDate The new effective date to set
	* @return Procedure
	*/
	public Procedure setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the outcome of this procedure
	* @return outcome
	*/
	public String getOutcome() {
		return getProperty("outcome");
	}


	/**
	* Changes the outcome of this Procedure
	* @param outcome The new outcome to set
	* @return Procedure
	*/
	public Procedure setOutcome(String outcome) {
		setProperty("outcome", outcome);
		return this;
	}


	/**
	* Gets the end date of this procedure
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this Procedure
	* @param endDate The new end date to set
	* @return Procedure
	*/
	public Procedure setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the performed period of this procedure
	* @return performedPeriod
	*/
	public String getPerformedPeriod() {
		return getProperty("performedPeriod");
	}


	/**
	* Changes the performed period of this Procedure
	* @param performedPeriod The new performed period to set
	* @return Procedure
	*/
	public Procedure setPerformedPeriod(String performedPeriod) {
		setProperty("performedPeriod", performedPeriod);
		return this;
	}


	/**
	* Gets the end time of this procedure
	* @return endTime
	*/
	public String getEndTime() {
		return getProperty("endTime");
	}


	/**
	* Changes the end time of this Procedure
	* @param endTime The new end time to set
	* @return Procedure
	*/
	public Procedure setEndTime(String endTime) {
		setProperty("endTime", endTime);
		return this;
	}


	/**
	* Gets the concept of this procedure
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this Procedure
	* @param concept The new concept to set
	* @return Procedure
	*/
	public Procedure setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}
}

