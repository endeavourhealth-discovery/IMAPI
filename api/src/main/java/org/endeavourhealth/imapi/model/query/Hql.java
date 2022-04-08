package org.endeavourhealth.imapi.model.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder ({"function","select","filter"})
public class Hql {

	private Function function;
	private List<SubSelect> select;
	private Match where;

	public Match getWhere() {
		return where;
	}

	public Hql setWhere(Match where) {
		this.where = where;
		return this;
	}


	public List<SubSelect> getSelect() {
		return select;
	}

	public Hql setSelect(List<SubSelect> select) {
		this.select = select;
		return this;
	}

	public Hql addSelect(SubSelect select) {
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(select);
		return this;
	}
	public Hql addSelect(String property) {
		SubSelect select= new SubSelect();
		select.setProperty(property);
		return addSelect(select);
	}


	public Function getFunction() {
		return function;
	}

	public Hql setFunction(Function function) {
		this.function = function;
		return this;
	}

}
