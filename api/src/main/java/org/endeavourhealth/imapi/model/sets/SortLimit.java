package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class SortLimit {
	TTIriRef orderBy;
	Integer count;
	Order direction;
	private List<Match> must;

	public List<Match> getTest() {
		return must;
	}

	public SortLimit setTest(List<Match> test) {
		this.must = test;
		return this;
	}

	public SortLimit addMust(Match test){
		if (this.must==null)
			this.must= new ArrayList<>();
		this.must.add(test);
		return this;
	}

	public TTIriRef getOrderBy() {
		return orderBy;
	}

	public SortLimit setOrderBy(TTIriRef orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public SortLimit setCount(Integer count) {
		this.count = count;
		return this;
	}

	public Order getDirection() {
		return direction;
	}

	public SortLimit setDirection(Order direction) {
		this.direction = direction;
		return this;
	}
}
