package org.endeavourhealth.imapi.model.workflow;

public class Task {

    private String name;
    private String state;
    private String id;
    private String workflow;

    public String getWorkflow() {
        return workflow;
    }

    public Task setWorkflow(String workflow) {
        this.workflow = workflow;
        return this;
    }

    public String getName() {
        return name;
    }

    public Task setName(String name) {
        this.name = name;
        return this;
    }

    public String getState() {
        return state;
    }

    public Task setState(String state) {
        this.state = state;
        return this;
    }

    public String getId() {
        return id;
    }

    public Task setId(String id) {
        this.id = id;
        return this;
    }
}
