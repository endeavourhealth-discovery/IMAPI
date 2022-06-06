package org.endeavourhealth.imapi.model.sets;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"orderBy","direction","count","test"})
public class OrderLimit {
	Alias orderBy;
	Integer count;
	Order direction;
	private List<Match> test;

	public OrderLimit test(Consumer<Match> builder){
		Match m= new Match();
		this.addTest(m);
		builder.accept(m);
		return this;
	}

	public OrderLimit orderBy(Consumer<Alias> builder){
		this.orderBy= new Alias();
		builder.accept(this.orderBy);
		return this;
	}

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

	public Alias getOrderBy() {
		return orderBy;
	}



	public OrderLimit setOrderBy(Alias orderBy) {
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
