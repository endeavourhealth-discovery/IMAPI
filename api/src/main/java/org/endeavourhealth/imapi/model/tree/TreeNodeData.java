package org.endeavourhealth.imapi.model.tree;

import lombok.Getter;

@Getter
public class TreeNodeData {
  private String predicate;
  private int totalCount;

  public TreeNodeData() {
  }

  public TreeNodeData(String predicate, int totalCount) {
    this.predicate = predicate;
    this.totalCount = totalCount;
  }

  public TreeNodeData setPredicate(String predicate) {
    this.predicate = predicate;
    return this;
  }

  public TreeNodeData setTotalCount(int totalCount) {
    this.totalCount = totalCount;
    return this;
  }
}
