package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;

import java.io.Serializable;
import java.util.List;

public class DownloadDto implements Serializable {

	private TTEntity summary;
	private List<EntityReferenceNode> hasSubTypes;
	private List<EntityReferenceNode> isA;
	private List<SemanticProperty> semanticProperties;
	private ExportValueSet members;
	private List<DataModelProperty> dataModelProperties;

	public List<EntityReferenceNode> getHasSubTypes() {
		return hasSubTypes;
	}

	public void setHasSubTypes(List<EntityReferenceNode> hasSubTypes) {
		this.hasSubTypes = hasSubTypes;
	}

	public List<EntityReferenceNode> getIsA() {
		return isA;
	}

	public void setIsA(List<EntityReferenceNode> isA) {
		this.isA = isA;
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

	public TTEntity getSummary() {
		return summary;
	}

	public void setSummary(TTEntity summary) {
		this.summary = summary;
	}
}
