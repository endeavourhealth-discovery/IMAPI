package org.endeavourhealth.imapi.model.imq;

public class PropertyRef extends Element {
	private boolean inverse;
	private String nodeRef;
	private String valueVariable;

	public String getNodeRef() {
		return nodeRef;
	}

	public PropertyRef setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
		return this;
	}

	public String getValueVariable() {
		return valueVariable;
	}

	public PropertyRef setValueVariable(String valueVariable) {
		this.valueVariable = valueVariable;
		return this;
	}


	public boolean isInverse() {
		return inverse;
	}

	public PropertyRef setInverse(boolean inverse) {
		this.inverse = inverse;
		return this;
	}

	public PropertyRef setParameter(String parameter){
		super.setParameter(parameter);
		return this;
	}


	public PropertyRef setVariable(String variable) {
		super.setVariable(variable);
		return this;
	}

}
