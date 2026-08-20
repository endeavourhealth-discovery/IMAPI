package org.endeavourhealth.imapi.model.codegen

import org.endeavourhealth.imapi.model.tripletree.TTIriRef

class DataModelProperty {
  var name: String? = null
    private set
  var dataType: TTIriRef? = null
    private set
  var isModel: Boolean? = null
    private set
  var comment: String? = null
    private set
  var maxCount: Int? = null
    private set
  var minCount: Int? = null
    private set

  fun setName(name: String?): DataModelProperty {
    this.name = name
    return this
  }

  fun setDataType(dataType: TTIriRef?): DataModelProperty {
    this.dataType = dataType
    return this
  }

  fun setModel(model: Boolean?): DataModelProperty {
    isModel = model
    return this
  }

  fun setComment(comment: String?): DataModelProperty {
    this.comment = comment
    return this
  }

  fun setMaxCount(maxCount: Int?): DataModelProperty {
    this.maxCount = maxCount
    return this
  }

  fun setMinCount(minCount: Int?): DataModelProperty {
    this.minCount = minCount
    return this
  }
}
