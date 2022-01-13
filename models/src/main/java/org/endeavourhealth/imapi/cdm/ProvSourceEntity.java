package org.endeavourhealth.imapi.cdm;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

public class ProvSourceEntity extends Entry {

	public ProvSourceEntity(){
		this.addType(TTIriRef.iri(IM.NAMESPACE+"ProvenanceSourceEntity"));
	}

	public TTIriRef getDerivationType() {
		return Utils.getIriValue(this,"derivationType");
	}

	public ProvSourceEntity setDerivationType(TTIriRef derivationType) {
		Utils.setTriple(this,"derivationType",derivationType);
		return this;
	}

	public TTIriRef getEntityIdentifier() {
		return Utils.getIriValue(this,"entityIdentifier");
	}

	public ProvSourceEntity setEntityIdentifier(TTIriRef entityIdentifier) {
		Utils.setTriple(this,"entityIdentifier",entityIdentifier);
		return this;
	}
}
