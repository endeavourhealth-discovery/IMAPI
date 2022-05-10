package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"name","distinct","entityType","entityId","entityIn","filter","sum","average","max","distinct","property"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Select {



	private String name;
	private boolean sum;
	private boolean average;
	private boolean max;
	private boolean count;
	private List<PropertyObject> property;
	private Filter filter;
	private boolean distinct;
	private ConceptRef entityType;
	private ConceptRef entityId;
	private TTIriRef entityIn;
	private List<String> groupBy;

	public List<String> getGroupBy() {
		return groupBy;
	}

	public Select setGroupBy(List<String> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public Select addGroupBy(String group){
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

	public List<PropertyObject> getProperty() {
		return property;
	}

	public Select setProperty(List<PropertyObject> property) {
		this.property = property;
		return this;
	}

	public Select addProperty(PropertyObject property){
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



	public Filter getFilter() {
		return filter;
	}

	public Select setFilter(Filter filter) {
		this.filter = filter;
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
