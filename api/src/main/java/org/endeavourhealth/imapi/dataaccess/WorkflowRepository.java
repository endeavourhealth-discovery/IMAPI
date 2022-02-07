package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;

import java.util.ArrayList;
import java.util.List;

public class WorkflowRepository {
    public List<StateMachineConfig> getWorkflows() {
        List<StateMachineConfig> result = new ArrayList<>();

        return result;
    }

    public List<Task> findAllTasks() {
        List<Task> result = new ArrayList<>();

        return result;
    }
}
