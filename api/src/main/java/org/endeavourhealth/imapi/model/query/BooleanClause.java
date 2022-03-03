package org.endeavourhealth.imapi.model.query;

import java.util.ArrayList;
import java.util.List;

public class BooleanClause {
	List<Clause> and;
	List<Clause> or;
	Clause not;

	public List<Clause> getAnd() {
		return and;
	}

	public BooleanClause setAnd(List<Clause> and) {
		this.and = and;
		return this;
	}

	public List<Clause> getOr() {
		return or;
	}

	public BooleanClause setOr(List<Clause> or) {
		this.or = or;
		return this;
	}

	public Clause getNot() {
		return not;
	}

	public BooleanClause setNot(Clause not) {
		this.not = not;
		return this;
	}
}
