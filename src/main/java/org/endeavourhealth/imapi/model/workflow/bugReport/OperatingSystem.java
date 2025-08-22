package org.endeavourhealth.imapi.model.workflow.bugReport;

public enum OperatingSystem {
    WINDOWS("windows"),
    MACOS("macos"),
    LINUX("linux"),
    OTHER("other");
    private final String text;
    OperatingSystem(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
