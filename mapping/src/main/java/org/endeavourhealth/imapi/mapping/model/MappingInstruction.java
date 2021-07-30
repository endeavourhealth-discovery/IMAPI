package org.endeavourhealth.imapi.mapping.model;

public class MappingInstruction {

	private String property;
	private String reference;
	private String function;

	public MappingInstruction() {
	}

	public MappingInstruction(String property, String reference, String function) {
		super();
		this.property = property;
		this.reference = reference;
		this.function = function;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

}
