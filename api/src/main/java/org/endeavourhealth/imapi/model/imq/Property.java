package org.endeavourhealth.imapi.model.imq;

public class Property extends Element{
	private String node;

	public String getNode() {
		return node;
	}

	public Property setNode(String node) {
		this.node = node;
		return this;
	}

	public Property setParameter(String parameter){
		super.setParameter(parameter);
		return this;
	}
}
