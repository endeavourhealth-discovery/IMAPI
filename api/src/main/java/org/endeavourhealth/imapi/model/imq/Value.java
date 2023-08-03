package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.XSD;

public class Value implements Assignable{
	private Operator operator;
	private String value;
	private String unit;
	private PropertyRef relativeTo;
	private TTIriRef dataType;

	public TTIriRef getDataType() {
		return dataType;
	}

	public Value setDataType(TTIriRef dataType) {
		this.dataType = dataType;
		return this;
	}

	public Operator getOperator() {
		return operator;
	}

	public Value setOperator(Operator operator) {
		this.operator = operator;
		return this;
	}

	public PropertyRef getRelativeTo() {
		return relativeTo;
	}

	public Value setRelativeTo(PropertyRef relativeTo) {
		this.relativeTo = relativeTo;
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
