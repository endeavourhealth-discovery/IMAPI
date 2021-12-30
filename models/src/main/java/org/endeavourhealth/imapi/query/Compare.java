package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder ({"comparison","value","function","argument"})
public class Compare {
	private Comparison comparison;
	private TTIriRef function;
	private List<String> argument;
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

	public TTIriRef getFunction() {
		return function;
	}

	public Compare setFunction(TTIriRef function) {
		this.function = function;
		return this;
	}

	public List<String> getArgument() {
		return argument;
	}

	public Compare setArgument(List<String> argument) {
		this.argument = argument;
		return this;
	}
	public Compare addArgument(String argument){
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}
}
