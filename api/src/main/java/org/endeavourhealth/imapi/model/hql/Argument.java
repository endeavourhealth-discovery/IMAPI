package org.endeavourhealth.imapi.model.hql;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Argument {

	String parameter;
	String valueData;
	TTIriRef valueIri;
	Match valueMatch;

	public String getParameter() {
		return parameter;
	}

	public Argument setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public String getValueData() {
		return valueData;
	}

	public Argument setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}

	public TTIriRef getValueIri() {
		return valueIri;
	}

	public Argument setValueIri(TTIriRef valueIri) {
		this.valueIri = valueIri;
		return this;
	}

	public Match getValueMatch() {
		return valueMatch;
	}

	public Argument setValueMatch(Match valueMatch) {
		this.valueMatch = valueMatch;
		return this;
	}
}
