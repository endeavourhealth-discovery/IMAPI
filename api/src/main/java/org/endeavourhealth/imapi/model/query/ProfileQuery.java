package org.endeavourhealth.imapi.model.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class ProfileQuery {
	TTIriRef entityType;
	BooleanClause bool;
	Clause match;

	public TTIriRef getEntityType() {
		return entityType;
	}

	public ProfileQuery setEntityType(TTIriRef entityType) {
		this.entityType = entityType;
		return this;
	}

	public BooleanClause getBool() {
		return bool;
	}

	public ProfileQuery setBool(BooleanClause bool) {
		this.bool = bool;
		return this;
	}

	public Clause getMatch() {
		return match;
	}

	public ProfileQuery setMatch(Clause match) {
		this.match = match;
		return this;
	}

}
