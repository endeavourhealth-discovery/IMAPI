package org.endeavourhealth.imapi.transform.qofimq.ast;

import java.util.ArrayList;
import java.util.List;

public class BoolNode implements AstNode {
  private final BoolOp op;
  private final List<AstNode> operands;

  public BoolNode(BoolOp op, List<AstNode> operands) {
    this.op = op;
    this.operands = operands != null ? operands : new ArrayList<>();
  }

  public BoolOp getOp() { return op; }
  public List<AstNode> getOperands() { return operands; }
}
