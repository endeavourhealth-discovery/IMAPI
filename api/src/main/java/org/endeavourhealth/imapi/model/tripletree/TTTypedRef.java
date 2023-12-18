package org.endeavourhealth.imapi.model.tripletree;

import org.endeavourhealth.imapi.vocabulary.Vocabulary;

public class TTTypedRef extends TTIriRef{
	private TTIriRef type;

	public TTIriRef getType() {
		return type;
	}

	public TTTypedRef setType(TTIriRef type) {
		this.type = type;
		return this;
	}
	public TTTypedRef setType(Vocabulary type) {
		return setType(type.asTTIriRef());
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
