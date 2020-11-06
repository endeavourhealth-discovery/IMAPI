package com.endavourhealth.models.objectModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConceptReferenceNode {
	
	private ConceptReference conceptReference;
	private Set<ConceptReferenceNode> parents;
	
	public ConceptReferenceNode(ConceptReference conceptReference) {
		this.conceptReference = conceptReference;
		this.parents = new HashSet<>();
	}

	public ConceptReference getConceptReference() {
		return conceptReference;
	}

	public Set<ConceptReferenceNode> getParents() {
		return Collections.unmodifiableSet(parents);
	}
	
	public boolean addParent(ConceptReferenceNode parent) {
		return parents.add(parent);
	}
	
	public boolean removeParent(ConceptReferenceNode parent) {
		return parents.remove(parent);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conceptReference == null) ? 0 : conceptReference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConceptReferenceNode other = (ConceptReferenceNode) obj;
		if (conceptReference == null) {
			if (other.conceptReference != null)
				return false;
		} else if (!conceptReference.equals(other.conceptReference))
			return false;
		return true;
	}
	
	
}
