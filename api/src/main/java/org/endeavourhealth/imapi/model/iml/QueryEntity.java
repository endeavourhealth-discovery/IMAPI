package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

public class QueryEntity extends Entity{

	private Query definition;



	public QueryEntity setIsContainedIn(Set<TTIriRef> isContainedIn) {
		super.setIsContainedIn(isContainedIn);
		return this;
	}



	public Query getDefinition() {
		return definition;
	}

	public QueryEntity setDefinition(Query definition) {
		this.definition = definition;
		return this;
	}
}
