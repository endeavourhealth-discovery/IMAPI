package org.endeavourhealth.imapi.model.workflow.task

enum class TaskType(val text: String) {
  BUG_REPORT("bug report"),
  ROLE_REQUEST("role request"),
  GRAPH_REQUEST("graph request"),
  ENTITY_APPROVAL("entity approval");
}
