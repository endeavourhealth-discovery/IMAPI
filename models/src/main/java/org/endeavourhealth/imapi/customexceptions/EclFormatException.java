package org.endeavourhealth.imapi.customexceptions;

public class EclFormatException extends Exception {
    public EclFormatException(String errorMessage, Throwable ex) {
        super(errorMessage, ex);
    }
}
