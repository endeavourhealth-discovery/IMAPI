package org.endeavourhealth.imapi.dataaccess;

import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;

import java.util.ArrayList;
import java.util.List;

public class WorkflowRepositoryImpl implements WorkflowRepository {
    @Override
    public List<StateMachineConfig> getWorkflows() {
        List<StateMachineConfig> result = new ArrayList<>();

        return result;
    }

    @Override
    public List<Task> findAllTasks() {
        List<Task> result = new ArrayList<>();

        return result;
    }
}
