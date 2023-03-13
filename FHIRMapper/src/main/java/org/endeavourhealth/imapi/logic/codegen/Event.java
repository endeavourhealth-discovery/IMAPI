package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents event
* An entry for something that is deemed to be valid at a point in time and may or may not be valid over time. In other words an entry with an effective date/time that does not have a property of end date/time defined
*/
public class Event extends IMDMBase<Event> {


	/**
	* Event constructor 
	*/
	public Event() {
		super("Event");
	}

	/**
	* Event constructor with identifier
	*/
	public Event(String id) {
		super("Event", id);
	}

	/**
	* Gets the text of this event
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this Event
	* @param text The new text to set
	* @return Event
	*/
	public Event setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the record owner of this event
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this Event
	* @param recordOwner The new record owner to set
	* @return Event
	*/
	public Event setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the patient of this event
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this Event
	* @param patient The new patient to set
	* @return Event
	*/
	public Event setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the concept of this event
	* @return concept
	*/
	public TerminologyConcept getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this Event
	* @param concept The new concept to set
	* @return Event
	*/
	public Event setConcept(TerminologyConcept concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the original concept of this event
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this Event
	* @param originalConcept The new original concept to set
	* @return Event
	*/
	public Event setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this event
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this Event
	* @param effectiveDate The new effective date to set
	* @return Event
	*/
	public Event setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this event
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this Event
	* @param endDate The new end date to set
	* @return Event
	*/
	public Event setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

