package org.endeavourhealth.imapi.model.cdm

import com.fasterxml.jackson.annotation.JsonSetter
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.model.tripletree.TTUtil
import org.endeavourhealth.imapi.vocabulary.IM

class ProvSourceEntity : Entry() {
    init {
        this.addType(TTIriRef.iri(IM.PROVENANCE_SOURCE_ENTITY))
    }

    val derivationType: TTIriRef?
        get() = TTUtil.get(this, TTIriRef.iri(IM.DERIVATION_TYPE), TTIriRef::class.java) as TTIriRef?

    @JsonSetter
    fun setDerivationType(derivationType: TTIriRef?): ProvSourceEntity {
        set(TTIriRef.iri(IM.DERIVATION_TYPE), derivationType)
        return this
    }

    val entityIdentifier: TTIriRef?
        get() = TTUtil.get(this, TTIriRef.iri(IM.ENTITY_IDENTIFIER), TTIriRef::class.java) as TTIriRef?

    @JsonSetter
    fun setEntityIdentifier(entityIdentifier: TTIriRef?): ProvSourceEntity {
        set(TTIriRef.iri(IM.ENTITY_IDENTIFIER), entityIdentifier)
        return this
    }
}
