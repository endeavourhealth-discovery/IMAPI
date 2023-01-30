package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"from","to","relativeTo"})
public class Range {
	private Assignable from;
	private Assignable to;


	public Assignable getFrom() {
		return from;
	}

	public Range setFrom(Value from) {
		this.from = from;
		return this;
	}

	public Assignable getTo() {
		return to;
	}

	public Range setTo(Value to) {
		this.to = to;
		return this;
	}
}
