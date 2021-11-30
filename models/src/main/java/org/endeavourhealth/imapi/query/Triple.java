package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Triple {
	private String subject;
	private TTIriRef predicate;
	private String object;

	public String getSubject() {
		return subject;
	}

	public Triple setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public TTIriRef getPredicate() {
		return predicate;
	}

	public Triple setPredicate(TTIriRef predicate) {
		this.predicate = predicate;
		return this;
	}

	public String getObject() {
		return object;
	}

	public Triple setObject(String object) {
		this.object = object;
		return this;
	}
}
