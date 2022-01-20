package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTUtil;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.InvalidClassException;

public class OrderLimit extends TTNode {

	public Integer getLimit() throws InvalidClassException {
		return (Integer) TTUtil.get(this,IM.LIMIT,Integer.class);
	}

	public OrderLimit setLimit(Integer count) {
		set(IM.LIMIT,TTLiteral.literal(count));
		return this;
	}

	public TTIriRef getField() throws InvalidClassException {
		return (TTIriRef) TTUtil.get(this,IM.SORT_FIELD,TTIriRef.class);
	}

	public OrderLimit setField(TTIriRef field) {
		set(IM.SORT_FIELD,field);
		return this;
	}


	public SortBy getSortBy() {
		if (get(IM.SORT_BY)==null)
			return null;
		else
			return SortBy.valueOf(get(IM.SORT_BY).asLiteral().getValue());

	}

	public OrderLimit setSortBy(SortBy sortBy) {
		set(IM.SORT_BY, TTLiteral.literal(sortBy.name()));
		return this;
	}

}
