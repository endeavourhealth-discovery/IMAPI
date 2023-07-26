package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.function.Consumer;

@JsonPropertyOrder({"node","variable","iri","name","function","as"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ReturnProperty {
	private String iri;
	private String nodeRef;
	private String propertyRef;
	private String value;
	private String valueRef;
	private boolean inverse;
	private FunctionClause function;
	private String unit;
	private String as;
	private Return returx;
	private TTIriRef dataType;
	private Case casex;


	@JsonProperty("case")
	public Case getCase() {
		return casex;
	}

	@JsonProperty("case")
	public ReturnProperty setCase(Case casex) {
		this.casex = casex;
		return this;
	}

	public ReturnProperty case_(Consumer<Case> builder){
		builder.accept(this.setCase(new Case()).getCase());
		return this;
	}

	public TTIriRef getDataType() {
		return dataType;
	}

	public ReturnProperty setDataType(TTIriRef dataType) {
		this.dataType = dataType;
		return this;
	}

	public String getNodeRef() {
		return nodeRef;
	}

	public ReturnProperty setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
		return this;
	}

	public String getValueRef() {
		return valueRef;
	}

	public ReturnProperty setValueRef(String valueRef) {
		this.valueRef = valueRef;
		return this;
	}

	@JsonProperty("return")
	public Return getReturn() {
		return returx;
	}

	public ReturnProperty setReturn(Return returx) {
		this.returx = returx;
		return this;
	}

	public ReturnProperty return_(Consumer<Return> builder) {
		this.returx = new Return();
		builder.accept(this.returx);
		return this;
	}

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


	public String getValue() {
		return value;
	}

	public ReturnProperty setValue(String value) {
		this.value = value;
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


}
