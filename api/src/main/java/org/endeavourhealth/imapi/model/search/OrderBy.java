package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.imq.Order;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OrderBy {
	private String field;
	private Order direction;


	public String getField() {
		return field;
	}

	public OrderBy setField(String field) {
		this.field = field;
		return this;
	}

	public Order getDirection() {
		return direction;
	}

	public OrderBy setDirection(Order direction) {
		this.direction = direction;
		return this;
	}


}
