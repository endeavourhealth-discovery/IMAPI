package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"displayText","name","iri","description","pathTo","notExist","entityType","entityId","entityInSet","entityInList","includeSubEntities","graph","inverseOf","path","property","includeSubProperties"
	,"isConcept","inValueSet","notInValueSet", "inRange","value","function","within","valueVar","match","isIndex","and","or","orderLimit","optional"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Match extends Heading {

	private List<Match> or;
	private List<Match> and;
	private TTIriRef graph;
	private ConceptRef entityType;
	private ConceptRef entityId;
	private List<ConceptRef> entityInSet;
	private List<ConceptRef> entityNotInSet;
	private String entityVar;
	private List<PropertyValue> property;
	private List<PropertyValue> orProperty;
	private boolean notExist;
	private List<ConceptRef> pathTo;
	boolean isIndex;
	private OrderLimit orderLimit;
	private List<PropertyValue> testProperty;
	private String displayText;




	public String getDisplayText() {
		return displayText;
	}

	public Match setDisplayText(String displayText) {
		this.displayText = displayText;
		return this;
	}

	public List<PropertyValue> getTestProperty() {
		return testProperty;
	}

	@JsonSetter
	public Match setTestProperty(List<PropertyValue> test) {
		this.testProperty = test;
		return this;
	}

	public Match addTestProperty(PropertyValue test){
		if (this.testProperty==null)
			this.testProperty= new ArrayList<>();
		this.testProperty.add(test);
		return this;
	}

	@JsonIgnore
	public Match testProperty(Consumer<PropertyValue> builder){
		PropertyValue test= new PropertyValue();
		this.addTestProperty(test);
		builder.accept(test);
		return this;
	}

	/**
	 * Lambda approach for setting order and limit
	 * @param builder lambda expression
	 * @return this for chaining
	 */
	public Match order(Consumer<OrderLimit> builder){
		OrderLimit ol= new OrderLimit();
		this.orderLimit= ol;
		builder.accept(ol);
		return this;
	}




	public OrderLimit getOrderLimit() {
		return orderLimit;
	}

	public Match setOrderLimit(OrderLimit orderLimit) {
		this.orderLimit = orderLimit;
		return this;
	}


	public List<Match> getAnd() {
		return and;
	}

	@JsonSetter
	public Match setAnd(List<Match> and) {
		this.and = and;
		return this;
	}




	public List<ConceptRef> getPathTo() {
		return pathTo;
	}

	@JsonSetter
	public Match setPathTo(List<ConceptRef> pathTo) {
		this.pathTo = pathTo;
		return this;
	}
	public Match addPathTo(ConceptRef pathTo) {
		if (this.pathTo==null)
			this.pathTo= new ArrayList<>();
		this.pathTo.add(pathTo);
		return this;
	}


	public List<PropertyValue> getOrProperty() {
		return orProperty;
	}

	public Match setOrProperty(List<PropertyValue> orProperty) {
		this.orProperty = orProperty;
		return this;
	}

	public Match addOrProperty(PropertyValue or){
		if (this.orProperty==null)
			this.orProperty = new ArrayList<>();
		this.orProperty.add(or);
		return this;
	}

	public Match orProperty(Consumer<PropertyValue> builder){
		PropertyValue pv= new PropertyValue();
		this.addOrProperty(pv);
		builder.accept(pv);
		return this;
	}

	public boolean isNotExist() {
		return notExist;
	}

	public Match setNotExist(boolean notExist) {
		this.notExist = notExist;
		return this;
	}






	public List<ConceptRef> getEntityNotInSet() {
		return entityNotInSet;
	}

	@JsonSetter
	public Match setEntityNotInSet(List<ConceptRef> entityNotInSet) {
		this.entityNotInSet = entityNotInSet;
		return this;
	}

	public Match addEntityNotInSet(ConceptRef notin){
		if (this.entityNotInSet==null)
			this.entityNotInSet= new ArrayList<>();
		this.entityNotInSet.add(notin);
		return this;
	}

	public Match addEntityNotInSet(TTIriRef notin){
		addEntityNotInSet(new ConceptRef(notin));
		return this;
	}

	public Match addEntityInSet(ConceptRef in){
		if (this.entityInSet==null)
			this.entityInSet= new ArrayList<>();
		this.entityInSet.add(in);
		return this;
	}

	public Match addEntityInSet(TTIriRef notin){
		addEntityInSet(new ConceptRef(notin));
		return this;
	}



	public Match or(Consumer<Match> builder){
		Match m= new Match();
		this.addOr(m);
		builder.accept(m);
		return this;
	}



	public List<ConceptRef> getEntityInSet() {
		return entityInSet;
	}




	@JsonSetter
	public Match setEntityInSet(List<ConceptRef> entityInSet) {
		this.entityInSet = entityInSet;
		return this;
	}

	public String getEntityVar() {
		return entityVar;
	}

	public Match setEntityVar(String entityVar) {
		this.entityVar = entityVar;
		return this;
	}

	@Override
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


	public Match addAnd(Match match){
		if (this.and ==null)
			this.and = new ArrayList<>();
		this.and.add(match);
		return this;
	}

	@Override
	public Match setDescription(String context) {
		super.setDescription(context);
		return this;
	}



	public Match(String iri) {
		super(iri);
	}
	public Match() {
	}



	@JsonIgnore
	public Match and(Consumer<Match> builder){
		Match match= new Match();
		this.addAnd(match);
		builder.accept(match);
		return this;
	}





	public List<PropertyValue> getProperty() {
		return property;
	}

	@JsonSetter
	public Match setProperty(List<PropertyValue> property) {
		this.property = property;
		return this;
	}

	public Match addProperty(PropertyValue pv){
		if (this.property ==null)
			this.property = new ArrayList<>();
		this.property.add(pv);
		return this;
	}


	@JsonIgnore
	public Match property(Consumer<PropertyValue> builder) {
		PropertyValue pv= new PropertyValue();
		this.addProperty(pv);
		builder.accept(pv);
		return this;
	}




	@JsonIgnore
	public String getasJson() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	}



}
