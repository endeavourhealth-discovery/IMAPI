package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;


@JsonPropertyOrder({"nodeVariable","id","type","set","variable","name"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Element implements Entailment{
	private String type;
	private String id;
	private String variable;
	private String name;
	private String set;
	private String parameter;
	private boolean ancestorsOf;
	private boolean descendantsOrSelfOf;
	private boolean descendantsOf;
	private boolean inverse;

	public String getParameter() {
		return parameter;
	}

	public Element setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public static Element iri(String iri) {
		return new Element(iri);
	}

	public Element(){}

	public Element(String iri){
		this.id= iri;
	}
	@JsonProperty("@type")
	public String getType() {
		return type;
	}

	@JsonProperty("@id")
	public String getId() {
		return id;
	}

	public String getVariable() {
		return variable;
	}

	public String getName() {
		return name;
	}

	@JsonProperty("@set")
	public String getSet() {
		return set;
	}

	public Element setSet(String set) {
		this.set = set;
		return this;
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

	public boolean isInverse() {
		return inverse;
	}

	public Element setInverse(boolean inverse) {
		this.inverse = inverse;
		return this;
	}




	public Element setType(String type) {
		this.type= type;
		return this;
	}


	public Element setId(String id) {
		this.id= id;
		return this;
	}

	public Element setIri(String id) {
		this.id= id;
		return this;
	}

	public Element setName(String name) {
		this.name=name;
		return this;
	}


	public Element setVariable(String variable) {
		this.variable=variable;
		return this;
	}



}
