package com.endavourhealth.models.objectModel;

public class Parameter {
    private String fromPath;
    private String fixed;
    private String variable;
    private Function function;

    public String getFromPath() {
        return fromPath;
    }

    public Parameter setFromPath(String fromPath) {
        this.fromPath = fromPath;
        return this;
    }

    public String getFixed() {
        return fixed;
    }

    public Parameter setFixed(String fixed) {
        this.fixed = fixed;
        return this;
    }

    public String getVariable() {
        return variable;
    }

    public Parameter setVariable(String variable) {
        this.variable = variable;
        return this;
    }

    public Function getFunction() {
        return function;
    }

    public Parameter setFunction(Function function) {
        this.function = function;
        return this;
    }
}
