package org.endeavourhealth.imapi.filer;

public class TaskFilerException extends Exception {
    public TaskFilerException(String message) {
        super(message);
    }

    public TaskFilerException(String message, Throwable exception) {
        super(message, exception);
    }
}
