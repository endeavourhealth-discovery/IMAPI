package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;

@JsonPropertyOrder({"alias","property","variable"})
public class Compare {
	private String alias;
	private TTAlias property;
	private String variable;

	public String getVariable() {
		return variable;
	}

	public Compare setVariable(String variable) {
		this.variable = variable;
		return this;
	}

	public String getAlias() {
		return alias;
	}

	public Compare setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	public TTAlias getProperty() {
		return property;
	}

	public Compare setProperty(TTAlias property) {
		this.property = property;
		return this;
	}
}
