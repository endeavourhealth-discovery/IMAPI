package com.endavourhealth.concept.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.validation.constraints.NotNull;

public class ConceptTreeNode {
	
	private Concept concept;
	private Set<ConceptTreeNode> parents;
	private ConceptTreeNode child;
	private Set<Concept> flatTree;
	
	/**
	 * Use when the concept is a leaf ie no child
	 * @param concept
	 */
	public ConceptTreeNode(Concept concept) {
		init(concept);

		flatTree = new HashSet<Concept>();
		flatTree.add(this.getConcept());
	}	
	
	public ConceptTreeNode(Concept concept, @NotNull ConceptTreeNode child) {
		init(concept);
		this.flatTree = child.getFlatTree();	
		this.child = child;		
		
		child.addParent(this);
	}
	
	public Set<ConceptTreeNode> getParents() {
		return Collections.unmodifiableSet(parents);
	}

	public boolean addParent(ConceptTreeNode parent) {
		// register parent
		flatTree.add(parent.getConcept());	

		return parents.add(parent);
	}

	public ConceptTreeNode getChild() {
		return child;
	}

	public Concept getConcept() {
		return concept;
	}
	
	public boolean contains(Concept concept) {
		return flatTree.contains(concept);
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
			
			// do they have the same number of parents
			equals = this.getParents().size() == other.getParents().size();
			
			if(equals) {
				
				Iterator<ConceptTreeNode> parentsItr = this.getParents().iterator();
				while(parentsItr.hasNext() && equals) {
					
					// does each parent match using deepEquals
					equals = contains(parentsItr.next(), other.getParents());
				}
			}
		}
		
		return equals;
	}	
	
	private void init(Concept concept) {
		this.concept = concept;		
		this.parents = new HashSet<ConceptTreeNode>();			
	}

	private boolean contains(ConceptTreeNode source, Set<ConceptTreeNode> target) {
		boolean match = false;
		
		Iterator<ConceptTreeNode> targetItr = target.iterator();
		
		while(targetItr.hasNext() && match == false) {
			match = source.deepEquals(targetItr.next());
		}
		
		return match;
	}

	private Set<Concept> getFlatTree() {
		return flatTree;
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
}
