package org.endeavourhealth.imapi.model.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"name","iri","description","bool","notExist","pathTo","entityType","property","valueCompare","valueIn","valueNotIn",
"valueRange","valueFunction","sort","test"})

public class Clause{
	String name;
	TTIriRef iri;
	String description;
	BooleanClause bool;
	TTIriRef pathTo;
	TTIriRef entityType;
	TTIriRef property;
	CompareClause valueCompare;
	List<TTIriRef> valueIn;
	List<TTIriRef> valueNotIn;
	Range valueRange;
	FunctionClause valueFunction;
	String valueVar;
	SortClause sort;
	Clause test;
	FunctionClause function;
	boolean notExist;


	public String getName() {
		return name;
	}

	public Clause setName(String name) {
		this.name = name;
		return this;
	}

	public TTIriRef getIri() {
		return iri;
	}

	public Clause setIri(TTIriRef iri) {
		this.iri = iri;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Clause setDescription(String description) {
		this.description = description;
		return this;
	}


	public Clause addValueIn(TTIriRef in){
		if (valueIn==null)
			valueIn= new ArrayList<>();
		valueIn.add(in);
		return this;
	}


	public Clause addValueNotIn(TTIriRef notIn){
		if (valueNotIn==null)
			valueNotIn= new ArrayList<>();
		valueNotIn.add(notIn);
		return this;
	}


	public Clause setValueTest(Comparison comp, String value) {
		setValueCompare(new CompareClause().setComparison(comp).setValueData(value));
		return this;
	}

	public Clause setRangeValueTest(Comparison fromComp, String fromValue,
																	Comparison toComp, String toValue){
		setValueRange(new Range()
			.setFrom(new Compare().setComparison(fromComp).setValue(fromValue))
			.setTo(new Compare().setComparison(toComp).setValue(toValue)));
		return this;
	}


	public TTIriRef getPathTo() {
		return pathTo;
	}

	public Clause setPathTo(TTIriRef pathTo) {
		this.pathTo = pathTo;
		return this;
	}

	public TTIriRef getEntityType() {
		return entityType;
	}

	public Clause setEntityType(TTIriRef entityType) {
		this.entityType = entityType;
		return this;
	}

	public TTIriRef getProperty() {
		return property;
	}

	public Clause setProperty(TTIriRef property) {
		this.property = property;
		return this;
	}

	public CompareClause getValueCompare() {
		return valueCompare;
	}

	public Clause setValueCompare(CompareClause valueCompare) {
		this.valueCompare = valueCompare;
		return this;
	}

	public List<TTIriRef> getValueIn() {
		return valueIn;
	}

	public Clause setValueIn(List<TTIriRef> valueIn) {
		this.valueIn = valueIn;
		return this;
	}

	public List<TTIriRef> getValueNotIn() {
		return valueNotIn;
	}

	public Clause setValueNotIn(List<TTIriRef> valueNotIn) {
		this.valueNotIn = valueNotIn;
		return this;
	}

	public Range getValueRange() {
		return valueRange;
	}

	public Clause setValueRange(Range valueRange) {
		this.valueRange = valueRange;
		return this;
	}

	public FunctionClause getValueFunction() {
		return valueFunction;
	}

	public Clause setValueFunction(FunctionClause valueFunction) {
		this.valueFunction = valueFunction;
		return this;
	}

	public String getValueVar() {
		return valueVar;
	}

	public Clause setValueVar(String valueVar) {
		this.valueVar = valueVar;
		return this;
	}

	public Clause getTest() {
		return test;
	}

	public Clause setTest(Clause test) {
		this.test = test;
		return this;
	}

	public FunctionClause getFunction() {
		return function;
	}

	public Clause setFunction(FunctionClause function) {
		this.function = function;
		return this;
	}

	public boolean isNotExist() {
		return notExist;
	}

	public Clause setNotExist(boolean notExist) {
		this.notExist = notExist;
		return this;
	}

	public BooleanClause getBool() {
		return bool;
	}

	public Clause setBool(BooleanClause bool) {
		this.bool = bool;
		return this;
	}

	public SortClause getSort() {
		return sort;
	}

	public Clause setSort(SortClause sort) {
		this.sort = sort;
		return this;
	}
}
