package com.endavourhealth.dataaccess.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ConceptPropertyObject {

	@Id()
	private Integer  dbid;
	@OneToOne()
	@JoinColumn(name="concept", referencedColumnName="dbid")
	private Concept  concept;
	private Integer  group;
	@OneToOne()
	@JoinColumn(name="property", referencedColumnName="dbid")
	private Concept  property;
	@OneToOne
	@JoinColumn(name="object", referencedColumnName="dbid")
	private Concept  object;
	@Column(name = "mincardinality")
	private Integer  minCardinality;
	@Column(name = "maxcardinality")
	private Integer  maxCardinality;
	private String operator;

	public ConceptPropertyObject() {
		super();
	}

	public ConceptPropertyObject(Integer dbid, Concept concept, Integer group, Concept property, Concept object, Integer minCardinality,
			Integer maxCardinality, String operator) {
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

	public Concept getObject() {
		return object;
	}

	public void setObject(Concept object) {
		this.object = object;
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
