package org.endeavourhealth.imapi.model.cdm

import com.fasterxml.jackson.annotation.JsonSetter
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.model.tripletree.TTUtil
import org.endeavourhealth.imapi.vocabulary.IM

class ProvAgent : Entry() {
    init {
        this.addType(TTIriRef.iri(IM.PROVENANCE_AGENT))
    }


    val participationType: TTIriRef?
        get() = TTUtil.get(this, TTIriRef.iri(IM.PARTICIPATION_TYPE), TTIriRef::class.java) as TTIriRef?

    @JsonSetter
    fun setParticipationType(participationType: TTIriRef?): ProvAgent {
        set(TTIriRef.iri(IM.PARTICIPATION_TYPE), participationType)
        return this
    }

    val personInRole: TTIriRef?
        get() = TTUtil.get(this, TTIriRef.iri(IM.PERSON_IN_ROLE), TTIriRef::class.java) as TTIriRef?

    @JsonSetter
    fun setPersonInRole(personInRole: TTIriRef?): ProvAgent {
        set(TTIriRef.iri(IM.PERSON_IN_ROLE), personInRole)
        return this
    }
}
