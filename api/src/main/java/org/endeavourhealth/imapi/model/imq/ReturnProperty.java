package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.function.Consumer;

@JsonPropertyOrder({"node","variable","iri","name","function","as"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ReturnProperty {
	private String iri;
	private String propertyRef;
	private Return node;
	private String name;
	private String valueVariable;
	private boolean inverse;
	private FunctionClause function;
	private String unit;
	private String as;

	public String getAs() {
		return as;
	}

	@JsonSetter
	public ReturnProperty setAs(String as) {
		this.as = as;
		return this;
	}

	public ReturnProperty as(String as) {
		this.as = as;
		return this;
	}

	public String getUnit() {
		return unit;
	}

	public ReturnProperty setUnit(String unit) {
		this.unit = unit;
		return this;
	}

	public FunctionClause getFunction() {
		return function;
	}

	public ReturnProperty setFunction(FunctionClause function) {
		this.function = function;
		return this;
	}

	public ReturnProperty function(Consumer<FunctionClause> builder){
		builder.accept(setFunction(new FunctionClause()).getFunction());
		return this;
	}

	public boolean isInverse() {
		return inverse;
	}

	public ReturnProperty setInverse(boolean inverse) {
		this.inverse = inverse;
		return this;
	}

	public String getValueVariable() {
		return valueVariable;
	}

	public ReturnProperty setValueVariable(String valueVariable) {
		this.valueVariable = valueVariable;
		return this;
	}

	public String getName() {
		return name;
	}

	public ReturnProperty setName(String name) {
		this.name = name;
		return this;
	}

	@JsonProperty("@id")
	public String getIri() {
		return iri;
	}

	public ReturnProperty setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public String getPropertyRef() {
		return propertyRef;
	}

	public ReturnProperty setPropertyRef(String propertyRef) {
		this.propertyRef = propertyRef;
		return this;
	}

	public Return getNode() {
		return node;
	}

	public ReturnProperty setNode(Return node) {
		this.node = node;
		return this;
	}



	public ReturnProperty node(Consumer<Return> builder){
		builder.accept(setNode(new Return()).getNode());
		return this;
	}

}
