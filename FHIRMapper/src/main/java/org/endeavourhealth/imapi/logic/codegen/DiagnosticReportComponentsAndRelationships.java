package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDateTime;

/**
* Represents diagnostic report components and relationships
* Below are the list of common components of a diagnostic report
*/
public class DiagnosticReportComponentsAndRelationships extends IMDMBase<DiagnosticReportComponentsAndRelationships> {


	/**
	* Diagnostic report components and relationships constructor 
	*/
	public DiagnosticReportComponentsAndRelationships() {
		super("DiagnosticReportComponentsAndRelationships");
	}

	/**
	* Diagnostic report components and relationships constructor with identifier
	*/
	public DiagnosticReportComponentsAndRelationships(String id) {
		super("DiagnosticReportComponentsAndRelationships", id);
	}

	/**
	* Gets the imaging study of this diagnostic report components and relationships
	* @return imagingStudy
	*/
	public String getImagingStudy() {
		return getProperty("imagingStudy");
	}


	/**
	* Changes the imaging study of this DiagnosticReportComponentsAndRelationships
	* @param imagingStudy The new imaging study to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setImagingStudy(String imagingStudy) {
		setProperty("imagingStudy", imagingStudy);
		return this;
	}


	/**
	* Gets the record owner of this diagnostic report components and relationships
	* @return recordOwner
	*/
	public Organisation getRecordOwner() {
		return getProperty("recordOwner");
	}


	/**
	* Changes the record owner of this DiagnosticReportComponentsAndRelationships
	* @param recordOwner The new record owner to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setRecordOwner(Organisation recordOwner) {
		setProperty("recordOwner", recordOwner);
		return this;
	}


	/**
	* Gets the text of this diagnostic report components and relationships
	* @return text
	*/
	public String getText() {
		return getProperty("text");
	}


	/**
	* Changes the text of this DiagnosticReportComponentsAndRelationships
	* @param text The new text to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setText(String text) {
		setProperty("text", text);
		return this;
	}


	/**
	* Gets the media of this diagnostic report components and relationships
	* @return media
	*/
	public String getMedia() {
		return getProperty("media");
	}


	/**
	* Changes the media of this DiagnosticReportComponentsAndRelationships
	* @param media The new media to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setMedia(String media) {
		setProperty("media", media);
		return this;
	}


	/**
	* Gets the observation cluster or battery of this diagnostic report components and relationships
	* @return observationClusterOrBattery
	*/
	public String getObservationClusterOrBattery() {
		return getProperty("observationClusterOrBattery");
	}


	/**
	* Changes the observation cluster or battery of this DiagnosticReportComponentsAndRelationships
	* @param observationClusterOrBattery The new observation cluster or battery to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setObservationClusterOrBattery(String observationClusterOrBattery) {
		setProperty("observationClusterOrBattery", observationClusterOrBattery);
		return this;
	}


	/**
	* Gets the patient of this diagnostic report components and relationships
	* @return patient
	*/
	public Patient getPatient() {
		return getProperty("patient");
	}


	/**
	* Changes the patient of this DiagnosticReportComponentsAndRelationships
	* @param patient The new patient to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setPatient(Patient patient) {
		setProperty("patient", patient);
		return this;
	}


	/**
	* Gets the observation results of this diagnostic report components and relationships
	* @return observationResults
	*/
	public String getObservationResults() {
		return getProperty("observationResults");
	}


	/**
	* Changes the observation results of this DiagnosticReportComponentsAndRelationships
	* @param observationResults The new observation results to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setObservationResults(String observationResults) {
		setProperty("observationResults", observationResults);
		return this;
	}


	/**
	* Gets the concept of this diagnostic report components and relationships
	* @return concept
	*/
	public TerminologyConcept getConcept() {
		return getProperty("concept");
	}


	/**
	* Changes the concept of this DiagnosticReportComponentsAndRelationships
	* @param concept The new concept to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setConcept(TerminologyConcept concept) {
		setProperty("concept", concept);
		return this;
	}


	/**
	* Gets the request of this diagnostic report components and relationships
	* @return request
	*/
	public String getRequest() {
		return getProperty("request");
	}


	/**
	* Changes the request of this DiagnosticReportComponentsAndRelationships
	* @param request The new request to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setRequest(String request) {
		setProperty("request", request);
		return this;
	}


	/**
	* Gets the original concept of this diagnostic report components and relationships
	* @return originalConcept
	*/
	public TerminologyConcept getOriginalConcept() {
		return getProperty("originalConcept");
	}


	/**
	* Changes the original concept of this DiagnosticReportComponentsAndRelationships
	* @param originalConcept The new original concept to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setOriginalConcept(TerminologyConcept originalConcept) {
		setProperty("originalConcept", originalConcept);
		return this;
	}


	/**
	* Gets the effective date of this diagnostic report components and relationships
	* @return effectiveDate
	*/
	public PartialDateTime getEffectiveDate() {
		return getProperty("effectiveDate");
	}


	/**
	* Changes the effective date of this DiagnosticReportComponentsAndRelationships
	* @param effectiveDate The new effective date to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setEffectiveDate(PartialDateTime effectiveDate) {
		setProperty("effectiveDate", effectiveDate);
		return this;
	}


	/**
	* Gets the specimen of this diagnostic report components and relationships
	* @return specimen
	*/
	public String getSpecimen() {
		return getProperty("specimen");
	}


	/**
	* Changes the specimen of this DiagnosticReportComponentsAndRelationships
	* @param specimen The new specimen to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setSpecimen(String specimen) {
		setProperty("specimen", specimen);
		return this;
	}


	/**
	* Gets the end date of this diagnostic report components and relationships
	* @return endDate
	*/
	public PartialDateTime getEndDate() {
		return getProperty("endDate");
	}


	/**
	* Changes the end date of this DiagnosticReportComponentsAndRelationships
	* @param endDate The new end date to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setEndDate(PartialDateTime endDate) {
		setProperty("endDate", endDate);
		return this;
	}


	/**
	* Gets the diagnostic service of this diagnostic report components and relationships
	* @return diagnosticService
	*/
	public String getDiagnosticService() {
		return getProperty("diagnosticService");
	}


	/**
	* Changes the diagnostic service of this DiagnosticReportComponentsAndRelationships
	* @param diagnosticService The new diagnostic service to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setDiagnosticService(String diagnosticService) {
		setProperty("diagnosticService", diagnosticService);
		return this;
	}


	/**
	* Gets the identifier of this diagnostic report components and relationships
	* @return identifier
	*/
	public String getIdentifier() {
		return getProperty("identifier");
	}


	/**
	* Changes the identifier of this DiagnosticReportComponentsAndRelationships
	* @param identifier The new identifier to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setIdentifier(String identifier) {
		setProperty("identifier", identifier);
		return this;
	}


	/**
	* Gets the report type of this diagnostic report components and relationships
	* @return reportType
	*/
	public String getReportType() {
		return getProperty("reportType");
	}


	/**
	* Changes the report type of this DiagnosticReportComponentsAndRelationships
	* @param reportType The new report type to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setReportType(String reportType) {
		setProperty("reportType", reportType);
		return this;
	}


	/**
	* Gets the service category of this diagnostic report components and relationships
	* @return serviceCategory
	*/
	public String getServiceCategory() {
		return getProperty("serviceCategory");
	}


	/**
	* Changes the service category of this DiagnosticReportComponentsAndRelationships
	* @param serviceCategory The new service category to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setServiceCategory(String serviceCategory) {
		setProperty("serviceCategory", serviceCategory);
		return this;
	}


	/**
	* Gets the report issue date of this diagnostic report components and relationships
	* @return reportIssueDate
	*/
	public String getReportIssueDate() {
		return getProperty("reportIssueDate");
	}


	/**
	* Changes the report issue date of this DiagnosticReportComponentsAndRelationships
	* @param reportIssueDate The new report issue date to set
	* @return DiagnosticReportComponentsAndRelationships
	*/
	public DiagnosticReportComponentsAndRelationships setReportIssueDate(String reportIssueDate) {
		setProperty("reportIssueDate", reportIssueDate);
		return this;
	}
}

