package org.endeavourhealth.imapi.model.customexceptions;

public class OpenSearchException extends Exception {
    public OpenSearchException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public OpenSearchException(String errorMessage) {
        super(errorMessage);
    }
}
