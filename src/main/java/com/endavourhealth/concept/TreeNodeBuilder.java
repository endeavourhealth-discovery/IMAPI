package com.endavourhealth.concept;

import java.util.HashMap;
import java.util.Map;

import com.endavourhealth.concept.models.TreeNode;
import com.endavourhealth.dataaccess.entity.Concept;

public class TreeNodeBuilder<D> {
	
	private Map<Concept, TreeNode<D>> allTreeNodes;
	
	private FromConceptConverter<D> fromConceptConverter;
	
	private TreeNode<D> seedNode;
	
	public TreeNodeBuilder(Concept seedConcept, FromConceptConverter<D> fromConceptConverter) {
		this.fromConceptConverter = fromConceptConverter;
		this.allTreeNodes = new HashMap<Concept, TreeNode<D>>();
		this.seedNode = addNode(seedConcept);
	}

	public TreeNode<D> addParentNode(Concept childConcept, Concept parentConcept) {
		TreeNode<D> parentTreeNode = null;

		if(allTreeNodes.containsKey(childConcept)) {
			
			parentTreeNode = addNode(parentConcept);			
			parentTreeNode.addChild(allTreeNodes.get(childConcept));		
		}
		else
		{
			// error - unknown node
		}
		
		return parentTreeNode;
	}
	
	public TreeNode<D> addChildNode(Concept childConcept, Concept parentConcept) {
		TreeNode<D> childTreeNode = null;

		if(allTreeNodes.containsKey(parentConcept)) {
			
			childTreeNode = addNode(childConcept);	
			childTreeNode.addParent(allTreeNodes.get(parentConcept));
		}
		else
		{
			// error - unknown node
		}
		
		return childTreeNode;	
	}	
	
	public TreeNode<D> addNode(Concept concept) {
		TreeNode<D> treeNode = null;
		
		if(allTreeNodes.containsKey(concept)) {
			treeNode = allTreeNodes.get(concept);
		}
		else {
			treeNode = new TreeNode<D>(fromConceptConverter.convert(concept));
			allTreeNodes.put(concept, treeNode);
		}

		return treeNode;
	}

	TreeNode<D> getSeedNode() {
		return seedNode;
	}
}
