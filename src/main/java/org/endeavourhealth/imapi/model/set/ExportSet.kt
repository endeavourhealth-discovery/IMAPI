package org.endeavourhealth.imapi.model.set

import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import java.io.Serializable

class ExportSet : Serializable {
  var valueSet: TTIriRef? = null
    private set
  var members: MutableList<SetMember>? = mutableListOf()
    private set
  var isLimited: Boolean = false
    private set

  fun setValueSet(valueSet: TTIriRef?): ExportSet {
    this.valueSet = valueSet
    return this
  }

  fun setMembers(includedMembers: MutableList<SetMember>?): ExportSet {
    this.members = includedMembers
    return this
  }

  fun addMembers(vsm: SetMember): ExportSet {
    if (this.members == null) this.members = mutableListOf()
    this.members!!.add(vsm)
    return this
  }

  fun addAllMembers(vsm: MutableCollection<SetMember>): ExportSet {
    if (this.members == null) this.members = mutableListOf()
    this.members!!.addAll(vsm)
    return this
  }

  fun setLimited(limited: Boolean): ExportSet {
    this.isLimited = limited
    return this
  }
}
