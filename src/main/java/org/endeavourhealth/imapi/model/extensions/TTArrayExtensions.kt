package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.imapi.model.tripletree.TTValueJava
import org.endeavourhealth.interfacemanager.model.TTArray

fun TTArray.add(value: TTValueJava): TTArray {
  val elements = this.elements;
  if (!elements.isNullOrEmpty() && elements.contains(value)) return this
  if (elements == null) this.elements = LinkedHashSet<TTValueJava>().toList()

}