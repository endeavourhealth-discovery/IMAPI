package com.endavourhealth.services.concept.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class ConceptTreeNode {
	
	@JsonProperty("id")
	private String id;
	
	private Concept concept;
	
	@JsonBackReference
	private Set<ConceptTreeNode> parents;

	@JsonManagedReference
	private Set<ConceptTreeNode> children;
	
	/**
	 * Use when the concept is a leaf ie no child
	 * @param concept
	 */
	public ConceptTreeNode(Concept concept) {
		this.concept = concept;		
		this.id = concept.getIri();
		this.parents = new HashSet<ConceptTreeNode>();	
		this.children = new HashSet<ConceptTreeNode>();
	}	
	
	public String getId() {
		return this.id;
	}
	
	public boolean addChild(ConceptTreeNode child) {
		child.doAddParent(this);
		
		return doAddChild(child);
	}
	
	public boolean addParent(ConceptTreeNode parent) {
		parent.doAddChild(parent);
		
		return doAddParent(parent);
	}
		
	public Set<ConceptTreeNode> getParents() {
		return Collections.unmodifiableSet(parents);
	}
	
	public Set<ConceptTreeNode> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	public Concept getConcept() {
		return concept;
	}

	public boolean hasParent(Concept concept) {
		boolean contains = false;
		
		Iterator<ConceptTreeNode> parentItr = getParents().iterator();
		
		while(parentItr.hasNext() && contains == false) {
			ConceptTreeNode parent = parentItr.next();
			
			contains = parent.getConcept().equals(concept);
			if(contains == false) {
				contains = parent.hasParent(concept);
			}
		}
		
		return contains;
	}
	
	public boolean hasParent(ConceptTreeNode parent) {
		boolean hasParent = false;
		
		Iterator<ConceptTreeNode> parentItr = getParents().iterator();
		
		while(parentItr.hasNext() && hasParent == false) {
			hasParent = parentItr.next().deepEquals(parent);
		}
		
		return hasParent;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((concept == null) ? 0 : concept.hashCode());
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
		ConceptTreeNode other = (ConceptTreeNode) obj;
		if (concept == null) {
			if (other.concept != null)
				return false;
		} else if (!concept.equals(other.concept))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		String string = concept.toString();
		String parentsString = toStringParents();
		
		if(parentsString != null) {
			string = string + " > " + parentsString;
		}
		
		return string;
	}		
	
	// this method is not safe if there are circular dependencies in 
	// either this or the other tree node - most likely a stack 
	// overflow will result
	public boolean deepEquals(ConceptTreeNode other) {
		boolean equals = false;
		
		// do concepts match
		equals = this.equals(other);
		
		if(equals) {
			
			equals = parentsAreEqual(other.getParents());
			
			if(equals) {
				equals = childrenAreEqual(other.getChildren());
			}
		}
		
		return equals;
	}
	
	private boolean parentsAreEqual(Set<ConceptTreeNode> targetParents) {
		Set<ConceptTreeNode> sourceParents = getParents();
			
		// do they have the same number of parents
		boolean equals = sourceParents.size() == targetParents.size();
		
		if(equals) {
			
			Iterator<ConceptTreeNode> parentsItr = sourceParents.iterator();
			while(parentsItr.hasNext() && equals) {
				equals = contains(parentsItr.next(), targetParents);
			}
		}
		
		return equals;
	}
	
	private boolean childrenAreEqual(Set<ConceptTreeNode> targetChildren) {
		Set<ConceptTreeNode> sourceChildren = getChildren();
			
		// do they have the same number of parents
		boolean equals = sourceChildren.size() == targetChildren.size();
		
		if(equals) {
			
			Iterator<ConceptTreeNode> childrenItr = sourceChildren.iterator();
			while(childrenItr.hasNext() && equals) {
				equals = contains(childrenItr.next(), targetChildren);
			}
		}
		
		return equals;
	}
	
	private boolean contains(ConceptTreeNode source, Set<ConceptTreeNode> target) {
		boolean match = false;
		
		Iterator<ConceptTreeNode> targetItr = target.iterator();
		
		while(targetItr.hasNext() && match == false) {
			match = source.equals(targetItr.next());
		}
		
		return match;
	}	


	private String toStringParents() {
		String parentsString = null;
		
		if(parents.isEmpty() == false) {
			StringBuilder stringBuilder = new StringBuilder();
			
			for(ConceptTreeNode parent : parents) {
				stringBuilder.append(parent.toString());
			}
			
			parentsString = stringBuilder.toString();
		}
		
		return parentsString;
	}
	
	private boolean doAddParent(ConceptTreeNode parent) {
		return parents.add(parent);
	}
	
	private boolean doAddChild(ConceptTreeNode child) {
		return children.add(child);
	}
}
