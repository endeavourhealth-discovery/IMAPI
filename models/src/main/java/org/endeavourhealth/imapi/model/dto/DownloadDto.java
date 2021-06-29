package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;

import java.io.Serializable;
import java.util.List;

public class DownloadDto implements Serializable {

	private List<EntityReferenceNode> children;
	private List<EntityReferenceNode> parents;
	private List<SemanticProperty> semanticProperties;
	private ExportValueSet members;
	private List<DataModelProperty> dataModelProperties;

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

	public List<SemanticProperty> getSemanticProperties() {
		return semanticProperties;
	}

	public void setSemanticProperties(List<SemanticProperty> semanticProperties) {
		this.semanticProperties = semanticProperties;
	}

	public ExportValueSet getMembers() {
		return members;
	}

	public void setMembers(ExportValueSet members) {
		this.members = members;
	}

	public List<DataModelProperty> getDataModelProperties() {
		return dataModelProperties;
	}

	public void setDataModelProperties(List<DataModelProperty> dataModelProperties) {
		this.dataModelProperties = dataModelProperties;
	}

}
