package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class From extends TTAlias{
	private boolean isType;
	private boolean isSet;

	public From(TTAlias ttAlias){
		super(ttAlias);
	}

	public From(){}

	public boolean isType() {
		return isType;
	}

	@JsonSetter
	public From setType(boolean type) {
		isType = type;
		return this;
	}

	public From setType(TTAlias alias){
		super.setIri(alias.getIri());
		if (alias.getAlias()!=null)
			super.setAlias(alias.getAlias());
		this.isType= true;
		return this;
	}

	public From setType(TTIriRef ttIriRef){
		super.setIri(ttIriRef.getIri());
		this.isType= true;
		return this;
	}

	public boolean isSet() {
		return isSet;
	}

	public From setSet(boolean set) {
		isSet = set;
		return this;
	}
}
