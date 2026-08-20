package org.endeavourhealth.imapi.model.cdm

import com.fasterxml.jackson.annotation.JsonSetter
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.model.tripletree.*
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.NAMESPACE

/**
 * Class which sets and gets Provenance activity entry
 */
@Slf4j
class ProvActivity : Entry() {
    init {
        this.addType(TTIriRef.iri(IM.PROVENANCE_ACTIVITY))
        this.setScheme(TTIriRef.iri(NAMESPACE.IM))
    }

    override fun setIri(iri: String?): ProvActivity {
        super.setIri(iri)
        return this
    }

    val targetEntity: TTIriRef?
        get() = if (get(TTIriRef.iri(IM.PROVENANCE_TARGET)) == null) null else get(TTIriRef.iri(IM.PROVENANCE_TARGET)).asIriRef()

    @JsonSetter
    fun setTargetEntity(targetEntity: TTIriRef?): ProvActivity {
        set(TTIriRef.iri(IM.PROVENANCE_TARGET), targetEntity)
        return this
    }

    val activityType: TTIriRef?
        get() = if (get(TTIriRef.iri(IM.PROVENANCE_ACTIVITY_TYPE)) == null) null else get(TTIriRef.iri(IM.PROVENANCE_ACTIVITY_TYPE)).asIriRef()

    @JsonSetter
    fun setActivityType(activityType: TTIriRef?): ProvActivity {
        set(TTIriRef.iri(IM.PROVENANCE_ACTIVITY_TYPE), activityType)
        return this
    }

    val effectiveDate: String?
        get() = if (get(TTIriRef.iri(IM.EFFECTIVE_DATE)) == null) null else get(TTIriRef.iri(IM.EFFECTIVE_DATE)).asLiteral().getValue()

    fun setEffectiveDate(effectiveDate: String?): ProvActivity {
        set(TTIriRef.iri(IM.EFFECTIVE_DATE), TTLiteral.literal(effectiveDate))
        return this
    }

    val startTime: String?
        get() = TTUtil.get(this, TTIriRef.iri(IM.START_TIME), String::class.java) as String?

    fun setStartTime(startTime: String?): ProvActivity {
        set(TTIriRef.iri(IM.START_TIME), TTLiteral.literal(startTime))
        return this
    }

    val agent: MutableList<TTIriRef?>?
        get() = TTUtil.getIriList(this, IM.PROVENANCE_AGENT.asIri())

    fun setAgent(agent: TTArray?): ProvActivity {
        set(TTIriRef.iri(IM.PROVENANCE_AGENT), agent)
        return this
    }

    fun addAgent(agent: TTValue): ProvActivity {
        TTUtil.add(this, TTIriRef.iri(IM.PROVENANCE_AGENT), agent)
        return this
    }

    val used: MutableList<TTIriRef?>?
        get() = TTUtil.getIriList(this, IM.PROVENANCE_USED.asIri())

    fun setUsed(used: TTArray?): ProvActivity {
        set(TTIriRef.iri(IM.PROVENANCE_USED), used)
        return this
    }

    fun addUsed(used: TTIriRef): ProvActivity {
        TTUtil.add(this, TTIriRef.iri(IM.PROVENANCE_USED), used)
        return this
    }
}
