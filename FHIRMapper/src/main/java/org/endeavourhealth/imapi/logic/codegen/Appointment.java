package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents appointment.
* This is information about a particular appointment. Does not include the processes involved in slot booking itself
*/
public class Appointment extends IMDMBase<Appointment> {


	/**
	* Appointment constructor with identifier
	*/
	public Appointment(UUID id) {
		super("Appointment", id);
	}

	/**
	* Gets the appointment category of this appointment
	* @return appointmentCategory
	*/
	public String getAppointmentCategory() {
		return getProperty("appointmentCategory");
	}


	/**
	* Changes the appointment category of this Appointment
	* @param appointmentCategory The new appointment category to set
	* @return Appointment
	*/
	public Appointment setAppointmentCategory(String appointmentCategory) {
		setProperty("appointmentCategory", appointmentCategory);
		return this;
	}


	/**
	* Gets the record owner of this appointment
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this Appointment
	* @param recordOwner The new record owner to set
	* @return Appointment
	*/
	public Appointment setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this appointment
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this Appointment
	* @param text The new text to set
	* @return Appointment
	*/
	public Appointment setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the attendance history of this appointment
	* @return attendanceHistory
	*/
	public String getAttendanceHistory() {
		return getProperty("attendanceHistory");
	}


	/**
	* Changes the attendance history of this Appointment
	* @param attendanceHistory The new attendance history to set
	* @return Appointment
	*/
	public Appointment setAttendanceHistory(String attendanceHistory) {
		setProperty("attendanceHistory", attendanceHistory);
		return this;
	}


	/**
	* Gets the attendance status of this appointment
	* @return attendanceStatus
	*/
	public String getAttendanceStatus() {
		return getProperty("attendanceStatus");
	}


	/**
	* Changes the attendance status of this Appointment
	* @param attendanceStatus The new attendance status to set
	* @return Appointment
	*/
	public Appointment setAttendanceStatus(String attendanceStatus) {
		setProperty("attendanceStatus", attendanceStatus);
		return this;
	}


	/**
	* Gets the patient of this appointment
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this Appointment
	* @param patient The new patient to set
	* @return Appointment
	*/
	public Appointment setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the booking history of this appointment
	* @return bookingHistory
	*/
	public String getBookingHistory() {
		return getProperty("bookingHistory");
	}


	/**
	* Changes the booking history of this Appointment
	* @param bookingHistory The new booking history to set
	* @return Appointment
	*/
	public Appointment setBookingHistory(String bookingHistory) {
		setProperty("bookingHistory", bookingHistory);
		return this;
	}


	/**
	* Gets the concept of this appointment
	* @return concept
	*/
	public UUID getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this Appointment
	* @param concept The new concept to set
	* @return Appointment
	*/
	public Appointment setConcept(UUID concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the booking urgency of this appointment
	* @return bookingUrgency
	*/
	public String getBookingUrgency() {
		return getProperty("bookingUrgency");
	}


	/**
	* Changes the booking urgency of this Appointment
	* @param bookingUrgency The new booking urgency to set
	* @return Appointment
	*/
	public Appointment setBookingUrgency(String bookingUrgency) {
		setProperty("bookingUrgency", bookingUrgency);
		return this;
	}


	/**
	* Gets the original concept of this appointment
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this Appointment
	* @param originalConcept The new original concept to set
	* @return Appointment
	*/
	public Appointment setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this appointment
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this Appointment
	* @param effectiveDate The new effective date to set
	* @return Appointment
	*/
	public Appointment setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the interaction type of this appointment
	* @return interactionType
	*/
	public String getInteractionType() {
		return getProperty("interactionType");
	}


	/**
	* Changes the interaction type of this Appointment
	* @param interactionType The new interaction type to set
	* @return Appointment
	*/
	public Appointment setInteractionType(String interactionType) {
		setProperty("interactionType", interactionType);
		return this;
	}


	/**
	* Gets the end date of this appointment
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this Appointment
	* @param endDate The new end date to set
	* @return Appointment
	*/
	public Appointment setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the linked schedule of this appointment
	* @return linkedSchedule
	*/
	public String getLinkedSchedule() {
		return getProperty("linkedSchedule");
	}


	/**
	* Changes the linked schedule of this Appointment
	* @param linkedSchedule The new linked schedule to set
	* @return Appointment
	*/
	public Appointment setLinkedSchedule(String linkedSchedule) {
		setProperty("linkedSchedule", linkedSchedule);
		return this;
	}


	/**
	* Gets the planned duration of this appointment
	* @return plannedDuration
	*/
	public String getPlannedDuration() {
		return getProperty("plannedDuration");
	}


	/**
	* Changes the planned duration of this Appointment
	* @param plannedDuration The new planned duration to set
	* @return Appointment
	*/
	public Appointment setPlannedDuration(String plannedDuration) {
		setProperty("plannedDuration", plannedDuration);
		return this;
	}


	/**
	* Gets the planned Reason of this appointment
	* @return plannedReason
	*/
	public String getPlannedReason() {
		return getProperty("plannedReason");
	}


	/**
	* Changes the planned Reason of this Appointment
	* @param plannedReason The new planned Reason to set
	* @return Appointment
	*/
	public Appointment setPlannedReason(String plannedReason) {
		setProperty("plannedReason", plannedReason);
		return this;
	}


	/**
	* Gets the slot booking status of this appointment
	* @return slotBookingStatus
	*/
	public String getSlotBookingStatus() {
		return getProperty("slotBookingStatus");
	}


	/**
	* Changes the slot booking status of this Appointment
	* @param slotBookingStatus The new slot booking status to set
	* @return Appointment
	*/
	public Appointment setSlotBookingStatus(String slotBookingStatus) {
		setProperty("slotBookingStatus", slotBookingStatus);
		return this;
	}


	/**
	* Gets the description of this appointment
	* @return description
	*/
	public String getDescription() {
		return getProperty("description");
	}


	/**
	* Changes the description of this Appointment
	* @param description The new description to set
	* @return Appointment
	*/
	public Appointment setDescription(String description) {
		setProperty("description", description);
		return this;
	}
}

