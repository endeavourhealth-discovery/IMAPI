package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;


@JsonPropertyOrder({"var","function","argument","comparison","value","set"})
public class Filter{
	private List<TTIriRef> in;
	private List<TTIriRef> notIn;
	private Comparison comparison;
	private String value;
	private TTIriRef function;
	private List<String> argument;
	private Range range;

	public Filter setValueTest(Comparison comp, String value){

		comparison= comp;
		this.value = value;
		return this;
	}

	public Filter setRangeValueTest(Comparison fromComp, String fromValue,
																	Comparison toComp,String toValue){
		setRange(new Range()
		.setFrom(new Compare().setComparison(fromComp).setValue(fromValue))
		.setTo(new Compare().setComparison(toComp).setValue(toValue)));
		return this;
	}

	public Comparison getComparison() {
		return comparison;
	}

	public Filter setComparison(Comparison comparison) {
		this.comparison = comparison;
		return this;
	}



	public Filter setFunction(TTIriRef function) {
		this.function = function;
		return this;
	}


	public String getValue() {
		return value;
	}

	public Filter setValue(String value) {
		this.value = value;
		return this;
	}



	public TTIriRef getFunction() {
		return function;
	}

	public Filter setFunction(String function) {
		this.function = TTIriRef.iri(function);
		return this;
	}

	public List<String> getArgument() {
		return argument;
	}

	public Filter setArgument(List<String> argument) {
		this.argument = argument;
		return this;
	}
	public Filter addArgument(String argument){
		if (this.argument==null)
			this.argument= new ArrayList<>();
		this.argument.add(argument);
		return this;
	}


	public Range getRange() {
		return range;
	}

	public Range setRange(Range range) {
		this.range = range;
		return range;
	}

	public List<TTIriRef> getIn() {
		return in;
	}

	public Filter setIn(List<TTIriRef> in) {
		this.in = in;
		return this;
	}
	public Filter addIn(TTIriRef in){
		if (this.in==null)
			this.in= new ArrayList<>();
		this.in.add(in);
		return this;
	}

	public List<TTIriRef> getNotIn() {
		return notIn;
	}

	public Filter setNotIn(List<TTIriRef> notIn) {
		this.notIn = notIn;
		return this;
	}
	public Filter addNotIn(TTIriRef notIn){
		if (this.notIn==null)
			this.notIn= new ArrayList<>();
		this.notIn.add(notIn);
		return this;
	}
}
