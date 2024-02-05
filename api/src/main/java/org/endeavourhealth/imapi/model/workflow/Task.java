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

    public Task(TTIriRef id, String createdBy, TaskType type, TaskState state, String assignedTo, LocalDate dateCreated) {
        this.id = id;
        this.createdBy = createdBy;
        this.type = type;
        this.state = state;
        this.assignedTo = assignedTo;
        this.dateCreated = dateCreated;
    }

    public Task() {}

    public Task setId(TTIriRef id) {
        this.id = id;
        return this;
    }

    public Task setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Task setType(TaskType type) {
        this.type = type;
        return this;
    }

    public Task setState(TaskState state) {
        this.state = state;
        return this;
    }

    public Task setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
        return this;
    }

    public Task setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }
}
