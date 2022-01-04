package org.endeavourhealth.imapi.query;

public class Argument {
	private String parameter;
	private String value;

	public String getParameter() {
		return parameter;
	}

	public Argument setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public String getValue() {
		return value;
	}

	public Argument setValue(String value) {
		this.value = value;
		return this;
	}
}
