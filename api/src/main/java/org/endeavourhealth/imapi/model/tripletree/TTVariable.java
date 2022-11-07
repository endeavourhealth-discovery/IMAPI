package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonPropertyOrder ({"inverse","iri","name","variable"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TTVariable extends TTIriRef {
	private String variable;
	private boolean isType;



	public boolean isType() {
		return isType;
	}

	@JsonSetter
	public TTVariable setType(boolean type) {
		this.isType = type;
		return this;
	}

	public TTVariable setType(TTIriRef type){
		setIri(type.getIri());
		if (type.getName()!=null)
			setName(type.getName());
		isType= true;
		return this;
	}

	public TTVariable setIsType(boolean asType) {
		this.isType = asType;
		return this;
	}



	public static TTVariable iri(String iri) {
		return new TTVariable().setIri(iri);
	}

	public TTVariable(TTIriRef iri){
		super.setIri(iri.getIri());
	}

	public String getVariable() {
		return variable;
	}

	public TTVariable setVariable(String variable) {
		this.variable = variable;
		return this;
	}


	public TTVariable(){}




	@JsonProperty("@id")
	public TTVariable setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public TTVariable setName(String name){
		super.setName(name);
		return this;
	}
}
