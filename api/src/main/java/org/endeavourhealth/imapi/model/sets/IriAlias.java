package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;


@JsonPropertyOrder({"iri","name","binding","alias"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class IriAlias extends TTIriRef {
	private String alias;


	public IriAlias(){}
	public IriAlias(String iri){
		if (iri.equals("id"))
			iri= IM.NAMESPACE+"id";

		super.setIri(iri);
	}

	public IriAlias(TTIriRef iri){
		super.setIri(iri.getIri());
	}

	public IriAlias(String iri, String name){
		super.setIri(iri);
		super.setName(name);
	}
	public IriAlias setIri(String iri){
		if (iri.equals("id"))
			iri= IM.NAMESPACE+"id";
		super.setIri(iri);
		return this;
	}
	public IriAlias setIri(String iri, String name){
		super.setIri(iri);
		super.setName(name);
		return this;
	}

	public IriAlias setIri(TTIriRef iri){
		super.setIri(iri.getIri());
		return this;
	}

	public IriAlias setName(String name){
		super.setName(name);
		return this;
	}



	public String getAlias() {
		return alias;
	}

	public IriAlias setAlias(String alias) {
		this.alias = alias;
		return this;
	}


}
