package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

public class SearchTermCode {
	String term;
	String code;
	TTIriRef status;

	public String getTerm() {
		return term;
	}

	public SearchTermCode setTerm(String term) {
		this.term = term;
		return this;
	}

	public String getCode() {
		return code;
	}

	public SearchTermCode setCode(String code) {
		this.code = code;
		return this;
	}

	public TTIriRef getStatus() {
		return status;
	}

	public SearchTermCode setStatus(TTIriRef status) {
		this.status = status;
		return this;
	}
}
