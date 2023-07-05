package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;


@JsonPropertyOrder({"nodeVariable","id","count","direction"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class OrderLimit extends PropertyRef {
	private Order direction;
	private int limit;
	private String description;

	public String getDescription() {
		return description;
	}

	public OrderLimit setDescription(String description) {
		this.description = description;
		return this;
	}

	public OrderLimit setId(String id){
		super.setIri(id);
		return this;
	}


	@JsonSetter
	public OrderLimit setIri(String iri){
		super.setIri(iri);
		return this;
	}



	public OrderLimit setVariable(String variable){
		super.setVariable(variable);
		return this;
	}



	public int getLimit() {
		return limit;
	}

	public OrderLimit setLimit(int limit) {
		this.limit = limit;
		return this;
	}




	public OrderLimit setName(String name){
		super.setName(name);
		return this;
	}

	public Order getDirection() {
		return direction;
	}

	public OrderLimit setDirection(Order direction) {
		this.direction = direction;
		return this;
	}
}
