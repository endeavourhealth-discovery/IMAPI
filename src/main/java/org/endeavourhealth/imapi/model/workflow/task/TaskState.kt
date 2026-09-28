package org.endeavourhealth.imapi.model.workflow.task

enum class TaskState(val text: String) {
  TODO("to do"),
  IN_PROGRESS("in progress"),
  APPROVED("approved"),
  COMPLETE("complete"),
  REJECTED("rejected"),
  CANCELLED("cancelled"),
  UNDER_REVIEW("under review");
}
