package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;


@JsonPropertyOrder({"iri","name","binding","alias"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Alias extends TTIriRef {
	private String alias;


	public Alias(){}
	public Alias(String iri){
		if (iri.equals("id"))
			iri= IM.NAMESPACE+"id";

		super.setIri(iri);
	}

	public Alias(TTIriRef iri){
		super.setIri(iri.getIri());
	}

	public Alias(String iri, String name){
		super.setIri(iri);
		super.setName(name);
	}
	public Alias setIri(String iri){
		if (iri.equals("id"))
			iri= IM.NAMESPACE+"id";
		super.setIri(iri);
		return this;
	}
	public Alias setIri(String iri, String name){
		super.setIri(iri);
		super.setName(name);
		return this;
	}

	public Alias setIri(TTIriRef iri){
		super.setIri(iri.getIri());
		return this;
	}

	public Alias setName(String name){
		super.setName(name);
		return this;
	}



	public String getAlias() {
		return alias;
	}

	public Alias setAlias(String alias) {
		this.alias = alias;
		return this;
	}


}
