package org.endeavourhealth.imapi.model.workflow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;

@Getter
@Schema(
    name="Search request",
    description = "Structure containing search request parameters and filters"
)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class WorkflowRequest {
    private int page = 1;
    private int size = 25;
    private String userId;

    public WorkflowRequest(HttpServletRequest request) throws JsonProcessingException {
        RequestObjectService requestObjectService = new RequestObjectService();
        this.userId = requestObjectService.getRequestAgentId(request);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public WorkflowRequest setPage(int page) {
        this.page = page;
        return this;
    }

    public WorkflowRequest setSize(int size) {
        this.size = size;
        return this;
    }

}
