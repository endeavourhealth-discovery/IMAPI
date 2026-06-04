package org.endeavourhealth.imapi.model.workflow;

import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.interfacemanager.model.TaskHistory;
import org.endeavourhealth.interfacemanager.model.TaskState;
import org.endeavourhealth.interfacemanager.model.TaskType;

import java.time.OffsetDateTime;
import java.util.List;

public interface Task {

  TTIriRef getId();

  String getCreatedBy();

  TaskType getType();

  TaskState getState();

  String getAssignedTo();

  OffsetDateTime getDateCreated();

  List<TaskHistory> getHistory();

  String getHostUrl();
}