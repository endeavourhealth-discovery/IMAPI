package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents medication authorisation
* Also referred as Medication statement entries, this entry is  for describing and authorising a course of medication or an intention to prescribe. Medication entries are precursors to the prescribing of a drug (medication order), dispensing of a drug (e.g.... chemist) or the administration of a drug (e.g.... medicine administration by nurse)</span><p>In General practice, acute prescriptions are based on medication entries with a single authorisation and repeat medications are based on medication entries with multiple authorisations. In hospital, the drug chart contains the medications.
*/
public class MedicationAuthorisation extends IMDMBase<MedicationAuthorisation> {


	/**
	* Medication authorisation constructor 
	*/
	public MedicationAuthorisation() {
		super("MedicationAuthorisation");
	}

	/**
	* Medication authorisation constructor with identifier
	*/
	public MedicationAuthorisation(String id) {
		super("MedicationAuthorisation", id);
	}

	/**
	* Gets the course type of this medication authorisation
	* @return courseType
	*/
	public String getCourseType() {
		return getProperty("courseType");
	}


	/**
	* Changes the course type of this MedicationAuthorisation
	* @param courseType The new course type to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setCourseType(String courseType) {
		setProperty("courseType", courseType);
		return this;
	}


	/**
	* Gets the record owner of this medication authorisation
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this MedicationAuthorisation
	* @param recordOwner The new record owner to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the course status of this medication authorisation
	* @return courseStatus
	*/
	public String getCourseStatus() {
		return getProperty("courseStatus");
	}


	/**
	* Changes the course status of this MedicationAuthorisation
	* @param courseStatus The new course status to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setCourseStatus(String courseStatus) {
		setProperty("courseStatus", courseStatus);
		return this;
	}


	/**
	* Gets the text of this medication authorisation
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this MedicationAuthorisation
	* @param text The new text to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the management authority of this medication authorisation
	* @return managementAuthority
	*/
	public String getManagementAuthority() {
		return getProperty("managementAuthority");
	}


	/**
	* Changes the management authority of this MedicationAuthorisation
	* @param managementAuthority The new management authority to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setManagementAuthority(String managementAuthority) {
		setProperty("managementAuthority", managementAuthority);
		return this;
	}


	/**
	* Gets the patient of this medication authorisation
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this MedicationAuthorisation
	* @param patient The new patient to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the concept of this medication authorisation
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this MedicationAuthorisation
	* @param concept The new concept to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the original concept of this medication authorisation
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this MedicationAuthorisation
	* @param originalConcept The new original concept to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this medication authorisation
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this MedicationAuthorisation
	* @param effectiveDate The new effective date to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the originated by of this medication authorisation
	* @return originatedBy
	*/
	public Organisation getOriginatedBy() {
		return getProperty("originatedBy");
	}


	/**
	* Changes the originated by of this MedicationAuthorisation
	* @param originatedBy The new originated by to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setOriginatedBy(Organisation originatedBy) {
		setProperty("originatedBy", originatedBy);
		return this;
	}


	/**
	* Gets the prescription duration of this medication authorisation
	* @return prescriptionDuration
	*/
	public Integer getPrescriptionDuration() {
		return getProperty("prescriptionDuration");
	}


	/**
	* Changes the prescription duration of this MedicationAuthorisation
	* @param prescriptionDuration The new prescription duration to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setPrescriptionDuration(Integer prescriptionDuration) {
		setProperty("prescriptionDuration", prescriptionDuration);
		return this;
	}


	/**
	* Gets the prescription duration units of this medication authorisation
	* @return prescriptionDurationUnits
	*/
	public String getPrescriptionDurationUnits() {
		return getProperty("prescriptionDurationUnits");
	}


	/**
	* Changes the prescription duration units of this MedicationAuthorisation
	* @param prescriptionDurationUnits The new prescription duration units to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setPrescriptionDurationUnits(String prescriptionDurationUnits) {
		setProperty("prescriptionDurationUnits", prescriptionDurationUnits);
		return this;
	}


	/**
	* Gets the order Quantity- units of this medication authorisation
	* @return orderQuantityUnits
	*/
	public String getOrderQuantityUnits() {
		return getProperty("orderQuantityUnits");
	}


	/**
	* Changes the order Quantity- units of this MedicationAuthorisation
	* @param orderQuantityUnits The new order Quantity- units to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setOrderQuantityUnits(String orderQuantityUnits) {
		setProperty("orderQuantityUnits", orderQuantityUnits);
		return this;
	}


	/**
	* Gets the additional instructions of this medication authorisation
	* @return additionalInstructions
	*/
	public String getAdditionalInstructions() {
		return getProperty("additionalInstructions");
	}


	/**
	* Changes the additional instructions of this MedicationAuthorisation
	* @param additionalInstructions The new additional instructions to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setAdditionalInstructions(String additionalInstructions) {
		setProperty("additionalInstructions", additionalInstructions);
		return this;
	}


	/**
	* Gets the dosage of this medication authorisation
	* @return dosage
	*/
	public String getDosage() {
		return getProperty("dosage");
	}


	/**
	* Changes the dosage of this MedicationAuthorisation
	* @param dosage The new dosage to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setDosage(String dosage) {
		setProperty("dosage", dosage);
		return this;
	}


	/**
	* Gets the end date of this medication authorisation
	* @return endDate
	*/
	public String getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this MedicationAuthorisation
	* @param endDate The new end date to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setEndDate(String endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the medication review of this medication authorisation
	* @return medicationReview
	*/
	public String getMedicationReview() {
		return getProperty("medicationReview");
	}


	/**
	* Changes the medication review of this MedicationAuthorisation
	* @param medicationReview The new medication review to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setMedicationReview(String medicationReview) {
		setProperty("medicationReview", medicationReview);
		return this;
	}


	/**
	* Gets the number repeats authorised of this medication authorisation
	* @return numberRepeatsAuthorised
	*/
	public String getNumberRepeatsAuthorised() {
		return getProperty("numberRepeatsAuthorised");
	}


	/**
	* Changes the number repeats authorised of this MedicationAuthorisation
	* @param numberRepeatsAuthorised The new number repeats authorised to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setNumberRepeatsAuthorised(String numberRepeatsAuthorised) {
		setProperty("numberRepeatsAuthorised", numberRepeatsAuthorised);
		return this;
	}


	/**
	* Gets the order in heading of this medication authorisation
	* @return orderInHeading
	*/
	public String getOrderInHeading() {
		return getProperty("orderInHeading");
	}


	/**
	* Changes the order in heading of this MedicationAuthorisation
	* @param orderInHeading The new order in heading to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setOrderInHeading(String orderInHeading) {
		setProperty("orderInHeading", orderInHeading);
		return this;
	}


	/**
	* Gets the pharmacy instructions of this medication authorisation
	* @return pharmacyInstructions
	*/
	public String getPharmacyInstructions() {
		return getProperty("pharmacyInstructions");
	}


	/**
	* Changes the pharmacy instructions of this MedicationAuthorisation
	* @param pharmacyInstructions The new pharmacy instructions to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setPharmacyInstructions(String pharmacyInstructions) {
		setProperty("pharmacyInstructions", pharmacyInstructions);
		return this;
	}


	/**
	* Gets the reason for ending of this medication authorisation
	* @return reasonForEnding
	*/
	public String getReasonForEnding() {
		return getProperty("reasonForEnding");
	}


	/**
	* Changes the reason for ending of this MedicationAuthorisation
	* @param reasonForEnding The new reason for ending to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setReasonForEnding(String reasonForEnding) {
		setProperty("reasonForEnding", reasonForEnding);
		return this;
	}


	/**
	* Gets the order quantity  number of units of this medication authorisation
	* @return orderQuantityNumberOfUnits
	*/
	public Integer getOrderQuantityNumberOfUnits() {
		return getProperty("orderQuantityNumberOfUnits");
	}


	/**
	* Changes the order quantity  number of units of this MedicationAuthorisation
	* @param orderQuantityNumberOfUnits The new order quantity  number of units to set
	* @return MedicationAuthorisation
	*/
	public MedicationAuthorisation setOrderQuantityNumberOfUnits(Integer orderQuantityNumberOfUnits) {
		setProperty("orderQuantityNumberOfUnits", orderQuantityNumberOfUnits);
		return this;
	}
}

