package org.endeavourhealth.imapi.model.hql;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"name","iri","description","bool","notExist","pathTo","entityType","property","valueCompare","valueIn","valueNotIn",
"valueRange","valueFunction","sort","test"})

public class Match {
	private List<Match> and;
	private List<Match> or;
	private List<Match> not;
	String name;
	TTIriRef id;
	String description;
	TTIriRef pathTo;
	TTIriRef entityType;
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


	public String getName() {
		return name;
	}

	public Match setName(String name) {
		this.name = name;
		return this;
	}

	public TTIriRef getId() {
		return id;
	}

	public Match setId(TTIriRef iri) {
		this.id = iri;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Match setDescription(String description) {
		this.description = description;
		return this;
	}

	public List<Match> getAnd() {
		return and;
	}

	public Match setAnd(List<Match> and) {
		this.and = and;
		return this;
	}

	public Match addAnd(){
		if (this.and==null)
			this.and= new ArrayList<>();
		Match newAnd= new Match();
		and.add(newAnd);
		return newAnd;
	}

	public Match addAnd(Match and){
		if (this.and==null)
			this.and= new ArrayList<>();
		this.and.add(and);
		return and;
	}

	public Match setOr(List<Match> or) {
		this.or = or;
		return this;
	}

	public Match addOr(){
		if (this.or==null)
			this.or= new ArrayList<>();
		Match newOr= new Match();
		this.or.add(newOr);
		return newOr;
	}

	public Match addOr(Match or){
		if (this.or==null)
			this.or= new ArrayList<>();
		this.or.add(or);
		return or;
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

	public TTIriRef getEntityType() {
		return entityType;
	}

	public Match setEntityType(TTIriRef entityType) {
		this.entityType = entityType;
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

	public List<Match> getOr() {
		return or;
	}

	public List<Match> getNot() {
		return not;
	}

	public Match setNot(List<Match> not) {
		this.not = not;
		return this;
	}

	public Match addNot(){
		if (this.not==null)
			this.not= new ArrayList<>();
		Match newNot= new Match();
		this.not.add(newNot);
		return newNot;
	}

	public Match addNot(Match not){
		if (this.not==null)
			this.not= new ArrayList<>();
		this.not.add(not);
		return not;
	}
}
