package org.endeavourhealth.imapi.model.set

import com.fasterxml.jackson.annotation.JsonSetter
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import java.io.Serializable

class SetMember : Serializable {
    var entity: TTIriRef? = null
        private set
    var code: String? = null
        private set
    var scheme: TTIriRef? = null
        private set
    var label: String? = null
        private set
    var type: MemberType? = null
  private set
    var directParent: TTIriRef? = null
  private set

    fun setEntity(entity: TTIriRef?): SetMember {
        this.entity = entity
        return this
    }

    fun setCode(code: String?): SetMember {
        this.code = code
        return this
    }

    fun setScheme(scheme: TTIriRef?): SetMember {
        this.scheme = scheme
        return this
    }

    fun setLabel(label: String?): SetMember {
        this.label = label
        return this
    }

  fun setType(type: MemberType?): SetMember {
    this.type = type
    return this
  }

  fun setDirectParent(directParent: TTIriRef?): SetMember {
    this.directParent = directParent
    return this
  }
}
