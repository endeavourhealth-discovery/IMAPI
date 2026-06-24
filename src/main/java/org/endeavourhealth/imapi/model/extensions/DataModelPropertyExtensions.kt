package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.DataModelProperty

fun DataModelProperty.isArray(): Boolean {
  return maxExclusive == null || maxExclusive?.isEmpty() == true || maxExclusive == "0"
}
