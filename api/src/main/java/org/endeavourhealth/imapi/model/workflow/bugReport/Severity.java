package org.endeavourhealth.imapi.model.workflow.bugReport;

public enum Severity {
    CRITICAL("critical"),
    MAJOR("major"),
    MINOR("minor"),
    TRIVIAL("trivial"),
    ENHANCEMENT("enhancement");
    private String text;
    Severity(String text) {
        this.text = text;
    }
    public String getText() {
        return this.text;
    }
}
