package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents care plan
* A dynamic document that notes the plan regarding the care of a patient. As in the document structure they tend to specialise and thus this highlights only the generic sections.
*/
public class CarePlan extends IMDMBase<CarePlan> {


	/**
	* Care plan constructor 
	*/
	public CarePlan() {
		super("CarePlan");
	}

	/**
	* Care plan constructor with identifier
	*/
	public CarePlan(String id) {
		super("CarePlan", id);
	}

	/**
	* Gets the associated practitioners of this care plan
	* @return associatedPractitioners
	*/
	public String getAssociatedPractitioners() {
		return getProperty("associatedPractitioners");
	}


	/**
	* Changes the associated practitioners of this CarePlan
	* @param associatedPractitioners The new associated practitioners to set
	* @return CarePlan
	*/
	public CarePlan setAssociatedPractitioners(String associatedPractitioners) {
		setProperty("associatedPractitioners", associatedPractitioners);
		return this;
	}


	/**
	* Gets the record owner of this care plan
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this CarePlan
	* @param recordOwner The new record owner to set
	* @return CarePlan
	*/
	public CarePlan setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this care plan
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this CarePlan
	* @param text The new text to set
	* @return CarePlan
	*/
	public CarePlan setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the associated teams of this care plan
	* @return associatedTeams
	*/
	public String getAssociatedTeams() {
		return getProperty("associatedTeams");
	}


	/**
	* Changes the associated teams of this CarePlan
	* @param associatedTeams The new associated teams to set
	* @return CarePlan
	*/
	public CarePlan setAssociatedTeams(String associatedTeams) {
		setProperty("associatedTeams", associatedTeams);
		return this;
	}


	/**
	* Gets the document Status of this care plan
	* @return documentStatus
	*/
	public String getDocumentStatus() {
		return getProperty("documentStatus");
	}


	/**
	* Changes the document Status of this CarePlan
	* @param documentStatus The new document Status to set
	* @return CarePlan
	*/
	public CarePlan setDocumentStatus(String documentStatus) {
		setProperty("documentStatus", documentStatus);
		return this;
	}


	/**
	* Gets the patient of this care plan
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this CarePlan
	* @param patient The new patient to set
	* @return CarePlan
	*/
	public CarePlan setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the linked activities of this care plan
	* @return linkedActivities
	*/
	public String getLinkedActivities() {
		return getProperty("linkedActivities");
	}


	/**
	* Changes the linked activities of this CarePlan
	* @param linkedActivities The new linked activities to set
	* @return CarePlan
	*/
	public CarePlan setLinkedActivities(String linkedActivities) {
		setProperty("linkedActivities", linkedActivities);
		return this;
	}


	/**
	* Gets the original concept of this care plan
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this CarePlan
	* @param originalConcept The new original concept to set
	* @return CarePlan
	*/
	public CarePlan setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this care plan
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this CarePlan
	* @param effectiveDate The new effective date to set
	* @return CarePlan
	*/
	public CarePlan setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the linked episodes of this care plan
	* @return linkedEpisodes
	*/
	public String getLinkedEpisodes() {
		return getProperty("linkedEpisodes");
	}


	/**
	* Changes the linked episodes of this CarePlan
	* @param linkedEpisodes The new linked episodes to set
	* @return CarePlan
	*/
	public CarePlan setLinkedEpisodes(String linkedEpisodes) {
		setProperty("linkedEpisodes", linkedEpisodes);
		return this;
	}


	/**
	* Gets the end date of this care plan
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this CarePlan
	* @param endDate The new end date to set
	* @return CarePlan
	*/
	public CarePlan setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the linked Headings of this care plan
	* @return linkedHeadings
	*/
	public String getLinkedHeadings() {
		return getProperty("linkedHeadings");
	}


	/**
	* Changes the linked Headings of this CarePlan
	* @param linkedHeadings The new linked Headings to set
	* @return CarePlan
	*/
	public CarePlan setLinkedHeadings(String linkedHeadings) {
		setProperty("linkedHeadings", linkedHeadings);
		return this;
	}


	/**
	* Gets the parent plan of this care plan
	* @return parentPlan
	*/
	public String getParentPlan() {
		return getProperty("parentPlan");
	}


	/**
	* Changes the parent plan of this CarePlan
	* @param parentPlan The new parent plan to set
	* @return CarePlan
	*/
	public CarePlan setParentPlan(String parentPlan) {
		setProperty("parentPlan", parentPlan);
		return this;
	}


	/**
	* Gets the time period of this care plan
	* @return timePeriod
	*/
	public String getTimePeriod() {
		return getProperty("timePeriod");
	}


	/**
	* Changes the time period of this CarePlan
	* @param timePeriod The new time period to set
	* @return CarePlan
	*/
	public CarePlan setTimePeriod(String timePeriod) {
		setProperty("timePeriod", timePeriod);
		return this;
	}


	/**
	* Gets the concept of this care plan
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this CarePlan
	* @param concept The new concept to set
	* @return CarePlan
	*/
	public CarePlan setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the description of this care plan
	* @return description
	*/
	public String getDescription() {
		return getProperty("description");
	}


	/**
	* Changes the description of this CarePlan
	* @param description The new description to set
	* @return CarePlan
	*/
	public CarePlan setDescription(String description) {
		setProperty("description", description);
		return this;
	}
}

