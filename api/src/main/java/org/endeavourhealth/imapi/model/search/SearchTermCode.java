package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

public class SearchTermCode {
	String name;
	String code;
	TTIriRef status;

	public String getName() {
		return name;
	}

	public SearchTermCode setName(String term) {
		this.name = term;
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
