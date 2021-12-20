package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class MatchPath {
	private TTIriRef property;
	private MatchValue matchValue;

	public TTIriRef getProperty() {
		return property;
	}

	public MatchPath setProperty(TTIriRef property) {
		this.property = property;
		return this;
	}

	public MatchValue getMatchValue() {
		return matchValue;
	}

	public MatchValue setMatchValue(MatchValue matchValue) {
		this.matchValue = matchValue;
		return matchValue;
	}
}
