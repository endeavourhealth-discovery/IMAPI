package org.endeavourhealth.imapi.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class IriVar extends TTIriRef{
	private String var;
	public IriVar(){}
	public IriVar(String iri){
		this.setIri(iri);
	}
	public IriVar(String iri, String name){
		this.setIri(iri);
		this.setName(name);
	}

	public static IriVar iri(String iri) {
		return new IriVar(iri);
	}
	public static IriVar iri(String iri, String name) {
		return new IriVar(iri, name);
	}

	@JsonProperty("@id")
	public String getIri() {
		return Query.getShort(this.getIri());
	}

	public String getVar() {
		return var;
	}

	public IriVar setVar(String var) {
		this.var = var;
		return this;
	}
}
