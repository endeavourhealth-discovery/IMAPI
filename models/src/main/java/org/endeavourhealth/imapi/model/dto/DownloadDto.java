package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.PropertyValue;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;

import java.io.Serializable;
import java.util.List;

public class DownloadDto implements Serializable {

	private List<EntityReferenceNode> children;
	private List<EntityReferenceNode> parents;
	private List<RecordStructureDto> semanticProperties;
	private ExportValueSet members;
	private List<PropertyValue> dataModelProperties;

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

	public List<RecordStructureDto> getSemanticProperties() {
		return semanticProperties;
	}

	public void setSemanticProperties(List<RecordStructureDto> semanticProperties) {
		this.semanticProperties = semanticProperties;
	}

	public ExportValueSet getMembers() {
		return members;
	}

	public void setMembers(ExportValueSet members) {
		this.members = members;
	}

	public List<PropertyValue> getDataModelProperties() {
		return dataModelProperties;
	}

	public void setDataModelProperties(List<PropertyValue> dataModelProperties) {
		this.dataModelProperties = dataModelProperties;
	}

}
