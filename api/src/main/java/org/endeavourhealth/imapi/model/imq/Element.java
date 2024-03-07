package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"parameter","iri","type","set","variable","name"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Element extends IriLD implements Entailment{
	private String variable;
	private String parameter;
	private boolean ancestorsOf;
	private boolean descendantsOrSelfOf;
	private boolean descendantsOf;
	private String nodeRef;

	public String getNodeRef() {
		return nodeRef;
	}

	public Element setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
		return this;
	}

	public static Element iri(String iri) {
		return new Element(iri);
	}
	public static Element iri(String iri, String name) {
		return new Element(iri, name);
	}

	public Element() {
	}
	public Element(String iri) {
		setIri(iri);
	}
	public Element(String iri, String name) {
		setIri(iri);
		setName(name);
	}

	public Element setIri(String iri) {
		super.setIri(iri);
		return this;
	}


	public Element setName(String name) {
		super.setName(name);
		return this;
	}



	public String getParameter() {
		return parameter;
	}

	public Element setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public String getVariable() {
		return variable;
	}


	public boolean isAncestorsOf() {
		return ancestorsOf;
	}


	public Element setAncestorsOf(boolean ancestorsOf) {
		this.ancestorsOf = ancestorsOf;
		return this;
	}


	public boolean isDescendantsOrSelfOf() {
		return descendantsOrSelfOf;
	}


	public Element setDescendantsOrSelfOf(boolean descendantsOrSelfOf) {
		this.descendantsOrSelfOf = descendantsOrSelfOf;
		return this;
	}


	public boolean isDescendantsOf() {
		return descendantsOf;
	}

	public Element setDescendantsOf(boolean descendantsOf) {
		this.descendantsOf = descendantsOf;
		return this;
	}


	public Element setVariable(String variable) {
		this.variable=variable;
		return this;
	}



}
