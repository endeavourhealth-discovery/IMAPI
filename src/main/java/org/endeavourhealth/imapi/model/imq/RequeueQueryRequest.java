package org.endeavourhealth.imapi.model.imq;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RequeueQueryRequest {
  private UUID queueId;
  private QueryRequest queryRequest;

  public RequeueQueryRequest() {
    super();
  }

  public RequeueQueryRequest setQueueId(UUID queueId) {
    this.queueId = queueId;
    return this;
  }

  public RequeueQueryRequest setQueryRequest(QueryRequest queryRequest) {
    this.queryRequest = queryRequest;
    return this;
  }
}
