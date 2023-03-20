package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents appointment attendance.
* Information about an actual attendance for a patient for an appointment i.e.. after the patient has arrived for appointment
*/
public class AppointmentAttendance extends IMDMBase<AppointmentAttendance> {


	/**
	* Appointment attendance constructor with identifier
	*/
	public AppointmentAttendance(UUID id) {
		super("AppointmentAttendance", id);
	}

	/**
	* Gets the actual duration of this appointment attendance
	* @return actualDuration
	*/
	public String getActualDuration() {
		return getProperty("actualDuration");
	}


	/**
	* Changes the actual duration of this AppointmentAttendance
	* @param actualDuration The new actual duration to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setActualDuration(String actualDuration) {
		setProperty("actualDuration", actualDuration);
		return this;
	}


	/**
	* Gets the record owner of this appointment attendance
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this AppointmentAttendance
	* @param recordOwner The new record owner to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this appointment attendance
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this AppointmentAttendance
	* @param text The new text to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the actual interaction type of this appointment attendance
	* @return actualInteractionType
	*/
	public String getActualInteractionType() {
		return getProperty("actualInteractionType");
	}


	/**
	* Changes the actual interaction type of this AppointmentAttendance
	* @param actualInteractionType The new actual interaction type to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setActualInteractionType(String actualInteractionType) {
		setProperty("actualInteractionType", actualInteractionType);
		return this;
	}


	/**
	* Gets the appointment slot of this appointment attendance
	* @return appointmentSlot
	*/
	public String getAppointmentSlot() {
		return getProperty("appointmentSlot");
	}


	/**
	* Changes the appointment slot of this AppointmentAttendance
	* @param appointmentSlot The new appointment slot to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setAppointmentSlot(String appointmentSlot) {
		setProperty("appointmentSlot", appointmentSlot);
		return this;
	}


	/**
	* Gets the patient of this appointment attendance
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this AppointmentAttendance
	* @param patient The new patient to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the status of this appointment attendance
	* @return status
	*/
	public String getStatus() {
		return getProperty("status");
	}


	/**
	* Changes the status of this AppointmentAttendance
	* @param status The new status to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setStatus(String status) {
		setProperty("status", status);
		return this;
	}


	/**
	* Gets the concept of this appointment attendance
	* @return concept
	*/
	public UUID getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this AppointmentAttendance
	* @param concept The new concept to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setConcept(UUID concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the original concept of this appointment attendance
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this AppointmentAttendance
	* @param originalConcept The new original concept to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this appointment attendance
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this AppointmentAttendance
	* @param effectiveDate The new effective date to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this appointment attendance
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this AppointmentAttendance
	* @param endDate The new end date to set
	* @return AppointmentAttendance
	*/
	public AppointmentAttendance setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

