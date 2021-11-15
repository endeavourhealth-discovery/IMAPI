package org.endeavourhealth.imapi.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.endeavourhealth.imapi.logic.service.WorkflowService;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("workflow")
@CrossOrigin(origins = "*")
@Api(value="WorkflowController")
@SwaggerDefinition(tags = {
        @Tag(name = "Workflow Controller", description = "Main Workflow endpoint")
})
public class WorkflowController {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowController.class);

    @Autowired
    WorkflowService workflowService;

    @GetMapping()
    public List<StateMachineConfig> getWorkflows() {
        LOG.debug("getWorkflows");
        return workflowService.getWorkflows();
    }

    @GetMapping("/tasks")
    public List<Task> getWorkflowTasks() {
        LOG.debug("getWorkflowTasks");
        return workflowService.getWorkflowTasks();
    }
}
