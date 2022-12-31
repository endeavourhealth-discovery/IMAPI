package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@JsonPropertyOrder({"alias","property","variable"})
public class Compare {

	private TTIriRef property;
	private String variable;
	private String alias;

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

	public TTIriRef getProperty() {
		return property;
	}

	public Compare setProperty(TTIriRef property) {
		this.property = property;
		return this;
	}
}
