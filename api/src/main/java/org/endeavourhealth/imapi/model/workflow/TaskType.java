package org.endeavourhealth.imapi.model.workflow;

public enum TaskType {
    BUG_REPORT("bug report"),
    ROLE_REQUEST("role request");

    private String text;

    TaskType (String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
