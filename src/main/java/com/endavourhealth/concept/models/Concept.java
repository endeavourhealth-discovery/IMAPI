package com.endavourhealth.concept.models;

import java.util.Collections;
import java.util.Set;

import javax.validation.constraints.NotBlank;

public class Concept {

	String name;
	String iri;
	String description;
	
	Set<Concept> children;

	ConceptTreeNode tree;
	
	public Concept(@NotBlank String iri) {
		this.iri = iri;
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
	
	public ConceptTreeNode getTree() {
		return tree;
	}

	public Set<Concept> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	public void setTree(ConceptTreeNode tree) {
		this.tree = tree;
	}

	public boolean isA(Concept parent) {
		return tree.contains(parent);
	}
	
	public boolean isA(String conceptIri) {
		return tree.contains(new Concept(conceptIri));
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
	
}
