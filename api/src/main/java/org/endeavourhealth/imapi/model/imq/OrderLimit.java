package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"nodeVariable","id","count","direction"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class OrderLimit extends Property {
	private Order direction;
	private int limit;

	public OrderLimit setId(String id){
		super.setId(id);
		return this;
	}

	public int getLimit() {
		return limit;
	}

	public OrderLimit setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public OrderLimit setProperty(String iri){
		super.setId(iri);
		return this;
	}

	public OrderLimit setNode(String node){
		super.setNode(node);
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
