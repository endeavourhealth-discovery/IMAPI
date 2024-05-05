package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashSet;
import java.util.Set;

public class ConceptSet extends Entity{
	private Query definition;
	private Set<TTIriRef> hasMember;
	private Set<TTIriRef> usedIn;

	public Set<TTIriRef> getUsedIn() {
		return usedIn;
	}

	public ConceptSet setUsedIn(Set<TTIriRef> usedIn) {
		this.usedIn = usedIn;
		return this;
	}

	public ConceptSet addUsedIn(TTIriRef query){
		if (this.usedIn==null)
			this.usedIn= new HashSet<>();
		this.usedIn.add(query);
		return this;
	}

	public Query getDefinition() {
		return definition;
	}

	public ConceptSet setDefinition(Query definition) {
		this.definition = definition;
		return this;
	}

	public Set<TTIriRef> getHasMember() {
		return hasMember;
	}

	public ConceptSet setHasMember(Set<TTIriRef> hasMember) {
		this.hasMember = hasMember;
		return this;
	}

	public ConceptSet addHasMember(TTIriRef member){
		if (this.hasMember==null)
			this.hasMember= new HashSet<>();
		this.hasMember.add(member);
		return this;
	}
}
