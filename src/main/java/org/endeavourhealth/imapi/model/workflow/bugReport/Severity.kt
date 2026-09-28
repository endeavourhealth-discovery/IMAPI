package org.endeavourhealth.imapi.model.workflow.bugReport

enum class Severity(val text: String) {
  CRITICAL("critical"),
  MAJOR("major"),
  MINOR("minor"),
  TRIVIAL("trivial"),
  ENHANCEMENT("enhancement"),
  UNASSIGNED("unassigned");
}
