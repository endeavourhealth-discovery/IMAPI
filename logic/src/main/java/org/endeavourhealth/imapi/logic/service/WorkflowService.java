package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.repository.WorkflowRepository;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class WorkflowService {

    WorkflowRepository workflowRepository = new WorkflowRepository();

    public List<StateMachineConfig> getWorkflows() throws SQLException {
        return workflowRepository.getWorkflows();
    }

    public List<Task> getWorkflowTasks() throws SQLException {
        return workflowRepository.findAllTasks();
    }
}
