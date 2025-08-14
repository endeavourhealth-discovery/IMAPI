package org.endeavourhealth.imapi.model.workflow.task;

public enum TaskType {
  BUG_REPORT("bug report"),
  ROLE_REQUEST("role request"),
  GRAPH_REQUEST("graph request"),
  ENTITY_APPROVAL("entity approval");

  private String text;

  TaskType(String text) {
    this.text = text;
  }

  public String getText() {
    return this.text;
  }
}
