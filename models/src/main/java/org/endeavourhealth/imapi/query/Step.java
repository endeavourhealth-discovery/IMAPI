package org.endeavourhealth.imapi.query;

import java.util.ArrayList;
import java.util.List;

public class Step {
	private Mandate mandate;
	private List<Clause> clause;

	public Mandate getMandate() {
		return mandate;
	}

	public Step setMandate(Mandate mandate) {
		this.mandate = mandate;
		return this;
	}

	public List<Clause> getClause() {
		return clause;
	}

	public Step setClause(List<Clause> clause) {
		this.clause = clause;
		return this;
	}

	public Step addClause(Clause clause){
		if (this.clause==null)
			this.clause= new ArrayList<>();
		this.clause.add(clause);
		return this;
	}
}
