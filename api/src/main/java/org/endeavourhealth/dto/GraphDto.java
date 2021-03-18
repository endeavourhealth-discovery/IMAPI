package org.endeavourhealth.dto;

import java.util.List;

public class GraphDto {

	String name;
	String iri;
	String propertyType;
	String inheritedFromIri;
	String inheritedFromName;
	List<GraphDto> children;
	
	public GraphDto() {}

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
