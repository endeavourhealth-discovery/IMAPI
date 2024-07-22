package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Value implements Assignable{
	private Operator operator;
	private String value;
	private String unit;



	public Operator getOperator() {
		return operator;
	}

	public Value setOperator(Operator operator) {
		this.operator = operator;
		return this;
	}




	public String getValue() {
		return value;
	}

	@Override
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
