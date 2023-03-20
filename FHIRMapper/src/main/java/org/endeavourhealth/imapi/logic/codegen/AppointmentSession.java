package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents appointment session.
* An appointment session is an appointment grouping implying a session or a clinic, which incorporates a number of appointments<p>In the Health data model, all appointments are linked to a schedule (whether a schedule pre-authored or not) i.e.. a standalone appointment would have one schedule for the stand alone appointment
*/
public class AppointmentSession extends IMDMBase<AppointmentSession> {


	/**
	* Appointment session constructor with identifier
	*/
	public AppointmentSession(UUID id) {
		super("AppointmentSession", id);
	}

	/**
	* Gets the record owner of this appointment session
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this AppointmentSession
	* @param recordOwner The new record owner to set
	* @return AppointmentSession
	*/
	public AppointmentSession setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the practitioner of this appointment session
	* @return practitioner
	*/
	public UUID getPractitioner() {
		return getProperty("practitioner");
	}


	/**
	* Changes the practitioner of this AppointmentSession
	* @param practitioner The new practitioner to set
	* @return AppointmentSession
	*/
	public AppointmentSession setPractitioner(UUID practitioner) {
		setProperty("practitioner", practitioner);
		return this;
	}


	/**
	* Gets the text of this appointment session
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this AppointmentSession
	* @param text The new text to set
	* @return AppointmentSession
	*/
	public AppointmentSession setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the linked appointment slots of this appointment session
	* @return linkedAppointmentSlots
	*/
	public String getLinkedAppointmentSlots() {
		return getProperty("linkedAppointmentSlots");
	}


	/**
	* Changes the linked appointment slots of this AppointmentSession
	* @param linkedAppointmentSlots The new linked appointment slots to set
	* @return AppointmentSession
	*/
	public AppointmentSession setLinkedAppointmentSlots(String linkedAppointmentSlots) {
		setProperty("linkedAppointmentSlots", linkedAppointmentSlots);
		return this;
	}


	/**
	* Gets the patient of this appointment session
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this AppointmentSession
	* @param patient The new patient to set
	* @return AppointmentSession
	*/
	public AppointmentSession setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the location of this appointment session
	* @return location
	*/
	public UUID getLocation() {
		return getProperty("location");
	}


	/**
	* Changes the location of this AppointmentSession
	* @param location The new location to set
	* @return AppointmentSession
	*/
	public AppointmentSession setLocation(UUID location) {
		setProperty("location", location);
		return this;
	}


	/**
	* Gets the organisation or service of this appointment session
	* @return organisationOrService
	*/
	public String getOrganisationOrService() {
		return getProperty("organisationOrService");
	}


	/**
	* Changes the organisation or service of this AppointmentSession
	* @param organisationOrService The new organisation or service to set
	* @return AppointmentSession
	*/
	public AppointmentSession setOrganisationOrService(String organisationOrService) {
		setProperty("organisationOrService", organisationOrService);
		return this;
	}


	/**
	* Gets the concept of this appointment session
	* @return concept
	*/
	public UUID getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this AppointmentSession
	* @param concept The new concept to set
	* @return AppointmentSession
	*/
	public AppointmentSession setConcept(UUID concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the schedule description of this appointment session
	* @return scheduleDescription
	*/
	public String getScheduleDescription() {
		return getProperty("scheduleDescription");
	}


	/**
	* Changes the schedule description of this AppointmentSession
	* @param scheduleDescription The new schedule description to set
	* @return AppointmentSession
	*/
	public AppointmentSession setScheduleDescription(String scheduleDescription) {
		setProperty("scheduleDescription", scheduleDescription);
		return this;
	}


	/**
	* Gets the original concept of this appointment session
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this AppointmentSession
	* @param originalConcept The new original concept to set
	* @return AppointmentSession
	*/
	public AppointmentSession setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the end date of this appointment session
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this AppointmentSession
	* @param endDate The new end date to set
	* @return AppointmentSession
	*/
	public AppointmentSession setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the schedule type of this appointment session
	* @return scheduleType
	*/
	public String getScheduleType() {
		return getProperty("scheduleType");
	}


	/**
	* Changes the schedule type of this AppointmentSession
	* @param scheduleType The new schedule type to set
	* @return AppointmentSession
	*/
	public AppointmentSession setScheduleType(String scheduleType) {
		setProperty("scheduleType", scheduleType);
		return this;
	}


	/**
	* Gets the speciality of this appointment session
	* @return speciality
	*/
	public String getSpeciality() {
		return getProperty("speciality");
	}


	/**
	* Changes the speciality of this AppointmentSession
	* @param speciality The new speciality to set
	* @return AppointmentSession
	*/
	public AppointmentSession setSpeciality(String speciality) {
		setProperty("speciality", speciality);
		return this;
	}


	/**
	* Gets the effective date of this appointment session
	* @return effectiveDate
	*/
	public String getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this AppointmentSession
	* @param effectiveDate The new effective date to set
	* @return AppointmentSession
	*/
	public AppointmentSession setEffectiveDate(String effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date/time of this appointment session
	* @return endDateTime
	*/
	public String getEndDateTime() {
		return getProperty("endDateTime");
	}


	/**
	* Changes the end date/time of this AppointmentSession
	* @param endDateTime The new end date/time to set
	* @return AppointmentSession
	*/
	public AppointmentSession setEndDateTime(String endDateTime) {
		setProperty("endDateTime", endDateTime);
		return this;
	}
}

