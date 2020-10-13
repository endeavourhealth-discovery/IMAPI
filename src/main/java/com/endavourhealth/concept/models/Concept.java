package com.endavourhealth.concept.models;

import java.util.Collections;
import java.util.Set;

import javax.validation.constraints.NotBlank;

public class Concept {

	String name;
	String iri;
	String description;
	
	//Set<Concept> children;

	ConceptTreeNode tree;
	
	public Concept(@NotBlank String iri) {
		this.iri = iri;
		this.tree = new ConceptTreeNode(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIri() {
		return iri;
	}
	
	// unordered
	public Set<Concept> getParentConcepts() {
		return Collections.unmodifiableSet(tree.getFlatParents());
	}
	
	public Set<ConceptTreeNode> getParents() {
		return tree.getParents();
	}
	
	public Set<Concept> getChildConcepts() {
		return Collections.unmodifiableSet(tree.getChildConcepts());
	}
	
	public Set<ConceptTreeNode> getChildren() {
		return tree.getChildren();
	}
	
	public boolean addParent(Concept parentConcept) {
		return tree.addParent(parentConcept.getTree());
	}
	
	public boolean addChild(Concept childConcept) {
		return tree.addChild(childConcept.getTree());
	}
	
	public boolean hasChildren() {
		return tree.getChildren() != null && tree.getChildren().isEmpty() == false; // need to be a set of children
	}
	
	public boolean hasParents() {
		return tree.getParents() != null && tree.getParents().isEmpty() == false; // need to be a set of children
	}
	
	public boolean isA(Concept parent) {
		return tree.hasParent(parent);
	}
		
	public boolean isA(String conceptIri) {
		return tree.hasParent(new Concept(conceptIri));
	}
	
	public boolean deepEquals(Concept other) {
		boolean deepEquals = this.equals(other);
		
		deepEquals = tree.deepEquals(other.tree);
		
		return deepEquals;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iri == null) ? 0 : iri.hashCode());
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
		Concept other = (Concept) obj;
		if (iri == null) {
			if (other.iri != null)
				return false;
		} else if (!iri.equals(other.iri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Concept [iri=" + iri + "]";
	}
	
	ConceptTreeNode getTree() {
		return tree;
	}
	
}
