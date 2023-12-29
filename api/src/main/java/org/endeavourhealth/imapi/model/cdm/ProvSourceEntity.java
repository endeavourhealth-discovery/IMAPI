package org.endeavourhealth.imapi.model.cdm;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Vocabulary;

public class ProvSourceEntity extends Entry {

	public ProvSourceEntity(){
		this.addType(IM.PROVENANCE_SOURCE_ENTITY);
	}

	public TTIriRef getDerivationType() {
		return
			(TTIriRef) TTUtil.get(this,IM.DERIVATION_TYPE,TTIriRef.class);
	}

	@JsonSetter
	public ProvSourceEntity setDerivationType(TTIriRef derivationType) {
		set(IM.DERIVATION_TYPE,derivationType);
		return this;
	}
	public ProvSourceEntity setDerivationType(Vocabulary derivationType) {
		set(IM.DERIVATION_TYPE,derivationType);
		return this;
	}

	public TTIriRef getEntityIdentifier() {
		return (TTIriRef)
			TTUtil.get(this,IM.ENTITY_IDENTIFIER,TTIriRef.class);
	}

	@JsonSetter
	public ProvSourceEntity setEntityIdentifier(TTIriRef entityIdentifier) {
		set(IM.ENTITY_IDENTIFIER,entityIdentifier);
		return this;
	}
	public ProvSourceEntity setEntityIdentifier(Vocabulary entityIdentifier) {
		set(IM.ENTITY_IDENTIFIER,entityIdentifier);
		return this;
	}
}
