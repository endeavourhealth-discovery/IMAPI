package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.function.Consumer;

@JsonPropertyOrder ({"range","compare","of","from"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Within {
	private Range range;
	private Compare compare;
	private String of;
	private Function function;
	private Match targetMatch;

	public Function getFunction() {
		return function;
	}

	public Within setFunction(Function function) {
		this.function = function;
		return this;
	}

	public Range getRange() {
		return range;
	}

	public Within setRange(Range range) {
		this.range = range;
		return this;
	}

	public Compare getCompare() {
		return compare;
	}

	@JsonSetter
	public Within setCompare(Compare compare) {
		this.compare = compare;
		return this;
	}

	@JsonIgnore
	public Within compare(Consumer<Compare> builder){
		this.compare= new Compare();
		builder.accept(this.compare);
		return this;
	}

	public String getOf() {
		return of;
	}

	public Within setOf(String of) {
		this.of = of;
		return this;
	}

	public Match getTargetFilter() {
		return targetMatch;
	}

	public Within setTargetFilter(Match targetMatch) {
		this.targetMatch = targetMatch;
		return this;
	}
}
