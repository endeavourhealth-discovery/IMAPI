package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;

import java.util.List;

public interface WorkflowRepository {
    List<StateMachineConfig> getWorkflows();

    List<Task> findAllTasks();
}
