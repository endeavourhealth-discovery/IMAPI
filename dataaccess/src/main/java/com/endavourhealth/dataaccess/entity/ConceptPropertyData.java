package com.endavourhealth.dataaccess.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ConceptPropertyData {
	@Id()
	private Integer dbid;
	@OneToOne()
	@JoinColumn(name="concept", referencedColumnName="dbid")
	private Concept concept;
	private Integer group;
	@OneToOne()
	@JoinColumn(name="property", referencedColumnName="dbid")
	private Concept property;
	private String data;
	@Column(name = "mincardinality")
	private Integer minCardinality;
	@Column(name = "maxcardinality")
	private Integer maxCardinality;
	private String operator;

	public ConceptPropertyData() {
		super();
	}

	public ConceptPropertyData(Integer dbid, Concept concept, Integer group, Concept property, String data, Integer minCardinality,
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

	public Concept getConcept() {
		return concept;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public Concept getProperty() {
		return property;
	}

	public void setProperty(Concept property) {
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
