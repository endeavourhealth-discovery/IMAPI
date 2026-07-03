package org.endeavourhealth.imapi.model.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;

@Getter
@Schema(name = "Search request", description = "Structure containing search request parameters and filters")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class WorkflowRequest {

  private Integer page = 1;
  private Integer size = 25;
  private String userId;

  public WorkflowRequest(String userId) throws JsonProcessingException, UserNotFoundException {
    this.userId = userId;
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
