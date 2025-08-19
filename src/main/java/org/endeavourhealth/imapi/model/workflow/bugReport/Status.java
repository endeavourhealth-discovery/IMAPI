package org.endeavourhealth.imapi.model.workflow.bugReport;

public enum Status {
    NEW("new"),
    FIXED("fixed"),
    ASSIGNED("assigned"),
    VERIFIED("verified"),
    REOPENED("reopened"),
    WONT_FIX("won't fix");
    private final String text;
    Status(String text) {
        this.text = text;
    }
    public String getText() {
        return this.text;
    }
}
