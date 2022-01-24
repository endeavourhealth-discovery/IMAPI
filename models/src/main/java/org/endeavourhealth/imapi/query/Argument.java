package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.InvalidClassException;

public class Argument extends TTNode {
	private String parameter;
	private String value;

	public TTValue getParameter() throws InvalidClassException {
		return (TTValue) TTUtil.get(this, IM.PARAMETER,TTValue.class);
	}

	public Argument setParameter(String parameter) {
		set(IM.PARAMETER, TTLiteral.literal(parameter));
		return this;
	}
	public Argument setParameter(TTIriRef parameter) {
		set(IM.PARAMETER, parameter);
		return this;
	}

	public TTValue getValue() throws InvalidClassException {
		return (TTValue) TTUtil.get(this,IM.VALUE_DATA,TTValue.class);
	}

	public Argument setValue(String value) {
		set(IM.VALUE_DATA,TTLiteral.literal(value));
		return this;
	}
	public Argument setValue(TTValue value) {
		set(IM.VALUE_DATA,value);
		return this;
	}
}
