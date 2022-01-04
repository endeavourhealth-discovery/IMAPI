package org.endeavourhealth.imapi.query;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"not","entity","property","function","valueVar","filter"})
public class Where {
	private List<IriVar> entity;
	private List<TTIriRef> property;
	private String valueVar;
	private List<Filter> filter;

	public List<IriVar> getEntity() {
		return entity;
	}

	public Where setEntity(List<IriVar> entity) {
		this.entity = entity;
		return this;
	}

	public Where addEntity(IriVar entity){
		if (this.entity==null)
			this.entity= new ArrayList<>();
		this.entity.add(entity);
		return this;
	}
	public Where addEntity(TTIriRef iri, String var){
		if (this.entity==null)
			this.entity= new ArrayList<>();
		this.entity.add(new IriVar(iri).setVar(var));
		return this;
	}

	public Where addEntityVar(String var){
		return addEntity(new IriVar().setVar(var));
	}



	public List<TTIriRef> getProperty() {
		return property;
	}


	public Where setProperty(String property) {
		this.property = new ArrayList<>();
		this.property.add(TTIriRef.iri(property));
		return this;
	}

	public Where setProperty(TTIriRef property) {
		this.property = new ArrayList<>();
		this.property.add(property);
		return this;
	}

	public Where setProperty(List<TTIriRef> property) {
		this.property = property;
		return this;
	}

	public Where addProperty(TTIriRef property) {
		if (this.property==null)
			this.property= new ArrayList<>();
		this.property.add(property);
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










