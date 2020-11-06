package org.endeavourhealth.informationmanager.model;

import java.util.Date;
import java.util.UUID;

public class DocumentInfo {
    private UUID documentId;
    private String documentTitle;
    private Date effectiveDate;
    private String publisher;
    private String purpose;

    public UUID getDocumentId() {
        return documentId;
    }

    public DocumentInfo setDocumentId(UUID documentId) {
        this.documentId = documentId;
        return this;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public DocumentInfo setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
        return this;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public DocumentInfo setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public String getPublisher() {
        return publisher;
    }

    public DocumentInfo setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public String getPurpose() {
        return purpose;
    }

    public DocumentInfo setPurpose(String purpose) {
        this.purpose = purpose;
        return this;
    }
}
