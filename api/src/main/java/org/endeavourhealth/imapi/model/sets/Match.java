package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIri;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"name","iri","description","notExist","entityType","entityId","subsetOf","includeMembers","graph","property","inverseOf","valueCompare","valueIn","valueNotIn",
"valueRange","valueFunction","valueWithin","valueVar","valueObject","isIndex","and","or","may"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends TTIri {

	private List<Match> and;
	private List<Match> or;
	private List<Match> may;
	private SortLimit sortLimit;
	private TTIriRef graph;
	private TTIriRef entityType;
	private TTIriRef entityId;
	private List<TTIri> subsetOf;
	TTIriRef property;
	Compare valueCompare;
	List<TTIriRef> valueIn;
	List<TTIriRef> valueNotIn;
	Range valueRange;
	Function valueFunction;
	String valueVar;
	Match valueObject;
	Function function;
	Within valueWithin;
	boolean inverseOf=false;
	boolean notExist=false;
	boolean includeMembers=false;
	boolean isIndex;

	public boolean isIndex() {
		return isIndex;
	}

	public Match setIndex(boolean index) {
		isIndex = index;
		return this;
	}

	public List<TTIri> getSubsetOf() {
		return subsetOf;
	}

	public Match setSubsetOf(List<TTIri> subsetOf) {
		this.subsetOf = subsetOf;
		return this;
	}

	public Match addSubsetOf(TTIri from){
		if (this.subsetOf ==null)
			this.subsetOf = new ArrayList<>();
		this.subsetOf.add(from);
		return this;
	}
	public boolean isIncludeMembers() {
		return includeMembers;
	}

	public Match setIncludeMembers(boolean includeMembers) {
		this.includeMembers = includeMembers;
		return this;
	}

	public TTIriRef getEntityId() {
		return entityId;
	}

	public Match setEntityId(TTIriRef entityId) {
		this.entityId = entityId;
		return this;
	}

	public SortLimit getSortLimit() {
		return sortLimit;
	}

	public Match setSortLimit(SortLimit sortLimit) {
		this.sortLimit = sortLimit;
		return this;
	}


	public TTIriRef getGraph() {
		return graph;
	}

	public Match setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}


	public TTIriRef getEntityType() {
		return entityType;
	}

	public Match setEntityType(TTIriRef entityType) {
		this.entityType = entityType;
		return this;
	}

	public List<Match> getAnd() {
		return and;
	}

	public Match setAnd(List<Match> and) {
		this.and = and;
		return this;
	}

	public Match addAnd(Match must){
		if (this.and ==null)
			this.and = new ArrayList<>();
		this.and.add(must);
		return this;
	}

	public List<Match> getOr() {
		return or;
	}

	public Match setOr(List<Match> or) {
		this.or = or;
		return this;
	}

	public Match addOr(Match or){
		if (this.or==null)
			this.or= new ArrayList<>();
		this.or.add(or);
		return this;
	}

	public List<Match> getMay() {
		return may;
	}

	public Match setMay(List<Match> may) {
		this.may = may;
		return this;
	}
	public Match addMay(Match may){
		if (this.may==null)
			this.may= new ArrayList<>();
		this.may.add(may);
		return this;
	}


	@Override
	public String getDescription() {
		return super.getDescription();
	}

	@Override
	public Match setDescription(String context) {
		super.setDescription(context);
		return this;
	}

	public Within getValueWithin() {
		return valueWithin;
	}

	public Match setValueWithin(Within valueWithin) {
		this.valueWithin = valueWithin;
		return this;
	}

	public boolean isNotExist() {
		return notExist;
	}

	public Match setNotExist(boolean notExist) {
		this.notExist = notExist;
		return this;
	}

	public Match(String iri) {
		super(iri);
	}
	public Match() {
	}




	public boolean isInverseOf() {
		return inverseOf;
	}

	public Match setInverseOf(boolean inverseOf) {
		this.inverseOf = inverseOf;
		return this;
	}

	public Match getValueObject() {
		return valueObject;
	}

	public Match setValueObject(Match valueObject) {
		this.valueObject = valueObject;
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



	public Function getFunction() {
		return function;
	}

	public Match setFunction(Function function) {
		this.function = function;
		return this;
	}




}
