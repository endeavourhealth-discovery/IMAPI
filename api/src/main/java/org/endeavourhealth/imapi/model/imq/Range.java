package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.function.Consumer;


@JsonPropertyOrder({"match","from","to","relativeTo"})
public class Range {
	private Assignable from;
	private Assignable to;

	@JsonProperty(required = true)
	public Assignable getFrom() {
		return from;
	}

	public Range setFrom(Value from) {
		this.from = from;
		return this;
	}
	@JsonProperty(required = true)
	public Assignable getTo() {
		return to;
	}

	public Range setTo(Value to) {
		this.to = to;
		return this;
	}

	public Range from(Consumer<Assignable> builder){
		this.from= new Value();
		builder.accept(this.from);
		return this;
	}
	public Range to(Consumer<Assignable> builder){
		this.to= new Value();
		builder.accept(this.to);
		return this;
	}
}
