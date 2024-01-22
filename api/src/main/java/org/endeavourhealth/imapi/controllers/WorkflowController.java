package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.WorkflowService;
import org.endeavourhealth.imapi.model.workflow.BugReport;
import org.endeavourhealth.imapi.model.workflow.Task;
import org.endeavourhealth.imapi.model.workflow.WorkflowRequest;
import org.endeavourhealth.imapi.model.workflow.WorkflowResponse;
import org.endeavourhealth.imapi.statemachine.StateMachineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@RequestMapping("api/workflow")
@CrossOrigin(origins = "*")
@Tag(name="WorkflowController")
@RequestScope
public class WorkflowController {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowController.class);
    private final WorkflowService workflowService = new WorkflowService();
    private final RequestObjectService requestObjectService = new RequestObjectService();

    @PostMapping(value = "/createBugReport", produces = "application/json")
    public BugReport createBugReport(HttpServletRequest request, @RequestBody BugReport bugReport) throws JsonProcessingException {
        LOG.debug("createBugReport");
        String id = requestObjectService.getRequestAgentId(request);
        if (null == bugReport.getCreatedBy()) bugReport.setCreatedBy(id);
        return workflowService.createBugReport(bugReport);
    }

    @GetMapping(value = "/getBugReport", produces = "application/json")
    public BugReport getBugReport(@RequestParam String id) {
        LOG.debug("getBugReport");
        return workflowService.getBugReport(id);
    }

    @GetMapping(value = "/getWorkflowsByCreatedBy",produces = "application/json")
    public WorkflowResponse getWorkflowsByCreatedBy(HttpServletRequest request, @RequestParam(required = false) int page, @RequestParam(required = false) int size) throws JsonProcessingException {
        LOG.debug("getWorkflowsByCreatedBy");
        WorkflowRequest wfRequest = new WorkflowRequest(request);
        if (page != 0) wfRequest.setPage(page);
        if (size != 0) wfRequest.setSize(size);
        return workflowService.getWorkflowsByCreatedBy(wfRequest);
    }

    @GetMapping(value = "/getWorkflowsByAssignedTo", produces = "application/json")
    public WorkflowResponse getWorkflowsByAssignedTo(HttpServletRequest request, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false) int size) throws JsonProcessingException {
        LOG.debug("getWorkflowsByAssignedTo");
        WorkflowRequest wfRequest = new WorkflowRequest(request);
        if (page != 0) wfRequest.setPage(page);
        if (size != 0) wfRequest.setSize(size);
        return workflowService.getWorkflowsByAssignedTo(wfRequest);
    }
}
