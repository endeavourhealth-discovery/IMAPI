package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.security.NamespacePermission;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.interfacemanager.model.NamespaceRequest;
import org.endeavourhealth.interfacemanager.model.TaskHistory;
import org.endeavourhealth.interfacemanager.model.TaskState;
import org.endeavourhealth.interfacemanager.model.TaskType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class NamespaceRequestExtended extends NamespaceRequest implements Task {

  public NamespaceRequestExtended(TTIriRefExtended id, TaskType type, String createdBy, String assignedTo, TaskState state, LocalDateTime dateCreated, List<TaskHistory> history, String hostUrl, NamespacePermission namespacePermission) {
    this.id(id).createdBy(createdBy).type(type).state(state).assignedTo(assignedTo).dateCreated(OffsetDateTime.from(dateCreated)).history(history).hostUrl(hostUrl);
    this.namespacePermission(namespacePermission);
  }

  public NamespaceRequestExtended namespacePermission(NamespacePermission namespacePermission) {
    this.setNamespacePermission(namespacePermission);
    return this;
  }
}
