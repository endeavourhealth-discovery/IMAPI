package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTAlias;

public class OrderBy extends TTAlias {
	private String direction;


	public OrderBy setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public OrderBy setAlias(String alias){
		super.setAlias(alias);
		return this;
	}

	public OrderBy setName(String name){
		super.setName(name);
		return this;
	}

	public String getDirection() {
		return direction;
	}

	public OrderBy setDirection(String direction) {
		this.direction = direction;
		return this;
	}
}
