package org.endeavourhealth.imapi.model.workflow.task;

import lombok.Getter;
import org.endeavourhealth.interfacemanager.model.TaskHistory;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
public class TaskHistoryExtended extends TaskHistory {

  public TaskHistoryExtended() {
  }

  public TaskHistoryExtended(String predicate, String originalObject, String newObject, LocalDateTime changeDate, String modifiedBy) {
    this.setPredicate(predicate);
    this.setOriginalObject(originalObject);
    this.setNewObject(newObject);
    this.setChangeDate(OffsetDateTime.from(changeDate));
    this.setModifiedBy(modifiedBy);
  }

  public TaskHistoryExtended predicate(String predicate) {
    this.setPredicate(predicate);
    return this;
  }

  public TaskHistoryExtended originalObject(String originalObject) {
    this.setOriginalObject(originalObject);
    return this;
  }

  public TaskHistoryExtended newObject(String newObject) {
    this.setNewObject(newObject);
    return this;
  }

  public TaskHistoryExtended dateTime(LocalDateTime changeDate) {
    this.setDateTime(OffsetDateTime.from(changeDate));
    return this;
  }

  public TaskHistoryExtended changeDate(LocalDateTime changeDate) {
    this.setChangeDate(OffsetDateTime.from(changeDate));
    return this;
  }

  public TaskHistoryExtended modifiedBy(String modifiedBy) {
    this.setModifiedBy(modifiedBy);
    return this;
  }
}
