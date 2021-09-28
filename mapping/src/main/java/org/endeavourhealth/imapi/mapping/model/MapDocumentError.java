package org.endeavourhealth.imapi.mapping.model;

public class MapDocumentError {
    private String message;
    private int lineNumber;

    public MapDocumentError(String exceptionMessage) {
        this.message = exceptionMessage;
        if (exceptionMessage.contains("[")) {
            String[] parts = exceptionMessage.split("\\[");
            this.lineNumber = Integer.parseInt(parts[1].replaceAll("[^0-9]+", ""));
        }
    }

    public String getMessage() {
        return message;
    }

    public MapDocumentError setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public MapDocumentError setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }
}
