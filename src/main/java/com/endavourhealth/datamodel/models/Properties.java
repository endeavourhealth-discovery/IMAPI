package com.endavourhealth.datamodel.models;

import java.util.ArrayList;
import java.util.List;

public class Properties {
	private List<Property> coreProperties = new ArrayList<Property>();
	private List<Property> inheritedProperties = new ArrayList<Property>();
	private List<Property> extendedProperties = new ArrayList<Property>();

	public Properties() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Properties(List<Property> coreProperties, List<Property> inheritedProperties,
			List<Property> extendedProperties) {
		super();
		this.coreProperties = coreProperties;
		this.inheritedProperties = inheritedProperties;
		this.extendedProperties = extendedProperties;
	}

	public List<Property> getCoreProperties() {
		return coreProperties;
	}

	public void setCoreProperties(List<Property> coreProperties) {
		this.coreProperties = coreProperties;
	}

	public List<Property> getInheritedProperties() {
		return inheritedProperties;
	}

	public void setInheritedProperties(List<Property> inheritedProperties) {
		this.inheritedProperties = inheritedProperties;
	}

	public List<Property> getExtendedProperties() {
		return extendedProperties;
	}

	public void setExtendedProperties(List<Property> extendedProperties) {
		this.extendedProperties = extendedProperties;
	}

}
