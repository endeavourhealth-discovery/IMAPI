package org.endeavourhealth.imapi.model.qof;

import org.endeavourhealth.imapi.logic.importers.QOFExpressionParser;

public class Rule {
    private String logicText;
    private QOFExpressionNode logic;
    private String ifTrue;
    private String ifFalse;
    private String description;
    private Integer order;

    public Integer getOrder() {
        return order;
    }

    public Rule setOrder(Integer order) {
        this.order = order;
        return this;
    }

    public String getLogicText() {
        return logicText;
    }

    public Rule setLogicText(String logicText) {
        this.logicText = logicText;
        this.logic = QOFExpressionParser.parseExpression(logicText);
        return this;
    }

  public QOFExpressionNode getLogic() {
    return logic;
  }


  public String getIfTrue() {
        return ifTrue;
    }

    public Rule setIfTrue(String ifTrue) {
        this.ifTrue = ifTrue;
        return this;
    }

    public String getIfFalse() {
        return ifFalse;
    }

    public Rule setIfFalse(String ifFalse) {
        this.ifFalse = ifFalse;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Rule setDescription(String description) {
        this.description = description;
        return this;
    }
}
