package org.endeavourhealth.imapi.model.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class SubSelect {
	private TTIriRef graph;
	private String alias;
	private String property;
	private String label;
	private Match filter;
	private List<SubSelect> select;

	public String getProperty() {
		return property;
	}

	public SubSelect setProperty(String property) {
		this.property = property;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public SubSelect setLabel(String label) {
		this.label = label;
		return this;
	}

	public Match getFilter() {
		return filter;
	}

	public SubSelect setFilter(Match filter) {
		this.filter = filter;
		return this;
	}



	public String getAlias() {
		return alias;
	}

	public SubSelect setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	public List<SubSelect> getSelect() {
		return select;
	}

	public SubSelect setSelect(List<SubSelect> select) {
		this.select = select;
		return this;
	}

	public SubSelect addSelect(SubSelect select){
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(select);
		return this;
	}

	public SubSelect addSelect(String property) {
		SubSelect select= new SubSelect();
		select.setProperty(property);
		return addSelect(select);
	}

	public TTIriRef getGraph() {
		return graph;
	}

	public SubSelect setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}
}
