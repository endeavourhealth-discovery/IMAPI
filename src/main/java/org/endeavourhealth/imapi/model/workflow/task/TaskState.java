package org.endeavourhealth.imapi.model.workflow.task;

public enum TaskState {
    TODO("to do"),
    IN_PROGRESS("in progress"),
    APPROVED("approved"),
    COMPLETE("complete"),
    REJECTED("rejected"),
    CANCELLED("cancelled"),
    UNDER_REVIEW("under review");

    private String text;
    TaskState (String text) {
        this.text = text;
    }

    public String getText() {
       return this.text;
    }
}
