package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.Compare
import org.endeavourhealth.interfacemanager.model.ValueSource
import java.util.function.Consumer

fun Compare.left(builder: Consumer<ValueSource?>): Compare {
  this.left = ValueSource()
  builder.accept(this.left)
  return this
}

fun Compare.right(builder: Consumer<ValueSource?>): Compare {
  this.right = ValueSource()
  builder.accept(this.right)
  return this
}