package org.endeavourhealth.imapi.model.ecl;

public class EclBuilderException extends Exception{
	public EclBuilderException(String message) {
		super(message);
	}

	public EclBuilderException(String message, Throwable exception) {
		super(message, exception);
	}
}
