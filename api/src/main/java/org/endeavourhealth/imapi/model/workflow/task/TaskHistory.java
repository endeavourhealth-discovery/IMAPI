package org.endeavourhealth.imapi.model.workflow.task;

import lombok.Getter;

@Getter
public class TaskHistory {
    private String subject;
    private String predicate;
    private String originalObject;
    private String newObject;

    public TaskHistory() {}
    public TaskHistory(String subject, String predicate, String originalObject, String newObject) {
        this.subject = subject;
        this.predicate = predicate;
        this.originalObject = originalObject;
        this.newObject = newObject;
    }

    public TaskHistory setSubject(String subject) {
        this.subject = subject;
        return this;
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
}
