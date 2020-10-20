package com.endavourhealth.concept.models;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TreeNode<D> {
	
	private D data;
	private Set<TreeNode<D>> parents;
	private Set<TreeNode<D>> children;
	
	public TreeNode(D data) {
		this.data = data;
		this.children = new HashSet<TreeNode<D>>();
		this.parents = new HashSet<TreeNode<D>>();
	}

	public D getData() {
		return data;
	}
	
	public Set<TreeNode<D>> getParents() {
		return Collections.unmodifiableSet(parents);
	}
	
	public Set<TreeNode<D>> getChildren() {
		return Collections.unmodifiableSet(children);
	}
		
	public boolean addChild(TreeNode<D> child) {
		child.doAddParent(this);
		
		return doAddChild(child);
	}
	
	public boolean addParent(TreeNode<D> parent) {
		parent.doAddChild(this);
		
		return doAddParent(parent);
	}
	
	private boolean doAddParent(TreeNode<D> parent) {
		return parents.add(parent);
	}
	
	private boolean doAddChild(TreeNode<D> child) {
		return children.add(child);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeNode other = (TreeNode) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}
	
	public boolean deepEquals(TreeNode<D> other) {
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
	
	private boolean parentsAreEqual(Set<TreeNode<D>> targetParents) {
		Set<TreeNode<D>> sourceParents = getParents();
			
		// do they have the same number of parents
		boolean equals = sourceParents.size() == targetParents.size();
		
		if(equals) {
			
			Iterator<TreeNode<D>> parentsItr = sourceParents.iterator();
			while(parentsItr.hasNext() && equals) {
				equals = contains(parentsItr.next(), targetParents);
			}
		}
		
		return equals;
	}
		
	private boolean childrenAreEqual(Set<TreeNode<D>> targetChildren) {
		Set<TreeNode<D>> sourceChildren = getChildren();
			
		// do they have the same number of parents
		boolean equals = sourceChildren.size() == targetChildren.size();
		
		if(equals) {
			
			Iterator<TreeNode<D>> childrenItr = sourceChildren.iterator();
			while(childrenItr.hasNext() && equals) {
				equals = contains(childrenItr.next(), targetChildren);
			}
		}
		
		return equals;
	}
	
	private boolean contains(TreeNode<D> source, Set<TreeNode<D>> target) {
		boolean match = false;
		
		Iterator<TreeNode<D>> targetItr = target.iterator();
		
		while(targetItr.hasNext() && match == false) {
			match = source.equals(targetItr.next());
		}
		
		return match;
	}	
}
