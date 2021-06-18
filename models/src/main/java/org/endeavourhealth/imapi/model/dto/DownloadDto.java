package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;

import java.io.Serializable;
import java.util.List;

public class DownloadDto implements Serializable {

	private List<EntityReferenceNode> children;
	private List<EntityReferenceNode> parents;
	private List<PropertyValue> properties;
	private ExportValueSet members;
	private List<PropertyValue> roles;

	public List<EntityReferenceNode> getChildren() {
		return children;
	}

	public void setChildren(List<EntityReferenceNode> children) {
		this.children = children;
	}

	public List<EntityReferenceNode> getParents() {
		return parents;
	}

	public void setParents(List<EntityReferenceNode> parents) {
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
