package org.endeavourhealth.imapi.model.iml;

public class Value {
	private String comparison;
	private String value;

	public String getComparison() {
		return comparison;
	}

	public Value setComparison(String comparison) {
		this.comparison = comparison;
		return this;
	}

	public String getValue() {
		return value;
	}

	public Value setValue(String value) {
		this.value = value;
		return this;
	}
}
