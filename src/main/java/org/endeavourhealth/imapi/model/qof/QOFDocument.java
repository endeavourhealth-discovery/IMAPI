package org.endeavourhealth.imapi.model.qof;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QOFDocument {
  private String name;
  private Map<String, String> terms = new HashMap<>();
  private List<Selection> selections = new ArrayList<>();
  private List<Register> registers = new ArrayList<>();
  private List<CodeCluster> codeClusters = new ArrayList<>();
  private List<ExtractionField> extractionFields = new ArrayList<>();
  private List<Indicator> indicators = new ArrayList<>();

  public String getName() {
    return name;
  }

  public QOFDocument setName(String name) {
    this.name = name;
    return this;
  }

  public Map<String, String> getTerms() {
    return terms;
  }

  public QOFDocument setTerms(Map<String, String> terms) {
    this.terms = terms;
    return this;
  }

  public QOFDocument addTerm(String key, String value) {
    terms.put(key, value);
    return this;
  }

  public boolean hasTerm(String key) {
    return terms.containsKey(key);
  }

  public List<Selection> getSelections() {
    return selections;
  }

  public QOFDocument setSelections(List<Selection> selections) {
    this.selections = selections;
    return this;
  }

  public List<Register> getRegisters() {
    return registers;
  }

  public QOFDocument setRegisters(List<Register> registers) {
    this.registers = registers;
    return this;
  }

  public List<CodeCluster> getCodeClusters() {
    return codeClusters;
  }

  public QOFDocument setCodeClusters(List<CodeCluster> codeClusters) {
    this.codeClusters = codeClusters;
    return this;
  }

  public QOFDocument addCodeCluster(CodeCluster codeCluster) {
    this.codeClusters.add(codeCluster);
    return this;
  }

  public List<ExtractionField> getExtractionFields() {
    return extractionFields;
  }

  public QOFDocument setExtractionFields(List<ExtractionField> extractionFields) {
    this.extractionFields = extractionFields;
    return this;
  }

  public List<Indicator> getIndicators() {
    return indicators;
  }

  public QOFDocument setIndicators(List<Indicator> indicators) {
    this.indicators = indicators;
    return this;
  }
}
