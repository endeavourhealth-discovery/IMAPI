package org.endeavourhealth.imapi.model.imq;

import java.util.List;
import java.util.function.Consumer;

public class When {
	private Property property;
	private ReturnProperty then;

	public Property getProperty() {
		return property;
	}

	public When setProperty(Property property) {
		this.property = property;
		return this;
	}

	public When property(Consumer<Property> builder) {
		this.property= new Property();
		builder.accept(this.property);
		return this;
	}

	public ReturnProperty getThen() {
		return then;
	}

	public When setThen(ReturnProperty then) {
		this.then = then;
		return this;
	}

	public When then(Consumer<ReturnProperty> builder) {
		this.then= new ReturnProperty();
		builder.accept(this.then);
		return this;
	}


}
