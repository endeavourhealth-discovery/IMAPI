package org.endeavourhealth.imapi.model.iml;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.function.Consumer;

@JsonPropertyOrder({"value","range","of"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Within {
	private Value value;
	private Range range;
	private Compare of;



	public Value getValue() {
		return value;
	}

	@JsonSetter
	public Within setValue(Value value) {
		this.value = value;
		return this;
	}

	public Within value (Consumer<Value> builder){
		this.value= new Value();
		builder.accept(this.value);
		return this;
	}

	public Range getRange() {
		return range;
	}

	@JsonSetter
	public Within setRange(Range range) {
		this.range = range;
		return this;
	}

	public Within range (Consumer<Range> builder){
		this.range= new Range();
		builder.accept(this.range);
		return this;
	}

	public Compare getOf() {
		return of;
	}

	@JsonSetter
	public Within setOf(Compare of) {
		this.of = of;
		return this;
	}

	public Within of (Consumer<Compare> builder){
		this.of= new Compare();
		builder.accept(this.of);
		return this;
	}
}
