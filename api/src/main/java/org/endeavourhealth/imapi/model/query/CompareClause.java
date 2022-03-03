package org.endeavourhealth.imapi.model.query;

import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

public class CompareClause {

	Comparison comparison;
	String valueData;
	Function function;

	public Comparison getComparison() {
		return comparison;
	}

	public CompareClause setComparison(Comparison comparison) {
		this.comparison = comparison;
		return this;
	}

	public String getValueData() {
		return valueData;
	}


	public String getValue() {
		return valueData;
	}

	public CompareClause setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}


	public CompareClause setValue(String valueData) {
		this.valueData = valueData;
		return this;
	}


	public Function getFunction() {
		return function;
	}

	public CompareClause setFunction(Function function) {
		this.function = function;
		return this;
	}
}
