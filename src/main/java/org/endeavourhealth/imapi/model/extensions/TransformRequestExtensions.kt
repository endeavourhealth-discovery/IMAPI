package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.TTIriRef
import org.endeavourhealth.interfacemanager.model.TransformRequest
import java.util.zip.DataFormatException


@Throws(DataFormatException::class)
fun TransformRequest.setTransformMap(iri: String): TransformRequest? {
  if (iri != null && !iri.isEmpty() && !iri.matches("[a-z]+[:].*".toRegex())) {
    throw DataFormatException("Invalid iri format : " + iri)
  } else {
    this.transformMap = TTIriRef().iri(iri)
    return this
  }
}

fun TransformRequest.addSource(type: String?, source: Map<String, Object>?): TransformRequest {
  if (this.source == null) this.source = HashMap<String, List<Map<String, Object>>>()
  this.source?.computeIfAbsent(type) { _ -> ArrayList<Map<String, Object>>() }?.add(source)
  return this
}