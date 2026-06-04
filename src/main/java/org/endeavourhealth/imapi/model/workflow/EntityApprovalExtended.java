package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.interfacemanager.model.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
public class EntityApprovalExtended extends EntityApproval implements Task {

  public EntityApprovalExtended() {
  }

  public EntityApprovalExtended(TTIriRefExtended id, TaskType type, String createdBy, String assignedTo, TaskState state, LocalDateTime dateCreated, List<TaskHistory> history, String hostUrl, TTIriRefExtended entityIri, ApprovalType approvalType) {
    this.id(id).createdBy(createdBy).type(type).state(state).assignedTo(assignedTo).dateCreated(OffsetDateTime.from(dateCreated)).history(history).hostUrl(hostUrl);
    this.entityIri(entityIri).approvalType(approvalType);
  }

  public EntityApprovalExtended approvalType(ApprovalType approvalType) {
    this.setApprovalType(approvalType);
    return this;
  }

  public EntityApprovalExtended entityIri(TTIriRefExtended entityIri) {
    this.setEntityIri(entityIri);
    return this;
  }
}
