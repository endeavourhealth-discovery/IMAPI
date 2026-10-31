package org.endeavourhealth.imapi.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSetter
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import java.io.Serializable

class DataModelProperty : Serializable {
  var property: TTIriRef? = null
    private set
  var type: TTIriRef? = null
    private set
  var minInclusive: String? = null
    private set
  var minExclusive: String? = null
    private set
  var maxInclusive: String? = null
    private set
  var maxExclusive: String? = null
    private set
  var pattern: String? = null
    private set
  var inheritedFrom: TTIriRef? = TTIriRef()
    private set
  var order: Int? = null
    private set

  fun setOrder(order: Int): DataModelProperty {
    this.order = order
    return this
  }

  fun setProperty(property: TTIriRef?): DataModelProperty {
    this.property = property
    return this
  }

  @JsonIgnore
  fun hasProperty(): Boolean {
    return property != null
  }

  fun setPattern(pattern: String?): DataModelProperty {
    this.pattern = pattern
    return this
  }

  @JsonSetter
  fun setType(objectType: TTIriRef?): DataModelProperty {
    this.type = objectType
    return this
  }

  @JsonIgnore
  fun hasType(): Boolean {
    return type != null
  }

  fun setMinInclusive(minInclusive: String?): DataModelProperty {
    this.minInclusive = minInclusive
    return this
  }

  fun setMinExclusive(minExclusive: String?): DataModelProperty {
    this.minExclusive = minExclusive
    return this
  }

  fun setMaxInclusive(maxInclusive: String?): DataModelProperty {
    this.maxInclusive = maxInclusive
    return this
  }

  fun setMaxExclusive(maxExclusive: String?): DataModelProperty {
    this.maxExclusive = maxExclusive
    return this
  }

  @JsonSetter
  fun setInheritedFrom(inheritedFrom: TTIriRef?): DataModelProperty {
    this.inheritedFrom = inheritedFrom
    return this
  }

  @get:JsonIgnore
  val isArray: Boolean
    get() = maxExclusive == null || maxExclusive!!.isEmpty() || maxExclusive == "0"
}
