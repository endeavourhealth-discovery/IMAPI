package org.endeavourhealth.imapi.model.tree;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.endeavourhealth.imapi.json.TTNodeDeserializerV2;
import org.endeavourhealth.imapi.json.TTNodeSerializerV2;
import org.endeavourhealth.imapi.json.TreeNodeSerializer;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@JsonSerialize(using = TreeNodeSerializer.class)
@JsonDeserialize(using = TTNodeDeserializerV2.class)
public class TreeNode implements Serializable {
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
