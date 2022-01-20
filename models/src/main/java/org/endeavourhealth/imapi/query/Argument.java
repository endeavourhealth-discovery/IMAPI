package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.InvalidClassException;

public class Argument extends TTNode {
	private String parameter;
	private String value;

	public String getParameter() throws InvalidClassException {
		return (String) TTUtil.get(this, IM.PARAMETER,String.class);
	}

	public Argument setParameter(String parameter) {
		set(IM.PARAMETER, TTLiteral.literal(parameter));
		return this;
	}

	public String getValue() throws InvalidClassException {
		return (String) TTUtil.get(this,IM.VALUE_DATA,String.class);
	}

	public Argument setValue(String value) {
		set(IM.VALUE_DATA,TTLiteral.literal(value));
		return this;
	}
}
