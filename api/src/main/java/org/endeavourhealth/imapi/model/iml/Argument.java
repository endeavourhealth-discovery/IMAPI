package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Argument {

	private String parameter;
	private String valueData;
	private String valueVariable;
	private Compare valueFrom;

	public Compare getValueFrom() {
		return valueFrom;
	}

	public Argument setValueFrom(Compare valueFrom) {
		this.valueFrom = valueFrom;
		return this;
	}

	public String getValueVariable() {
		return valueVariable;
	}

	public Argument setValueVariable(String valueVariable) {
		this.valueVariable = valueVariable;
		return this;
	}

	public String getParameter() {
		return parameter;
	}

	public Argument setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public String getValueData() {
		return valueData;
	}

	public Argument setValueData(String valueData) {
		this.valueData = valueData;
		return this;
	}

}
