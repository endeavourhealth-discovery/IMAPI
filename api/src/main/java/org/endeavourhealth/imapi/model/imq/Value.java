package org.endeavourhealth.imapi.model.imq;

public class Value implements Assignable{
	private Operator operator;
	private String value;
	private String unit;
	private Property relativeTo;

	public Operator getOperator() {
		return operator;
	}

	public Value setOperator(Operator operator) {
		this.operator = operator;
		return this;
	}

	public Property getRelativeTo() {
		return relativeTo;
	}

	public Value setRelativeTo(Property relativeTo) {
		this.relativeTo = relativeTo;
		return this;
	}


	public String getValue() {
		return value;
	}

	public Value setValue(String value) {
		this.value = value;
		return this;
	}

	public String getUnit() {
		return unit;
	}

	public Value setUnit(String unit) {
		this.unit = unit;
		return this;
	}
}
