package org.endeavourhealth.imapi.model.workflow.bugReport;

public enum TaskModule {
    DIRECTORY("directory"),
    QUERY("query"),
    CREATOR("creator"),
    EDITOR("editor"),
    UPRN("uprn"),
    AUTH("auth");

    private String text;
    TaskModule(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
