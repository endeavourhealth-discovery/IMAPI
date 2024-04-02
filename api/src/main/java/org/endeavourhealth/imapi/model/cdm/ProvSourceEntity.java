package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class ProvSourceEntity extends Entry {

	public ProvSourceEntity(){
		this.addType(iri(IM.PROVENANCE_SOURCE_ENTITY));
	}

	public TTIriRef getDerivationType() {
		return
			(TTIriRef) TTUtil.get(this,iri(IM.DERIVATION_TYPE),TTIriRef.class);
	}

	@JsonSetter
	public ProvSourceEntity setDerivationType(TTIriRef derivationType) {
		set(iri(IM.DERIVATION_TYPE),derivationType);
		return this;
	}

	public TTIriRef getEntityIdentifier() {
		return (TTIriRef)
			TTUtil.get(this,iri(IM.ENTITY_IDENTIFIER),TTIriRef.class);
	}

	@JsonSetter
	public ProvSourceEntity setEntityIdentifier(TTIriRef entityIdentifier) {
		set(iri(IM.ENTITY_IDENTIFIER),entityIdentifier);
		return this;
	}
}
