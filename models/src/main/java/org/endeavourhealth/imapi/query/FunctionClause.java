package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

/**
 * A clause which applies a function to a set of supplied arguments to produce a result
 */
public class FunctionClause {
	private TTIriRef functionName;
	private List<Object> arguments = new ArrayList<>();
	private String resultVariable;
	private Class resultType;

	public TTIriRef getFunctionName() {
		return functionName;
	}

	public FunctionClause setFunctionName(TTIriRef functionName) {
		this.functionName = functionName;
		return this;
	}

	public List<Object> getArguments() {
		return arguments;
	}

	public FunctionClause setArguments(List<Object> arguments) {
		this.arguments = arguments;
		return this;
	}

	public FunctionClause addArgument(Object argument) {
		this.arguments.add(argument);
		return this;
	}

	public String getResultVariable() {
		return resultVariable;
	}

	public FunctionClause setResultVariable(String resultVariable) {
		this.resultVariable = resultVariable;
		return this;
	}

	public Class getResultType() {
		return resultType;
	}

	public FunctionClause setResultType(Class resultType) {
		this.resultType = resultType;
		return this;
	}
}
