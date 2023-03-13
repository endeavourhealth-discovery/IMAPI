package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents immunisation
* Immunisation extends a simple observation by providing more information about the immunisation procedure and vaccine used.<p>This is a summary of immunisation, (expected to be extended)
*/
public class Immunisation extends IMDMBase<Immunisation> {


	/**
	* Immunisation constructor 
	*/
	public Immunisation() {
		super("Immunisation");
	}

	/**
	* Immunisation constructor with identifier
	*/
	public Immunisation(String id) {
		super("Immunisation", id);
	}

	/**
	* Gets the manufacturer of this immunisation
	* @return manufacturer
	*/
	public String getManufacturer() {
		return getProperty("manufacturer");
	}


	/**
	* Changes the manufacturer of this Immunisation
	* @param manufacturer The new manufacturer to set
	* @return Immunisation
	*/
	public Immunisation setManufacturer(String manufacturer) {
		setProperty("manufacturer", manufacturer);
		return this;
	}


	/**
	* Gets the record owner of this immunisation
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this Immunisation
	* @param recordOwner The new record owner to set
	* @return Immunisation
	*/
	public Immunisation setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this immunisation
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this Immunisation
	* @param text The new text to set
	* @return Immunisation
	*/
	public Immunisation setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the reaction of this immunisation
	* @return reaction
	*/
	public String getReaction() {
		return getProperty("reaction");
	}


	/**
	* Changes the reaction of this Immunisation
	* @param reaction The new reaction to set
	* @return Immunisation
	*/
	public Immunisation setReaction(String reaction) {
		setProperty("reaction", reaction);
		return this;
	}


	/**
	* Gets the concept of this immunisation
	* @return concept
	*/
	public String getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this Immunisation
	* @param concept The new concept to set
	* @return Immunisation
	*/
	public Immunisation setConcept(String concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the patient of this immunisation
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this Immunisation
	* @param patient The new patient to set
	* @return Immunisation
	*/
	public Immunisation setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the vaccine product of this immunisation
	* @return vaccineProduct
	*/
	public String getVaccineProduct() {
		return getProperty("vaccineProduct");
	}


	/**
	* Changes the vaccine product of this Immunisation
	* @param vaccineProduct The new vaccine product to set
	* @return Immunisation
	*/
	public Immunisation setVaccineProduct(String vaccineProduct) {
		setProperty("vaccineProduct", vaccineProduct);
		return this;
	}


	/**
	* Gets the original concept of this immunisation
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this Immunisation
	* @param originalConcept The new original concept to set
	* @return Immunisation
	*/
	public Immunisation setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this immunisation
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this Immunisation
	* @param effectiveDate The new effective date to set
	* @return Immunisation
	*/
	public Immunisation setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the batch number of this immunisation
	* @return batchNumber
	*/
	public String getBatchNumber() {
		return getProperty("batchNumber");
	}


	/**
	* Changes the batch number of this Immunisation
	* @param batchNumber The new batch number to set
	* @return Immunisation
	*/
	public Immunisation setBatchNumber(String batchNumber) {
		setProperty("batchNumber", batchNumber);
		return this;
	}


	/**
	* Gets the end date of this immunisation
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this Immunisation
	* @param endDate The new end date to set
	* @return Immunisation
	*/
	public Immunisation setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the dose sequence of this immunisation
	* @return doseSequence
	*/
	public String getDoseSequence() {
		return getProperty("doseSequence");
	}


	/**
	* Changes the dose sequence of this Immunisation
	* @param doseSequence The new dose sequence to set
	* @return Immunisation
	*/
	public Immunisation setDoseSequence(String doseSequence) {
		setProperty("doseSequence", doseSequence);
		return this;
	}


	/**
	* Gets the doses required of this immunisation
	* @return dosesRequired
	*/
	public String getDosesRequired() {
		return getProperty("dosesRequired");
	}


	/**
	* Changes the doses required of this Immunisation
	* @param dosesRequired The new doses required to set
	* @return Immunisation
	*/
	public Immunisation setDosesRequired(String dosesRequired) {
		setProperty("dosesRequired", dosesRequired);
		return this;
	}


	/**
	* Gets the expiry date of this immunisation
	* @return expiryDate
	*/
	public String getExpiryDate() {
		return getProperty("expiryDate");
	}


	/**
	* Changes the expiry date of this Immunisation
	* @param expiryDate The new expiry date to set
	* @return Immunisation
	*/
	public Immunisation setExpiryDate(String expiryDate) {
		setProperty("expiryDate", expiryDate);
		return this;
	}
}

