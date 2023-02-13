package org.endeavourhealth.imapi.model.tripletree;

public class TTTypedRef extends TTIriRef{
	private TTIriRef type;

	public TTIriRef getType() {
		return type;
	}

	public TTTypedRef setType(TTIriRef type) {
		this.type = type;
		return this;
	}


	public TTTypedRef setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public TTTypedRef setName(String name){
		super.setName(name);
		return this;
	}
}
