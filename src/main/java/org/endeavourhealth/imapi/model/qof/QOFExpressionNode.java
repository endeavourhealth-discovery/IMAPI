package org.endeavourhealth.imapi.model.qof;

import java.util.ArrayList;
import java.util.List;

public class QOFExpressionNode {
  private String operator;
  private String condition;
  private String passResult;
  private String failResult;
  private List<QOFExpressionNode> children;

  private QOFExpressionNode() {
    this.children = new ArrayList<>();
  }

  // Factory methods to avoid constructor ambiguity
  public static QOFExpressionNode createOperatorNode(String operator) {
    QOFExpressionNode node = new QOFExpressionNode();
    node.operator = operator;
    return node;
  }

  public static QOFExpressionNode createConditionNode(String condition) {
    QOFExpressionNode node = new QOFExpressionNode();
    node.condition = condition;
    return node;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public List<QOFExpressionNode> getChildren() {
    return children;
  }

  public void addChild(QOFExpressionNode child) {
    this.children.add(child);
  }

  public String getPassResult() {
    return passResult;
  }

  public QOFExpressionNode setPassResult(String result) {
    this.passResult = result;
    return this;
  }

  public String getFailResult() {
    return failResult;
  }

  public QOFExpressionNode setFailResult(String failResult) {
    this.failResult = failResult;
    return this;
  }

  public String toFormattedString() {
    return toRecursiveFormattedString(0)
      .trim()+"\n"
      + "On Pass: " + this.passResult+"\n"
      + "On Fail: " + this.failResult;
  }
  private String toRecursiveFormattedString(int indent) {
    StringBuilder sb = new StringBuilder();
    String indentStr = "    ".repeat(indent);

    if (operator != null && !operator.isEmpty()) {
      sb.append(indentStr).append(operator).append("\n");
      for (QOFExpressionNode child : children) {
        sb.append(child.toRecursiveFormattedString(indent + 1)).append("\n");
      }
    } else if (condition != null && !condition.isEmpty()) {
      sb.append(indentStr).append(condition);
    }

    return sb.toString();
  }
}
