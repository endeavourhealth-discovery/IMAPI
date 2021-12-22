package org.endeavourhealth.imapi.query;

public class MatchRange {
	private String fromComparator;
	private String fromValue;
	private String fromUnits;
	private String toComparator;
	private String toValue;

	public String getFromComparator() {
		return fromComparator;
	}

	public MatchRange setFromComparator(String fromComparator) {
		this.fromComparator = fromComparator;
		return this;
	}

	public String getFromValue() {
		return fromValue;
	}

	public MatchRange setFromValue(String fromValue) {
		this.fromValue = fromValue;
		return this;
	}

	public String getFromUnits() {
		return fromUnits;
	}

	public MatchRange setFromUnits(String fromUnits) {
		this.fromUnits = fromUnits;
		return this;
	}

	public String getToComparator() {
		return toComparator;
	}

	public MatchRange setToComparator(String toComparator) {
		this.toComparator = toComparator;
		return this;
	}

	public String getToValue() {
		return toValue;
	}

	public MatchRange setToValue(String toValue) {
		this.toValue = toValue;
		return this;
	}
}
