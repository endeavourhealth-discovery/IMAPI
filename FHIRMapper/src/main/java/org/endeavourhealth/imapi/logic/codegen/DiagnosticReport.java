package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents diagnostic report
* A diagnostic report is the set of information that is typically provided by a diagnostic service when investigations are complete. It includes a mix of component events often arranged hierarchically, some structured, some unstructured.<p>A diagnostic report has a header and set of components
*/
public class DiagnosticReport extends IMDMBase<DiagnosticReport> {


	/**
	* Diagnostic report constructor 
	*/
	public DiagnosticReport() {
		super("DiagnosticReport");
	}

	/**
	* Diagnostic report constructor with identifier
	*/
	public DiagnosticReport(String id) {
		super("DiagnosticReport", id);
	}

	/**
	* Gets the diagnostic service of this diagnostic report
	* @return diagnosticService
	*/
	public String getDiagnosticService() {
		return getProperty("diagnosticService");
	}


	/**
	* Changes the diagnostic service of this DiagnosticReport
	* @param diagnosticService The new diagnostic service to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setDiagnosticService(String diagnosticService) {
		setProperty("diagnosticService", diagnosticService);
		return this;
	}


	/**
	* Gets the record owner of this diagnostic report
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this DiagnosticReport
	* @param recordOwner The new record owner to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this diagnostic report
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this DiagnosticReport
	* @param text The new text to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the identifier of this diagnostic report
	* @return identifier
	*/
	public String getIdentifier() {
		return getProperty("identifier");
	}


	/**
	* Changes the identifier of this DiagnosticReport
	* @param identifier The new identifier to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setIdentifier(String identifier) {
		setProperty("identifier", identifier);
		return this;
	}


	/**
	* Gets the report type of this diagnostic report
	* @return reportType
	*/
	public String getReportType() {
		return getProperty("reportType");
	}


	/**
	* Changes the report type of this DiagnosticReport
	* @param reportType The new report type to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setReportType(String reportType) {
		setProperty("reportType", reportType);
		return this;
	}


	/**
	* Gets the patient of this diagnostic report
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this DiagnosticReport
	* @param patient The new patient to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the service category of this diagnostic report
	* @return serviceCategory
	*/
	public String getServiceCategory() {
		return getProperty("serviceCategory");
	}


	/**
	* Changes the service category of this DiagnosticReport
	* @param serviceCategory The new service category to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setServiceCategory(String serviceCategory) {
		setProperty("serviceCategory", serviceCategory);
		return this;
	}


	/**
	* Gets the concept of this diagnostic report
	* @return concept
	*/
	public TerminologyConcept getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this DiagnosticReport
	* @param concept The new concept to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setConcept(TerminologyConcept concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the report issue date of this diagnostic report
	* @return reportIssueDate
	*/
	public String getReportIssueDate() {
		return getProperty("reportIssueDate");
	}


	/**
	* Changes the report issue date of this DiagnosticReport
	* @param reportIssueDate The new report issue date to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setReportIssueDate(String reportIssueDate) {
		setProperty("reportIssueDate", reportIssueDate);
		return this;
	}


	/**
	* Gets the original concept of this diagnostic report
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this DiagnosticReport
	* @param originalConcept The new original concept to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this diagnostic report
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this DiagnosticReport
	* @param effectiveDate The new effective date to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the end date of this diagnostic report
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this DiagnosticReport
	* @param endDate The new end date to set
	* @return DiagnosticReport
	*/
	public DiagnosticReport setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}
}

