package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.entityApproval.ApprovalType;
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.model.workflow.task.TaskType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EntityApproval extends Task {
  private TTIriRef entityIri;
  private ApprovalType approvalType;

  public EntityApproval() {
  }

  public EntityApproval(TTIriRef id, TaskType type, String createdBy, String assignedTo, TaskState state, LocalDateTime dateCreated, List<TaskHistory> history, String hostUrl, TTIriRef entityIri, ApprovalType approvalType) {
    super(id, createdBy, type, state, assignedTo, dateCreated, history, hostUrl);
    this.entityIri = entityIri;
    this.approvalType = approvalType;
  }

  public EntityApproval setApprovalType(ApprovalType approvalType) {
    this.approvalType = approvalType;
    return this;
  }

  public EntityApproval setEntityIri(TTIriRef entityIri) {
    this.entityIri = entityIri;
    return this;
  }
}
