package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class FunctionRequest {
	private String functionIri;
	private List<Argument> arguments;
	public String getFunctionIri() {
		return functionIri;
	}
	public FunctionRequest setFunctionIri(String functionIri) {
		this.functionIri = functionIri;
		return this;
	}
	public List<Argument> getArguments() {
		return arguments;
	}

	public FunctionRequest setArguments(List<Argument> arguments) {
		this.arguments = arguments;
		return this;
	}

	public FunctionRequest addArgument(Argument argument) {
		if (null == argument) this.arguments = new ArrayList<>();
		this.arguments.add(argument);
		return this;
	}
}
