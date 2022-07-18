package org.endeavourhealth.imapi.model.forms;

public class FormArgument {
	String parameter;
	String valuePath;
	String valueArgument;

	public String getParameter() {
		return parameter;
	}

	public FormArgument setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public String getValuePath() {
		return valuePath;
	}

	public FormArgument setValuePath(String valuePath) {
		this.valuePath = valuePath;
		return this;
	}

	public String getValueArgument() {
		return valueArgument;
	}

	public FormArgument setValueArgument(String valueArgument) {
		this.valueArgument = valueArgument;
		return this;
	}
}
