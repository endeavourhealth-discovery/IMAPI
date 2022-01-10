package org.endeavourhealth.imapi.query;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"not","entityVar","entity","property","function","valueVar","valueEntity","filter"})
public class Where {
	private TTIriRef entity;
	private String entityVar;
	private TTIriRef property;
	private TTIriRef valueEntity;
	private String propertyVar;
	private String valueVar;
	private List<Filter> filter;

	public TTIriRef getEntity() {
		return entity;
	}

	public Where setEntity(TTIriRef entity) {
		this.entity = entity;
		return this;
	}



	public TTIriRef getProperty() {
		return property;
	}




	@JsonProperty("property")
	public Where setProperty(TTIriRef property) {
		this.property =property;
		return this;
	}


	public String getEntityVar() {
		return entityVar;
	}

	public Where setEntityVar(String entityVar) {
		this.entityVar = entityVar;
		return this;
	}

	public String getPropertyVar() {
		return propertyVar;
	}

	public Where setPropertyVar(String propertyVar) {
		this.propertyVar = propertyVar;
		return this;
	}

	public TTIriRef getValueEntity() {
		return valueEntity;
	}

	public Where setValueEntity(TTIriRef valueEntity) {
		this.valueEntity = valueEntity;
		return this;
	}

	public List<Filter> getFilter() {
		return filter;
	}



	public Where addFilter(Filter fr){
		if (this.filter==null)
			this.filter= new ArrayList<>();
		this.filter.add(fr);
		return this;
	}


	public Where setFilter(List<Filter> filter) {
		this.filter = filter;
		return this;
	}

	public String getValueVar() {
		return valueVar;
	}

	public Where setValueVar(String valueVar) {
		this.valueVar = valueVar;
		return this;
	}
}










