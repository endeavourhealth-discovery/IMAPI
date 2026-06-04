package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.interfacemanager.model.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
public class RoleRequestExtended extends RoleRequest implements Task {
  private UserRole role;

  public RoleRequestExtended(TTIriRefExtended id, TaskType type, String createdBy, String assignedTo, TaskState state, LocalDateTime dateCreated, List<TaskHistory> history, String hostUrl, UserRole role) {
    this.id(id).createdBy(createdBy).type(type).state(state).assignedTo(assignedTo).dateCreated(OffsetDateTime.from(dateCreated)).history(history).hostUrl(hostUrl);
    this.role = role;
  }

  public RoleRequestExtended() {

  }

  public RoleRequestExtended role(UserRole role) {
    this.setRole(role);
    return this;
  }
}
