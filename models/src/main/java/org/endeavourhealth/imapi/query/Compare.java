package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder ({"comparison","value","function","argument"})
public class Compare {
	private Comparison comparison;
	private Function function;
	private String value;

	public Comparison getComparison() {
		return comparison;
	}

	public Compare setComparison(Comparison comparison) {
		this.comparison = comparison;
		return this;
	}

	public String getValue() {
		return value;
	}

	public Compare setValue(String value) {
		this.value = value;
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
