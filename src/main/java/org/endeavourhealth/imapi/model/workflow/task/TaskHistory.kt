package org.endeavourhealth.imapi.model.workflow.task

import java.time.LocalDateTime

class TaskHistory {
  var predicate: String? = null
  var originalObject: String? = null
  var newObject: String? = null
  var changeDate: LocalDateTime? = null
  var modifiedBy: String? = null
}
