package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.Case
import org.endeavourhealth.interfacemanager.model.Expression
import org.endeavourhealth.interfacemanager.model.When
import java.util.function.Consumer

fun Case.`when`(builder: Consumer<When?>): Case {
  val `when`: When = When()
  this.addWhenItem(`when`)
  builder.accept(`when`)
  return this
}

fun Case.else_(builder: Consumer<Expression?>): Case {
  val expression: Expression = Expression()
  setElse(expression)
  builder.accept(expression)
  return this
}
