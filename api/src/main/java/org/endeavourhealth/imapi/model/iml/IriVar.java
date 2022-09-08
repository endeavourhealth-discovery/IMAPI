package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IriVar extends TTIriRef {
	private String var;

	public IriVar(){}

	public IriVar(String iriVar){
		if (iriVar.matches("(<<|<)?\\S*:\\S*"))
			super.setIri(iriVar);
		else
			this.var= iriVar;
	}

	public IriVar(String iriVar,String name){
		new IriVar(iriVar).setName(name);
	}

	public static IriVar iri(String iri) {
		return new IriVar().setIri(iri);

	}
	public static IriVar iri(String iri, String name) {
		return new IriVar(iri, name);
	}

	public static IriVar var(String var){
		return new IriVar().setVar(var);
	}

	public static IriVar var(String var,String name){
		return new IriVar().setVar(var).setName(name);
	}

	public String getVar() {
		return var;
	}

	public IriVar setVar(String var) {
		this.var = var;
		return this;
	}

	@JsonProperty("@id")
	public IriVar setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public IriVar setName(String name){
		super.setName(name);
		return this;
	}
}
