package org.endeavourhealth.imapi.model.sets;

public class Range {
	Compare from;
	Compare to;



	public Compare getFrom() {
		return from;
	}

	public Range setFrom(Compare from) {
		this.from = from;
		return this;
	}

	public Compare getTo() {
		return to;
	}

	public Range setTo(Compare to) {
		this.to = to;
		return this;
	}
}
