package org.endeavourhealth.imapi.mapping.model;

public class MappingInstruction {

	private String property;
	private String reference;
	private String constant;
	private String template;
	private String function;

	public MappingInstruction() {
	}

	public String getProperty() {
		return property;
	}

	public MappingInstruction setProperty(String property) {
		this.property = property;
		return this;
	}

	public String getReference() {
		return reference;
	}

	public MappingInstruction setReference(String reference) {
		this.reference = reference;
		return this;
	}

	public String getFunction() {
		return function;
	}

	public MappingInstruction setFunction(String function) {
		this.function = function;
		return this;
	}

	public String getConstant() {
		return constant;
	}

	public MappingInstruction setConstant(String constant) {
		this.constant = constant;
		return this;
	}

	public String getTemplate() {
		return template;
	}

	public MappingInstruction setTemplate(String template) {
		this.template = template;
		return this;
	}

}
