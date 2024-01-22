package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.time.LocalDate;

@Getter
public class Task {

    private TTIriRef id;
    private String createdBy;
    private TaskType type;
    private TaskState state;
    private String assignedTo;
    private LocalDate dateCreated;

    public void setId(TTIriRef id) {
        this.id = id;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
}
