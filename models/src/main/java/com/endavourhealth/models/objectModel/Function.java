package com.endavourhealth.models.objectModel;

import java.util.List;

public class Function {
    private String name;
    private String description;
    private List<Parameter> parameter;

    public String getName() {
        return name;
    }

    public Function setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Function setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Parameter> getParameter() {
        return parameter;
    }

    public Function setParameter(List<Parameter> parameter) {
        this.parameter = parameter;
        return this;
    }
}
