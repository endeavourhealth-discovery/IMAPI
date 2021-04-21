package org.endeavourhealth.dto;

import java.io.Serializable;
import java.util.List;

import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;

public class DownloadDto implements Serializable {

	private List<ConceptReferenceNode> children;
	private List<TTValue> parents;
	private List<PropertyValue> properties;
	private ExportValueSet members;
	private List<PropertyValue> roles;

	public List<ConceptReferenceNode> getChildren() {
		return children;
	}

	public void setChildren(List<ConceptReferenceNode> children) {
		this.children = children;
	}

	public List<TTValue> getParents() {
		return parents;
	}

	public void setParents(List<TTValue> parents) {
		this.parents = parents;
	}

	public List<PropertyValue> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyValue> properties) {
		this.properties = properties;
	}

	public ExportValueSet getMembers() {
		return members;
	}

	public void setMembers(ExportValueSet members) {
		this.members = members;
	}

	public List<PropertyValue> getRoles() {
		return roles;
	}

	public void setRoles(List<PropertyValue> roles) {
		this.roles = roles;
	}

}
