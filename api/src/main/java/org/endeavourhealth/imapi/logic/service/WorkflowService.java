package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.WorkflowRepository;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowService {

  private final WorkflowRepository workflowRepository = new WorkflowRepository();

  public List<StateMachineConfig> getWorkflows() {
    return workflowRepository.getWorkflows();
  }

  public List<Task> getWorkflowTasks() {
    return workflowRepository.findAllTasks();
  }
}
