package org.endeavourhealth.imapi.aws;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
