package org.endeavourhealth.imapi.model.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphDto {

	String name;
	String iri;
	String propertyType;
	String valueTypeIri;
	String valueTypeName;
	String inheritedFromIri;
	String inheritedFromName;
	String min;
	String max;
	List<GraphDto> children;
	
	public GraphDto() {
		this.children = new ArrayList<GraphDto>();
	}
	
	public String getMin() {
		return min;
	}

	public GraphDto setMin(String min) {
		this.min = min;
		return this;
	}
	
	public String getMax() {
		return min;
	}

	public GraphDto setMax(String max) {
		this.max = max;
		return this;
	}

	public String getValueTypeIri() {
		return valueTypeIri;
	}

	public GraphDto setValueTypeIri(String valueTypeIri) {
		this.valueTypeIri = valueTypeIri;
		return this;
	}

	public String getValueTypeName() {
		return valueTypeName;
	}

	public GraphDto setValueTypeName(String valueTypeName) {
		this.valueTypeName = valueTypeName;
		return this;
	}

	public String getName() {
		return name;
	}

	public GraphDto setName(String name) {
		this.name = name;
		return this;
	}

	public String getIri() {
		return iri;
	}

	public GraphDto setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public GraphDto setPropertyType(String propertyType) {
		this.propertyType = propertyType;
		return this;
	}

	public String getInheritedFromIri() {
		return inheritedFromIri;
	}

	public GraphDto setInheritedFromIri(String inheritedFromIri) {
		this.inheritedFromIri = inheritedFromIri;
		return this;
	}

	public String getInheritedFromName() {
		return inheritedFromName;
	}

	public GraphDto setInheritedFromName(String inheritedFromName) {
		this.inheritedFromName = inheritedFromName;
		return this;
	}

	public List<GraphDto> getChildren() {
		return children;
	}

	public GraphDto setChildren(List<GraphDto> children) {
		this.children = children;
		return this;
	}
}
