package org.endeavourhealth.imapi.model.hql;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Sort {
	TTIriRef orderBy;
	Integer count;
	Order direction;

	public TTIriRef getOrderBy() {
		return orderBy;
	}

	public Sort setOrderBy(TTIriRef orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public Sort setCount(Integer count) {
		this.count = count;
		return this;
	}

	public Order getDirection() {
		return direction;
	}

	public Sort setDirection(Order direction) {
		this.direction = direction;
		return this;
	}
}
