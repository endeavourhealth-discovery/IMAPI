package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents episode of care.
* A care episode is an association between a patient and a healthcare provider during which time care is provided. The association implies that the provider has some responsibility for the provision of care during the period of time covered by the episode.<p>A care episode may be a concept that is explicitly stated. For example, GP registration is an explicit process by which the patient registers for care and in due course may be de-registered when they move elsewhere.<p>A care episode may otherwise be deduced from the data provided , usually relating to encounters. For example the acceptance of a referral or the attendance at accident and emergency provide episode of care start points. Discharge from an outpatient clinical may be used to deduce the end of a care episode.
*/
public class EpisodeOfCare extends IMDMBase<EpisodeOfCare> {


	/**
	* Episode of care constructor with identifier
	*/
	public EpisodeOfCare(UUID id) {
		super("EpisodeOfCare", id);
	}

	/**
	* Gets the patient type of this episode of care
	* @return patientType
	*/
	public String getPatientType() {
		return getProperty("patientType");
	}


	/**
	* Changes the patient type of this EpisodeOfCare
	* @param patientType The new patient type to set
	* @return EpisodeOfCare
	*/
	public EpisodeOfCare setPatientType(String patientType) {
		setProperty("patientType", patientType);
		return this;
	}


	/**
	* Gets the record owner of this episode of care
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this EpisodeOfCare
	* @param recordOwner The new record owner to set
	* @return EpisodeOfCare
	*/
	public EpisodeOfCare setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this episode of care
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this EpisodeOfCare
	* @param text The new text to set
	* @return EpisodeOfCare
	*/
	public EpisodeOfCare setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the initiating Referral of this episode of care
	* @return initiatingReferral
	*/
	public String getInitiatingReferral() {
		return getProperty("initiatingReferral");
	}


	/**
	* Changes the initiating Referral of this EpisodeOfCare
	* @param initiatingReferral The new initiating Referral to set
	* @return EpisodeOfCare
	*/
	public EpisodeOfCare setInitiatingReferral(String initiatingReferral) {
		setProperty("initiatingReferral", initiatingReferral);
		return this;
	}


	/**
	* Gets the linked Entries of this episode of care
	* @return linkedEntries
	*/
	public String getLinkedEntries() {
		return getProperty("linkedEntries");
	}


	/**
	* Changes the linked Entries of this EpisodeOfCare
	* @param linkedEntries The new linked Entries to set
	* @return EpisodeOfCare
	*/
	public EpisodeOfCare setLinkedEntries(String linkedEntries) {
		setProperty("linkedEntries", linkedEntries);
		return this;
	}


	/**
	* Gets the patient of this episode of care
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this EpisodeOfCare
	* @param patient The new patient to set
	* @return EpisodeOfCare
	*/
	public EpisodeOfCare setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the concept of this episode of care
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this EpisodeOfCare
	* @param concept The new concept to set
	* @return EpisodeOfCare
	*/
	public EpisodeOfCare setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the original concept of this episode of care
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this EpisodeOfCare
	* @param originalConcept The new original concept to set
	* @return EpisodeOfCare
	*/
	public EpisodeOfCare setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this episode of care
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this EpisodeOfCare
	* @param effectiveDate The new effective date to set
	* @return EpisodeOfCare
	*/
	public EpisodeOfCare setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this episode of care
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this EpisodeOfCare
	* @param endDate The new end date to set
	* @return EpisodeOfCare
	*/
	public EpisodeOfCare setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

