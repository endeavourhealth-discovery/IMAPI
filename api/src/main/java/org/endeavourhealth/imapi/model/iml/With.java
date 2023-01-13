package org.endeavourhealth.imapi.model.iml;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class With extends Where {
	private List<OrderBy> orderBy;
	private Integer limit;
	private String direction;


	public String getDirection() {
		return direction;
	}

	public With setDirection(String direction) {
		this.direction = direction;
		return this;
	}

	public With addOrderBy(OrderBy orderBy){
		if (this.orderBy==null)
			this.orderBy= new ArrayList<>();
		this.orderBy.add(orderBy);
		return this;
	}

	public With orderBy(Consumer<OrderBy> builder){
		OrderBy orderBy= new OrderBy();
		addOrderBy(orderBy);
		builder.accept(orderBy);
		return this;
	}



	public Integer getLimit() {
		return limit;
	}

	public With setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}


}
