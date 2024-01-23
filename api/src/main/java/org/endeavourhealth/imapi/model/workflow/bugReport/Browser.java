package org.endeavourhealth.imapi.model.workflow.bugReport;

public enum Browser {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    IE("internet explorer"),
    OTHER("other");

    private String text;
    Browser(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
