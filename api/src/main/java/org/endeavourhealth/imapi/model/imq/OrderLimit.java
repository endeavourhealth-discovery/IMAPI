package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;


@JsonPropertyOrder({"iri","count","direction"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class OrderLimit extends TTAlias {
	private String direction;
	private int count=1;


	public OrderLimit setIri(String iri){
		super.setIri(iri);
		return this;
	}

	public OrderLimit setAlias(String alias){
		super.setAlias(alias);
		return this;
	}

	public OrderLimit setName(String name){
		super.setName(name);
		return this;
	}

	public String getDirection() {
		return direction;
	}

	public OrderLimit setDirection(String direction) {
		this.direction = direction;
		return this;
	}
}
