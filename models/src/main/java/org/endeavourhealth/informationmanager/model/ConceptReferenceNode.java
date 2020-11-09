package org.endeavourhealth.informationmanager.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConceptReferenceNode extends ConceptReference {

	private Set<ConceptReferenceNode> parents;
	
	public ConceptReferenceNode(String iri) {
		super(iri);
		this.parents = new HashSet<>();
	}

	public Set<ConceptReferenceNode> getParents() {
		return Collections.unmodifiableSet(parents);
	}

	public ConceptReferenceNode setParents(Set<ConceptReferenceNode> parents) {
	    this.parents = parents;
	    return this;
    }

	public boolean addParent(ConceptReferenceNode parent) {
		return parents.add(parent);
	}
	
	public boolean removeParent(ConceptReferenceNode parent) {
		return parents.remove(parent);
	}	
}
