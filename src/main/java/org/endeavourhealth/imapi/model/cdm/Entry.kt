package org.endeavourhealth.imapi.model.cdm

import com.fasterxml.jackson.annotation.JsonSetter
import org.endeavourhealth.imapi.model.tripletree.TTEntity
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.model.tripletree.TTLiteral
import org.endeavourhealth.imapi.model.tripletree.TTUtil
import org.endeavourhealth.imapi.vocabulary.IM

abstract class Entry : TTEntity() {
  val dataController: TTIriRef?
    get() = TTUtil.get(this, TTIriRef.iri("dataController"), TTIriRef::class.java) as TTIriRef?

  @JsonSetter
  fun setDataController(dataController: TTIriRef?): Entry {
    set(TTIriRef.iri("dataController"), dataController)
    return this
  }

  val dateOfEntry: String?
    get() = TTUtil.get(this, TTIriRef.iri(IM.DATE_OF_ENTRY), String::class.java) as String?

  fun setDateOfEntry(dateOfEntry: String?): Entry {
    set(TTIriRef.iri(IM.DATE_OF_ENTRY), TTLiteral.literal(dateOfEntry))
    return this
  }
}