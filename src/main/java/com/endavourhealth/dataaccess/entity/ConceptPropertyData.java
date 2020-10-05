package com.endavourhealth.dataaccess.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptPropertyData {
	@Id()
	private String concept;
	private String group;
	private String property;
	private String data;
	@Column(name = "mincardinality")
	private String minCardinality;
	@Column(name = "maxcardinality")
	private String maxCardinality;
	private String operator;

	public ConceptPropertyData() {
		super();
	}

	public ConceptPropertyData(String concept, String group, String property, String data, String minCardinality,
			String maxCardinality, String operator) {
		super();
		this.concept = concept;
		this.group = group;
		this.property = property;
		this.data = data;
		this.minCardinality = minCardinality;
		this.maxCardinality = maxCardinality;
		this.operator = operator;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMinCardinality() {
		return minCardinality;
	}

	public void setMinCardinality(String minCardinality) {
		this.minCardinality = minCardinality;
	}

	public String getMaxCardinality() {
		return maxCardinality;
	}

	public void setMaxCardinality(String maxCardinality) {
		this.maxCardinality = maxCardinality;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
