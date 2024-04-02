package org.endeavourhealth.imapi.model.customexceptions;

public class DownloadException extends Exception {
    public DownloadException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public DownloadException(String errorMessage) {
        super(errorMessage);
    }
}
