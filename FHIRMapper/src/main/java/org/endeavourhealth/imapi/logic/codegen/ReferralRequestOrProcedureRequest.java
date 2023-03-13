package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents referral request or procedure request
* A referral request or (procedure request) includes request for advice or invitation to participate in care and is not limited to conventional referrals. A referral request often precedes the encounter or care transfer that occurs subsequently. Furthermore a referral request may accompany a care transfer e.g.... a request for input from a community health professional during the discharge process. The referral type is considered as the core observation concept<p>Referral inherits attribution
*/
public class ReferralRequestOrProcedureRequest extends IMDMBase<ReferralRequestOrProcedureRequest> {


	/**
	* Referral request or procedure request constructor 
	*/
	public ReferralRequestOrProcedureRequest() {
		super("ReferralRequestOrProcedureRequest");
	}

	/**
	* Referral request or procedure request constructor with identifier
	*/
	public ReferralRequestOrProcedureRequest(String id) {
		super("ReferralRequestOrProcedureRequest", id);
	}

	/**
	* Gets the linked episode of this referral request or procedure request
	* @return linkedEpisode
	*/
	public String getLinkedEpisode() {
		return getProperty("linkedEpisode");
	}


	/**
	* Changes the linked episode of this ReferralRequestOrProcedureRequest
	* @param linkedEpisode The new linked episode to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setLinkedEpisode(String linkedEpisode) {
		setProperty("linkedEpisode", linkedEpisode);
		return this;
	}


	/**
	* Gets the record owner of this referral request or procedure request
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this ReferralRequestOrProcedureRequest
	* @param recordOwner The new record owner to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this referral request or procedure request
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this ReferralRequestOrProcedureRequest
	* @param text The new text to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the priority of this referral request or procedure request
	* @return priority
	*/
	public String getPriority() {
		return getProperty("priority");
	}


	/**
	* Changes the priority of this ReferralRequestOrProcedureRequest
	* @param priority The new priority to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setPriority(String priority) {
		setProperty("priority", priority);
		return this;
	}


	/**
	* Gets the procedure or Service type requested of this referral request or procedure request
	* @return procedureOrServiceTypeRequested
	*/
	public String getProcedureOrServiceTypeRequested() {
		return getProperty("procedureOrServiceTypeRequested");
	}


	/**
	* Changes the procedure or Service type requested of this ReferralRequestOrProcedureRequest
	* @param procedureOrServiceTypeRequested The new procedure or Service type requested to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setProcedureOrServiceTypeRequested(String procedureOrServiceTypeRequested) {
		setProperty("procedureOrServiceTypeRequested", procedureOrServiceTypeRequested);
		return this;
	}


	/**
	* Gets the patient of this referral request or procedure request
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this ReferralRequestOrProcedureRequest
	* @param patient The new patient to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the recipient location of this referral request or procedure request
	* @return recipientLocation
	*/
	public String getRecipientLocation() {
		return getProperty("recipientLocation");
	}


	/**
	* Changes the recipient location of this ReferralRequestOrProcedureRequest
	* @param recipientLocation The new recipient location to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setRecipientLocation(String recipientLocation) {
		setProperty("recipientLocation", recipientLocation);
		return this;
	}


	/**
	* Gets the concept of this referral request or procedure request
	* @return concept
	*/
	public TerminologyConcept getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this ReferralRequestOrProcedureRequest
	* @param concept The new concept to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setConcept(TerminologyConcept concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the recipient practitioner of this referral request or procedure request
	* @return recipientPractitioner
	*/
	public String getRecipientPractitioner() {
		return getProperty("recipientPractitioner");
	}


	/**
	* Changes the recipient practitioner of this ReferralRequestOrProcedureRequest
	* @param recipientPractitioner The new recipient practitioner to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setRecipientPractitioner(String recipientPractitioner) {
		setProperty("recipientPractitioner", recipientPractitioner);
		return this;
	}


	/**
	* Gets the original concept of this referral request or procedure request
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this ReferralRequestOrProcedureRequest
	* @param originalConcept The new original concept to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this referral request or procedure request
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this ReferralRequestOrProcedureRequest
	* @param effectiveDate The new effective date to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the recipient service or organisation of this referral request or procedure request
	* @return recipientServiceOrOrganisation
	*/
	public String getRecipientServiceOrOrganisation() {
		return getProperty("recipientServiceOrOrganisation");
	}


	/**
	* Changes the recipient service or organisation of this ReferralRequestOrProcedureRequest
	* @param recipientServiceOrOrganisation The new recipient service or organisation to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setRecipientServiceOrOrganisation(String recipientServiceOrOrganisation) {
		setProperty("recipientServiceOrOrganisation", recipientServiceOrOrganisation);
		return this;
	}


	/**
	* Gets the end date of this referral request or procedure request
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this ReferralRequestOrProcedureRequest
	* @param endDate The new end date to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the referral mode of this referral request or procedure request
	* @return referralMode
	*/
	public String getReferralMode() {
		return getProperty("referralMode");
	}


	/**
	* Changes the referral mode of this ReferralRequestOrProcedureRequest
	* @param referralMode The new referral mode to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setReferralMode(String referralMode) {
		setProperty("referralMode", referralMode);
		return this;
	}


	/**
	* Gets the referred by type of this referral request or procedure request
	* @return referredByType
	*/
	public String getReferredByType() {
		return getProperty("referredByType");
	}


	/**
	* Changes the referred by type of this ReferralRequestOrProcedureRequest
	* @param referredByType The new referred by type to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setReferredByType(String referredByType) {
		setProperty("referredByType", referredByType);
		return this;
	}


	/**
	* Gets the request Reason of this referral request or procedure request
	* @return requestReason
	*/
	public String getRequestReason() {
		return getProperty("requestReason");
	}


	/**
	* Changes the request Reason of this ReferralRequestOrProcedureRequest
	* @param requestReason The new request Reason to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setRequestReason(String requestReason) {
		setProperty("requestReason", requestReason);
		return this;
	}


	/**
	* Gets the source organisation of this referral request or procedure request
	* @return sourceOrganisation
	*/
	public Organisation getSourceOrganisation() {
		return getProperty("sourceOrganisation");
	}


	/**
	* Changes the source organisation of this ReferralRequestOrProcedureRequest
	* @param sourceOrganisation The new source organisation to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setSourceOrganisation(Organisation sourceOrganisation) {
		setProperty("sourceOrganisation", sourceOrganisation);
		return this;
	}


	/**
	* Gets the speciality requested of this referral request or procedure request
	* @return specialityRequested
	*/
	public String getSpecialityRequested() {
		return getProperty("specialityRequested");
	}


	/**
	* Changes the speciality requested of this ReferralRequestOrProcedureRequest
	* @param specialityRequested The new speciality requested to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setSpecialityRequested(String specialityRequested) {
		setProperty("specialityRequested", specialityRequested);
		return this;
	}


	/**
	* Gets the referral UBRN of this referral request or procedure request
	* @return referralUbrn
	*/
	public String getReferralUbrn() {
		return getProperty("referralUbrn");
	}


	/**
	* Changes the referral UBRN of this ReferralRequestOrProcedureRequest
	* @param referralUbrn The new referral UBRN to set
	* @return ReferralRequestOrProcedureRequest
	*/
	public ReferralRequestOrProcedureRequest setReferralUbrn(String referralUbrn) {
		setProperty("referralUbrn", referralUbrn);
		return this;
	}
}

