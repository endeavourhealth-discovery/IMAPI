package org.endeavourhealth.imapi.model.iml;

public class Range {
	private Value from;
	private Value to;

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
