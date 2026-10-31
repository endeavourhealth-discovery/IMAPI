package org.endeavourhealth.imapi.model.workflow

import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.model.workflow.task.TaskHistory
import org.endeavourhealth.imapi.model.workflow.task.TaskState
import org.endeavourhealth.imapi.model.workflow.task.TaskType
import java.time.LocalDateTime

open class Task(
  var id: TTIriRef? = null,
  var createdBy: String? = null,
  var type: TaskType? = null,
  var state: TaskState? = null,
  var assignedTo: String? = null,
  var dateCreated: LocalDateTime? = null,
  var history: MutableList<TaskHistory>? = null,
  var hostUrl: String? = null
) {
  fun addTaskHistory(taskHistory: TaskHistory): Task {
    if (null == this.history) this.history = arrayListOf()
    this.history!!.add(taskHistory)
    return this
  }
}
