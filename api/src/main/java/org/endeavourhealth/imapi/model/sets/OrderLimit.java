package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"orderBy","direction","count","test"})
public class OrderLimit {
	TTIriRef orderBy;
	Integer count;
	Order direction;
	private List<Match> test;

	public List<Match> getTest() {
		return test;
	}

	public OrderLimit setTest(List<Match> test) {
		this.test = test;
		return this;
	}

	public OrderLimit addTest(Match test){
		if (this.test ==null)
			this.test = new ArrayList<>();
		this.test.add(test);
		return this;
	}

	public TTIriRef getOrderBy() {
		return orderBy;
	}

	public OrderLimit setOrderBy(TTIriRef orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public OrderLimit setCount(Integer count) {
		this.count = count;
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
