package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IMQ;

@JsonPropertyOrder({"optional","s","p","o"})
public class QueryTriple{
	private boolean optional;
	private String s;
	private String p;
	private String o;

	public boolean isOptional() {
		return optional;
	}

	public String getS() {
		return s;
	}

	public QueryTriple setS(String s) {
		this.s = s;
		return this;
	}

	public String getP() {
		return p;
	}

	public QueryTriple setP(String p) {
		this.p = p;
		return this;
	}

	public String getO() {
		return o;
	}

	public QueryTriple setO(String o) {
		this.o = o;
		return this;
	}

	public QueryTriple setOptional(boolean optional) {
		this.optional = optional;
		return this;
	}
}
