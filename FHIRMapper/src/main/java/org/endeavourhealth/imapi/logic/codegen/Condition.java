package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents condition.
* An issue , problem or diagnosis. The main purposes of problem structures are to highlight significant issues and to group entries in the record to enable a narrative view categorised by a focus of care. In different care domains different terms are used such as "problem", "issue" or "need" but from a structural perspective they are the same
*/
public class Condition extends IMDMBase<Condition> {


	/**
	* Condition constructor with identifier
	*/
	public Condition(UUID id) {
		super("Condition", id);
	}

	/**
	* Gets the concept of this condition
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this Condition
	* @param concept The new concept to set
	* @return Condition
	*/
	public Condition setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the record owner of this condition
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this Condition
	* @param recordOwner The new record owner to set
	* @return Condition
	*/
	public Condition setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this condition
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this Condition
	* @param text The new text to set
	* @return Condition
	*/
	public Condition setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the anticipated duration of this condition
	* @return anticipatedDuration
	*/
	public String getAnticipatedDuration() {
		return getProperty("anticipatedDuration");
	}


	/**
	* Changes the anticipated duration of this Condition
	* @param anticipatedDuration The new anticipated duration to set
	* @return Condition
	*/
	public Condition setAnticipatedDuration(String anticipatedDuration) {
		setProperty("anticipatedDuration", anticipatedDuration);
		return this;
	}


	/**
	* Gets the patient of this condition
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this Condition
	* @param patient The new patient to set
	* @return Condition
	*/
	public Condition setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the parent of this condition
	* @return parent
	*/
	public UUID getParent() {
		return getProperty("parent");
	}


	/**
	* Changes the parent of this Condition
	* @param parent The new parent to set
	* @return Condition
	*/
	public Condition setParent(UUID parent) {
		setProperty("parent", parent);
		return this;
	}


	/**
	* Gets the significance of this condition
	* @return significance
	*/
	public String getSignificance() {
		return getProperty("significance");
	}


	/**
	* Changes the significance of this Condition
	* @param significance The new significance to set
	* @return Condition
	*/
	public Condition setSignificance(String significance) {
		setProperty("significance", significance);
		return this;
	}


	/**
	* Gets the original concept of this condition
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this Condition
	* @param originalConcept The new original concept to set
	* @return Condition
	*/
	public Condition setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this condition
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this Condition
	* @param effectiveDate The new effective date to set
	* @return Condition
	*/
	public Condition setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the description of this condition
	* @return description
	*/
	public String getDescription() {
		return getProperty("description");
	}


	/**
	* Changes the description of this Condition
	* @param description The new description to set
	* @return Condition
	*/
	public Condition setDescription(String description) {
		setProperty("description", description);
		return this;
	}


	/**
	* Gets the end date of this condition
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this Condition
	* @param endDate The new end date to set
	* @return Condition
	*/
	public Condition setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the problem episode of this condition
	* @return problemEpisode
	*/
	public String getProblemEpisode() {
		return getProperty("problemEpisode");
	}


	/**
	* Changes the problem episode of this Condition
	* @param problemEpisode The new problem episode to set
	* @return Condition
	*/
	public Condition setProblemEpisode(String problemEpisode) {
		setProperty("problemEpisode", problemEpisode);
		return this;
	}
}

