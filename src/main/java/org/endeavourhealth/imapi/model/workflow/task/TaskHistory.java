package org.endeavourhealth.imapi.model.workflow.task;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskHistory {
    private String predicate;
    private String originalObject;
    private String newObject;
    private LocalDateTime changeDate;
    private String modifiedBy;

    public TaskHistory() {}
    public TaskHistory(String predicate, String originalObject, String newObject, LocalDateTime changeDate, String modifiedBy) {
        this.predicate = predicate;
        this.originalObject = originalObject;
        this.newObject = newObject;
        this.changeDate = changeDate;
        this.modifiedBy = modifiedBy;
    }

    public TaskHistory setPredicate(String predicate) {
        this.predicate = predicate;
        return this;
    }

    public TaskHistory setOriginalObject(String originalObject) {
        this.originalObject = originalObject;
        return this;
    }

    public TaskHistory setNewObject(String newObject) {
        this.newObject = newObject;
        return this;
    }

    public TaskHistory setDateTime(LocalDateTime changeDate) {
        this.changeDate = changeDate;
        return this;
    }

    public TaskHistory setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
        return this;
    }

    public TaskHistory setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
}
