package org.endeavourhealth.imapi.transforms;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.endeavourhealth.imapi.logic.exporters.ImportMaps;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdResources {
  public static final String GUID = "urn:uuid:";
  private static final Logger LOG = LoggerFactory.getLogger(EqdResources.class);
  private final ImportMaps importMaps = new ImportMaps();
  private final Map<Object, Object> vocabMap = new HashMap<>();
  private final Map<String, Set<TTIriRef>> valueMap = new HashMap<>();
  @Getter
  Map<String, String> reportNames = new HashMap<>();
  private Properties dataMap;
  private String activeReport;
  @Getter
  private String activeReportName;
  @Getter
  private TTDocument document;
  private int counter = 0;
  private int setCounter = 0;
  private String sourceContext;


  public EqdResources() {
    setVocabMaps();
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

  public EqdResources setDataMap(Properties dataMap) {
    this.dataMap = dataMap;
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
        .addInstanceOf(new Node().setIri(GUID + search.getReportGuid()).setMemberOf(true))
        .setName("in the cohort " + reportNames.get(search.getReportGuid()));
      return match;
    } else {
      return convertCriterion(eqCriteria.getCriterion());
    }
  }


  private Match convertCriterion(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {

    if (eqCriterion.getLinkedCriterion() != null) {
      return convertLinkedCriterion(eqCriterion);
    } else {
      return convertStandardCriterion(eqCriterion);
    }

  }


  private Match setMatchId(EQDOCCriterion eqCriterion, Match match) {
    if (eqCriterion.getId() != null) {
      match.setIri(GUID + eqCriterion.getId());
    }
    if (match.getWhere() != null && match.getWhere().size() > 1) {
      match.setBoolWhere(Bool.and);
    }

    if (eqCriterion.getDescription() != null)
      match.setName(eqCriterion.getDescription());
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

  private Match convertColumns(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {
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
    String property;
    if (columnPath.contains(" ")) {
      property = columnPath.substring(columnPath.lastIndexOf(" ") + 1);
      columnPath = columnPath.substring(0, columnPath.lastIndexOf(" "));
    } else {
      property = columnPath;
      columnPath = "";
    }
    String fullPath = (tablePath + " " + columnPath).trim();
    Match match = pathMatchMap.get(fullPath);
    if (match == null) {
      match = new Match();
      matches.add(match);
      pathMatchMap.put(fullPath, match);
      if (tablePath.contains(" ")) {
        match.addPath(new IriLD().setIri(tablePath.split(" ")[0]));
        match.setTypeOf(new Node().setIri(tablePath.split(" ")[1]));
      }
      if (columnPath.contains(" ")) {
        String[] paths = columnPath.split(" ");
        for (int i = 0; i < paths.length; i = i + 2) {
          IriLD pathIri = new IriLD();
          String path = paths[i];
          if (path.startsWith("^")) {
            pathIri.setInverse(true);
            path = path.substring(1);
          }
          pathIri.setIri(path);
          match.addPath(pathIri);
          match.setTypeOf(new Node().setIri(paths[i + 1]));
        }
      }
    }
    Where where = new Where();
    match.addWhere(where);
    where.setIri(property);
    setProperty(cv, where);
    if (match.getWhere().size() > 1)
      match.setBoolWhere(Bool.and);

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
        String vsetName;
        setCounter++;
        vsetName = "Library set " + setCounter;
        valueLabel = valueLabel + (valueLabel.isEmpty() ? "" : ", ") + vsetName;
        Node iri = new Node().setIri(GUID + vset);
        iri.setMemberOf(true);
        if (vsetName != null)
          iri.setName(vsetName);
        else {
          setCounter++;
          iri.setName("Library set " + setCounter);
        }
        if (!notIn)
          pv.addIs(iri);
        else {
          iri.setExclude(true);
          pv.addIs(iri);
        }
        pv.setValueLabel(valueLabel);
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
    if (target == null)
      throw new EQDException("unknown map : " + eqdPath);
    if (target.equals(""))
      return "";
    String[] paths = ((String) target).split(" ");
    for (int i = 0; i < paths.length; i++) {
      String path = paths[i];
      String inverse = "";
      if (path.startsWith("^")) {
        inverse = "^";
        path = path.substring(1);
      }
      path = inverse + (path.contains("rdfs") ? RDFS.NAMESPACE + path.split(":")[1] : IM.NAMESPACE + path);
      paths[i] = path;
    }
    return String.join(" ", paths);
  }


  private Match convertRestrictionCriterion(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {
    Match restricted = convertColumns(eqCriterion);
    setRestriction(eqCriterion, restricted);
    if (eqCriterion.getFilterAttribute().getRestriction().getTestAttribute() != null) {
      Match testMatch = restrictionTest(eqCriterion);
      if (testMatch != null) {
        testMatch.setPath(null);
        restricted.setThen(testMatch);
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
    String linkColumn = eqCriterion.getFilterAttribute().getRestriction()
      .getColumnOrder().getColumns().get(0).getColumn().get(0);
    String orderBy = getPath(eqCriterion.getTable() + "/" + linkColumn);
    restricted.orderBy(o -> o
      .setProperty(new OrderDirection()
        .setIri(orderBy)
        .setDirection(direction))
      .setLimit(1));
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
    relationProperty
      .setIri(child);
    if (eqRel.getRangeValue() != null) {
      EQDOCRangeValue eqRange = eqRel.getRangeValue();
      if (eqRange.getRangeFrom() != null && eqRange.getRangeTo() != null) {
        Range range = new Range();
        relationProperty.setRange(range);
        Value from = new Value();
        range.setFrom(from);
        from.setOperator((Operator) vocabMap.get(eqRange.getRangeFrom().getOperator()))
          .setValue(eqRange.getRangeFrom().getValue().getValue());
        if (eqRange.getRangeFrom().getValue().getUnit() != null) {
          setUnitsOrArgument(relationProperty, eqRange.getRangeFrom().getValue().getUnit().value());
        }
        Value to = new Value();
        range.setTo(to);
        to.setOperator((Operator) vocabMap.get(eqRange.getRangeTo().getOperator()))
          .setValue(eqRange.getRangeTo().getValue().getValue());
        if (eqRange.getRangeTo().getValue().getUnit() != null) {
          setUnitsOrArgument(to, eqRange.getRangeTo().getValue().getUnit().value());
        }
      } else if (eqRel.getRangeValue().getRangeFrom() != null) {
        relationProperty
          .setOperator((Operator) vocabMap.get(eqRange.getRangeFrom().getOperator()))
          .setValue(eqRange.getRangeFrom().getValue().getValue());
        if (eqRange.getRangeFrom().getValue().getUnit() != null) {
          setUnitsOrArgument(relationProperty, eqRange.getRangeFrom().getValue().getUnit().value());
        }
      } else {
        relationProperty
          .setOperator((Operator) vocabMap.get(eqRange.getRangeTo().getOperator()))
          .setValue(eqRange.getRangeTo().getValue().getValue());
        if (eqRange.getRangeTo().getValue().getUnit() != null) {
          setUnitsOrArgument(relationProperty, eqRange.getRangeTo().getValue().getUnit().value());
        }
      }
    } else {
      relationProperty.setOperator(Operator.eq);
    }
    relationProperty.relativeTo(r -> r.setNodeRef(nodeRef).setIri(parent));
    if (linkMatch.getMatch() == null)
      linkMatch.addWhere(relationProperty);
    else
      linkMatch.getMatch().get(0).addWhere(relationProperty);
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
      } else
        setCompareTo(pv, rTo);
    }
    if (rv.getRelativeTo() != null) {
      if (rv.getRelativeTo().equals("BASELINE")) {
        pv.setRelativeTo(new PropertyRef().setParameter("$baselineDate"));
      }
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
    if (rFrom.getOperator() != null)
      comp = (Operator) vocabMap.get(rFrom.getOperator());
    else
      comp = Operator.eq;
    String value = null;
    String units = null;
    VocRelation relation = null;
    if (rFrom.getValue() != null) {
      value = rFrom.getValue().getValue();
      if (rFrom.getValue().getUnit() != null)
        units = rFrom.getValue().getUnit().value();

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
    if (units != null)
      setUnitsOrArgument(where, units);
  }

  private void setUnitsOrArgument(Assignable assignable, String units) throws EQDException {
    switch (units) {
      case "YEAR":
        assignable.setIntervalUnit(TTIriRef.iri(IM.NAMESPACE + "years"));
        break;
      case "MONTH":
        assignable.setIntervalUnit(TTIriRef.iri(IM.NAMESPACE + "months"));
        break;
      case "DAY":
        assignable.setIntervalUnit(TTIriRef.iri(IM.NAMESPACE + "days"));
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
    if (units != null)
      setUnitsOrArgument(where, units);
  }


  private void setCompareTo(Where pv, EQDOCRangeTo rTo) throws EQDException {
    Operator comp;
    if (rTo.getOperator() != null)
      comp = (Operator) vocabMap.get(rTo.getOperator());
    else
      comp = Operator.eq;
    String value = null;
    String units = null;
    VocRelation relation = null;
    if (rTo.getValue() != null) {
      value = rTo.getValue().getValue();
      if (rTo.getValue().getUnit() != null)
        units = rTo.getValue().getUnit().value();
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
    if (rFrom.getOperator() != null)
      comp = (Operator) vocabMap.get(rFrom.getOperator());
    else
      comp = Operator.eq;
    String value = rFrom.getValue().getValue();
    String units = null;
    if (rFrom.getValue().getUnit() != null)
      units = rFrom.getValue().getUnit().value();
    VocRelation relation = VocRelation.ABSOLUTE;
    if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
      relation = VocRelation.RELATIVE;
    }
    setCompare(where, fromValue, comp, value, units, relation);
    Value toValue = new Value();
    range.setTo(toValue);
    if (rFrom.getOperator() != null)
      comp = (Operator) vocabMap.get(rTo.getOperator());
    else
      comp = Operator.eq;
    value = rTo.getValue().getValue();
    units = null;
    if (rTo.getValue().getUnit() != null)
      units = rTo.getValue().getUnit().value();
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
      } else
        LOG.error("Missing exception sets {} {}", ev.getValue(), ev.getDisplayName());
    }

    return valueSet;
  }

  private TTIriRef getClusterSet(EQDOCValueSet vs) throws IOException {
    if (vs.getCodeSystem() == VocCodeSystemEx.SNOMED_CONCEPT) {
      if (vs.getDescription() != null) {
        if (vs.getClusterCode().contains("FlattenedCodeList")) {
          return importMaps.getReferenceFromCoreTerm(vs.getDescription());
        }
      }
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
      pv.addIs(new Node().setIri(cluster.getIri()).setName(cluster.getName())
        .setMemberOf(true));
      return;
    }
    Set<Node> setContent = new HashSet<>();
    for (EQDOCValueSetValue ev : vs.getValues()) {
      Set<Node> setMembers = processEQDOCValueSet(scheme, ev);
      if (!setMembers.isEmpty()) {
        setContent.addAll(setMembers);
      }
    }
    if (setContent.size() > 3) {
      TTIriRef valueSet = iri(createValueSet(vs, setContent).getIri());
      pv.addIs(new Node().setIri(valueSet.getIri()).setName(valueSet.getName())
        .setMemberOf(true));
    } else {
      String name = "";
      String exclusions = "";
      for (Node node : setContent) {
        if (node.isExclude())
          exclusions = " (exclusions)";
        pv.addIs(node);
        if (node.getName() != null)
          name = name.equals("") ? getShortName(node.getName(), null) : name + ", " + getShortName(node.getName(), null);

      }
      if (vs.getDescription() != null)
        name = vs.getDescription();
      pv.setValueLabel(name + exclusions);
    }
  }

  private TTEntity createValueSet(EQDOCValueSet vs, Set<Node> setContent) {
    String name = vs.getDescription();
    if (name == null)
      name = "Set used in " + activeReportName;
    TTEntity entity = new TTEntity()
      .setIri(GUID + vs.getId())
      .setName(name)
      .addType(iri(IM.CONCEPT_SET));
    for (Node node : setContent) {
      TTNode instance = new TTNode();
      if (!node.isExclude()) {
        instance.set(iri(IM.INCLUDE), node.getIri());
      } else
        instance.set(iri(IM.EXCLUDE), iri(node.getIri()));
      if (node.isAncestorsOf())
        instance.set(iri(IM.ANCESTORS_OF), TTLiteral.literal(true));
      if (node.isDescendantsOf())
        instance.set(iri(IM.DESCENDANTS_OF), TTLiteral.literal(true));
      if (node.isDescendantsOrSelfOf())
        instance.set(iri(IM.DESCENDANTS_OR_SELF_OF), TTLiteral.literal(true));
      entity.addObject(iri(IM.INSTANCE_OF), instance);
    }
    document.addEntity(entity);
    return entity;
  }

  private Set<Node> processEQDOCValueSet(VocCodeSystemEx scheme, EQDOCValueSetValue ev) throws IOException {
    Set<Node> setMembers = new HashSet<>();
    Set<Node> concepts = getValueConcepts(scheme, ev);
    if (concepts != null) {
      for (Node iri : concepts) {
        Node conRef = new Node().setIri(iri.getIri()).setName(iri.getName());
        if (ev.isIncludeChildren())
          conRef.setDescendantsOrSelfOf(true);
        setMembers.add(conRef);
      }
    } else
      LOG.error("Missing {} {}", ev.getValue(), ev.getDisplayName());
    if (!ev.getException().isEmpty()) {
      for (EQDOCException exc : ev.getException()) {
        for (EQDOCExceptionValue val : exc.getValues()) {
          Set<Node> exceptionValue = getValueConcepts(scheme, val);
          if (exceptionValue != null) {
            for (Node iri : exceptionValue) {
              Node conRef = new Node().setIri(iri.getIri()).setName(iri.getName());
              if (val.isIncludeChildren())
                conRef.setDescendantsOrSelfOf(true);
              conRef.setExclude(true);
              setMembers.add(conRef);
            }
          }
        }
      }
    }
    return setMembers;
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


  private Set<Node> getValueConcepts(VocCodeSystemEx scheme, EQDOCExceptionValue ev) throws IOException {
    return getValueConcepts(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
  }


  private Set<Node> getValueConcepts(VocCodeSystemEx scheme, EQDOCValueSetValue ev) throws IOException {
    return getValueConcepts(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
  }

  private Set<Node> getValueConcepts(VocCodeSystemEx scheme, String originalCode,
                                     String originalTerm, String legacyCode) throws IOException {
    if (scheme == VocCodeSystemEx.EMISINTERNAL) {
      String key = sourceContext + "/EMISINTERNAL/" + originalCode;
      Object mapValue = dataMap.get(key);
      if (mapValue != null) {
        return getValueIriResult(mapValue);
      } else {
        try {
          // Try to parse the string as a UUID
          UUID uuid = UUID.fromString(originalCode);
          Node iri = new Node().setIri("urn:uuid:" + key).setName(originalTerm);
        } catch (IllegalArgumentException e) {
          // If parsing fails, it's not a valid UUID
          throw new IllegalArgumentException("unmapped emis internal code : " + key);
        }
      }
    } else if (scheme != VocCodeSystemEx.SNOMED_CONCEPT && !scheme.value().contains("SCT")) {
      throw new IllegalArgumentException("code scheme not recognised : " + scheme.value());
    }
    List<String> schemes = new ArrayList<>();
    schemes.add(SNOMED.NAMESPACE);
    schemes.add(GRAPH.EMIS);
    Set<TTIriRef> snomed = valueMap.get(originalCode);
    if (snomed == null) {
      snomed = setValueSnomedChecks(originalCode, originalTerm, legacyCode, schemes);
      if (snomed != null)
        valueMap.put(originalCode, snomed);
    }
    if (snomed != null) {
      for (TTIriRef sn : snomed) {
        if (sn.getIri() == null) {
          System.out.println("null snomed");
        }
      }
      return snomed.stream().map(e -> new Node().setIri(e.getIri()).setName(e.getName())).collect(Collectors.toSet());
    } else
      return Collections.emptySet();
  }

  private Set<Node> getValueIriResult(Object mapValue) throws IOException {
    Node iri = new Node().setIri(mapValue.toString());
    String name = importMaps.getCoreName(iri.getIri());
    if (name != null)
      iri.setName(name);
    Set<Node> result = new HashSet<>();
    result.add(iri);
    return result;
  }

  private Set<TTIriRef> setValueSnomedChecks(String originalCode, String originalTerm, String legacyCode, List<String> schemes) {
    Set<TTIriRef> snomed;
    snomed = getCoreFromCode(originalCode, schemes);
    if (snomed == null && legacyCode != null)
      snomed = getCoreFromCode(legacyCode, schemes);
    if (snomed == null && originalTerm != null)
      snomed = getCoreFromLegacyTerm(originalTerm);
    if (snomed == null)
      snomed = getCoreFromCodeId(originalCode);
    if (snomed == null)
      snomed = getLegacyFromTermCode(originalCode);
    return snomed;
  }


  private Set<TTIriRef> getCoreFromCodeId(String originalCode) {

    try {
      return importMaps.getCoreFromCodeId(originalCode, GRAPH.EMIS);
    } catch (Exception e) {
      LOG.error("unable to retrieve iri from term code {}", e.getMessage(), e);
      return Collections.emptySet();
    }
  }


  private Set<TTIriRef> getLegacyFromTermCode(String originalCode) {
    try {
      return importMaps.getLegacyFromTermCode(originalCode, GRAPH.EMIS);
    } catch (Exception e) {
      LOG.error("unable to retrieve iri match term code {}", e.getMessage());
      return Collections.emptySet();
    }
  }


  private Set<TTIriRef> getCoreFromLegacyTerm(String originalTerm) {
    try {
      if (originalTerm.contains("s disease of lymph nodes of head, face AND/OR neck"))
        LOG.info("!!");

      return importMaps.getCoreFromLegacyTerm(originalTerm, GRAPH.EMIS);
    } catch (Exception e) {
      LOG.error("unable to retrieve iri match term {}", e.getMessage());
      return Collections.emptySet();
    }
  }

  private Set<TTIriRef> getCoreFromCode(String originalCode, List<String> schemes) {
    return importMaps.getCoreFromCode(originalCode, schemes);
  }

}
