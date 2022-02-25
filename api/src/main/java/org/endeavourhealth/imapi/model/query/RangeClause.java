package org.endeavourhealth.imapi.model.query;

import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

public class RangeClause {
	CompareClause  from;
	CompareClause to;

	public CompareClause getFrom() {
		return from;
	}

	public RangeClause setFrom(CompareClause from) {
		this.from = from;
		return this;
	}

	public CompareClause getTo() {
		return to;
	}

	public RangeClause setTo(CompareClause to) {
		this.to = to;
		return this;
	}
}
