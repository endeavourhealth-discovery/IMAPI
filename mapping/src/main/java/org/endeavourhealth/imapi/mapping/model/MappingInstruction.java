package org.endeavourhealth.imapi.mapping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MappingInstruction {

	private String property;
	private String parentProperty;
	private boolean isBnode;
	private String valueType; // [reference, constant, template, functionValue]
	private String value;

	public MappingInstruction() {
	}

	public MappingInstruction(String property, String parentProperty, boolean isBnode, String valueType, String value) {
		this.parentProperty = parentProperty;
		this.isBnode = isBnode;
		this.property = property;
		this.valueType = valueType;
		this.value = value;
	}

	public MappingInstruction(String property, String valueType, String value) {
		this.property = property;
		this.valueType = valueType;
		this.value = value;
	}

	@JsonIgnore
	public String getPathFromReference(String reference) {
		reference = reference == null ? this.value : reference;
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

	public String getValueType() {
		return valueType;
	}

	public MappingInstruction setValueType(String valueType) {
		this.valueType = valueType;
		return this;
	}

	public String getValue() {
		return value;
	}

	public MappingInstruction setValue(String value) {
		this.value = value;
		return this;
	}

	public boolean isBnode() {
		return isBnode;
	}

	public MappingInstruction setBnode(boolean isBnode) {
		this.isBnode = isBnode;
		return this;
	}

	public String getParentProperty() {
		return parentProperty;
	}

	public MappingInstruction setParentProperty(String parentProperty) {
		this.parentProperty = parentProperty;
		return this;
	}

	public String getProperty() {
		return property;
	}

	public MappingInstruction setProperty(String property) {
		this.property = property;
		return this;
	}

}
