package org.endeavourhealth.imapi.model.iml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.function.Consumer;

@JsonPropertyOrder({"comparison","value","relativeTo"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Value {
	private String comparison;
	private String value;
	private Compare valueOf;
	private String unitOfTime;

	public String getUnitOfTime() {
		return unitOfTime;
	}

	public Value setUnitOfTime(String unitOfTime) {
		this.unitOfTime = unitOfTime;
		return this;
	}

	public Compare getValueOf() {
		return valueOf;
	}

	@JsonSetter
	public Value setValueOf(Compare valueOf) {
		this.valueOf = valueOf;
		return this;
	}

	public Value valueOf(Consumer<Compare> builder){
		this.valueOf= new Compare();
		builder.accept(this.valueOf);
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
