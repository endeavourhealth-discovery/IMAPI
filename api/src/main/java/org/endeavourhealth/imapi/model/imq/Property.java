package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Property extends Element {
	private boolean inverse;


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
