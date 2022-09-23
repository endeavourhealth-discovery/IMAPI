package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class From {
	private TTAlias type;
	private TTAlias instance;
	private String alias;
	private TTAlias set;

	public TTAlias getSet() {
		return set;
	}

	public From setSet(TTAlias set) {
		this.set = set;
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public From setAlias(String alias) {
		this.alias = alias;
		return this;
	}



	public TTAlias getType() {
		return type;
	}

	@JsonSetter
	public From setType(TTAlias type) {
		this.type = type;
		return this;
	}

	public From setType(TTIriRef type) {
		this.type = new TTAlias(type);
		return this;
	}

	public From setType(String type){
		this.type= TTAlias.iri(type);
		return this;
	}

	public TTAlias getInstance() {
		return instance;
	}

	@JsonSetter
	public From setInstance(TTAlias instance) {
		this.instance = instance;
		return this;
	}


	public From setInstance(String instance) {
		this.instance = new TTAlias(TTIriRef.iri(instance));
		return this;
	}
}
