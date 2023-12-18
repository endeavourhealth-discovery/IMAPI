package org.endeavourhealth.imapi.model.cdm;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

public class ProvSourceEntity extends Entry {

	public ProvSourceEntity(){
		this.addType(TTIriRef.iri(IM.NAMESPACE.iri+"ProvenanceSourceEntity"));
	}

	public TTIriRef getDerivationType() {
		return
			(TTIriRef) TTUtil.get(this,TTIriRef.iri("derivationType"),TTIriRef.class);
	}

	public ProvSourceEntity setDerivationType(TTIriRef derivationType) {
		set(TTIriRef.iri(IM.NAMESPACE.iri+"derivationType"),derivationType);
		return this;
	}

	public TTIriRef getEntityIdentifier() {
		return (TTIriRef)
			TTUtil.get(this,TTIriRef.iri(IM.NAMESPACE.iri+"entityIdentifier"),TTIriRef.class);
	}

	public ProvSourceEntity setEntityIdentifier(TTIriRef entityIdentifier) {
		set(TTIriRef.iri(IM.NAMESPACE.iri+"entityIdentifier"),entityIdentifier);
		return this;
	}
}
