package org.endeavourhealth.imapi.errorHandling;

public enum ErrorCodes {
    DATA_FORMAT_EXCEPTION ("DataFormatException"),
    UNKNOWN_FORMAT_CONVERSION_EXCEPTION("UnknownFormatConversionException"),
    UNHANDLED_EXCEPTION("UnhandledException");

    private String code;

    public String asString() {
        return code;
    }

    ErrorCodes (String code) {
        this.code = code;
    }
}
