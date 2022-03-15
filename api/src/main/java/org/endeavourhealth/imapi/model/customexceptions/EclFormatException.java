package org.endeavourhealth.imapi.model.customexceptions;

public class EclFormatException extends Exception {
    public EclFormatException(String errorMessage, Throwable ex) {
        super(errorMessage, ex);
    }
}
