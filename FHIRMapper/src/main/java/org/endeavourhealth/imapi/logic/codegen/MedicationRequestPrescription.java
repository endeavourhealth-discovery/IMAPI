package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.UUID;

/**
* Represents medication request prescription.
* The actual prescription for, or issue of a medication item. Seperate from the authorisation process
*/
public class MedicationRequestPrescription extends IMDMBase<MedicationRequestPrescription> {


	/**
	* Medication request prescription constructor with identifier
	*/
	public MedicationRequestPrescription(UUID id) {
		super("MedicationRequestPrescription", id);
	}

	/**
	* Gets the record owner of this medication request prescription
	* @return recordOwner
	*/
	public UUID getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this MedicationRequestPrescription
	* @param recordOwner The new record owner to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setRecordOwner(UUID recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the authorisation of this medication request prescription
	* @return authorisation
	*/
	public UUID getAuthorisation() {
		return getProperty("authorisation");
	}


	/**
	* Changes the authorisation of this MedicationRequestPrescription
	* @param authorisation The new authorisation to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setAuthorisation(UUID authorisation) {
		setProperty("authorisation", authorisation);
		return this;
	}


	/**
	* Gets the text of this medication request prescription
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this MedicationRequestPrescription
	* @param text The new text to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the course type of this medication request prescription
	* @return courseType
	*/
	public String getCourseType() {
		return getProperty("courseType");
	}


	/**
	* Changes the course type of this MedicationRequestPrescription
	* @param courseType The new course type to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setCourseType(String courseType) {
		setProperty("courseType", courseType);
		return this;
	}


	/**
	* Gets the concept of this medication request prescription
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this MedicationRequestPrescription
	* @param concept The new concept to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the patient of this medication request prescription
	* @return patient
	*/
	public UUID getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this MedicationRequestPrescription
	* @param patient The new patient to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setPatient(UUID patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the order Quantity- units of this medication request prescription
	* @return orderQuantityUnits
	*/
	public String getOrderQuantityUnits() {
		return getProperty("orderQuantityUnits");
	}


	/**
	* Changes the order Quantity- units of this MedicationRequestPrescription
	* @param orderQuantityUnits The new order Quantity- units to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setOrderQuantityUnits(String orderQuantityUnits) {
		setProperty("orderQuantityUnits", orderQuantityUnits);
		return this;
	}


	/**
	* Gets the original concept of this medication request prescription
	* @return originalConcept
	*/
	public UUID getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this MedicationRequestPrescription
	* @param originalConcept The new original concept to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setOriginalConcept(UUID originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this medication request prescription
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this MedicationRequestPrescription
	* @param effectiveDate The new effective date to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the order quantity  number of units of this medication request prescription
	* @return orderQuantityNumberOfUnits
	*/
	public Integer getOrderQuantityNumberOfUnits() {
		return getProperty("orderQuantityNumberOfUnits");
	}


	/**
	* Changes the order quantity  number of units of this MedicationRequestPrescription
	* @param orderQuantityNumberOfUnits The new order quantity  number of units to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setOrderQuantityNumberOfUnits(Integer orderQuantityNumberOfUnits) {
		setProperty("orderQuantityNumberOfUnits", orderQuantityNumberOfUnits);
		return this;
	}


	/**
	* Gets the end date of this medication request prescription
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this MedicationRequestPrescription
	* @param endDate The new end date to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the prescription duration of this medication request prescription
	* @return prescriptionDuration
	*/
	public String getPrescriptionDuration() {
		return getProperty("prescriptionDuration");
	}


	/**
	* Changes the prescription duration of this MedicationRequestPrescription
	* @param prescriptionDuration The new prescription duration to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setPrescriptionDuration(String prescriptionDuration) {
		setProperty("prescriptionDuration", prescriptionDuration);
		return this;
	}


	/**
	* Gets the prescription duration units of this medication request prescription
	* @return prescriptionDurationUnits
	*/
	public String getPrescriptionDurationUnits() {
		return getProperty("prescriptionDurationUnits");
	}


	/**
	* Changes the prescription duration units of this MedicationRequestPrescription
	* @param prescriptionDurationUnits The new prescription duration units to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setPrescriptionDurationUnits(String prescriptionDurationUnits) {
		setProperty("prescriptionDurationUnits", prescriptionDurationUnits);
		return this;
	}


	/**
	* Gets the additional instructions of this medication request prescription
	* @return additionalInstructions
	*/
	public String getAdditionalInstructions() {
		return getProperty("additionalInstructions");
	}


	/**
	* Changes the additional instructions of this MedicationRequestPrescription
	* @param additionalInstructions The new additional instructions to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setAdditionalInstructions(String additionalInstructions) {
		setProperty("additionalInstructions", additionalInstructions);
		return this;
	}


	/**
	* Gets the dosage of this medication request prescription
	* @return dosage
	*/
	public String getDosage() {
		return getProperty("dosage");
	}


	/**
	* Changes the dosage of this MedicationRequestPrescription
	* @param dosage The new dosage to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setDosage(String dosage) {
		setProperty("dosage", dosage);
		return this;
	}


	/**
	* Gets the number from authorised count of this medication request prescription
	* @return numberFromAuthorisedCount
	*/
	public String getNumberFromAuthorisedCount() {
		return getProperty("numberFromAuthorisedCount");
	}


	/**
	* Changes the number from authorised count of this MedicationRequestPrescription
	* @param numberFromAuthorisedCount The new number from authorised count to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setNumberFromAuthorisedCount(String numberFromAuthorisedCount) {
		setProperty("numberFromAuthorisedCount", numberFromAuthorisedCount);
		return this;
	}


	/**
	* Gets the pharmacy instructions of this medication request prescription
	* @return pharmacyInstructions
	*/
	public String getPharmacyInstructions() {
		return getProperty("pharmacyInstructions");
	}


	/**
	* Changes the pharmacy instructions of this MedicationRequestPrescription
	* @param pharmacyInstructions The new pharmacy instructions to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setPharmacyInstructions(String pharmacyInstructions) {
		setProperty("pharmacyInstructions", pharmacyInstructions);
		return this;
	}


	/**
	* Gets the authorisation status of this medication request prescription
	* @return authorisationStatus
	*/
	public String getAuthorisationStatus() {
		return getProperty("authorisationStatus");
	}


	/**
	* Changes the authorisation status of this MedicationRequestPrescription
	* @param authorisationStatus The new authorisation status to set
	* @return MedicationRequestPrescription
	*/
	public MedicationRequestPrescription setAuthorisationStatus(String authorisationStatus) {
		setProperty("authorisationStatus", authorisationStatus);
		return this;
	}
}

