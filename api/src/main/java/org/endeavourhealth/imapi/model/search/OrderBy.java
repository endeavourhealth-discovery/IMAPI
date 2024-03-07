package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.imq.Order;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OrderBy {
	private String field;
	private Order direction;
	private List<TTIriRef> iriValue;
	private List<OrderBy> and;
	private List<String> textValue;
	private boolean not;
	private boolean startsWithTerm;

	public List<String> getTextValue() {
		return textValue;
	}

	public OrderBy setTextValue(List<String> textValue) {
		this.textValue = textValue;
		return this;
	}
	public OrderBy addTextValue(String text){
		if (this.textValue==null)
			this.textValue= new ArrayList<>();
		this.textValue.add(text);
		return this;
	}

	public boolean isStartsWithTerm() {
		return startsWithTerm;
	}

	public OrderBy setStartsWithTerm(boolean startsWithTerm) {
		this.startsWithTerm = startsWithTerm;
		return this;
	}

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

	public boolean isNot() {
		return not;
	}

	public OrderBy setNot(boolean not) {
		this.not = not;
		return this;
	}

	public List<OrderBy> getAnd() {
		return and;
	}

	public OrderBy setAnd(List<OrderBy> and) {
		this.and = and;
		return this;
	}

	public OrderBy addAnd(OrderBy and) {
		if (this.and==null) {
			this.and= new ArrayList<>();
		}
		this.and.add(and);
		return this;
	}

	public OrderBy and(Consumer<OrderBy> builder){
		OrderBy order= new OrderBy();
		addAnd(order);
		builder.accept(order);
		return this;
	}

	public List<TTIriRef> getIriValue() {
		return iriValue;
	}
	public OrderBy setIriValue(List<TTIriRef> iriValue) {
		this.iriValue = iriValue;
		return this;
	}
	public OrderBy addIriValue(TTIriRef iriValue) {
		if (this.iriValue==null) {
			this.iriValue= new ArrayList<>();
		}
		this.iriValue.add(iriValue);
		return this;
	}
}
