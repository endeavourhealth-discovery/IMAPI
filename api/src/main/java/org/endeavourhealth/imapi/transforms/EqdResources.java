package org.endeavourhealth.imapi.transforms;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.endeavourhealth.imapi.logic.exporters.ImportMaps;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.iml.ConceptSet;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
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
  public static final String URN_UUID = "urn:uuid:";
  private static final Logger LOG = LoggerFactory.getLogger(EqdResources.class);
  private final ImportMaps importMaps = new ImportMaps();
  private final Map<Object, Object> vocabMap = new HashMap<>();
  private final Map<String, Set<TTIriRef>> valueMap = new HashMap<>();
  @Getter
  Map<String, String> reportNames = new HashMap<>();
  private Properties dataMap;
  private Properties labels;
  private String activeReport;
  @Getter
  private String activeReportName;
  @Getter
  private ModelDocument document;
  private int counter = 0;
  private int setCounter = 0;


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

  public EqdResources setDocument(ModelDocument document) {
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

  public EqdResources setLabels(Properties labels) {
    this.labels = labels;
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

  public TTIriRef getIri(String token) throws IOException {
    if (token.equals("label"))
      return iri(RDFS.LABEL).setName("label");
    else {
      if (!token.contains(":")) {
        TTIriRef iri = TTIriRef.iri(IM.NAMESPACE + token);
        iri.setName(importMaps.getCoreName(IM.NAMESPACE + token));
        return iri;
      } else {
        TTIriRef iri = TTIriRef.iri(token);
        iri.setName(importMaps.getCoreName(token));
        return iri;
      }
    }
  }


  public Match convertCriteria(EQDOCCriteria eqCriteria) throws IOException, QueryException, EQDException {
    if ((eqCriteria.getPopulationCriterion() != null)) {
      EQDOCSearchIdentifier search = eqCriteria.getPopulationCriterion();
      Match match = new Match();
      match
        .addInstanceOf(new Node().setIri(URN_UUID + search.getReportGuid()).setMemberOf(true))
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
      match.setIri(URN_UUID + eqCriterion.getId());
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
        match.addPath(new IriLD().setIri(IM.NAMESPACE + tablePath.split(" ")[0].replace("^", "")));
        match.setTypeOf(new Node().setIri(IM.NAMESPACE + tablePath.split(" ")[1]));
      }
      if (columnPath.contains(" ")) {
        String[] paths = columnPath.split(" ");
        for (int i = 0; i < paths.length; i = i + 2) {
          Where subProperty = new Where();
          match.addWhere(subProperty);
          subProperty.setIri(IM.NAMESPACE + columnPath.split(" ")[i]);
          subProperty.setMatch(new Match());
          match = subProperty.getMatch();
          match.setTypeOf(new Node().setIri(IM.NAMESPACE + columnPath.split(" ")[i + 1]));
        }
      }
    }
    Where where = new Where();
    match.addWhere(where);
    where.setIri(IM.NAMESPACE + property);
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


  private void setProperty(EQDOCColumnValue cv, Where pv) throws IOException {

    VocColumnValueInNotIn in = cv.getInNotIn();
    boolean notIn = (in == VocColumnValueInNotIn.NOTIN);
    if (!cv.getValueSet().isEmpty()) {
      setPropertyValueSetSetters(cv, pv, notIn);
    } else if (!CollectionUtils.isEmpty(cv.getLibraryItem())) {
      String valueLabel = "";
      for (String vset : cv.getLibraryItem()) {
        String vsetName;
        if (labels.get(vset) != null) {
          vsetName = (String) labels.get(vset);
        } else {
          setCounter++;
          vsetName = "Library set " + setCounter;
        }
        valueLabel = valueLabel + (valueLabel.isEmpty() ? "" : ", ") + vsetName;
        Node iri = new Node().setIri(URN_UUID + vset);
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

  private void setPropertyValueSetSetters(EQDOCColumnValue cv, Where pv, boolean notIn) throws IOException {
    for (EQDOCValueSet vs : cv.getValueSet()) {
      if (vs.getId() != null && labels.get(vs.getId()) != null) {
        pv.setValueLabel(labels.get(vs.getId()).toString());
      }
      if (vs.getAllValues() != null) {
        List<Node> values = getExceptionSet(vs.getAllValues());
        for (Node node : values) {
          node.setExclude(true);
        }
        pv.setIs(values);
      } else {
        if (!notIn) {
          pv.addIs(getInlineValues(vs));
        } else {
          pv.addIs(getInlineValues(vs).setExclude(true));
        }
      }
    }
  }


  public String getPath(String eqdPath) throws EQDException {
    Object target = dataMap.get(eqdPath);
    if (target == null)
      throw new EQDException("unknown map : " + eqdPath);
    return (String) target;
  }


  private Match convertRestrictionCriterion(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {
    Match restricted = convertColumns(eqCriterion);
    setRestriction(eqCriterion, restricted);
    if (eqCriterion.getFilterAttribute().getRestriction().getTestAttribute() != null) {
      Match testMatch = restrictionTest(eqCriterion);
      if (testMatch != null) {
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
      .setIri(child)
      .setOperator((Operator) vocabMap.get(eqRel.getRangeValue().getRangeFrom().getOperator()))
      .setValue(eqRel.getRangeValue().getRangeFrom().getValue().getValue())
      .setUnit(eqRel.getRangeValue().getRangeFrom().getValue().getUnit().value())
      .relativeTo(r -> r.setNodeRef(nodeRef).setIri(parent));
    if (linkMatch.getMatch() == null)
      linkMatch.addWhere(relationProperty);
    else
      linkMatch.getMatch().get(0).addWhere(relationProperty);
    return linked;
  }

  private void setRangeValue(EQDOCRangeValue rv, Where pv) {

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
      setCompareTo(pv, rTo);
    }

  }

  private void setCompareFrom(Where where, EQDOCRangeFrom rFrom) {
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
    setCompare(where, comp, value, units, relation);
  }

  private void setCompare(Where where, Value pv, Operator comp, String value, String units, VocRelation relation) {
    if (relation == VocRelation.RELATIVE) {
      where.setRelativeTo(new PropertyRef().setParameter("$referenceDate"));
    }

    pv.setOperator(comp);
    pv.setValue(value);
    if (units != null)
      where.setUnit(units);
  }

  private void setCompare(Where where, Operator comp, String value, String units, VocRelation relation) {
    if (relation == VocRelation.RELATIVE) {
      where.setRelativeTo(new PropertyRef().setParameter("$referenceDate"));
    }

    where.setOperator(comp);
    where.setValue(value);
    if (units != null)
      where.setUnit(units);
  }


  private void setCompareTo(Where pv, EQDOCRangeTo rTo) {
    Operator comp;
    if (rTo.getOperator() != null)
      comp = (Operator) vocabMap.get(rTo.getOperator());
    else
      comp = Operator.eq;
    String value = rTo.getValue().getValue();
    String units = null;
    if (rTo.getValue().getUnit() != null)
      units = rTo.getValue().getUnit().value();
    VocRelation relation = VocRelation.ABSOLUTE;
    if (rTo.getValue().getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
      relation = VocRelation.RELATIVE;
    }
    setCompare(pv, comp, value, units, relation);
  }


  private void setRangeCompare(Where where, EQDOCRangeFrom rFrom, EQDOCRangeTo rTo) {
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
      Set<Node> values = getValue(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
      if (values != null) {
        valueSet.addAll(values.stream().map(v -> v.setExclude(true)).toList());
      } else
        LOG.error("Missing exception sets {} {}", ev.getValue(), ev.getDisplayName());
    }

    return valueSet;
  }


  private Node getInlineValues(EQDOCValueSet vs) throws IOException {
    Set<Node> setContent = new HashSet<>();
    VocCodeSystemEx scheme = vs.getCodeSystem();
    String exclusions = "";
    if (vs.getClusterCode() != null && !vs.getClusterCode().isEmpty()) {
      return new Node().setParameter(vs.getClusterCode().get(0));
    }
    for (EQDOCValueSetValue ev : vs.getValues()) {
      boolean evExclusion = processEQDOCValueSet(scheme, ev, setContent);
      if (evExclusion) exclusions = " (with exclusions)";
    }
    Query query = new Query();
    Match match = new Match();
    match.setBoolMatch(Bool.or);
    query.addMatch(match);
    String name = "";
    int i = 0;
    for (Node node : setContent) {
      i++;
      Match member = new Match();
      match.addMatch(member);
      member.addInstanceOf(node);
      if (node.getName() != null) {
        if (i == 3)
          name = name + " + more...";
        else if (i == 1)
          name = getShortName(node.getName(), null);
        else if (i == 2) {
          name = getShortName(node.getName(), name);
        }
      }
      node.setName(name);
    }
    if (vs.getDescription() != null)
      name = vs.getDescription();
    ConceptSet set = new ConceptSet();
    set.setIri(URN_UUID + vs.getId());
    set.setName(name);
    if (!memberOnly(query))
      set.setDefinition(query);
    else
      setMemberOnlySet(set, query);
    set.addUsedIn(TTIriRef.iri(URN_UUID + activeReport));
    document.addConceptSet(set);
    return new Node().setIri(set.getIri()).setName(name + exclusions);
  }

  private boolean processEQDOCValueSet(VocCodeSystemEx scheme, EQDOCValueSetValue ev, Set<Node> setContent) throws IOException {
    boolean hasExclusions = false;
    Set<Node> concepts = getValue(scheme, ev);
    if (concepts != null) {
      for (Node iri : concepts) {
        Node conRef = new Node().setIri(iri.getIri()).setName(iri.getName());
        if (ev.isIncludeChildren())
          conRef.setDescendantsOrSelfOf(true);
        setContent.add(conRef);
      }
    } else
      LOG.error("Missing {} {}", ev.getValue(), ev.getDisplayName());
    if (!ev.getException().isEmpty()) {
      hasExclusions = true;
      for (EQDOCException exc : ev.getException()) {
        for (EQDOCExceptionValue val : exc.getValues()) {
          Set<Node> exceptionValue = getValue(scheme, val);
          if (exceptionValue != null) {
            for (Node iri : exceptionValue) {
              Node conRef = new Node().setIri(iri.getIri()).setName(iri.getName());
              if (val.isIncludeChildren())
                conRef.setDescendantsOrSelfOf(true);
              conRef.setExclude(true);
              setContent.add(conRef);
            }
          }
        }
      }
    }
    return hasExclusions;
  }

  private void setMemberOnlySet(ConceptSet set, Match match) {
    if (match.getInstanceOf() != null) {
      for (Node node : match.getInstanceOf()) {
        set.addHasMember(iri(node.getIri()));
      }
      if (match.getMatch() != null) {
        for (Match subMatch : match.getMatch()) {
          setMemberOnlySet(set, subMatch);
        }
      }
    }

  }

  private boolean memberOnly(Match match) {
    if (match.getInstanceOf() != null) {
      for (Node node : match.getInstanceOf()) {
        if (node.isDescendantsOrSelfOf())
          return false;
      }
    }
    if (match.getMatch() != null) {
      for (Match subMatch : match.getMatch()) {
        if (!memberOnly(subMatch))
          return false;
      }
    }
    return true;
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


  private Set<Node> getValue(VocCodeSystemEx scheme, EQDOCExceptionValue ev) throws IOException {
    return getValue(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
  }


  private Set<Node> getValue(VocCodeSystemEx scheme, EQDOCValueSetValue ev) throws IOException {
    return getValue(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
  }

  private Set<Node> getValue(VocCodeSystemEx scheme, String originalCode,
                             String originalTerm, String legacyCode) throws IOException {
    if (scheme == VocCodeSystemEx.EMISINTERNAL) {
      String key = "EMISINTERNAL/" + originalCode;
      Object mapValue = dataMap.get(key);
      if (mapValue != null) {
        return getValueIriResult(mapValue);
      } else
        throw new IllegalArgumentException("unmapped emis internal code : " + key);
    } else if (scheme == VocCodeSystemEx.SNOMED_CONCEPT || scheme.value().contains("SCT")) {
      List<String> schemes = new ArrayList<>();
      schemes.add(SNOMED.NAMESPACE);
      schemes.add(GRAPH.EMIS);
      Set<TTIriRef> snomed = valueMap.get(originalCode);
      if (snomed == null) {
        snomed = setValueSnomedChecks(originalCode, originalTerm, legacyCode, schemes);
        if (snomed != null)
          valueMap.put(originalCode, snomed);
      }
      if (snomed != null)
        return snomed.stream().map(e -> new Node().setIri(e.getIri()).setName(e.getName())).collect(Collectors.toSet());
      else return Collections.emptySet();
    } else
      throw new IllegalArgumentException("code scheme not recognised : " + scheme.value());

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
