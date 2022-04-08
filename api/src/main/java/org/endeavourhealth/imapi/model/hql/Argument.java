package org.endeavourhealth.imapi.model.hql;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public class Argument {

	String parameter;
	Object value;

	public String getParameter() {
		return parameter;
	}

	public Argument setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public Argument setValue(Object value) {
		if (!(List.of(String.class,TTIriRef.class,Match.class,Boolean.class,
			Double.class,Float.class).contains(value.getClass())))
			throw new IllegalArgumentException("Argument values must be strings, booleans, numbers,floats or Match clauses");
		this.value= value;
		return this;
	}

}
