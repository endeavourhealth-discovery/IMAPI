package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public class Argument {

	String parameter;
	String valueData;
	String valueVariable;

	public String getValueVariable() {
		return valueVariable;
	}

	public Argument setValueVariable(String valueVariable) {
		this.valueVariable = valueVariable;
		return this;
	}

	public String getParameter() {
		return parameter;
	}

	public Argument setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public Object getValueData() {
		return valueData;
	}

	public Argument setValueData(String valueData) {
		if (!(List.of(String.class,TTIriRef.class, Match.class,Boolean.class,
			Double.class,Float.class).contains(valueData.getClass())))
			throw new IllegalArgumentException("Argument values must be strings, booleans, numbers,floats or Match clauses");
		this.valueData = valueData;
		return this;
	}

}
