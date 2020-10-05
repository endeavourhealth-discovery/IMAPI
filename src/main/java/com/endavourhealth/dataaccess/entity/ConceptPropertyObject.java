package com.endavourhealth.dataaccess.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptPropertyObject {

	@Id()
	private int dbid;
	private int concept;
	private int group;
	private int property;
	private int object;
	@Column(name = "mincardinality")
	private int minCardinality;
	@Column(name = "maxcardinality")
	private int maxCardinality;
	private String operator;

	public ConceptPropertyObject() {
		super();
	}

	public ConceptPropertyObject(int dbid, int concept, int group, int property, int object, int minCardinality,
			int maxCardinality, String operator) {
		super();
		this.dbid = dbid;
		this.concept = concept;
		this.group = group;
		this.property = property;
		this.object = object;
		this.minCardinality = minCardinality;
		this.maxCardinality = maxCardinality;
		this.operator = operator;
	}

	public int getDbid() {
		return dbid;
	}

	public void setDbid(int dbid) {
		this.dbid = dbid;
	}

	public int getConcept() {
		return concept;
	}

	public void setConcept(int concept) {
		this.concept = concept;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getProperty() {
		return property;
	}

	public void setProperty(int property) {
		this.property = property;
	}

	public int getObject() {
		return object;
	}

	public void setObject(int object) {
		this.object = object;
	}

	public int getMinCardinality() {
		return minCardinality;
	}

	public void setMinCardinality(int minCardinality) {
		this.minCardinality = minCardinality;
	}

	public int getMaxCardinality() {
		return maxCardinality;
	}

	public void setMaxCardinality(int maxCardinality) {
		this.maxCardinality = maxCardinality;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
