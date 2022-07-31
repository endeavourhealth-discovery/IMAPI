package org.endeavourhealth.imapi.model.function;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashMap;
import java.util.Map;

public class FunctionRequest {
    private String functionIri;
    private Map<String,Object> arguments;

    public String getFunctionIri() {
        return functionIri;
    }

    public FunctionRequest setFunctionIri(String functionIri) {
        this.functionIri = functionIri;
        return this;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public FunctionRequest setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
        return this;
    }

    @JsonIgnore
    public FunctionRequest putArgument(String variable, String value) {
        if (this.arguments==null)
            this.arguments= new HashMap<>();
        arguments.put(variable,value);
        return this;
    }

    @JsonIgnore
    public FunctionRequest addArgument(String variable, String value){
        putArgument(variable,value);
        return this;
    }
}
