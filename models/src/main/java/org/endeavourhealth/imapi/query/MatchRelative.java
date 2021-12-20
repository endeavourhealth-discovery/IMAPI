package org.endeavourhealth.imapi.query;

public class MatchRelative extends MatchRange {
	private String relativeTo;

	public String getRelativeTo() {
		return relativeTo;
	}

	public MatchRelative setRelativeTo(String relativeTo) {
		this.relativeTo = relativeTo;
		return this;
	}
}
