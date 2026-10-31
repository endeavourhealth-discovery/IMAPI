package org.endeavourhealth.imapi.model.workflow.bugReport

enum class Status(val text: String) {
  NEW("new"),
  FIXED("fixed"),
  ASSIGNED("assigned"),
  VERIFIED("verified"),
  REOPENED("reopened"),
  WONT_FIX("won't fix");
}
