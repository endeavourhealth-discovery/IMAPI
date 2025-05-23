package org.endeavourhealth.imapi.model.workflow;

import lombok.Getter;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;

import java.util.ArrayList;
import java.util.List;


public class WorkflowResponse {
    @Getter
    private Integer page;
    @Getter
    private Integer count;
    private List<Task> tasks = new ArrayList<>();

    public WorkflowResponse setPage(Integer page) {
        this.page = page;
        return this;
    }

    public WorkflowResponse setCount(Integer count) {
        this.count = count;
        return this;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public WorkflowResponse setTasks(List<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public WorkflowResponse addTask(Task task) {
        this.tasks.add(task);
        return this;
    }

    public WorkflowResponse addTasks(List<Task> tasks) {
         this.tasks.addAll(tasks);
         return this;
    }
}
