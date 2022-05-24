package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"name","iri","description","notExist","entityType","entityId","subsetOf","includeSubEntities","includeMembers","graph","property","includeSubProperties","inverseOf","valueCompare","valueIn","valueNotIn",
"valueRange","valueFunction","valueWithin","valueVar","valueObject","valueConcept","isIndex","and","or","sortLimit","function","optional"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Filter extends Heading {

	private List<Filter> and;
	private List<Filter> or;
	private List<Filter> optional;
	private SortLimit sortLimit;
	private TTIriRef graph;
	private ConceptRef entityType;
	private TTIriRef entityId;
	private TTIriRef entityIn;

	ConceptRef property;
	Compare valueCompare;
	List<TTIriRef> valueIn;
	List<TTIriRef> valueNotIn;
	List<ConceptRef> valueConcept;
	List<ConceptRef> valueNotConcept;
	List<TTIriRef> valueSuperTypeOf;
	Range valueRange;
	String entityVar;
	String propertyVar;
	String valueVar;
	Filter valueObject;
	Function function;
	Within valueWithin;
	boolean inverseOf=false;
	boolean notExist=false;
	boolean isIndex;
	boolean includeSubEntities;




	public TTIriRef getEntityIn() {
		return entityIn;
	}

	public List<ConceptRef> getValueConcept() {
		return valueConcept;
	}

	public Filter setValueConcept(List<ConceptRef> valueConcept) {
		this.valueConcept = valueConcept;
		return this;
	}

	public Filter addValueConcept(ConceptRef value){
		if (this.valueConcept==null)
			this.valueConcept= new ArrayList<>();
		this.valueConcept.add(value);
		return this;
	}

	public Filter addValueConcept(TTIriRef value){
		ConceptRef cr= new ConceptRef(value.getIri(),value.getName());
		addValueConcept(cr);
		return this;
	}
	public Filter addValueNotConcept(ConceptRef value){
		if (this.valueNotConcept==null)
			this.valueNotConcept= new ArrayList<>();
		this.valueNotConcept.add(value);
		return this;
	}

	public List<ConceptRef> getValueNotConcept() {
		return valueNotConcept;
	}

	public Filter setValueNotConcept(List<ConceptRef> valueNotConcept) {
		this.valueNotConcept = valueNotConcept;
		return this;
	}

	public Filter setEntityIn(TTIriRef entityIn) {
		this.entityIn = entityIn;
		return this;
	}

	public List<TTIriRef> getValueSuperTypeOf() {
		return valueSuperTypeOf;
	}

	public Filter setValueSuperTypeOf(List<TTIriRef> valueSuperTypeOf) {
		this.valueSuperTypeOf = valueSuperTypeOf;
		return this;
	}

	public Filter addValueSuperTypeOf(TTIriRef in){
		if (this.valueSuperTypeOf==null)
			this.valueSuperTypeOf= new ArrayList<>();
		this.valueSuperTypeOf.add(in);
		return this;
	}

	public String getEntityVar() {
		return entityVar;
	}

	public Filter setEntityVar(String entityVar) {
		this.entityVar = entityVar;
		return this;
	}

	public String getPropertyVar() {
		return propertyVar;
	}

	public Filter setPropertyVar(String propertyVar) {
		this.propertyVar = propertyVar;
		return this;
	}


	public boolean isIncludeSubEntities() {
		return includeSubEntities;
	}

	public Filter setIncludeSubEntities(boolean includeSubEntities) {
		this.includeSubEntities = includeSubEntities;
		return this;
	}

	public Filter setName(String name){
		super.setName(name);
		return this;
	}


	public boolean isIndex() {
		return isIndex;
	}

	public Filter setIndex(boolean index) {
		isIndex = index;
		return this;
	}



	public TTIriRef getEntityId() {
		return entityId;
	}

	public Filter setEntityId(TTIriRef entityId) {
		this.entityId = entityId;
		return this;
	}

	public SortLimit getSortLimit() {
		return sortLimit;
	}

	public Filter setSortLimit(SortLimit sortLimit) {
		this.sortLimit = sortLimit;
		return this;
	}


	public TTIriRef getGraph() {
		return graph;
	}

	public Filter setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}


	public ConceptRef getEntityType() {
		return entityType;
	}

	@JsonSetter
	public Filter setEntityType(ConceptRef entityType) {
		this.entityType = entityType;
		return this;
	}


	public Filter setEntityType(TTIriRef entityType) {
		this.entityType = ConceptRef.iri(entityType.getIri());
		return this;
	}

	public List<Filter> getAnd() {
		return and;
	}

	public Filter setAnd(List<Filter> and) {
		this.and = and;
		return this;
	}

	public Filter addAnd(Filter must){
		if (this.and ==null)
			this.and = new ArrayList<>();
		this.and.add(must);
		return this;
	}

	public List<Filter> getOr() {
		return or;
	}

	public Filter setOr(List<Filter> or) {
		this.or = or;
		return this;
	}

	public Filter addOr(Filter or){
		if (this.or==null)
			this.or= new ArrayList<>();
		this.or.add(or);
		return this;
	}

	public List<Filter> getOptional() {
		return optional;
	}

	public Filter setOptional(List<Filter> optional) {
		this.optional = optional;
		return this;
	}
	public Filter addOptional(Filter may){
		if (this.optional ==null)
			this.optional = new ArrayList<>();
		this.optional.add(may);
		return this;
	}

	@Override
	public Filter setDescription(String context) {
		super.setDescription(context);
		return this;
	}

	public Within getValueWithin() {
		return valueWithin;
	}

	public Filter setValueWithin(Within valueWithin) {
		this.valueWithin = valueWithin;
		return this;
	}

	public boolean isNotExist() {
		return notExist;
	}

	public Filter setNotExist(boolean notExist) {
		this.notExist = notExist;
		return this;
	}

	public Filter(String iri) {
		super(iri);
	}
	public Filter() {
	}




	public boolean isInverseOf() {
		return inverseOf;
	}

	public Filter setInverseOf(boolean inverseOf) {
		this.inverseOf = inverseOf;
		return this;
	}

	public Filter getValueObject() {
		return valueObject;
	}

	public Filter setValueObject(Filter valueObject) {
		this.valueObject = valueObject;
		return this;
	}




	public Filter setValueCompare(Comparison comp, String value) {
		setValueCompare(new Compare().setComparison(comp).setValueData(value));
		return this;
	}

	public Filter setRangeValueTest(Comparison fromComp, String fromValue,
																	Comparison toComp, String toValue){
		setValueRange(new Range()
			.setFrom(new Compare().setComparison(fromComp).setValue(fromValue))
			.setTo(new Compare().setComparison(toComp).setValue(toValue)));
		return this;
	}



	public ConceptRef getProperty() {
		return property;
	}

	@JsonSetter
	public Filter setProperty(ConceptRef property) {
		this.property = property;
		return this;
	}

	public Filter setProperty(TTIriRef property) {
		this.property = ConceptRef.iri(property.getIri());
		if (property.getName()!=null)
			this.property.setName(property.getName());
		return this;
	}

	public Compare getValueCompare() {
		return valueCompare;
	}

	public Filter setValueCompare(Compare valueCompare) {
		this.valueCompare = valueCompare;
		return this;
	}


	public List<TTIriRef> getValueIn() {
		return valueIn;
	}
	public Filter addValueIn(TTIriRef value){
		if (this.valueIn==null)
			this.valueIn= new ArrayList<>();
		this.valueIn.add(value);
		return this;
	}

	public Filter addValueNotIn(TTIriRef value){
		if (this.valueNotIn==null)
			this.valueNotIn= new ArrayList<>();
		this.valueNotIn.add(value);
		return this;
	}

	public Filter setValueIn(List<TTIriRef> valueIn) {
		this.valueIn = valueIn;
		return this;
	}

	public List<TTIriRef> getValueNotIn() {
		return valueNotIn;
	}

	public Filter setValueNotIn(List<TTIriRef> valueNotIn) {
		this.valueNotIn = valueNotIn;
		return this;
	}

	public Range getValueRange() {
		return valueRange;
	}

	public Filter setValueRange(Range valueRange) {
		this.valueRange = valueRange;
		return this;
	}



	public String getValueVar() {
		return valueVar;
	}

	public Filter setValueVar(String valueVar) {
		this.valueVar = valueVar;
		return this;
	}



	public Function getFunction() {
		return function;
	}

	public Filter setFunction(Function function) {
		this.function = function;
		return this;
	}




}
