package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;

import java.util.ArrayList;
import java.util.List;

public class WorkflowRepository {
    public List<StateMachineConfig> getWorkflows() {
        return new ArrayList<>();
    }

    public List<Task> findAllTasks() {
        return new ArrayList<>();
    }
}
