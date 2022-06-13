package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.filer.TTImportByType;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A property of a select statement with option for iri and alias and supports nested selects for object format
 */
@JsonPropertyOrder({"name","distinct","sum","average","max","entityType","entityId","entityIn","property","match",})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Select {


	private String name;
	private boolean sum;
	private boolean average;
	private boolean max;
	private boolean count;
	private List<PropertySelect> property;
	private Match match;
	private boolean distinct;
	private ConceptRef entityType;
	private ConceptRef entityId;
	private TTIriRef entityIn;
	private List<PropertySelect> groupBy;
	private OrderLimit orderLimit;

	/**
	 * Lambda approach to setting property selects
	 * @param builder the lambda expression
	 * @return this for chaining
	 */
	public Select property(Consumer<PropertySelect> builder){
		PropertySelect ps= new PropertySelect();
		addProperty(ps);
		builder.accept(ps);
		return this;
	}



	/**
	 * Lambda approach for setting match clause
	 * @param builder lambda expression
	 * @return this for chaining
	 */
	public Select match(Consumer<Match> builder){
		Match match= new Match();
		this.match= match;
		builder.accept(match);
		return this;
	}

	/**
	 * Lambda approach for setting order and limit
	 * @param builder lambda expression
	 * @return this for chaining
	 */
	public Select order(Consumer<OrderLimit> builder){
		OrderLimit ol= new OrderLimit();
		this.orderLimit= ol;
		builder.accept(ol);
		return this;
	}




	public OrderLimit getOrderLimit() {
		return orderLimit;
	}

	public Select setOrderLimit(OrderLimit orderLimit) {
		this.orderLimit = orderLimit;
		return this;
	}

	public List<PropertySelect> getGroupBy() {
		return groupBy;
	}

	public Select setGroupBy(List<PropertySelect> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public Select addGroupBy(PropertySelect group){
		if (this.groupBy==null)
			this.groupBy= new ArrayList<>();
		groupBy.add(group);
		return this;
	}

	public String getName() {
		return name;
	}

	public Select setName(String name) {
		this.name = name;
		return this;
	}

	public ConceptRef getEntityId() {
		return entityId;
	}

	@JsonSetter
	public Select setEntityId(ConceptRef entityId) {
		this.entityId = entityId;
		return this;
	}

	public TTIriRef getEntityIn() {
		return entityIn;
	}

	public Select setEntityIn(TTIriRef entityIn) {
		this.entityIn = entityIn;
		return this;
	}

	public ConceptRef getEntityType() {
		return entityType;
	}

	@JsonSetter
	public Select setEntityType(ConceptRef entityType) {
		this.entityType = entityType;
		return this;
	}

	public Select setEntityType(TTIriRef entityType) {
		this.entityType = new ConceptRef();
			this.entityType.setIri(entityType.getIri());
			if (entityType.getName()!=null)
				this.entityType.setName(entityType.getName());
		return this;
	}

	public List<PropertySelect> getProperty() {
		return property;
	}

	@JsonSetter
	public Select setProperty(List<PropertySelect> property) {
		this.property = property;
		return this;
	}

	public Select addProperties(List<TTIriRef> properties){
		for (TTIriRef property:properties){
			this.addProperty(new PropertySelect(property));
		}
		return this;
	}

	public Select addProperty(PropertySelect property){
		if (this.property==null)
			this.property= new ArrayList<>();
		this.property.add(property);
		return this;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public Select setDistinct(boolean distinct) {
		this.distinct = distinct;
		return this;
	}



	public Match getMatch() {
		return match;
	}

	public Select setMatch(Match match) {
		this.match = match;
		return this;
	}



	public boolean isCount() {
		return count;
	}

	public Select setCount(boolean count) {
		this.count = count;
		return this;
	}

	public boolean isSum() {
		return sum;
	}

	public Select setSum(boolean sum) {
		this.sum = sum;
		return this;
	}

	public boolean isAverage() {
		return average;
	}

	public Select setAverage(boolean average) {
		this.average = average;
		return this;
	}

	public boolean isMax() {
		return max;
	}

	public Select setMax(boolean max) {
		this.max = max;
		return this;
	}


}
