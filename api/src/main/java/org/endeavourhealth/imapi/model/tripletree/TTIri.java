package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/** Extends TTIriRef with optional variable, alias, and description
 * for use in dynamic data modelling
 */
@JsonPropertyOrder({"description","@id","name","alias","var"})
public class TTIri extends TTIriRef{
	private String var;
	private String description;
	private String alias;

	public static TTIri iri(String iri) {
		return new TTIri(iri);
	}
	public static TTIri iri(String iri, String name) {
		return new TTIri(iri, name);
	}

	public TTIri(){
	}

	public TTIri setName(String name){
		super.setName(name);
		return this;
	}


	public TTIri(String iri) {
		setIri(iri);
	}
	public TTIri(String iri, String name) {
		setIri(iri);
		setName(name);
	}

	public TTIri(TTIriRef iriRef){
		setIri(iriRef.getIri());
		if (iriRef.getName()!=null)
			setName(iriRef.getName());
	}


	public String getVar() {
		return var;
	}

	public TTIri setVar(String var) {
		this.var = var;
		return this;
	}


	public String getDescription() {
		return description;
	}

	public TTIri setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public TTIri setAlias(String alias) {
		this.alias = alias;
		return this;
	}
}
