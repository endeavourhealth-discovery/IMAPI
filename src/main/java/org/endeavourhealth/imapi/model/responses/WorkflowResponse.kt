package org.endeavourhealth.imapi.model.responses

import org.endeavourhealth.imapi.model.workflow.Task

class WorkflowResponse {
  var page: Int = 1
    private set

  var count: Int = 0
    private set

  var tasks: MutableList<Task> = arrayListOf()
    private set

  fun setPage(page: Int): WorkflowResponse {
    this.page = if (page > 0) page else 1
    return this
  }

  fun setCount(count: Int): WorkflowResponse {
    this.count = if (count > 0) count else 0
    return this
  }

  fun setTasks(tasks: MutableList<Task>): WorkflowResponse {
    this.tasks = tasks
    return this
  }

  fun addTask(task: Task): WorkflowResponse {
    this.tasks.add(task)
    return this
  }

  fun addTasks(tasks: MutableList<Task>): WorkflowResponse {
    this.tasks.addAll(tasks)
    return this
  }
}
