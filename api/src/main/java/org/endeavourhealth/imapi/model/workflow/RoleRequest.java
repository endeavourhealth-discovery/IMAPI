package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;

import java.time.LocalDate;

@Getter
public class RoleRequest extends Task {
    private UserRole role;

    public RoleRequest(TTIriRef id, TaskType type, String createdBy, String assignedTo, TaskState state, LocalDate dateCreated, UserRole role) {
        super(id, createdBy, type, state, assignedTo, dateCreated);
        this.role = role;
    }

    public RoleRequest() {

    }

    public RoleRequest setRole(UserRole role) {
        this.role = role;
        return this;
    }
}
