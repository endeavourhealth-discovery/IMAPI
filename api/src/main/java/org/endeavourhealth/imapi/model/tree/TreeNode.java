package org.endeavourhealth.imapi.model.tree;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TreeNode {
  private String key;
  private String iri;
  private String label;
  private List<TreeNode> children;
  private String type;
  private TTValue data;

  public TreeNode() {
    children = new ArrayList<>();
  }

  public TreeNode setKey(String key) {
    this.key = key;
    return this;
  }

  public TreeNode setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public TreeNode setLabel(String label) {
    this.label = label;
    return this;
  }

  public TreeNode setChildren(List<TreeNode> children) {
    this.children = children;
    return this;
  }

  public TreeNode addChild(TreeNode child) {
    if (children == null) {
      children = new ArrayList<>();
    }
    children.add(child);
    return this;
  }

  public TreeNode setType(String type) {
    this.type = type;
    return this;
  }

  public TreeNode setData(TTValue data) {
    this.data = data;
    return this;
  }
}
