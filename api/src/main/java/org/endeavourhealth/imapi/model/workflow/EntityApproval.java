package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.entityApproval.ApprovalType;
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.model.workflow.task.TaskType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EntityApproval extends Task{
    private ApprovalType approvalType;

    public EntityApproval() {};
    public EntityApproval(TTIriRef id, TaskType type, String createdBy, String assignedTo, TaskState state, LocalDateTime dateCreated, List<TaskHistory> history, ApprovalType approvalType) {
        super(id, createdBy, type, state, assignedTo, dateCreated, history);
        this.approvalType = approvalType;
    }

    public EntityApproval setApprovalType(ApprovalType approvalType) {
        this.approvalType = approvalType;
        return this;
    }
}
