package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.sets.IriAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;

public class From extends IriAlias {
	private List<From> and;
	private List<From> or;

	public List<From> getAnd() {
		return and;
	}

	public From setAnd(List<From> and) {
		this.and = and;
		return this;
	}


	public List<From> getOr() {
		return or;
	}

	public From setOr(List<From> or) {
		this.or = or;
		return this;
	}

	public From(){}
	public From(String iri){
		if (iri.equals("id"))
			iri= IM.NAMESPACE+"id";

		super.setIri(iri);
	}

	public From(TTIriRef iri){
		super.setIri(iri.getIri());
	}

	public From(String iri, String name){
		super.setIri(iri);
		super.setName(name);
	}
	public From setIri(String iri){
		if (iri.equals("id"))
			iri= IM.NAMESPACE+"id";
		super.setIri(iri);
		return this;
	}
	public From setIri(String iri, String name){
		super.setIri(iri);
		super.setName(name);
		return this;
	}

	public From setIri(TTIriRef iri){
		super.setIri(iri.getIri());
		return this;
	}

	public From setName(String name){
		super.setName(name);
		return this;
	}



	public String getAlias() {
		return super.getAlias();
	}

	public From setAlias(String alias) {
		super.setAlias(alias);
		return this;
	}


}
