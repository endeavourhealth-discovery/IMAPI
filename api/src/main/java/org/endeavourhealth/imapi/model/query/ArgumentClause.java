package org.endeavourhealth.imapi.model.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class ArgumentClause {

	TTIriRef parameter;
	String valueData;
	TTIriRef valueIri;
	Clause valueMatch;

	public TTIriRef getParameter() {
		return parameter;
	}

	public ArgumentClause setParameter(TTIriRef parameter) {
		this.parameter = parameter;
		return this;
	}

	public String getValueData() {
		return valueData;
	}

	public ArgumentClause setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}

	public TTIriRef getValueIri() {
		return valueIri;
	}

	public ArgumentClause setValueIri(TTIriRef valueIri) {
		this.valueIri = valueIri;
		return this;
	}

	public Clause getValueMatch() {
		return valueMatch;
	}

	public ArgumentClause setValueMatch(Clause valueMatch) {
		this.valueMatch = valueMatch;
		return this;
	}
}
