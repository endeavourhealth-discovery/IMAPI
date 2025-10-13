package org.endeavourhealth.imapi.model.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.endeavourhealth.imapi.logic.service.CasdoorService;

@Getter
@Schema(
  name = "Search request",
  description = "Structure containing search request parameters and filters"
)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class WorkflowRequest {
  private final CasdoorService casdoorService = new CasdoorService();
  private Integer page = 1;
  private Integer size = 25;
  private String userId;

  public WorkflowRequest(HttpSession session) throws JsonProcessingException {
    this.userId = casdoorService.getUser(session).id;
  }

  public WorkflowRequest(Integer page, Integer size, String userId) {
    this.page = page;
    this.size = size;
    this.userId = userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public WorkflowRequest setPage(Integer page) {
    this.page = page;
    return this;
  }

  public WorkflowRequest setSize(Integer size) {
    this.size = size;
    return this;
  }

}
