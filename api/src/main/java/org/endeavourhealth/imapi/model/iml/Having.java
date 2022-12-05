package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;

import java.util.function.Consumer;

public class Having {

	private Aggregate aggregate;
	private TTAlias property;
	private Value value;

	public Having property(Consumer<TTAlias> builder){
		this.property= new TTAlias();
		builder.accept(this.property);
		return this;
	}


	public Having value(Consumer<Value> builder){
		this.value= new Value();
		builder.accept(value);
		return this;
	}

	public Aggregate getAggregate() {
		return aggregate;
	}

	public Having setAggregate(Aggregate aggregate) {
		this.aggregate = aggregate;
		return this;
	}

	public TTAlias getProperty() {
		return property;
	}

	public Having setProperty(TTAlias property) {
		this.property = property;
		return this;
	}

	public Value getValue() {
		return value;
	}

	@JsonSetter
	public Having setValue(Value value) {
		this.value = value;
		return this;
	}
}
