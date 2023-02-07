package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder ({"inverse","iri","name","alias","inverse","sourceType","entity","set"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TTAlias extends TTIriRef {
	private String alias;
	private String id;
	private String variable;
	private SourceType sourceType;
	private boolean includeSupertypes;
	private boolean includeSubtypes;
	private boolean excludeSelf;
	private boolean inverse;

	public SourceType getSourceType() {
		return sourceType;
	}

	public TTAlias setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
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

	public boolean isExcludeSelf() {
		return excludeSelf;
	}

	public TTAlias setExcludeSelf(boolean excludeSelf) {
		this.excludeSelf = excludeSelf;
		return this;
	}



	public boolean isIncludeSupertypes() {
		return includeSupertypes;
	}

	public TTAlias setIncludeSupertypes(boolean includeSupertypes) {
		this.includeSupertypes = includeSupertypes;
		return this;
	}

	public boolean isIncludeSubtypes() {
		return includeSubtypes;
	}

	public TTAlias setIncludeSubtypes(boolean includeSubtypes) {
		this.includeSubtypes = includeSubtypes;
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
