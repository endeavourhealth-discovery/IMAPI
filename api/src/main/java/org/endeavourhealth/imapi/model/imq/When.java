package org.endeavourhealth.imapi.model.imq;

public class When extends Property{
	private ReturnProperty then;

	public ReturnProperty getThen() {
		return then;
	}

	public When setThen(ReturnProperty then) {
		this.then = then;
		return this;
	}
}
