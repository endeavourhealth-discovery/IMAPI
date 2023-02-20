package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonPropertyOrder ({"inverse","iri","name","alias","inverse","type","set"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TTAlias extends TTIriRef {
	private String alias;
	private String id;
	private String variable;
	private String type;
	private String set;
	private boolean ancestorsOf;
	private boolean descendantsOrSelfOf;
	private boolean descendantsOf;
	private boolean inverse;

	@JsonProperty("@type")
	public String getType() {
		return type;
	}

	public TTAlias setType(String type) {
		this.type = type;
		return this;
	}

	@JsonProperty("@set")
	public String getSet() {
		return set;
	}

	public TTAlias setSet(String set) {
		this.set = set;
		return this;
	}

	public String getVariable() {
		return variable;
	}

	public TTAlias setVariable(String variable) {
		this.variable = variable;
		return this;
	}

	public boolean isInverse() {
		return inverse;
	}

	public TTAlias setInverse(boolean inverse) {
		this.inverse = inverse;
		return this;
	}

	public String getId() {
		return id;
	}

	@JsonSetter
	public TTAlias setId(String id) {
		this.id = id;
		return this;
	}

	public boolean isDescendantsOf() {
		return descendantsOf;
	}

	public TTAlias setDescendantsOf(boolean descendantsOf) {
		this.descendantsOf = descendantsOf;
		return this;
	}



	public boolean isAncestorsOf() {
		return ancestorsOf;
	}

	public TTAlias setAncestorsOf(boolean ancestorsOf) {
		this.ancestorsOf = ancestorsOf;
		return this;
	}

	public boolean isDescendantsOrSelfOf() {
		return descendantsOrSelfOf;
	}

	public TTAlias setDescendantsOrSelfOf(boolean descendantsOrSelfOf) {
		this.descendantsOrSelfOf = descendantsOrSelfOf;
		return this;
	}


	public static TTAlias iri(String iri) {
		return new TTAlias().setIri(iri);
	}

	public TTAlias(TTIriRef iri){
		super.setIri(iri.getIri());
	}

	public TTAlias(){}

	public String getAlias() {
		return alias;
	}

	public TTAlias setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	@JsonProperty("@id")
	public TTAlias setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public TTAlias setName(String name){
		super.setName(name);
		return this;
	}
}
