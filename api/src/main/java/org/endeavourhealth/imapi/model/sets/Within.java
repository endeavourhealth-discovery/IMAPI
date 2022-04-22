package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder ({"range","compare","of","from"})
public class Within {
	private Range range;
	private Compare compare;
	private String of;
	private Match targetMatch;

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

	public Within setCompare(Compare compare) {
		this.compare = compare;
		return this;
	}

	public String getOf() {
		return of;
	}

	public Within setOf(String of) {
		this.of = of;
		return this;
	}

	public Match getTargetMatch() {
		return targetMatch;
	}

	public Within setTargetMatch(Match targetMatch) {
		this.targetMatch = targetMatch;
		return this;
	}
}
