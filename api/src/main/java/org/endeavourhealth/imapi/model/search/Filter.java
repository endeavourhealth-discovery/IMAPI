package org.endeavourhealth.imapi.model.search;

import org.endeavourhealth.imapi.model.imq.Order;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Filter {
	private String field;
	private List<String> value;
	private List<TTIriRef> iriValue;
	private List<Filter> and;
	private boolean not;

	public boolean isNot() {
		return not;
	}

	public Filter setNot(boolean not) {
		this.not = not;
		return this;
	}

	public List<Filter> getAnd() {
		return and;
	}

	public Filter setAnd(List<Filter> and) {
		this.and = and;
		return this;
	}

	public Filter addAnd(Filter and) {
		if (this.and==null) {
			this.and= new ArrayList<>();
		}
		this.and.add(and);
		return this;
	}

	public Filter and(Consumer<Filter> builder){
		Filter order= new Filter();
		addAnd(order);
		builder.accept(order);
		return this;
	}

	public String getField() {
		return field;
	}

	public Filter setField(String field) {
		this.field = field;
		return this;
	}



	public List<String> getValue() {
		return value;
	}

	public Filter setValue(List<String> value) {
		this.value = value;
		return this;
	}
	public Filter addValue(String value) {
		if (this.value==null)
			this.value= new ArrayList<>();
		this.value .add(value);
		return this;
	}

	public List<TTIriRef> getIriValue() {
		return iriValue;
	}

	public Filter setIriValue(List<TTIriRef> iriValue) {
		this.iriValue = iriValue;
		return this;
	}

	public Filter addIriValue(TTIriRef iriValue) {
		if (this.iriValue==null) {
			this.iriValue= new ArrayList<>();
		}
		this.iriValue.add(iriValue);
		return this;
	}

}
