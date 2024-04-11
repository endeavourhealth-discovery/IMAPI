package org.endeavourhealth.imapi.model.iml;

import java.util.Set;

public class SetContent {

	private String setDefinition;
	private Set<Concept> subsets;
	private Set<Concept> concepts;

	public String getSetDefinition() {
		return setDefinition;
	}

	public Set<Concept> getSubsets() {
		return subsets;
	}

	public SetContent setSubsets(Set<Concept> subsets) {
		this.subsets = subsets;
		return this;
	}

	public SetContent setSetDefinition(String setDefinition) {
		this.setDefinition = setDefinition;
		return this;
	}

	public Set<Concept> getConcepts() {
		return concepts;
	}

	public SetContent setConcepts(Set<Concept> concepts) {
		this.concepts = concepts;
		return this;
	}
}
