package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.entityApproval.ApprovalType;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.model.workflow.task.TaskType;

import java.time.LocalDate;

@Getter
public class EntityApproval extends Task{
    private ApprovalType approvalType;

    public EntityApproval() {};
    public EntityApproval(TTIriRef id, TaskType type, String createdBy, String assignedTo, TaskState state, LocalDate dateCreated, ApprovalType approvalType) {
        super(id, createdBy, type, state, assignedTo, dateCreated);
        this.approvalType = approvalType;
    }

    public EntityApproval setApprovalType(ApprovalType approvalType) {
        this.approvalType = approvalType;
        return this;
    }
}
