package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.model.workflow.task.TaskType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RoleRequest extends Task {
  private UserRole role;

  public RoleRequest(TTIriRef id, TaskType type, String createdBy, String assignedTo, TaskState state, LocalDateTime dateCreated, List<TaskHistory> history, String hostUrl, UserRole role) {
    super(id, createdBy, type, state, assignedTo, dateCreated, history, hostUrl);
    this.role = role;
  }

  public RoleRequest() {

  }

  public RoleRequest setRole(UserRole role) {
    this.role = role;
    return this;
  }
}
