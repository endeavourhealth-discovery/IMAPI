package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"name","iri","description","notExist","entityType","entityId","subsetOf","includeSubEntities","includeMembers","graph"+ ",inverseOf","property","includeSubProperties"
	,"isConcept","inValueSet","notInValueSet", "inRange","value","function","within","valueVar","match","isIndex","and","or","orderLimit","optional"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends Heading {

	private List<Match> and;
	private List<Match> or;
	private List<Match> optional;
	private OrderLimit orderLimit;
	private TTIriRef graph;
	private ConceptRef entityType;
	private ConceptRef entityId;
	private ConceptRef entityInValueSet;

	ConceptRef property;
	Compare value;
	List<TTIriRef> inSet;
	List<TTIriRef> notInSet;
	List<ConceptRef> isConcept;
	List<ConceptRef> isNotConcept;
	Range inRange;
	String entityVar;

	Match match;
	Function function;
	Within within;
	boolean inverseOf=false;
	boolean notExist=false;
	boolean isIndex;


	public Match and(Consumer<Match> builder){
		Match m= new Match();
		this.addAnd(m);
		builder.accept(m);
		return this;
	}

	public Match or(Consumer<Match> builder){
		Match m= new Match();
		this.addOr(m);
		builder.accept(m);
		return this;
	}

	public Match optional(Consumer<Match> builder){
		Match m= new Match();
		this.addOptional(m);
		builder.accept(m);
		return this;
	}


	public ConceptRef getEntityInValueSet() {
		return entityInValueSet;
	}

	public List<ConceptRef> getIsConcept() {
		return isConcept;
	}

	public Match setIsConcept(List<ConceptRef> valueConcept) {
		this.isConcept = valueConcept;
		return this;
	}

	@JsonIgnore
	public Match isConcept(List<ConceptRef> valueConcept) {
		this.isConcept = valueConcept;
		return this;
	}

	public Match addIsConcept(ConceptRef value){
		if (this.isConcept==null)
			this.isConcept= new ArrayList<>();
		this.isConcept.add(value);
		return this;
	}

	public Match addIsConcept(TTIriRef value){
		ConceptRef cr= new ConceptRef(value.getIri(),value.getName());
		addIsConcept(cr);
		return this;
	}

	public Match addIsNotConcept(ConceptRef value){
		if (this.isNotConcept ==null)
			this.isNotConcept = new ArrayList<>();
		this.isNotConcept.add(value);
		return this;
	}

	public List<ConceptRef> getIsNotConcept() {
		return isNotConcept;
	}

	public Match setIsNotConcept(List<ConceptRef> isNotConcept) {
		this.isNotConcept = isNotConcept;
		return this;
	}

	@JsonSetter
	public Match setEntityInValueSet(ConceptRef entityInValueSet) {
		this.entityInValueSet = entityInValueSet;
		return this;
	}

	public Match setEntityInValueSet(TTIriRef entityInValueSet) {
		this.entityInValueSet = new ConceptRef(entityInValueSet);
		return this;
	}


	public String getEntityVar() {
		return entityVar;
	}

	public Match setEntityVar(String entityVar) {
		this.entityVar = entityVar;
		return this;
	}




	public Match setName(String name){
		super.setName(name);
		return this;
	}


	public boolean isIndex() {
		return isIndex;
	}

	public Match setIndex(boolean index) {
		isIndex = index;
		return this;
	}



	public ConceptRef getEntityId() {
		return entityId;
	}

	public Match setEntityId(TTIriRef entityId) {
		this.entityId = new ConceptRef(entityId);
		return this;
	}

	@JsonSetter
	public Match setEntityId(ConceptRef entityId) {
		this.entityId = entityId;
		return this;
	}

	public OrderLimit getOrderLimit() {
		return orderLimit;
	}

	public Match setOrderLimit(OrderLimit orderLimit) {
		this.orderLimit = orderLimit;
		return this;
	}


	public TTIriRef getGraph() {
		return graph;
	}

	public Match setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}


	public ConceptRef getEntityType() {
		return entityType;
	}

	@JsonSetter
	public Match setEntityType(ConceptRef entityType) {
		this.entityType = entityType;
		return this;
	}

	@JsonIgnore
	public Match entityType(ConceptRef entityType) {
		this.entityType = entityType;
		return this;
	}

	@JsonIgnore
	public Match entityType(TTIriRef entityType) {
		this.entityType = ConceptRef.iri(entityType);
		return this;
	}


	public Match setEntityType(TTIriRef entityType) {
		this.entityType = new ConceptRef(entityType);
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

	public List<Match> getOptional() {
		return optional;
	}

	public Match setOptional(List<Match> optional) {
		this.optional = optional;
		return this;
	}
	public Match addOptional(Match may){
		if (this.optional ==null)
			this.optional = new ArrayList<>();
		this.optional.add(may);
		return this;
	}

	@Override
	public Match setDescription(String context) {
		super.setDescription(context);
		return this;
	}

	public Within getWithin() {
		return within;
	}

	public Match setWithin(Within within) {
		this.within = within;
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

	public Match getMatch() {
		return match;
	}

	@JsonSetter
	public Match setMatch(Match match) {
		this.match = match;
		return this;
	}

	@JsonIgnore
	public Match match(Match match) {
		this.match = match;
		return this;
	}

	@JsonIgnore
	public Match match(Consumer<Match> builder){
		Match match= new Match();
		this.match= match;
		builder.accept(match);
		return this;
	}




	public Match setValue(Comparison comp, String value) {
		setValue(new Compare().setComparison(comp).setValueData(value));
		return this;
	}




	public ConceptRef getProperty() {
		return property;
	}

	@JsonSetter
	public Match setProperty(ConceptRef property) {
		this.property = property;
		return this;
	}

	@JsonIgnore
	public Match property(ConceptRef property) {
		this.property = property;
		return this;
	}

	@JsonIgnore
	public Match property(TTIriRef property) {
		this.property = new ConceptRef(property);
		return this;
	}

	public Match setProperty(TTIriRef property) {
		this.property = ConceptRef.iri(property.getIri());
		if (property.getName()!=null)
			this.property.setName(property.getName());
		return this;
	}

	public Compare getValue() {
		return value;
	}

	public Match setValue(Compare value) {
		this.value = value;
		return this;
	}


	public List<TTIriRef> getInSet() {
		return inSet;
	}
	public Match addInSet(TTIriRef value){
		if (this.inSet ==null)
			this.inSet = new ArrayList<>();
		this.inSet.add(value);
		return this;
	}

	public Match addNotInSet(TTIriRef value){
		if (this.notInSet ==null)
			this.notInSet = new ArrayList<>();
		this.notInSet.add(value);
		return this;
	}

	public Match setInSet(List<TTIriRef> inSet) {
		this.inSet = inSet;
		return this;
	}

	public List<TTIriRef> getNotInSet() {
		return notInSet;
	}

	public Match setNotInSet(List<TTIriRef> notInSet) {
		this.notInSet = notInSet;
		return this;
	}

	public Range getInRange() {
		return inRange;
	}

	public Match setInRange(Range inRange) {
		this.inRange = inRange;
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
