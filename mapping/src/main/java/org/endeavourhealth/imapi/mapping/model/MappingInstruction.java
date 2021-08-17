package org.endeavourhealth.imapi.mapping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MappingInstruction {

	private String property;
	private String reference;
	private String constant;
	private String template;
	private String function;

	public MappingInstruction() {
	}

	@JsonIgnore
	public String getValueTypeString() {
		if (getReference() != null) {
			return "reference";
		} else if (getConstant() != null) {
			return "constant";
		} else if (getFunction() != null) {
			return "function";
		} else if (getTemplate() != null) {
			return "template";
		}
		return null;
	}

	@JsonIgnore
	public String getPathFromReference(String reference) {
		reference = reference == null ? this.reference : reference;
		if (reference.contains(".")) {
			reference = reference.replaceAll(".", "/");
		}
		if (reference.contains("[")) {
			reference = reference.replaceAll("[\\[\\]]", "/");
		}
		if (reference.contains("//")) {
			reference = reference.replaceAll("//", "/");
		}
		if (reference.contains("'")) {
			reference = reference.replaceAll("'", "");
		}
		if (reference.endsWith("/")) {
			reference = reference.substring(0, reference.length() - 1);
		}
		return "/" + reference;
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
