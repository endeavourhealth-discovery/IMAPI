package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class IriVar extends TTIriRef {
	private String var;

	public IriVar(){}

	public IriVar(TTIriRef iri){
		this.setIri(iri.getIri());
		this.setName(iri.getName());
	}

	public IriVar(TTIriRef iri,String var){
		this.setIri(iri.getIri());
		this.setName(iri.getName());
		this.setVar(var);
	}

	public String getVar() {
		return var;
	}

	public IriVar setVar(String var) {
		this.var = var;
		return this;
	}
}
