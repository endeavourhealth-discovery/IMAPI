package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.CodeGenDto

fun CodeGenDto.getDataType(datatype: String?): String? {
  this.getDatatypeMap()?.let { return it[datatype] }
  return null;
}

fun CodeGenDto.hasCollectionWrapper(): Boolean {
  return !this.collectionWrapper.isNullOrEmpty()
}