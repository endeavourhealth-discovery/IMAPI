package com.endavourhealth.dataaccess.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptPropertyData {
	@Id()
	private Integer dbid;
	private Integer concept;
	private Integer group;
	private Integer property;
	private String data;
	@Column(name = "mincardinality")
	private Integer minCardinality;
	@Column(name = "maxcardinality")
	private Integer maxCardinality;
	private String operator;

	public ConceptPropertyData() {
		super();
	}

	public ConceptPropertyData(Integer dbid, Integer concept, Integer group, Integer property, String data, Integer minCardinality,
			Integer maxCardinality, String operator) {
		super();
		this.dbid = dbid;
		this.concept = concept;
		this.group = group;
		this.property = property;
		this.data = data;
		this.minCardinality = minCardinality;
		this.maxCardinality = maxCardinality;
		this.operator = operator;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public Integer getConcept() {
		return concept;
	}

	public void setConcept(Integer concept) {
		this.concept = concept;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public Integer getProperty() {
		return property;
	}

	public void setProperty(Integer property) {
		this.property = property;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getMinCardinality() {
		return minCardinality;
	}

	public void setMinCardinality(Integer minCardinality) {
		this.minCardinality = minCardinality;
	}

	public Integer getMaxCardinality() {
		return maxCardinality;
	}

	public void setMaxCardinality(Integer maxCardinality) {
		this.maxCardinality = maxCardinality;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
