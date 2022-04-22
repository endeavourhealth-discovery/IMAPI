package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Compare {

	Comparison comparison;
	String valueData;
	Function function;

	public Comparison getComparison() {
		return comparison;
	}

	public Compare setComparison(Comparison comparison) {
		this.comparison = comparison;
		return this;
	}

	public String getValueData() {
		return valueData;
	}


	@JsonIgnore
	public String getValue() {
		return valueData;
	}

	public Compare setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}


	@JsonIgnore
	public Compare setValue(String valueData) {
		this.valueData = valueData;
		return this;
	}


	public Function getFunction() {
		return function;
	}

	public Compare setFunction(Function function) {
		this.function = function;
		return this;
	}
}
