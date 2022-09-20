package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.function.Consumer;

@JsonPropertyOrder({"comparison","value","relativeTo"})
public class Value {
	private String comparison;
	private String value;
	private Compare relativeTo;

	public Compare getRelativeTo() {
		return relativeTo;
	}

	@JsonSetter
	public Value setRelativeTo(Compare relativeTo) {
		this.relativeTo = relativeTo;
		return this;
	}

	public Value relativeTo(Consumer<Compare> builder){
		this.relativeTo= new Compare();
		builder.accept(this.relativeTo);
		return this;
	}

	public String getComparison() {
		return comparison;
	}

	public Value setComparison(String comparison) {
		this.comparison = comparison;
		return this;
	}

	public String getValue() {
		return value;
	}

	public Value setValue(String value) {
		this.value = value;
		return this;
	}
}
