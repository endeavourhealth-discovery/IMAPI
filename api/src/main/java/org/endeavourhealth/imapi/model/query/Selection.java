package org.endeavourhealth.imapi.model.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

public class Selection {
	private TTIriRef graph;
	private String alias;
	private TTIriRef property;
	private String label;
	private Match filter;
	private boolean inverseOf;
	private List<Selection> select;

	public boolean isInverseOf() {
		return inverseOf;
	}

	public Selection setInverseOf(boolean inverseOf) {
		this.inverseOf = inverseOf;
		return this;
	}

	public TTIriRef getProperty() {
		return property;
	}

	public Selection setProperty(TTIriRef property) {
		this.property = property;
		return this;
	}

	public Selection setProperty(String property) {
		this.property = TTIriRef.iri(property);
		return this;
	}

	public String getLabel() {
		return label;
	}

	public Selection setLabel(String label) {
		this.label = label;
		return this;
	}

	public Match getFilter() {
		return filter;
	}

	public Selection setFilter(Match filter) {
		this.filter = filter;
		return this;
	}



	public String getAlias() {
		return alias;
	}

	public Selection setAlias(String alias) {
		this.alias = alias;
		return this;
	}

	public List<Selection> getSelect() {
		return select;
	}

	public Selection setSelect(List<Selection> select) {
		this.select = select;
		return this;
	}

	public Selection addSelect(Selection select){
		if (this.select==null)
			this.select= new ArrayList<>();
		this.select.add(select);
		return this;
	}

	public Selection addSelect(String property) throws DataFormatException {
		Selection select= new Selection();
		if (property.equals("id")||property.equals("iri"))
			return addSelect(TTIriRef.iri("im:id"));
		try {
			select.setProperty(TTIriRef.iri(property));
		} catch (Exception e) {
			throw new DataFormatException("Invalid iri - " + property);
		}
		return addSelect(select);
	}

	public Selection addSelect(TTIriRef property) {
		Selection select= new Selection();
		select.setProperty(property);
		return addSelect(select);
	}

	public TTIriRef getGraph() {
		return graph;
	}

	public Selection setGraph(TTIriRef graph) {
		this.graph = graph;
		return this;
	}
}
