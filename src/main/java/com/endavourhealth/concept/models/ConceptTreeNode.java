package com.endavourhealth.concept.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ConceptTreeNode {
	
	private Concept concept;
	private Set<ConceptTreeNode> parents;
	//private ConceptTreeNode child;
	//private Set<Concept> flatParents;
	private Set<ConceptTreeNode> children;
	
	/**
	 * Use when the concept is a leaf ie no child
	 * @param concept
	 */
	public ConceptTreeNode(Concept concept) {
		init(concept);

		//flatParents = new HashSet<Concept>();
		//flatParents.add(this.getConcept());
	}	
	
//	public ConceptTreeNode(Concept concept) {
//		init(concept);
//		this.flatParents = child.getFlatParents();	
//		this.child = child;		
//		
//		child.addParent(this);
//	}
	
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

	public Set<Concept> getChildConcepts() {
		Set<Concept> childConcepts = new HashSet<Concept>();
		
		for(ConceptTreeNode child : children) {
			childConcepts.add(child.getConcept());
		}
		
		return childConcepts;
	}


//	public ConceptTreeNode getChild() {
//		return child;
//	}

	public Concept getConcept() {
		return concept;
	}
	
	// hasParent
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
	
	public Set<Concept> getFlatParents() {
		Set<Concept> parents = new HashSet<Concept>();
		
		Iterator<ConceptTreeNode> parentItr = getParents().iterator();
		
		while(parentItr.hasNext()) {
			ConceptTreeNode parent = parentItr.next();
			
			parents.add(parent.getConcept());
			parents.addAll(parent.getFlatParents());
		}		
		
		return parents;
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
			
			
//			// do they have the same number of parents
//			equals = this.getParents().size() == other.getParents().size();
//			
//			if(equals) {
//				
//				Iterator<ConceptTreeNode> parentsItr = this.getParents().iterator();
//				while(parentsItr.hasNext() && equals) {
//					
//					// does each parent match using deepEquals
//					equals = contains(parentsItr.next(), other.getParents());
//				}
//			}
			
//			if(equals) {
//				
//				// do they have the same number of parents
//				equals = this.getChildren().size() == other.getChildren().size();
//				
//				if(equals) {
//				
//					Iterator<ConceptTreeNode> childrenItr = this.getChildren().iterator();
//					while(childrenItr.hasNext() && equals) {
//						
//						// does each child match using deepEquals
//						equals = contains(childrenItr.next(), other.getChildren());
//					}
//				}			
//			}
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
	
//	private boolean parentsAreEqual(ConceptTreeNode sourceParent, Set<ConceptTreeNode> targetParents) {
//		boolean match = false;
//		
//		Iterator<ConceptTreeNode> targetItr = targetParents.iterator();
//		
//		while(targetItr.hasNext() && match == false) {
//			match = sourceParent.equals(targetItr.next());
//		}
//		
//		return match;
//	}
	
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
	
//	Set<Concept> getFlatParents() {
//		return flatParents;
//	}
	
	private void init(Concept concept) {
		this.concept = concept;		
		this.parents = new HashSet<ConceptTreeNode>();	
		this.children = new HashSet<ConceptTreeNode>();
	}

//	private boolean contains(ConceptTreeNode source, Set<ConceptTreeNode> target) {
//		boolean match = false;
//		
//		Iterator<ConceptTreeNode> targetItr = target.iterator();
//		
//		while(targetItr.hasNext() && match == false) {
//			match = source.deepEquals(targetItr.next());
//		}
//		
//		return match;
//	}

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
		// register parent
		//flatParents.add(parent.getConcept());
		//flatParents.addAll(parent.getFlatParents());
		
		return parents.add(parent);
	}
	
	private boolean doAddChild(ConceptTreeNode child) {
		return children.add(child);
	}
}
