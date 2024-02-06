package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory;
import org.endeavourhealth.imapi.model.workflow.task.TaskState;
import org.endeavourhealth.imapi.model.workflow.task.TaskType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Task {

    private TTIriRef id;
    private String createdBy;
    private TaskType type;
    private TaskState state;
    private String assignedTo;
    private LocalDate dateCreated;
    private List<TaskHistory> history;

    public Task(TTIriRef id, String createdBy, TaskType type, TaskState state, String assignedTo, LocalDate dateCreated, List<TaskHistory> history) {
        this.id = id;
        this.createdBy = createdBy;
        this.type = type;
        this.state = state;
        this.assignedTo = assignedTo;
        this.dateCreated = dateCreated;
        this.history = history;
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

    public Task setHistory(List<TaskHistory> history) {
        this.history = history;
        return this;
    }

    public Task addTaskHistory(TaskHistory taskHistory) {
        if (null == this.history) this.history = new ArrayList<>();
        this.history.add(taskHistory);
        return this;
    }
}
