package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIri;

public class PropertyMap extends TTIri {
	private SetModel object;

	public SetModel getObject() {
		return object;
	}

	public PropertyMap setObject(SetModel object) {
		this.object = object;
		return this;
	}

	public PropertyMap setVar(String var){
		super.setVar(var);
		return this;
	}

	public PropertyMap setAlias(String alias){
		super.setAlias(alias);
		return this;
	}

	public PropertyMap setName(String name){
		super.setName(name);
		return this;
	}
}
