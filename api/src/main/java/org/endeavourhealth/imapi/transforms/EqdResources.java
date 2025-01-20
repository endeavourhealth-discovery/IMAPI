package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.endeavourhealth.imapi.logic.exporters.ImportMaps;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.transforms.EqdToIMQ.*;

public class EqdResources {

  private static final Logger LOG = LoggerFactory.getLogger(EqdResources.class);
  private final ImportMaps importMaps = new ImportMaps();
  private final Map<Object, Object> vocabMap = new HashMap<>();
  private final Map<String, Set<Node>> valueMap = new HashMap<>();
  private String namespace;
  @Getter
  Map<String, String> reportNames = new HashMap<>();
  private final Properties dataMap;
  private String activeReport;
  @Getter
  private String activeReportName;
  @Getter
  private String activeFolder;
  @Getter
  private TTDocument document;
  private TTIriRef columnGroup;
  private int counter = 0;
  private int setCounter = 0;
  private String sourceContext;
  private boolean isTestSet;

  public TTIriRef getColumnGroup() {
    return columnGroup;
  }

  public EqdResources setColumnGroup(TTIriRef columnGroup) {
    this.columnGroup = columnGroup;
    return this;
  }

  public EqdResources(TTDocument document, Properties dataMap) {
    this.dataMap= dataMap;
    this.document= document;
    this.namespace= document.getGraph().getIri();
    setVocabMaps();
  }

  public String getNamespace() {
    return namespace;
  }

  public EqdResources setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  public EqdResources setActiveFolder(String activeFolder) {
    this.activeFolder = activeFolder;
    return this;
  }


  public EqdResources setReportNames(Map<String, String> reportNames) {
    this.reportNames = reportNames;
    return this;
  }

  public EqdResources setActiveReportName(String activeReportName) {
    this.activeReportName = activeReportName;
    return this;
  }

  public EqdResources setDocument(TTDocument document) {
    this.document = document;
    return this;
  }

  public EqdResources setActiveReport(String activeReport) {
    this.activeReport = activeReport;
    return this;
  }


  private void setVocabMaps() {
    vocabMap.put(VocRangeFromOperator.GTEQ, Operator.gte);
    vocabMap.put(VocRangeFromOperator.GT, Operator.gt);
    vocabMap.put(VocRangeToOperator.LT, Operator.lt);
    vocabMap.put(VocRangeToOperator.LTEQ, Operator.lte);
    vocabMap.put(VocOrderDirection.DESC, "DESC");
    vocabMap.put(VocOrderDirection.ASC, "ASC");
  }


  public Match convertCriteria(EQDOCCriteria eqCriteria) throws IOException, QueryException, EQDException {
    if ((eqCriteria.getPopulationCriterion() != null)) {
      EQDOCSearchIdentifier search = eqCriteria.getPopulationCriterion();
      Match match = new Match();
      match
        .addInstanceOf(new Node().setIri(namespace+search.getReportGuid()).setMemberOf(true))
        .setName("in the cohort " + reportNames.get(search.getReportGuid()));
      return match;
    } else {
      return convertCriterion(eqCriteria.getCriterion());
    }
  }


  private Match convertCriterion(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {
    if (!eqCriterion.getBaseCriteriaGroup().isEmpty()) {
      return convertBaseCriteriaGroup(eqCriterion);
    }
    if (eqCriterion.getLinkedCriterion() != null) {
      return convertLinkedCriterion(eqCriterion);
    } else {
      return convertStandardCriterion(eqCriterion);
    }

  }

  private Match convertBaseCriteriaGroup(EQDOCCriterion eqCriterion) throws QueryException, EQDException, IOException {
    Match baseMatch = new Match();
    baseMatch.setBoolMatch(Bool.and);
    for (EQDOCBaseCriteriaGroup baseGroup : eqCriterion.getBaseCriteriaGroup()) {
      Match baseGroupMatch = new Match();
      baseMatch.addMatch(baseGroupMatch);
      VocMemberOperator memberOp = baseGroup.getDefinition().getMemberOperator();
      if (memberOp == VocMemberOperator.AND) {
        baseGroupMatch.setBoolMatch(Bool.and);
      } else baseGroupMatch.setBoolMatch(Bool.or);
      for (EQDOCCriteria eqCriteria : baseGroup.getDefinition().getCriteria()) {
        baseGroupMatch.addMatch(convertCriteria(eqCriteria));
      }
    }
    return baseMatch;
  }


  private Match setMatchId(EQDOCCriterion eqCriterion, Match match) {
    if (eqCriterion.getId() != null) {
      match.setIri(namespace + eqCriterion.getId());
    }
    if (match.getWhere() != null && match.getWhere().size() > 1) {
      match.setBoolWhere(Bool.and);
    }

    if (eqCriterion.getDescription() != null) match.setName(eqCriterion.getDescription());
    return match;
  }


  private Match convertStandardCriterion(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {
    Match match;
    if (eqCriterion.getFilterAttribute().getRestriction() != null) {
      match = convertRestrictionCriterion(eqCriterion);
      if (eqCriterion.isNegation()) {
        match.setExclude(true);
      }
      setMatchId(eqCriterion, match);


    } else {
      match = convertColumns(eqCriterion);
      if (eqCriterion.isNegation()) {
        match.setExclude(true);
      }

    }
    return match;
  }

  private Match convertColumns(EQDOCCriterion eqCriterion) throws IOException, EQDException {
    EQDOCFilterAttribute filterAttribute = eqCriterion.getFilterAttribute();
    List<EQDOCColumnValue> cvs = filterAttribute.getColumnValue();
    return convertColumnValues(cvs, eqCriterion.getTable());
  }

  private void convertPaths(List<Match> matches, Map<String, Match> pathMatchMap, String eqTable, EQDOCColumnValue cv) throws IOException, EQDException {
    String tablePath = getPath(eqTable);
    String eqColumn = String.join("/", cv.getColumn());
    String eqURL = eqTable + "/" + eqColumn;
    sourceContext = eqURL;
    String columnPath = getPath(eqURL);
    String fullPath = (tablePath + " " + columnPath).trim();
    Match match = fullPath.contains(" ") ?pathMatchMap.get(fullPath.substring(0, fullPath.lastIndexOf(" "))): pathMatchMap.get("");
    if (match == null) {
      match = new Match();
      matches.add(match);
      pathMatchMap.put(fullPath.contains(" ")? fullPath.substring(0,fullPath.lastIndexOf(" ")): "", match);
      String[] paths = fullPath.split(" ");
      for (int i = 0; i < paths.length - 1; i++) {
        IriLD pathIri = new IriLD();
        String path = paths[i];
        if (path.startsWith("^")) {
          pathIri.setInverse(true);
          path = path.substring(1);
        }
        pathIri.setIri(path);
        match.addPath(pathIri);
      }
    }
    String property;
    if (columnPath.contains(" ")) {
      property = columnPath.substring(columnPath.lastIndexOf(" ") + 1);
    } else {
      property = columnPath;
    }
    Where where = new Where();
    match.addWhere(where);
    where.setIri(property);
    setProperty(cv, where);
    if (match.getWhere().size() > 1) match.setBoolWhere(Bool.and);

  }


  private Match convertColumnValues(List<EQDOCColumnValue> cvs, String eqTable) throws IOException, EQDException {
    List<Match> matches = new ArrayList<>();
    Map<String, Match> pathMatchMap = new HashMap<>();
    for (EQDOCColumnValue cv : cvs) {
      convertPaths(matches, pathMatchMap, eqTable, cv);
    }

    if (matches.size() == 1) {
      return matches.get(0);
    } else {
      Match match = new Match();
      match.setBoolMatch(Bool.and);
      match.setMatch(matches);
      return match;
    }
  }


  private void setProperty(EQDOCColumnValue cv, Where pv) throws IOException, EQDException {

    VocColumnValueInNotIn in = cv.getInNotIn();
    boolean notIn = (in == VocColumnValueInNotIn.NOTIN);
    if (!cv.getValueSet().isEmpty()) {
      setPropertyValueSets(cv, pv, notIn);
    } else if (!CollectionUtils.isEmpty(cv.getLibraryItem())) {
      String valueLabel = "";
      for (String vset : cv.getLibraryItem()) {
        setCounter++;
        String vsetName= "Unnamed library set " + setCounter ;
        valueLabel = valueLabel + (valueLabel.isEmpty() ? "" : ", ") + vsetName;
        Node iri = new Node().setIri(namespace + vset);
        iri.setMemberOf(true);
        if (!notIn) pv.addIs(iri);
        else {
          iri.setExclude(true);
          pv.addIs(iri);
        }
        pv.setValueLabel(valueLabel);
        createLibraryValueSet(iri.getIri(), vsetName);
      }
    } else if (cv.getRangeValue() != null) {
      setRangeValue(cv.getRangeValue(), pv);
    }
  }

  private void setPropertyValueSets(EQDOCColumnValue cv, Where pv, boolean notIn) throws IOException {
    for (EQDOCValueSet vs : cv.getValueSet()) {
      if (vs.getAllValues() != null) {
        List<Node> values = getExceptionSet(vs.getAllValues());
        for (Node node : values) {
          node.setExclude(true);
        }
        pv.setIs(values);
      } else {
        if (!notIn) {
          setInlineValues(vs, pv);
        } else {
          pv.setExclude(true);
          setInlineValues(vs, pv);
        }
      }
    }
  }


  public String getPath(String eqdPath) throws EQDException {
    Object target = dataMap.get(eqdPath);
    if (target == null) throw new EQDException("unknown map : " + eqdPath);
    if (target.equals("")) return "";
    String[] paths = ((String) target).split(" ");
    for (int i = 0; i < paths.length; i++) {
      String path = paths[i];
      String inverse = "";
      if (path.startsWith("^")) {
        inverse = "^";
        path = path.substring(1);
      }
      path = inverse + (path.startsWith("http") ? path : IM.NAMESPACE + path);
      paths[i] = path;
    }
    return String.join(" ", paths);
  }


  private Match convertRestrictionCriterion(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {
    Match restricted = convertColumns(eqCriterion);
    setRestriction(eqCriterion, restricted);
    isTestSet = false;
    if (eqCriterion.getFilterAttribute().getRestriction().getTestAttribute() != null) {
      isTestSet = true;
      Match testMatch = restrictionTest(eqCriterion);
      if (testMatch != null) {
        testMatch.setPath(null);
        restricted.setThen(testMatch);
        isTestSet = false;
      }

    }
    return restricted;
  }


  private Match restrictionTest(EQDOCCriterion eqCriterion) throws IOException, EQDException {
    EQDOCTestAttribute testAtt = eqCriterion.getFilterAttribute().getRestriction().getTestAttribute();
    if (testAtt != null) {
      return convertColumnValues(testAtt.getColumnValue(), eqCriterion.getTable());
    }
    return null;

  }

  private void setRestriction(EQDOCCriterion eqCriterion, Match restricted) throws EQDException {
    Order direction;
    EQDOCFilterRestriction restrict = eqCriterion.getFilterAttribute().getRestriction();
    if (restrict.getColumnOrder().getColumns().get(0).getDirection() == VocOrderDirection.ASC) {
      direction = Order.ascending;
    } else {
      direction = Order.descending;
    }
    String linkColumn = eqCriterion.getFilterAttribute().getRestriction().getColumnOrder().getColumns().get(0).getColumn().get(0);
    String orderBy = getPath(eqCriterion.getTable() + "/" + linkColumn);
    restricted.orderBy(o -> o.setProperty(new OrderDirection().setIri(orderBy).setDirection(direction)).setLimit(restrict.getColumnOrder().getRecordCount()));
  }


  private Match convertLinkedCriterion(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {
    counter++;
    String nodeRef = "match_" + counter;
    Match linked = new Match();
    linked.setBoolMatch(Bool.and);
    Match topMatch = convertStandardCriterion(eqCriterion);
    topMatch.setVariable(nodeRef);
    linked.addMatch(topMatch);
    EQDOCLinkedCriterion eqLinked = eqCriterion.getLinkedCriterion();
    EQDOCCriterion eqLinkedCriterion = eqLinked.getCriterion();
    Match linkMatch = convertCriterion(eqLinkedCriterion);
    linked.addMatch(linkMatch);
    Where relationProperty = new Where();
    EQDOCRelationship eqRel = eqLinked.getRelationship();
    String parent = getPath(eqCriterion.getTable() + "/" + eqRel.getParentColumn());
    String child = getPath(eqLinkedCriterion.getTable() + "/" + eqRel.getChildColumn());
    relationProperty.setIri(child);
    if (eqRel.getRangeValue() != null) {
      EQDOCRangeValue eqRange = eqRel.getRangeValue();
      if (eqRange.getRangeFrom() != null && eqRange.getRangeTo() != null) {
        Range range = new Range();
        relationProperty.setRange(range);
        Value from = new Value();
        range.setFrom(from);
        from.setOperator((Operator) vocabMap.get(eqRange.getRangeFrom().getOperator())).setValue(eqRange.getRangeFrom().getValue().getValue());
        if (eqRange.getRangeFrom().getValue().getUnit() != null) {
          setUnitsOrArgument(relationProperty, eqRange.getRangeFrom().getValue().getUnit().value());
        }
        Value to = new Value();
        range.setTo(to);
        to.setOperator((Operator) vocabMap.get(eqRange.getRangeTo().getOperator())).setValue(eqRange.getRangeTo().getValue().getValue());
        if (eqRange.getRangeTo().getValue().getUnit() != null) {
          setUnitsOrArgument(to, eqRange.getRangeTo().getValue().getUnit().value());
        }
      } else if (eqRel.getRangeValue().getRangeFrom() != null) {
        relationProperty.setOperator((Operator) vocabMap.get(eqRange.getRangeFrom().getOperator())).setValue(eqRange.getRangeFrom().getValue().getValue());
        if (eqRange.getRangeFrom().getValue().getUnit() != null) {
          setUnitsOrArgument(relationProperty, eqRange.getRangeFrom().getValue().getUnit().value());
        }
      } else {
        relationProperty.setOperator((Operator) vocabMap.get(eqRange.getRangeTo().getOperator())).setValue(eqRange.getRangeTo().getValue().getValue());
        if (eqRange.getRangeTo().getValue().getUnit() != null) {
          setUnitsOrArgument(relationProperty, eqRange.getRangeTo().getValue().getUnit().value());
        }
      }
    } else {
      relationProperty.setOperator(Operator.eq);
    }
    relationProperty.relativeTo(r -> r.setNodeRef(nodeRef).setIri(parent));
    if (linkMatch.getMatch() == null) linkMatch.addWhere(relationProperty);
    else linkMatch.getMatch().get(0).addWhere(relationProperty);
    return linked;
  }

  private void setRangeValue(EQDOCRangeValue rv, Where pv) throws EQDException {

    EQDOCRangeFrom rFrom = rv.getRangeFrom();
    EQDOCRangeTo rTo = rv.getRangeTo();
    if (rFrom != null) {
      if (rTo == null) {
        setCompareFrom(pv, rFrom);
      } else {
        setRangeCompare(pv, rFrom, rTo);
      }
    }
    if (rTo != null && rFrom == null) {
      if (rTo.getValue() != null && rTo.getValue().getValue() != null && rTo.getValue().getValue().equals("This")) {
        if (rTo.getValue().getUnit() == VocValueUnit.FISCALYEAR) {
          setFiscalYear(pv, rTo);
        } else throw new EQDException("unknown units with 'This' value : " + rTo.getValue().getUnit());
      } else setCompareTo(pv, rTo);
    }
    if (rv.getRelativeTo() != null && rv.getRelativeTo().equals("BASELINE")) {
      pv.setRelativeTo(new PropertyRef().setParameter("$baselineDate"));
    }
  }

  private void setFiscalYear(Where where, EQDOCRangeTo rTo) throws EQDException {
    if (rTo.getOperator() == VocRangeToOperator.LT) {
      where.setValueParameter("$startOfFiscalYear");
      where.setOperator(Operator.lt);
    } else if (rTo.getOperator() == VocRangeToOperator.LTEQ) {
      where.setValueParameter("$endOfFiscalYear");
      where.setOperator(Operator.lte);
    } else throw new EQDException("Unknown fiscal year operator " + rTo.getOperator().value());
  }

  private void setCompareFrom(Where where, EQDOCRangeFrom rFrom) throws EQDException {
    Operator comp;
    if (rFrom.getOperator() != null) comp = (Operator) vocabMap.get(rFrom.getOperator());
    else comp = Operator.eq;
    String value = null;
    String units = null;
    VocRelation relation = null;
    if (rFrom.getValue() != null) {
      value = rFrom.getValue().getValue();
      if (rFrom.getValue().getUnit() != null) units = rFrom.getValue().getUnit().value();

      relation = VocRelation.ABSOLUTE;
      if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
        relation = VocRelation.RELATIVE;
      }
    }
    setCompare(where, comp, value, units, relation);
  }

  private void setCompare(Where where, Value pv, Operator comp, String value, String units, VocRelation relation) throws EQDException {
    if (relation == VocRelation.RELATIVE) {
      where.setRelativeTo(new PropertyRef().setParameter("$referenceDate"));
    }

    pv.setOperator(comp);
    pv.setValue(value);
    if (units != null) setUnitsOrArgument(where, units);
  }

  private void setUnitsOrArgument(Assignable assignable, String units) throws EQDException {
    switch (units) {
      case "YEAR":
        assignable.setUnit(TTIriRef.iri(IM.NAMESPACE + "Years"));
        break;
      case "MONTH":
        assignable.setUnit(TTIriRef.iri(IM.NAMESPACE + "Months"));
        break;
      case "DAY":
        assignable.setUnit(TTIriRef.iri(IM.NAMESPACE + "Days"));
        break;
      case "DATE":
        break;
      default:
        throw new EQDException("unknown unit map: " + units);

    }
  }


  private void setCompare(Where where, Operator comp, String value, String units, VocRelation relation) throws EQDException {
    if (relation == VocRelation.RELATIVE) {
      where.setRelativeTo(new PropertyRef().setParameter("$referenceDate"));
    }
    if (comp != null) {
      where.setOperator(comp);
    }
    if (value != null) {
      where.setValue(value);
    }
    if (units != null) setUnitsOrArgument(where, units);
  }


  private void setCompareTo(Where pv, EQDOCRangeTo rTo) throws EQDException {
    Operator comp;
    if (rTo.getOperator() != null) comp = (Operator) vocabMap.get(rTo.getOperator());
    else comp = Operator.eq;
    String value = null;
    String units = null;
    VocRelation relation = null;
    if (rTo.getValue() != null) {
      value = rTo.getValue().getValue();
      if (rTo.getValue().getUnit() != null) units = rTo.getValue().getUnit().value();
      relation = VocRelation.ABSOLUTE;
      if (rTo.getValue().getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
        relation = VocRelation.RELATIVE;
      }
    }
    setCompare(pv, comp, value, units, relation);
  }


  private void setRangeCompare(Where where, EQDOCRangeFrom rFrom, EQDOCRangeTo rTo) throws EQDException {
    Range range = new Range();
    where.setRange(range);
    Value fromValue = new Value();
    range.setFrom(fromValue);
    Operator comp;
    if (rFrom.getOperator() != null) comp = (Operator) vocabMap.get(rFrom.getOperator());
    else comp = Operator.eq;
    String value = rFrom.getValue().getValue();
    String units = null;
    if (rFrom.getValue().getUnit() != null) units = rFrom.getValue().getUnit().value();
    VocRelation relation = VocRelation.ABSOLUTE;
    if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
      relation = VocRelation.RELATIVE;
    }
    setCompare(where, fromValue, comp, value, units, relation);
    Value toValue = new Value();
    range.setTo(toValue);
    if (rFrom.getOperator() != null) comp = (Operator) vocabMap.get(rTo.getOperator());
    else comp = Operator.eq;
    value = rTo.getValue().getValue();
    units = null;
    if (rTo.getValue().getUnit() != null) units = rTo.getValue().getUnit().value();
    relation = VocRelation.ABSOLUTE;
    if (rTo.getValue().getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
      relation = VocRelation.RELATIVE;
    }
    setCompare(where, toValue, comp, value, units, relation);
  }


  private List<Node> getExceptionSet(EQDOCException set) throws IOException {
    List<Node> valueSet = new ArrayList<>();
    VocCodeSystemEx scheme = set.getCodeSystem();
    for (EQDOCExceptionValue ev : set.getValues()) {
      Set<Node> values = getValueConcepts(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
      if (values != null) {
        valueSet.addAll(values.stream().map(v -> v.setExclude(true)).toList());
      } else LOG.error("Missing exception sets {} {}", ev.getValue(), ev.getDisplayName());
    }

    return valueSet;
  }

  private TTIriRef getClusterSet(EQDOCValueSet vs) throws IOException {
    if (vs.getCodeSystem() == VocCodeSystemEx.SNOMED_CONCEPT && vs.getDescription() != null && vs.getClusterCode().contains("FlattenedCodeList")) {
      return importMaps.getReferenceFromCoreTerm(vs.getDescription());
    }
    return null;
  }


  private void setInlineValues(EQDOCValueSet vs, Where pv) throws IOException {
    VocCodeSystemEx scheme = vs.getCodeSystem();
    if (vs.getDescription() != null) {
      pv.setValueLabel(vs.getDescription());
    }
    TTIriRef cluster = getClusterSet(vs);
    if (cluster != null) {
      pv.addIs(new Node().setIri(cluster.getIri()).setName(cluster.getName()).setMemberOf(true));
      return;
    }
    Set<Node> setContent = new HashSet<>();
    for (EQDOCValueSetValue ev : vs.getValues()) {
      Set<Node> setMembers = processEQDOCValueSet(scheme, ev);
      if (!setMembers.isEmpty()) {
        for (Node memberOrConcept:setMembers){
          if (memberOrConcept.isMemberOf()){
            String setIri= memberOrConcept.getIri();
            TTEntity usedIn= setIriToEntity.get(setIri);
            if (usedIn==null) {
              usedIn = new TTEntity()
                .setIri(setIri)
                .setCrud(iri(IM.ADD_QUADS));
              document.addEntity(usedIn);
              setIriToEntity.put(setIri,usedIn);
            }
            addUsedIn(usedIn);
          }
        }
        setContent.addAll(setMembers);
      }
    }
    if (setContent.size() > 3) {
      for (Node node:setContent){
        if (node.isMemberOf())
          pv.addIs(node);
      }
      Set<Node> conceptContent= setContent.stream().filter(c-> !c.isMemberOf()).collect(Collectors.toSet());
      TTEntity valueSet = createValueSet(vs, conceptContent);
        pv.addIs(new Node().setIri(valueSet.getIri()).setName(valueSet.getName())
          .setMemberOf(true));
    }
    else {
      String name = "";
      String exclusions = "";
      for (Node node : setContent) {
        if (node.isExclude()) exclusions = " (exclusions)";
        pv.addIs(node);
        if (node.getName() != null)
          name = name.equals("") ? getShortName(node.getName(), null) : name + ", " + getShortName(node.getName(), null);

      }
      if (vs.getDescription() != null) name = vs.getDescription();
      pv.setValueLabel(name + exclusions);
    }
  }


  private Set<Node> processEQDOCValueSet(VocCodeSystemEx scheme, EQDOCValueSetValue ev) throws IOException {
    Set<Node> concepts = getValueConcepts(scheme, ev);
    if (concepts != null&& (!concepts.isEmpty())){
      if (ev.isIncludeChildren()) {
        for (Node node : concepts) {
          node.setDescendantsOrSelfOf(true);
        }
      }
    }
    else
      LOG.error("Missing {} {}", ev.getValue(), ev.getDisplayName());
    if (!ev.getException().isEmpty()) {
      for (EQDOCException exc : ev.getException()) {
        for (EQDOCExceptionValue val : exc.getValues()) {
          Set<Node> exceptionValue = getValueConcepts(scheme, val);
          if (exceptionValue != null) {
            for (Node node: exceptionValue) {
              if (val.isIncludeChildren())
                node.setDescendantsOrSelfOf(true);
              node.setExclude(true);
              concepts.add(node);
            }
          }
        }
      }
    }
    return concepts;
  }


  private Set<Node> getValueConcepts(VocCodeSystemEx scheme, EQDOCExceptionValue ev) throws IOException {
    return getValueConcepts(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
  }


  private Set<Node> getValueConcepts(VocCodeSystemEx scheme, EQDOCValueSetValue ev) throws IOException {
    return getValueConcepts(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
  }

  private Set<Node> getValueConcepts(VocCodeSystemEx scheme, String originalCode, String originalTerm, String legacyCode) throws IOException {
    if (scheme == VocCodeSystemEx.EMISINTERNAL) {
      String key = sourceContext + "/EMISINTERNAL/" + originalCode;
      Object mapValue = dataMap.get(key);
      if (mapValue != null) {
        return getValueIriResult(mapValue);
      }
      else {
          if (isValidUUID(originalCode)) {
            return getValueIriResult(namespace+originalCode);
          }
          else
            throw new IllegalArgumentException("unmapped emis internal code : " + key);
        }
    }
    else if (scheme != VocCodeSystemEx.SNOMED_CONCEPT && !scheme.value().contains("SCT")) {
      throw new IllegalArgumentException("code scheme not recognised : " + scheme.value());
    }
    List<String> schemes = new ArrayList<>();
    schemes.add(SNOMED.NAMESPACE);
    schemes.add(GRAPH.EMIS);
    schemes.add(GRAPH.DISCOVERY);
    Set<Node> snomed = valueMap.get(originalCode);
    if (snomed == null) {
      snomed = getValuesFromOriginal(originalCode, originalTerm, legacyCode, schemes);
      if (snomed != null)
        valueMap.put(originalCode, snomed);
    }
    if (snomed != null) {
      for (Node sn : snomed) {
        if (sn.getIri() == null) {
          System.out.println("null snomed");
        }
      }
      return snomed;
    } else
      return Collections.emptySet();
  }

  private Set<Node> getValueIriResult(Object mapValue) throws IOException {
    Node iri = new Node().setIri(mapValue.toString());
    String name = importMaps.getCoreName(iri.getIri());
    if (name != null) iri.setName(name);
    Set<Node> result = new HashSet<>();
    result.add(iri);
    return result;
  }

  private Set<Node> getValuesFromOriginal(String originalCode, String originalTerm, String legacyCode, List<String> schemes) {
    Set<Entity> snomed;
    snomed = getCoreFromCode(originalCode, schemes);
    if (snomed == null && legacyCode != null)
      snomed = getCoreFromCode(legacyCode, schemes);
    if (snomed == null && originalTerm != null)
      snomed = getCoreFromLegacyTerm(originalTerm);
    if (snomed == null)
      snomed = getLegacyFromTermCode(originalCode);
    if (snomed == null && originalTerm != null)
        snomed = getCoreFromLegacyTerm(originalTerm+" (emis code id)");
    if (snomed==null)
      return null;
    return snomed.stream().map(e-> {
      Node n= new Node();
      n.setIri(e.getIri());
      n.setName(e.getName());
      if (Set.of(iri(IM.CONCEPT_SET),iri(IM.VALUESET)).stream().anyMatch(e.getEntityType()::contains))
        n.setMemberOf(true);
      return n;})
      .collect(Collectors.toSet());
  }




  private Set<Entity> getLegacyFromTermCode(String originalCode) {
    try {
      return importMaps.getLegacyFromTermCode(originalCode, GRAPH.EMIS);
    } catch (Exception e) {
      LOG.error("unable to retrieve iri match term code {}", e.getMessage());
      return Collections.emptySet();
    }
  }


  private Set<Entity> getCoreFromLegacyTerm(String originalTerm) {
    try {
      if (originalTerm.contains("s disease of lymph nodes of head, face AND/OR neck")) LOG.info("!!");

      return importMaps.getCoreFromLegacyTerm(originalTerm, GRAPH.EMIS);
    } catch (Exception e) {
      LOG.error("unable to retrieve iri match term {}", e.getMessage());
      return Collections.emptySet();
    }
  }

  private Set<Entity> getCoreFromCode(String originalCode, List<String> schemes) {
    return importMaps.getCoreFromCode(originalCode, schemes);
  }

  private void addUsedIn(TTEntity set){
    if (columnGroup!=null){
      set.addObject(iri(IM.USED_IN),iri(columnGroup.getIri()));
    }
    else
      set.addObject(iri(IM.USED_IN), iri(namespace +activeReport));

  }


  private TTEntity createLibraryValueSet(String iri, String name){
    TTEntity valueSet = new TTEntity()
      .setIri(iri)
      .setName(name)
      .addType(iri(IM.CONCEPT_SET));
    addUsedIn(valueSet);
    document.addEntity(valueSet);
    return valueSet;
  }

  private void updateSetName(TTEntity setName,String thisName) {
    String currentName = setName.getName();
    if (!currentName.contains(thisName)) {
      setName.setName(currentName.split(" (set)")[0]+" ... (set");
    }
  }



  private TTEntity createValueSet(EQDOCValueSet vs, Set<Node> setContent) throws JsonProcessingException {
    String description= vs.getDescription();
    String name=description==null ? "unnamed set": description;
    String entailedMembers= new ObjectMapper().writeValueAsString(setContent);
    TTEntity duplicate= setContentToEntity.get(entailedMembers);
    if (duplicate!=null) {
      addUsedIn(duplicate);
      if (columnGroup!=null){
        updateSetName(duplicate,name);
      }
      else {
        updateSetName(duplicate,name);
      }
      return duplicate;
    }
    TTEntity valueSet = new TTEntity()
      .setIri(namespace + vs.getId())
      .setName(name)
      .addType(iri(IM.CONCEPT_SET));
    if (columnGroup!=null){
      valueSet.addObject(iri(IM.USED_IN),iri(columnGroup.getIri()));
    }
    else
      valueSet.addObject(iri(IM.USED_IN), iri(namespace + activeReport));
    for (Node node : setContent) {
      TTNode instance = new TTNode();
      valueSet.addObject(iri(IM.ENTAILED_MEMBER), instance);
      instance.set(IM.INSTANCE_OF,TTIriRef.iri(node.getIri()));
      if (node.isExclude()) {
        instance.set(IM.EXCLUDE,TTLiteral.literal(true));
      }
      if (node.isAncestorsOf())
        instance.set(IM.ENTAILMENT, iri(IM.ANCESTORS_OF));
      if (node.isDescendantsOf())
        instance.set(IM.ENTAILMENT,iri(IM.DESCENDANTS_OF));
      if (node.isDescendantsOrSelfOf())
        instance.set(IM.ENTAILMENT,iri(IM.DESCENDANTS_OR_SELF_OF));

    }
    setContentToEntity.put(entailedMembers,valueSet);
    document.addEntity(valueSet);
    return valueSet;
  }

  private String getShortName(String name, String previous) {
    name = name.split(" \\(")[0];
    if (previous == null) {
      return name;
    } else {
      if (name.contains("resolved"))
        return previous + " or resolved";
      else
        return previous + " or " + name;
    }
  }

  private static boolean isValidUUID(String str) {
    try {
      UUID uuid = UUID.fromString(str);
      return str.equals(uuid.toString()); // Ensure string matches canonical UUID format
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

}
