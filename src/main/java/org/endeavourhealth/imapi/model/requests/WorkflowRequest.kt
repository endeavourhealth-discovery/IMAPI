package org.endeavourhealth.imapi.model.requests

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Search request", description = "Structure containing search request parameters and filters")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class WorkflowRequest {
  var page: Int = 1
    private set
  var size: Int = 25
    private set
  var userId: String
    private set

  constructor(userId: String) {
    this.userId = userId
  }

  constructor(page: Int, size: Int, userId: String) {
    setPage(page)
    setSize(size)
    this.userId = userId
  }

  fun setUserId(userId: String) {
    this.userId = userId
  }

  fun setPage(page: Int): WorkflowRequest {
    this.page = if (page > 0) page else 1
    return this
  }

  fun setSize(size: Int): WorkflowRequest {
    this.size = if (size > 0) size else 25
    return this
  }
}
