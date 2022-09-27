package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.function.Consumer;

@JsonPropertyOrder({"from","to","relativeTo"})
public class Range {
	private Value from;
	private Value to;
	private Compare relativeTo;

	public Compare getRelativeTo() {
		return relativeTo;
	}

	@JsonSetter
	public Range setRelativeTo(Compare relativeTo) {
		this.relativeTo = relativeTo;
		return this;
	}

	public Range relativeTo(Consumer<Compare> builder){
		this.relativeTo= new Compare();
		builder.accept(this.relativeTo);
		return this;
	}

	public Value getFrom() {
		return from;
	}

	public Range setFrom(Value from) {
		this.from = from;
		return this;
	}

	public Value getTo() {
		return to;
	}

	public Range setTo(Value to) {
		this.to = to;
		return this;
	}
}
