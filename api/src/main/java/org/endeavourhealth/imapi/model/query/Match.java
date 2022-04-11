package org.endeavourhealth.imapi.model.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"name","iri","description","bool","notExist","pathTo","graph","entityId","includeMembers","entityType","property","valueCompare","valueIn","valueNotIn",
"valueRange","valueFunction","sort","test"})

public class Match extends Profile {


	TTIriRef entityId;
	boolean includeMembers;
	TTIriRef graph;
	TTIriRef pathTo;
	TTIriRef property;
	Compare valueCompare;
	List<TTIriRef> valueIn;
	List<TTIriRef> valueNotIn;
	Range valueRange;
	Function valueFunction;
	String valueVar;
	Sort sort;
	Match test;
	Function function;
	boolean notExist;

	public boolean isIncludeMembers() {
		return includeMembers;
	}

	public Match setIncludeMembers(boolean includeMembers) {
		this.includeMembers = includeMembers;
		return this;
	}

	public TTIriRef getGraph() {
		return graph;
	}

	public Match setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}

	public Match setEntityType(TTIriRef entityType) {
		super.setEntityType(entityType);
		return this;
	}



	public Match addValueIn(TTIriRef in){
		if (valueIn==null)
			valueIn= new ArrayList<>();
		valueIn.add(in);
		return this;
	}


	public Match addValueNotIn(TTIriRef notIn){
		if (valueNotIn==null)
			valueNotIn= new ArrayList<>();
		valueNotIn.add(notIn);
		return this;
	}


	public Match setValueCompare(Comparison comp, String value) {
		setValueCompare(new Compare().setComparison(comp).setValueData(value));
		return this;
	}

	public Match setRangeValueTest(Comparison fromComp, String fromValue,
																 Comparison toComp, String toValue){
		setValueRange(new Range()
			.setFrom(new Compare().setComparison(fromComp).setValue(fromValue))
			.setTo(new Compare().setComparison(toComp).setValue(toValue)));
		return this;
	}


	public TTIriRef getPathTo() {
		return pathTo;
	}

	public Match setPathTo(TTIriRef pathTo) {
		this.pathTo = pathTo;
		return this;
	}


	public TTIriRef getProperty() {
		return property;
	}

	public Match setProperty(TTIriRef property) {
		this.property = property;
		return this;
	}

	public Compare getValueCompare() {
		return valueCompare;
	}

	public Match setValueCompare(Compare valueCompare) {
		this.valueCompare = valueCompare;
		return this;
	}

	public List<TTIriRef> getValueIn() {
		return valueIn;
	}

	public Match setValueIn(List<TTIriRef> valueIn) {
		this.valueIn = valueIn;
		return this;
	}

	public List<TTIriRef> getValueNotIn() {
		return valueNotIn;
	}

	public Match setValueNotIn(List<TTIriRef> valueNotIn) {
		this.valueNotIn = valueNotIn;
		return this;
	}

	public Range getValueRange() {
		return valueRange;
	}

	public Match setValueRange(Range valueRange) {
		this.valueRange = valueRange;
		return this;
	}

	public Function getValueFunction() {
		return valueFunction;
	}

	public Match setValueFunction(Function valueFunction) {
		this.valueFunction = valueFunction;
		return this;
	}

	public String getValueVar() {
		return valueVar;
	}

	public Match setValueVar(String valueVar) {
		this.valueVar = valueVar;
		return this;
	}

	public Match getTest() {
		return test;
	}

	public Match setTest(Match test) {
		this.test = test;
		return this;
	}

	public Function getFunction() {
		return function;
	}

	public Match setFunction(Function function) {
		this.function = function;
		return this;
	}

	public boolean isNotExist() {
		return notExist;
	}

	public Match setNotExist(boolean notExist) {
		this.notExist = notExist;
		return this;
	}


	public Sort getSort() {
		return sort;
	}

	public Match setSort(Sort sort) {
		this.sort = sort;
		return this;
	}

	public TTIriRef getEntityId() {
		return entityId;
	}

	public Match setEntityId(TTIriRef entityId) {
		this.entityId = entityId;
		return this;
	}
}
