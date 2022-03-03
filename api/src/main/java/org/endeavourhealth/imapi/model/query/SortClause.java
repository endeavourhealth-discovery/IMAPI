package org.endeavourhealth.imapi.model.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class SortClause {
	TTIriRef orderBy;
	Integer count;
	Order direction;

	public TTIriRef getOrderBy() {
		return orderBy;
	}

	public SortClause setOrderBy(TTIriRef orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public SortClause setCount(Integer count) {
		this.count = count;
		return this;
	}

	public Order getDirection() {
		return direction;
	}

	public SortClause setDirection(Order direction) {
		this.direction = direction;
		return this;
	}
}
