package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents allergy intolerance or adverse reaction
* Allergies, intolerance and adverse substance reactions are grouped together and are extensions of simple observations whereby the simple observation code includes the full concept of the allergy e.g.... "allergy to penicillin"<p>The additional data relates to more specific information about the substance and reaction.
*/
public class AllergyIntoleranceOrAdverseReaction extends IMDMBase<AllergyIntoleranceOrAdverseReaction> {


	/**
	* Allergy intolerance or adverse reaction constructor 
	*/
	public AllergyIntoleranceOrAdverseReaction() {
		super("AllergyIntoleranceOrAdverseReaction");
	}

	/**
	* Allergy intolerance or adverse reaction constructor with identifier
	*/
	public AllergyIntoleranceOrAdverseReaction(String id) {
		super("AllergyIntoleranceOrAdverseReaction", id);
	}

	/**
	* Gets the manifestation of this allergy intolerance or adverse reaction
	* @return manifestation
	*/
	public String getManifestation() {
		return getProperty("manifestation");
	}


	/**
	* Changes the manifestation of this AllergyIntoleranceOrAdverseReaction
	* @param manifestation The new manifestation to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setManifestation(String manifestation) {
		setProperty("manifestation", manifestation);
		return this;
	}


	/**
	* Gets the record owner of this allergy intolerance or adverse reaction
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this AllergyIntoleranceOrAdverseReaction
	* @param recordOwner The new record owner to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this allergy intolerance or adverse reaction
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this AllergyIntoleranceOrAdverseReaction
	* @param text The new text to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the manifestation description of this allergy intolerance or adverse reaction
	* @return manifestationDescription
	*/
	public String getManifestationDescription() {
		return getProperty("manifestationDescription");
	}


	/**
	* Changes the manifestation description of this AllergyIntoleranceOrAdverseReaction
	* @param manifestationDescription The new manifestation description to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setManifestationDescription(String manifestationDescription) {
		setProperty("manifestationDescription", manifestationDescription);
		return this;
	}


	/**
	* Gets the observations of this allergy intolerance or adverse reaction
	* @return observations
	*/
	public String getObservations() {
		return getProperty("observations");
	}


	/**
	* Changes the observations of this AllergyIntoleranceOrAdverseReaction
	* @param observations The new observations to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setObservations(String observations) {
		setProperty("observations", observations);
		return this;
	}


	/**
	* Gets the patient of this allergy intolerance or adverse reaction
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this AllergyIntoleranceOrAdverseReaction
	* @param patient The new patient to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the severity of this allergy intolerance or adverse reaction
	* @return severity
	*/
	public String getSeverity() {
		return getProperty("severity");
	}


	/**
	* Changes the severity of this AllergyIntoleranceOrAdverseReaction
	* @param severity The new severity to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setSeverity(String severity) {
		setProperty("severity", severity);
		return this;
	}


	/**
	* Gets the original concept of this allergy intolerance or adverse reaction
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this AllergyIntoleranceOrAdverseReaction
	* @param originalConcept The new original concept to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this allergy intolerance or adverse reaction
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this AllergyIntoleranceOrAdverseReaction
	* @param effectiveDate The new effective date to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the substance of this allergy intolerance or adverse reaction
	* @return substance
	*/
	public String getSubstance() {
		return getProperty("substance");
	}


	/**
	* Changes the substance of this AllergyIntoleranceOrAdverseReaction
	* @param substance The new substance to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setSubstance(String substance) {
		setProperty("substance", substance);
		return this;
	}


	/**
	* Gets the end date of this allergy intolerance or adverse reaction
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this AllergyIntoleranceOrAdverseReaction
	* @param endDate The new end date to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the concept of this allergy intolerance or adverse reaction
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this AllergyIntoleranceOrAdverseReaction
	* @param concept The new concept to set
	* @return AllergyIntoleranceOrAdverseReaction
	*/
	public AllergyIntoleranceOrAdverseReaction setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}
}

