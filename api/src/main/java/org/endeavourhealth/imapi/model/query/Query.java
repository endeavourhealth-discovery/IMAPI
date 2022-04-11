package org.endeavourhealth.imapi.model.query;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder ({"function","select","filter"})
public class Query {

	private List<Selection> select;
	private Match where;

	public Match getWhere() {
		return where;
	}

	public Query setWhere(Match where) {
		this.where = where;
		return this;
	}


	public List<Selection> getSelect() {
		return select;
	}

	public Query setSelect(List<Selection> select) {
		this.select = select;
		return this;
	}

	public Query addSelect(Selection select) {
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(select);
		return this;
	}
	public Query addSelect(String property) {
		if (property.equals("id")||property.equals("iri"))
			property="im:id";
		Selection select= new Selection();
		select.setProperty(TTIriRef.iri(property));
		return addSelect(select);
	}



}
