package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.Argument

fun Argument.getHashString(): String {
  val hs = StringBuilder()
  this.parameter?.let { hs.append(it) }
  this.valueData?.let { hs.append(it) }
  this.valueParameter?.let { hs.append(it) }
  this.valueIri?.let { hs.append(it.iri) }
  this.valueDataList?.let {
    val sorted: MutableList<String?> = it.stream().sorted().toList()
    for (s in sorted) hs.append(s)
  }
  this.valuePath?.let { hs.append(it.iri) }
  this.valueNodeRef?.let { hs.append(it) }
  this.dataType?.let { hs.append(it.iri) }
  this.valueObject?.let { hs.append(it) }
  this.valueVariable?.let { hs.append(it) }
  return hs.toString()
}