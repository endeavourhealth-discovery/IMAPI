package org.endeavourhealth.imapi.model.requests

import com.fasterxml.jackson.annotation.JsonSetter
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import java.util.zip.DataFormatException

class TransformRequest {
  var transformMap: TTIriRef? = null
    private set
  var sourceFormat: String? = null
    private set
  var targetFormat: String? = null
    private set
  var source: MutableMap<String, MutableList<Any>>? = null
    private set

  @JsonSetter
  fun setTransformMap(transformMap: TTIriRef?): TransformRequest {
    this.transformMap = transformMap
    return this
  }

  @Throws(DataFormatException::class)
  fun setTransformMap(iri: String?): TransformRequest {
    if (!iri.isNullOrEmpty() && !iri.matches("[a-z]+[:].*".toRegex())) {
      throw DataFormatException("Invalid iri format : $iri")
    } else {
      this.transformMap = TTIriRef.iri(iri)
      return this
    }
  }

  fun setSourceFormat(sourceFormat: String?): TransformRequest {
    this.sourceFormat = sourceFormat
    return this
  }

  fun setTargetFormat(targetFormat: String?): TransformRequest {
    this.targetFormat = targetFormat
    return this
  }

  fun setSource(source: MutableMap<String, MutableList<Any>>?): TransformRequest {
    this.source = source
    return this
  }

  fun addSource(type: String, source: Any): TransformRequest {
    if (this.source == null) this.source = hashMapOf()
    this.source!!.computeIfAbsent(type) { t: String -> java.util.ArrayList<kotlin.Any>() }.add(source)
    return this
  }
}
