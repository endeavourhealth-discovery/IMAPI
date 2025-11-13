package org.endeavourhealth.imapi.model.qof;

import org.endeavourhealth.imapi.logic.importers.QOFExpressionParser;

public class ExtractionField {
  private int field;
  private String name;
  private String cluster;
  private String logicText;
  private QOFExpressionNode logic;
  private String description;

  public int getField() {
    return field;
  }

  public ExtractionField setField(int field) {
    this.field = field;
    return this;
  }

  public String getName() {
    return name;
  }

  public ExtractionField setName(String name) {
    this.name = name;
    return this;
  }

  public String getCluster() {
    return cluster;
  }

  public ExtractionField setCluster(String cluster) {
    if (!"n/a".equals(cluster)) {
      this.cluster = cluster;
    }
    return this;
  }

  public String getLogicText() {
    return logicText;
  }

  public ExtractionField setLogicText(String logicText) {
    this.logicText = logicText;
    this.logic = QOFExpressionParser.parseExpression(logicText);
    return this;
  }

  public QOFExpressionNode getLogic() {
    return logic;
  }

  public String getDescription() {
    return description;
  }

  public ExtractionField setDescription(String description) {
    this.description = description;
    return this;
  }
}