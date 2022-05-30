package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

/** Extends TTIriRef with optional variable, alias, and description
 * for use in dynamic data modelling
 */
@JsonPropertyOrder({"description","@id","name","alias","var"})
public class Heading extends TTIriRef {
	private String var;
	private String description;




	public static Heading iri(String iri) {
		return new Heading(iri);
	}
	public static Heading iri(String iri, String name) {
		return new Heading(iri, name);
	}
	public static Heading iri(TTIriRef iri) {
		return new Heading(iri.getIri());
	}

	public Heading(){
	}

	public Heading setName(String name){
		super.setName(name);
		return this;
	}


	public Heading(String iri) {
		setIri(iri);
	}
	public Heading(String iri, String name) {
		setIri(iri);
		setName(name);
	}

	public Heading(TTIriRef iriRef){
		setIri(iriRef.getIri());
		if (iriRef.getName()!=null)
			setName(iriRef.getName());
	}


	public String getVar() {
		return var;
	}

	public Heading setVar(String var) {
		this.var = var;
		return this;
	}


	public String getDescription() {
		return description;
	}

	public Heading setDescription(String description) {
		this.description = description;
		return this;
	}


}
