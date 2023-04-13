package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.vocabulary.IM;

public class TripleVar {
	private String nodeVar;
	private String iri;
	private String pathVar;
	private String variable;
	private String name;


	public String getPathVar() {
		return pathVar;
	}

	public TripleVar setPathVar(String pathVar) {
		this.pathVar = pathVar;
		return this;
	}

	public String getName() {
		return name;
	}

	@JsonProperty("@id")
	public String getIri(){
		return iri;
	}

	public TripleVar setName(String name) {
		this.name = name;
		return this;
	}

	public String getNodeVar() {
		return nodeVar;
	}

	public TripleVar setNodeVar(String nodeVar) {
		this.nodeVar = nodeVar;
		return this;
	}


	@JsonSetter
	public TripleVar setIri(String iri) {
		this.iri = assignIri(iri);
		return this;
	}

	public String getVariable() {
		return variable;
	}

	public TripleVar setVariable(String variable) {
		this.variable = variable;
		return this;
	}



	public String assignIri(String iri){
		if (iri != null && !iri.isEmpty()) {
			if (!iri.matches("([a-z]+)?[:].*")) {
				return IM.NAMESPACE + iri;
			}
		}
		return iri;
	}
}
