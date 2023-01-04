package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

public class QueryEntity extends Entity{

	private Set<TTIriRef> isContainedIn;
	private Query definition;


	public Set<TTIriRef> getIsContainedIn() {
		return isContainedIn;
	}

	public QueryEntity setIsContainedIn(Set<TTIriRef> isContainedIn) {
		this.isContainedIn = isContainedIn;
		return this;
	}

	public QueryEntity addIsContainedIn(TTIriRef folder){
		if (this.isContainedIn==null)
			this.isContainedIn= new HashSet<>();
		this.isContainedIn.add(folder);
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
