package org.endeavourhealth.imapi.model.workflow.roleRequest;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum UserRole {
  ADMIN,
  DEVELOPER,
  PUBLISHER,
  CREATOR,
  EDITOR,
  TASK_MANAGER,
  AUTHORISER,
  APPROVER,
  EXECUTOR
}
