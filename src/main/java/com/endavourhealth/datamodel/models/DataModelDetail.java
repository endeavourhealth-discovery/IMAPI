package com.endavourhealth.datamodel.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.endavourhealth.concept.models.TreeNode;
import com.endavourhealth.dataaccess.entity.Concept;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DataModel
 */
@Validated

public class DataModelDetail {
	@JsonProperty("iri")
	private String iri = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("description")
	private String description = null;
	
	@JsonProperty("properties")
	@Valid
	private Properties properties = new Properties();

	@JsonProperty("parents")
	@Valid
	//private List<Ancestory> parents = new ArrayList<Ancestory>();
	private Set<TreeNode<DataModelDetail>> parents = new HashSet<TreeNode<DataModelDetail>>();
	
	@JsonProperty("children")
	@Valid
	private Set<TreeNode<DataModelDetail>> children = new HashSet<TreeNode<DataModelDetail>>();

	//private List<DataModelDetail> children = new ArrayList<DataModelDetail>();
	
	public DataModelDetail() {
		super();
	}

	public DataModelDetail(Concept concept, Properties properties) {
		this();
		this.setName(concept.getName());
		this.setIri(concept.getIri());
		this.setDescription(concept.getDescription());
		this.properties = properties;
	}

	@NotNull

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	@NotNull

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public boolean addParents(Set<TreeNode<DataModelDetail>> parents) {
		boolean modified = false;
		
		if(parents != null) {
		
			modified = this.parents.addAll(parents);
		}
		
		return modified;
	}
	
	public boolean addChildren(Set<TreeNode<DataModelDetail>> children) {
		boolean modified = false;
		
		if(children != null) {
			modified = this.children.addAll(children);
		}
		
		return modified;
	}
	
	public Set<TreeNode<DataModelDetail>> getParents() {
		return Collections.unmodifiableSet(parents);
	}

	public Set<TreeNode<DataModelDetail>> getChildren() {
		return Collections.unmodifiableSet(children);
	}
	
	public boolean isA(String iri) {
		boolean isA = false;
		
		if(iri != null && iri.length() > 0) {
			Iterator<TreeNode<DataModelDetail>> parentItr = parents.iterator();
			
			while(parentItr.hasNext() && isA == false) {
				isA = isA(parentItr.next(), iri);
			}
		}
		
		return isA;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iri == null) ? 0 : iri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataModelDetail other = (DataModelDetail) obj;
		if (iri == null) {
			if (other.iri != null)
				return false;
		} else if (!iri.equals(other.iri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataModel [iri=" + iri + "]";
	}
	
	private boolean isA(TreeNode<DataModelDetail> treeNode, String iri) {
		boolean isA = treeNode.getData().getIri().equals(iri);
		
		if(isA == false) {
		Iterator<TreeNode<DataModelDetail>> parentItr = treeNode.getParents().iterator();
		
			while(parentItr.hasNext() && isA == false) {
				isA = isA(parentItr.next(), iri);
			}
		}
		
		return isA;
	}	
}
