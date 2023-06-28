package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Property extends Element {
	private boolean inverse;
	private String nodeRef;
	private String valueVariable;

	public String getNodeRef() {
		return nodeRef;
	}

	public Property setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
		return this;
	}

	public String getValueVariable() {
		return valueVariable;
	}

	public Property setValueVariable(String valueVariable) {
		this.valueVariable = valueVariable;
		return this;
	}


	public boolean isInverse() {
		return inverse;
	}

	public Property setInverse(boolean inverse) {
		this.inverse = inverse;
		return this;
	}

	public Property setParameter(String parameter){
		super.setParameter(parameter);
		return this;
	}


	public Property setVariable(String variable) {
		super.setVariable(variable);
		return this;
	}

}
