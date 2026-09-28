package org.endeavourhealth.imapi.model

import org.endeavourhealth.imapi.model.tripletree.TTIriRef

class PropertyDisplay {
  var order: Number? = null
    private set
  var group: TTIriRef? = null
    private set
  var property: MutableList<TTIriRef> = mutableListOf(TTIriRef())
    private set
  var type: MutableList<TTIriRef> = mutableListOf()
    private set
  var cardinality: String? = null
    private set
  var isOr: Boolean? = null
    private set
  var isType: Boolean? = null
    private set
  var isNode: Boolean? = null
    private set
  var reverseCardinality: String? = null
    private set

  fun setOrder(order: Int?): PropertyDisplay {
    this.order = order
    return this
  }

  fun setGroup(group: TTIriRef?): PropertyDisplay {
    this.group = group
    return this
  }

  fun setProperty(property: MutableList<TTIriRef>): PropertyDisplay {
    this.property = property
    return this
  }

  fun addProperty(property: TTIriRef): PropertyDisplay {
    this.property.add(property)
    return this
  }

  fun addType(type: TTIriRef): PropertyDisplay {
    this.type.add(type)
    return this
  }

  fun setCardinality(cardinality: String?): PropertyDisplay {
    this.cardinality = cardinality
    return this
  }

  fun setIsOr(or: Boolean): PropertyDisplay {
    isOr = or
    return this
  }

  fun setIsType(type: Boolean): PropertyDisplay {
    isType = type
    return this
  }

  fun setType(type: MutableList<TTIriRef>): PropertyDisplay {
    this.type = type
    return this
  }

  fun setIsNode(node: Boolean?): PropertyDisplay {
    isNode = node
    return this
  }

  fun setReverseCardinality(cardinality: String?): PropertyDisplay {
    reverseCardinality = cardinality
    return this
  }
}
