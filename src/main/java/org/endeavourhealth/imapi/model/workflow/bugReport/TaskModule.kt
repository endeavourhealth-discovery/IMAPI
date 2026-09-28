package org.endeavourhealth.imapi.model.workflow.bugReport

enum class TaskModule(val text: String) {
  DIRECTORY("directory"),
  QUERY("query"),
  CREATOR("creator"),
  EDITOR("editor"),
  UPRN("uprn"),
  AUTH("auth");
}
