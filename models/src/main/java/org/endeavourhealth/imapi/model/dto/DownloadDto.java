package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.DataModelProperty;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;

import java.io.Serializable;
import java.util.List;

public class DownloadDto implements Serializable {

	private TTEntity summary;
	private List<EntityReferenceNode> hasSubTypes;
	private TTNode inferred;
	private TTNode axioms;
	private ExportValueSet members;
	private List<DataModelProperty> dataModelProperties;
	private List<TermCode> terms;
	private TTValue isChildOf;
	private TTValue hasChildren;

	public List<EntityReferenceNode> getHasSubTypes() {
		return hasSubTypes;
	}

	public void setHasSubTypes(List<EntityReferenceNode> hasSubTypes) {
		this.hasSubTypes = hasSubTypes;
	}

    public TTNode getInferred() {
        return inferred;
    }

    public DownloadDto setInferred(TTNode inferred) {
        this.inferred = inferred;
        return this;
    }

    public TTNode getAxioms() {
        return axioms;
    }

    public DownloadDto setAxioms(TTNode axioms) {
        this.axioms = axioms;
        return this;
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

	public List<TermCode> getTerms() {
		return terms;
	}

	public void setTerms(List<TermCode> terms) {
		this.terms = terms;
	}

	public TTValue getIsChildOf() {
		return isChildOf;
	}

	public void setIsChildOf(TTValue isChildOf) {
		this.isChildOf = isChildOf;
	}

	public TTValue getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(TTValue hasChildren) {
		this.hasChildren = hasChildren;
	}
}
