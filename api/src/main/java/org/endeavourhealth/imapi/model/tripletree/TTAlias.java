package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder ({"inverse","iri","name","alias","path"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TTAlias extends TTIriRef {
	private String alias;
	private boolean inverse;
	private String variable;
	private boolean includeSupertypes;
	private boolean includeSubtypes;
	private boolean includeMembers;
	private boolean excludeSelf;
	private boolean isType;
	private boolean isSet;


	public TTAlias setSet(boolean set) {
		isSet = set;
		return this;
	}

	@JsonProperty("isSet")
	public boolean isSet() {
		return isSet;
	}

	@JsonSetter
	public TTAlias setIsSet(boolean set) {
		isSet = set;
		return this;
	}




	public boolean isType() {
		return isType;
	}

	@JsonSetter
	public TTAlias setType(boolean type) {
		this.isType = type;
		return this;
	}

	public TTAlias setType(TTIriRef type){
		setIri(type.getIri());
		if (type.getName()!=null)
			setName(type.getName());
		isType= true;
		return this;
	}

	public TTAlias setIsType(boolean asType) {
		this.isType = asType;
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

	public boolean isIncludeMembers() {
		return includeMembers;
	}

	public TTAlias setIncludeMembers(boolean includeMembers) {
		this.includeMembers = includeMembers;
		return this;
	}

	public static TTAlias iri(String iri) {
		return new TTAlias().setIri(iri);
	}

	public TTAlias(TTIriRef iri){
		super.setIri(iri.getIri());
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
