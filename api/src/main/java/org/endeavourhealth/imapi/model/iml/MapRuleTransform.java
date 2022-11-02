package org.endeavourhealth.imapi.model.iml;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.function.Consumer;

@JsonPropertyOrder({"context","variable","min","max","function"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MapRuleTransform {
	private String context;
	private String variable;
	private Function function;

	public String getContext() {
		return context;
	}

	public MapRuleTransform setContext(String context) {
		this.context = context;
		return this;
	}

	public Function getFunction() {
		return function;
	}

	public MapRuleTransform setFunction(Function function) {
		this.function = function;
		return this;
	}

	public MapRuleTransform function(Consumer<Function> builder){
		this.function= new Function();
		builder.accept(this.function);
		return this;
	}

	public String getVariable() {
		return variable;
	}

	public MapRuleTransform setVariable(String variable) {
		this.variable = variable;
		return this;
	}

}
