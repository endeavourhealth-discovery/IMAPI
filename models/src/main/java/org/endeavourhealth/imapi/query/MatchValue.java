package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class MatchValue {
	private String variable;
	private TTIriRef type;
	private TTIriRef in;
	private MatchRange absoluteRange;
	private MatchRelative relativeRange;
	private String equal;
	private MatchClause match;

	public String getVariable() {
		return variable;
	}

	public MatchValue setVariable(String variable) {
		this.variable = variable;
		return this;
	}

	public TTIriRef getType() {
		return type;
	}

	public MatchValue setType(TTIriRef type) {
		this.type = type;
		return this;
	}

	public TTIriRef getIn() {
		return in;
	}

	public MatchValue setIn(TTIriRef in) {
		this.in = in;
		return this;
	}

	public MatchRange getAbsoluteRange() {
		return absoluteRange;
	}

	public MatchValue setAbsoluteRange(MatchRange absoluteRange) {
		this.absoluteRange = absoluteRange;
		return this;
	}

	public MatchRelative getRelativeRange() {
		return relativeRange;
	}

	public MatchValue setRelativeRange(MatchRelative relativeRange) {
		this.relativeRange = relativeRange;
		return this;
	}

	public String getEqual() {
		return equal;
	}

	public MatchValue setEqual(String equal) {
		this.equal = equal;
		return this;
	}

	public MatchClause getMatch() {
		return match;
	}

	public MatchClause setMatch(MatchClause match) {
		this.match = match;
		return match;
	}
}
