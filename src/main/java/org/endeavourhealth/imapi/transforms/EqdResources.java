package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.endeavourhealth.imapi.logic.exporters.ImportMaps;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdResources {
  private static final Logger log = LoggerFactory.getLogger(EqdResources.class);
  private final ImportMaps importMaps = new ImportMaps();
  private final Map<Object, Object> vocabMap = new HashMap<>();
  private final Map<String, Set<Node>> valueMap = new HashMap<>();
  private final Properties dataMap;
  private final Set<String> acronyms = new HashSet<>();
  @Getter
  Map<String, String> reportNames = new HashMap<>();
  @Setter
  @Getter
  TTEntity queryEntity;
  @Getter
  private NAMESPACE namespace;
  @Setter
  private String activeReport;
  @Getter
  @Setter
  private String activeReportName;
  @Getter
  private String activeFolder;
  @Getter
  private TTDocument document;
  @Getter
  @Setter
  private TTIriRef columnGroup;
  private int setCounter = 0;
  private String sourceContext;
  @Getter
  @Setter
  private QueryType queryType;
  private int matchCounter = 0;
  @Setter
  @Getter
  private int rule = 0;
  @Setter
  @Getter
  private int subRule = 0;
  private Map<String, Match> nodeRefMap;

  public EqdResources(TTDocument document, Properties dataMap, NAMESPACE namespace) {
    this.dataMap = dataMap;
    this.document = document;
    this.namespace = namespace;
    this.setVocabMaps();
  }

  private static boolean isValidUUID(String str) {
    try {
      UUID uuid = UUID.fromString(str);
      return str.equals(uuid.toString());
    } catch (IllegalArgumentException var2) {
      return false;
    }
  }

  public EqdResources setMatchCounter(int matchCounter) {
    this.matchCounter = matchCounter;
    return this;
  }

  public void incrementRule() {
    ++this.rule;
  }

  public void incrementSubRule() {
    ++this.subRule;
  }

  public EqdResources setNamespace(NAMESPACE namespace) {
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

  public EqdResources setDocument(TTDocument document) {
    this.document = document;
    return this;
  }

  private void setVocabMaps() {
    this.vocabMap.put(VocRangeFromOperator.GTEQ, Operator.gte);
    this.vocabMap.put(VocRangeFromOperator.GT, Operator.gt);
    this.vocabMap.put(VocRangeToOperator.LT, Operator.lt);
    this.vocabMap.put(VocRangeToOperator.LTEQ, Operator.lte);
    this.vocabMap.put(VocOrderDirection.DESC, "DESC");
    this.vocabMap.put(VocOrderDirection.ASC, "ASC");
  }

  public Match convertGroup(EQDOCCriteriaGroup eqGroup) throws IOException, QueryException, EQDException {
    this.incrementRule();
    if (eqGroup.getDefinition().getParentPopulationGuid() != null) {
      String parent = eqGroup.getDefinition().getParentPopulationGuid();
      Match match = new Match();
      if (EqdToIMQ.versionMap.containsKey(parent)) {
        parent = EqdToIMQ.versionMap.get(parent);
      }
      String finalParentId = this.namespace + parent;
      if (EqdToIMQ.gmsPatients.contains(parent)) {
        finalParentId = NAMESPACE.IM + "Q_RegisteredGMS";
      }
      match.addIs(Node.iri(finalParentId).setName(this.reportNames.get(parent)));
      queryEntity.addObject(iri(IM.DEPENDENT_ON), iri(finalParentId));
      return match;
    } else {
      List<EQDOCCriteria> groupCriteria = eqGroup.getDefinition().getCriteria();
      Match groupMatch= this.getMatchFromGroup(groupCriteria, eqGroup.getDefinition().getMemberOperator());
      if (eqGroup.getDefinition().getMemberOperator() == VocMemberOperator.SCORE) {
        EQDOCScore eqScore=eqGroup.getDefinition().getScore();
        EQDOCRangeValue range=eqScore.getRangeValue();
        Having having=new Having();
        groupMatch.setHaving(having);
        having.setFunction(Aggregate.SUM);
        having.setIdentifier("score");
        if (range.getRangeFrom()!=null &&range.getRangeTo()==null){
           having.setOperator((Operator) this.vocabMap.get(range.getRangeFrom().getOperator()));
           having.setValue(range.getRangeFrom().getValue().getValue());
        } else if (range.getRangeTo()!=null && range.getRangeFrom()==null){
          having.setOperator((Operator) this.vocabMap.get(range.getRangeTo().getOperator()));
          having.setValue(range.getRangeTo().getValue().getValue());
        } else {
          having.setRange(new Range());
          having.getRange().setFrom(new Value().setOperator((Operator) this.vocabMap.get(range.getRangeFrom().getOperator())).setValue(range.getRangeFrom().getValue().getValue()));
          having.getRange().setTo(new Value().setOperator((Operator) this.vocabMap.get(range.getRangeTo().getOperator())).setValue(range.getRangeTo().getValue().getValue()));
        }
      }
      return groupMatch;
    }
  }

  private Match getMatchFromGroup(List<EQDOCCriteria> groupCriteria, VocMemberOperator memberOp) throws QueryException, EQDException, IOException {
    this.subRule = 0;
    if (groupCriteria.size() <= 1) {
      EQDOCCriteria eqCriteria = groupCriteria.getFirst();
      if (isNegatedCriteria(eqCriteria)) {
        Match match = convertCriteria(eqCriteria);
        match.setNotExists(true);
        return match;
      } else return convertCriteria(groupCriteria.getFirst());
    } else {
      Match boolMatch = new Match();
      if (memberOp == null) {
        memberOp = VocMemberOperator.OR;
      }


      for (EQDOCCriteria eqCriteria : groupCriteria) {
        Match match = this.convertCriteria(eqCriteria);
        if (isNegatedCriteria(eqCriteria)) {
          match.setNotExists(true);
        }
        if (memberOp == VocMemberOperator.AND) {
          boolMatch.addAnd(match);
        } else boolMatch.addOr(match);
      }

      return boolMatch;
    }
  }

  private boolean isNegatedCriteria(EQDOCCriteria criteria) {
    if (criteria.getCriterion() != null) {
      return criteria.getCriterion().isNegation();
    } else return false;
  }

  public Match convertCriteria(EQDOCCriteria eqCriteria) throws IOException, QueryException, EQDException {
    acronyms.clear();
    String score=null;
    if (eqCriteria.getScoreWeightage()!=null){
      score=eqCriteria.getScoreWeightage().toString();
    }
    if (eqCriteria.getPopulationCriterion() != null) {
      return this.getPopulationQuery(eqCriteria);
    } else {
      this.incrementSubRule();
      if (eqCriteria.getCriterion() != null) {
        Match match = this.convertCriterion(eqCriteria.getCriterion());
        if (eqCriteria.getCriterion().getDescription() != null)
          match.setDescription(eqCriteria.getCriterion().getDescription());
        if (score!=null){
         addScore(match,score);
        }
        return match;
      } else {
        Map<String, EQDOCCriterion> libraryItems = EqdToIMQ.getLibraryItems();
        String libraryId = eqCriteria.getLibraryItem().getLibraryItem();
        if (!libraryItems.containsKey(libraryId)) {
          System.err.println("Library item not found: " + libraryId);
          Match libraryMatch = new Match();
          libraryMatch.addIs(new Node().setIri(this.namespace + libraryId));
          if (score!=null)
            addScore(libraryMatch,score);

          return libraryMatch;
        } else {
          System.out.println("Library item found : " + libraryId);
          Match match=this.convertCriterion(libraryItems.get(libraryId));
          if (score!=null)
            addScore(match,score);
          return match;
        }
      }
    }
  }

  private void addScore(Match match, String score) {
    Case scoreCase= new Case();
    When scoreWhen= new When();
    scoreCase.addWhen(scoreWhen);
    scoreWhen.setExists(true);
    scoreWhen.setThen(score);
    match.addReturn(new Return().setCase(scoreCase).setAs("score"));
    scoreCase.setElse("0");
  }

  public Match getPopulationQuery(EQDOCCriteria eqCriteria) {
    String score=null;
    if (eqCriteria.getScoreWeightage()!=null){
      score=eqCriteria.getScoreWeightage().toString();
    }
    EQDOCSearchIdentifier search = eqCriteria.getPopulationCriterion();
    String searchId = search.getReportGuid();
    if (search.getVersionIndependentGuid() != null) searchId = search.getVersionIndependentGuid();
    if (EqdToIMQ.versionMap.containsKey(searchId)) {
      searchId = EqdToIMQ.versionMap.get(searchId);
    }
    Match match = new Match();
    match.addIs(new Node().setIri(namespace + searchId)
      .setIsCohort(true).setName((String) this.reportNames.get(search.getReportGuid())));
    String finalSearchId = namespace + searchId;
    if (EqdToIMQ.gmsPatients.contains(searchId)) {
      finalSearchId = NAMESPACE.IM + "Q_RegisteredGMS";
    }

    queryEntity.addObject(iri(IM.DEPENDENT_ON), iri(finalSearchId));
    if (score!=null){
      addScore(match,score);
    }
    return match;
  }


  private Match convertCriterion(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {
    nodeRefMap = new HashMap<>();
    Match baseMatch = null;
    Match standardMatch = null;
    Match testMatch = null;
    Match linkedMatch = null;
    Match lastMatch = null;
    EQDOCFilterAttribute filter = eqCriterion.getFilterAttribute();
    boolean hasLinked = eqCriterion.getLinkedCriterion() != null;
    boolean hasStandard = (!filter.getColumnValue().isEmpty() || filter.getRestriction() != null);
    if (!eqCriterion.getBaseCriteriaGroup().isEmpty()) {
      baseMatch = this.convertBaseCriteriaGroups(eqCriterion);
      lastMatch = baseMatch;
    }

    if (hasStandard) {
      standardMatch = this.convertStandardCriterion(eqCriterion, baseMatch);
      if (baseMatch != null) {
        if (standardMatch.getWhere() != null) {
          if (baseMatch.getWhere() != null) {
            throw new EQDException("Cannot combine base criteria and standard criteria where clauses");
          }
          baseMatch.setWhere(standardMatch.getWhere());
        }
        if (standardMatch.getOrderBy() != null) {
          baseMatch.setOrderBy(standardMatch.getOrderBy());
        }
        standardMatch = null;
      } else lastMatch = standardMatch;
      if (eqCriterion.getFilterAttribute().getRestriction() != null && eqCriterion.getFilterAttribute().getRestriction().getTestAttribute() != null) {
        testMatch = this.convertTestCriterion(eqCriterion);
        lastMatch.setThen(testMatch.getWhere());
      }
    }
    if (lastMatch == null) {
      throw new EQDException("No matches found for criterion");
    }

    if (hasLinked) {
      setKeepMatchNode(lastMatch, eqCriterion.getLinkedCriterion().getRelationship().getParentColumn());
      linkedMatch = this.convertLinkedCriterion(eqCriterion, lastMatch);
      setMatchNode(linkedMatch);
    }
    List<Match> steps = new ArrayList<>();

    if (baseMatch != null) {
      steps.add(baseMatch);
    }
    if (standardMatch != null) {
      steps.add(standardMatch);
    }
    if (linkedMatch != null) {
      steps.add(linkedMatch);
    }

    if (steps.size() > 1) {
      Match outerMatch = new Match();
      outerMatch.setAnd(steps);
      return outerMatch;
    } else return lastMatch;
  }

  private Match convertBaseCriteriaGroups(EQDOCCriterion eqCriterion) throws QueryException, EQDException, IOException {
    Match baseMatch;
    if (eqCriterion.getBaseCriteriaGroup().size() > 1) {
      baseMatch = new Match();
      for (EQDOCBaseCriteriaGroup baseGroup : eqCriterion.getBaseCriteriaGroup()) {
        Match subQuery = this.convertBaseCriteriaGroup(baseGroup);
        baseMatch.addOr(subQuery);
      }
    } else {
      baseMatch = this.convertBaseCriteriaGroup(eqCriterion.getBaseCriteriaGroup().getFirst());
    }
    return baseMatch;
  }


  private Match convertBaseCriteriaGroup(EQDOCBaseCriteriaGroup baseGroup) throws QueryException, EQDException, IOException {
    return this.getMatchFromGroup(baseGroup.getDefinition().getCriteria(), baseGroup.getDefinition().getMemberOperator());
  }


  private Match convertStandardCriterion(EQDOCCriterion eqCriterion, Match matchToTest) throws IOException, EQDException {
    Match match = null;
    if (!eqCriterion.getFilterAttribute().getColumnValue().isEmpty()) {
      match = this.convertColumns(eqCriterion.getTable(), eqCriterion.getId(), eqCriterion.getFilterAttribute().getColumnValue(), matchToTest);
    }

    if (eqCriterion.getFilterAttribute().getRestriction() != null) {
      if (match == null) {
        match = new Match();
      }
      this.setRestriction(eqCriterion, match);
    }

    if (match == null) {
      throw new EQDException("No match found for standard criterion");
    }
    if (eqCriterion.isNegation()) match.setNotExists(true);
    return match;
  }

  private Match convertLinkedCriterion(EQDOCCriterion eqCriterion, Match parentMatch) throws IOException, QueryException, EQDException {
    EQDOCCriterion eqLinkedCriterion = eqCriterion.getLinkedCriterion().getCriterion();
    Match linkedMatch = this.convertCriterion(eqLinkedCriterion);
    if (eqLinkedCriterion.getDescription() != null) linkedMatch.setDescription(eqLinkedCriterion.getDescription());
    Where relationWhere = new Where();
    addMatchWhere(linkedMatch, relationWhere);
    EQDOCRelationship eqRelationship = eqCriterion.getLinkedCriterion().getRelationship();
    String table = eqLinkedCriterion.getTable();
    String child = this.getIMPath(table + "/" + eqRelationship.getChildColumn());
    ValueSource relationLeft = new ValueSource();
    relationLeft
      .setNodeRef(getNodeRef(linkedMatch))
      .setIri(child.substring(child.lastIndexOf(" ") + 1));

    String parentProperty = eqRelationship.getParentColumn();
    if (!parentProperty.contains("DATE") &&!parentProperty.contains("DOB"))
      throw new EQDException("No match found for linked criterion parent property");
    ValueSource relationRight = new ValueSource();
    if (eqRelationship.getParentColumn().contains("DATE")) {
      relationRight.setIri(NAMESPACE.IM + "effectiveDate");
      injectPropertyReturn(parentMatch, relationRight.getIri());
      relationRight.setNodeRef(getNode(parentMatch));
      relationRight.setNodeRef(getKeepAs(parentMatch));
      relationRight.setPropertyRef(relationRight.getIri().substring(relationRight.getIri().lastIndexOf("#") + 1));
    } else if (eqRelationship.getParentColumn().contains("VALUE")) {
      relationRight.setIri(NAMESPACE.IM + "value");
      injectPropertyReturn(parentMatch, relationRight.getIri());
      relationRight.setNodeRef(parentMatch.getNode());
      relationRight.setPropertyRef(relationRight.getIri().substring(relationRight.getIri().lastIndexOf("#") + 1));
      parentMatch.setNode(parentMatch.getNode() + "_VAL");
      relationRight.setNodeRef(parentMatch.getNode());
    } else if (eqRelationship.getParentColumn().contains("DOB")) {
        Path linkedMatchPath = new Path();
        linkedMatchPath.setIri(NAMESPACE.IM+"patient");
        linkedMatchPath.setNode("patient");
        linkedMatchPath.setTypeOf(NAMESPACE.IM+"Patient");
        linkedMatch.addPath(linkedMatchPath);
      relationRight.setNodeRef("patient").setIri(NAMESPACE.IM + "dateOfBirth");
    } else throw new EQDException("No match found for linked criterion");

    if (eqRelationship.getRangeValue() != null) {
      EQDOCRangeValue eqRange = eqRelationship.getRangeValue();
      if (eqRange.getRangeFrom() != null && eqRange.getRangeTo() != null) {
        Range range = new Range();
        relationWhere.setRange(range);
        String fromValue = eqRange.getRangeFrom().getValue().getValue();

        TTIriRef fromUnits = setQualifierGetunits(relationWhere, eqRange.getRangeFrom().getValue().getUnit());
        if (fromValue.equals("0")) {
          fromValue = null;
          fromUnits = null;
        }
        Operator fromOperator = ((Operator) this.vocabMap.get(eqRange.getRangeFrom().getOperator()));
        Value from = new Value();
        range.setFrom(from);
        from.setOperator(fromOperator);
        from.setValue(fromValue);
        buildCompare(from, fromUnits, relationLeft, relationRight);
        Value to = new Value();
        range.setTo(to);
        String toValue = eqRange.getRangeTo().getValue().getValue();
        Operator toOperator = ((Operator) this.vocabMap.get(eqRange.getRangeTo().getOperator()));
        TTIriRef toUnits = setQualifierGetunits(relationWhere, eqRange.getRangeTo().getValue().getUnit());
        if (toValue.equals("0")) {
          toValue = null;
          toUnits = null;
        }
        from.setOperator(toOperator);
        from.setValue(toValue);
        buildCompare(to, toUnits, relationLeft, relationRight);

      } else if (eqRelationship.getRangeValue().getRangeFrom() != null) {
        String fromValue = eqRange.getRangeFrom().getValue().getValue();
        TTIriRef fromUnits = setQualifierGetunits(relationWhere, eqRange.getRangeFrom().getValue().getUnit());
        Operator fromOperator = ((Operator) this.vocabMap.get(eqRange.getRangeFrom().getOperator()));
        if (fromValue.equals("0")) {
          fromValue = null;
          fromUnits = null;
        }
        relationWhere.setOperator(fromOperator);
        relationWhere.setValue(fromValue);
        buildCompare(relationWhere, fromUnits, relationLeft, relationRight);

      } else {
        String toValue = eqRange.getRangeTo().getValue().getValue();
        Operator toOperator = ((Operator) this.vocabMap.get(eqRange.getRangeTo().getOperator()));
        TTIriRef toUnits = setQualifierGetunits(relationWhere, eqRange.getRangeTo().getValue().getUnit());
        if (toValue.equals("0")) {
          toValue = null;
          toUnits = null;
        }
        relationWhere.setOperator(toOperator);
        relationWhere.setValue(toValue);
        buildCompare(relationWhere, toUnits, relationLeft, relationRight);
      }
    } else {
      relationWhere.setCompare(new Compare());
      relationWhere.getCompare().setLeft(relationLeft);
      relationWhere.getCompare().setRight(relationRight);
      relationWhere.setOperator(Operator.eq);
    }
    return linkedMatch;
  }

  private String getNode(Match parentMatch) throws EQDException {
    if (parentMatch.getNode() != null) return parentMatch.getNode();
    if (parentMatch.getAnd() != null) return getNode(parentMatch.getAnd().getLast());
    if (parentMatch.getOr() != null) return getNode(parentMatch.getOr().getLast());
    throw new EQDException("Could not find node for match");
  }

  private String getKeepAs(Match parentMatch) throws EQDException {
    if (parentMatch.getNode() != null) return parentMatch.getNode();
    if (parentMatch.getAnd() != null) return getKeepAs(parentMatch.getAnd().getLast());
    if (parentMatch.getOr() != null) return getKeepAs(parentMatch.getOr().getLast());
    throw new EQDException("Could not find keep as for match");
  }

  private TTIriRef setQualifierGetunits(Where where, VocValueUnit eqUnits) throws EQDException {
    if (eqUnits == null) return null;
    TTIriRef units = getIMUnits(eqUnits);
    if (units == null) return null;
    if (units.getIri().equals(IM.FISCAL_YEAR.toString())) {
      where.setQualifier(units);
      units = null;
    } else if (units.getIri().equals(IM.QUARTER.toString())) {
      where.setQualifier(units);
      units = null;
    }
    return units;
  }

  private void buildCompare(Assignable assignable, TTIriRef units,
                            ValueSource relationLeft,
                            ValueSource relationRight) {

    assignable.setCompare(new Compare());
    assignable.getCompare().setLeft(relationLeft);
    assignable.getCompare().setRight(relationRight);
    if (units != null)
      assignable.getCompare().setUnits(units);
  }


  private Match convertColumns(String table, String eqId, List<EQDOCColumnValue> columns, Match matchToTest) throws EQDException, IOException {
    int index = 0;
    Match match = new Match();
    match.setTypeOf(this.getIMPath(table));
    for (EQDOCColumnValue cv : columns) {
      ++index;
      this.convertColumn(table, eqId, cv, match);
    }
    if (match.getPath() != null) {
      match.setTypeOf(new Node().setIri(match.getPath().getFirst().getTypeOf().getIri()));
    }

    return match;
  }

  private Match convertTestColumns(String table, String eqId, List<EQDOCColumnValue> columns) throws EQDException, IOException {
    int index = 0;
    Match match = new Match();
    for (EQDOCColumnValue cv : columns) {
      ++index;
      this.convertColumn(table, eqId, cv, match);
    }

    return match;
  }

  private void convertColumn(String table, String eqId, EQDOCColumnValue cv, Match match) throws EQDException, IOException {
    String tablePath = this.getIMPath(table);
    String eqColumn = String.join("/", cv.getColumn());
    String eqURL = table + "/" + eqColumn;
    this.sourceContext = eqURL;
    String columnPath = getIMPath(eqURL);
    String[] fullPath = (tablePath + " " + columnPath).trim().split(" ");

    Where where = new Where();
    String variable = this.setMatchPath(match, fullPath);
    if (variable != null) {
      where.setNodeRef(variable);
    }

    addMatchWhere(match, where);
    where.setIri(fullPath[fullPath.length - 1]);
    this.convertColumnValue(cv, where);
  }

  private void setMatchNode(Match match) {
    if (match.getAnd() != null) {
      for (Match m : match.getAnd()) {
        this.setMatchNode(m);
      }
    } else if (match.getNode() == null) {
      matchCounter++;
      match.setNode("m_" + matchCounter);
      nodeRefMap.put(match.getNode(), match);
    }
  }

  private void injectPropertyReturn(Match matchToTest, String iri) {
    if (matchToTest.getWhere() != null) {
      boolean alreadyIn = false;
      if (matchToTest.getReturn() != null) {
        for (Return returnProp : matchToTest.getReturn()) {
          if (returnProp.getIri().equals(iri)) {
            alreadyIn = true;
            break;
          }
        }
      }
      if (!alreadyIn) {
        matchToTest.return_(p -> p.setNodeRef(getNodeRef(matchToTest)).setIri(iri).setAs(iri.substring(iri.lastIndexOf("#") + 1)));
      }
    } else if (matchToTest.getOr() != null) {
      for (Match m : matchToTest.getOr()) {
        injectPropertyReturn(m, iri);
      }
    } else if (matchToTest.getAnd() != null) {
      Match lastMatch = matchToTest.getAnd().getLast();
      injectPropertyReturn(lastMatch, iri);
    }
  }


  public String setMatchPath(Match match, String[] paths) {
    if (paths.length == 2) {
      return null;
    } else {
      String path = paths[1];
      boolean inverse = path.startsWith("^");
      String pathIri = path.replaceFirst("^", "");
      if (match.getPath() != null) {
        Path pathMatch = match.getPath().getFirst();
        if (pathMatch.getIri().equals(pathIri) && pathMatch.isInverse() == inverse) {
          if (paths.length == 4) {
            return pathMatch.getNode();
          }
          return this.getPathFromPath(pathMatch, paths, 3);
        }
      }

      Path pathMatch = new Path();
      match.addPath(pathMatch);
      pathMatch.setIri(pathIri);
      pathMatch.setInverse(inverse);
      pathMatch.setNode(getAcronym(paths[2]));
      ;
      pathMatch.setTypeOf((new Node()).setIri(paths[2]));
      return paths.length == 4 ? pathMatch.getNode() : this.getPathFromPath(pathMatch, paths, 3);
    }
  }

  public String getNodeRef(Path path) {
    if (path.getPath() != null) {
      Path pathMatch = path.getPath().getFirst();
      if (pathMatch.getNode() != null && pathMatch.getPath() == null) {
        return pathMatch.getNode();
      } else return getNodeRef(pathMatch);
    }
    return "";
  }

  public String getNodeRef(Match match) {
    if (match.getPath() != null) {
      Path pathMatch = match.getPath().getFirst();
      if (pathMatch.getNode() != null && pathMatch.getPath() == null) {
        return pathMatch.getNode();
      } else return getNodeRef(pathMatch);
    }
    return null;
  }


  private String getPathFromPath(Path pathMatch, String[] paths, int offset) {
    String path = paths[offset];
    boolean inverse = path.startsWith("^");
    String pathIri = path.replaceFirst("^", "");
    if (pathMatch.getPath() != null) {
      Path subPathMatch = pathMatch.getPath().getFirst();
      if (subPathMatch.getIri().equals(pathIri) && subPathMatch.isInverse() == inverse) {
        if (paths.length == offset + 3) {
          return subPathMatch.getNode();
        }
        return this.getPathFromPath(subPathMatch, paths, offset + 2);
      }
    }

    Path subPathMatch = new Path();
    pathMatch.addPath(subPathMatch);
    subPathMatch.setIri(pathIri);
    subPathMatch.setInverse(inverse);
    subPathMatch.setNode(getAcronym(paths[offset + 1]));
    subPathMatch.setTypeOf((new Node()).setIri(paths[offset + 1]));
    return paths.length == offset + 3 ? pathMatch.getNode() : this.getPathFromPath(pathMatch, paths, offset + 2);
  }

  private void convertColumnValue(EQDOCColumnValue cv, Where pv) throws IOException, EQDException {
    boolean in = cv.getInNotIn() == VocColumnValueInNotIn.IN;
    if (!cv.getValueSet().isEmpty()) {
      this.setPropertyValueSets(cv, pv, in);
    } else if (!CollectionUtils.isEmpty(cv.getLibraryItem())) {
      String valueLabel = "Library value set";
      for (String vset : cv.getLibraryItem()) {
        ++this.setCounter;
        String vsetName = "Unnamed library set " + this.setCounter;
        Node iri = (new Node()).setIri(this.namespace + vset).setName(vsetName);
        iri.setMemberOf(true);
        pv.addIs(iri);
        if (!in)
          pv.setNot(true);
        pv.setValueLabel(valueLabel);
        this.createLibraryValueSet(iri.getIri(), vsetName);
      }
    } else if (cv.getRangeValue() != null) {
      this.setRangeValue(cv.getRangeValue(), pv);
    } else if (cv.getSingleValue() != null) {
      setSingleValue(cv, pv, in);
    }
    if (cv.getFunction() != null) {
      throw new EQDException("functions not yest supported");
    }
  }

  private void setPropertyValueSets(EQDOCColumnValue cv, Where where, boolean in) throws IOException, EQDException {

    for (EQDOCValueSet vs : cv.getValueSet()) {
      if (vs.getAllValues() != null) {
        List<Node> values = this.getExceptionSet(vs.getAllValues());
        for (Node node : values) {
          node.setExclude(true);
        }
        where.setIs(values);
      } else this.setInlineValues(vs, where, in);
    }
  }

  public String getIMPath(String eqdPath) throws EQDException {
    String target;
    try {
      target = (String) this.dataMap.get(eqdPath);
    } catch (Exception e) {
      throw new EQDException("unknown map : " + eqdPath);
    }
    if (target == null) {
      throw new EQDException("unknown map : " + eqdPath);
    }

    if (target.isEmpty()) {
      return "";
    } else if (target.startsWith("$")) {
      return target;
    } else {
      String[] paths = (target).split(" ");
      for (int i = 0; i < paths.length; ++i) {
        String path = paths[i];
        String inverse = "";
        if (path.startsWith("^")) {
          inverse = "^";
          path = path.substring(1);
        }

        path = inverse + (path.startsWith("http") ? path : NAMESPACE.IM + path);
        paths[i] = path;
      }

      return String.join(" ", paths);
    }
  }


  private String resolveIri(String iri) {
    return iri.startsWith("http") ? iri : NAMESPACE.IM + iri;
  }

  private Match convertTestCriterion(EQDOCCriterion eqCriterion) throws EQDException, IOException {
    EQDOCTestAttribute testAtt = eqCriterion.getFilterAttribute().getRestriction().getTestAttribute();
    return this.convertTestColumns(eqCriterion.getTable(), null, testAtt.getColumnValue());
  }

  private void setRestriction(EQDOCCriterion eqCriterion, Match restricted) throws EQDException {
    EQDOCFilterRestriction restrict = eqCriterion.getFilterAttribute().getRestriction();
    Order direction;
    if ((restrict.getColumnOrder().getColumns().getFirst()).getDirection() == VocOrderDirection.ASC) {
      direction = Order.ascending;
    } else {
      direction = Order.descending;
    }
    String linkColumn = eqCriterion.getFilterAttribute().getRestriction().getColumnOrder().getColumns().getFirst().getColumn().getFirst();
    String table = eqCriterion.getTable();
    String orderBy = this.getIMPath(table + "/" + linkColumn);
    if (restrict.getColumnOrder().getRecordCount() != 1000) {
      String nodeRef = getNodeRef(restricted);
      restricted.orderBy((o) -> o
        .addProperty(new OrderDirection()
          .setNodeRef(nodeRef)
          .setIri(orderBy)
          .setDirection(direction))
        .setLimit(restrict.getColumnOrder().getRecordCount()));
    }
  }

  private void addMatchWhere(Match match, Where where) {
    if (match.getWhere() == null) {
      match.setWhere(where);
    } else if (match.getWhere().getIri() != null) {
      Where boolWhere = new Where();
      boolWhere.addAnd(match.getWhere());
      match.setWhere(boolWhere);
      boolWhere.addAnd(where);
    } else {
      match.getWhere().addAnd(where);
    }
  }


  private String getRelationship(EQDOCRelationship eqRelationship) throws QueryException {
    StringBuilder relationship = new StringBuilder();
    relationship.append(eqRelationship.getParentColumnDisplayName());
    if (eqRelationship.getRangeValue() == null) {
      relationship.append(" on same date as");
    } else {
      EQDOCRangeFrom eqFrom = eqRelationship.getRangeValue().getRangeFrom();
      if (eqFrom != null) {
        VocRangeFromOperator op = eqFrom.getOperator();
        switch (op) {
          case GT:
            relationship.append(" after");
            break;
          case GTEQ:
            relationship.append(" on or after");
            break;
          default:
            throw new QueryException("Unknown operator " + op);
        }
      } else {
        EQDOCRangeTo eqTo = eqRelationship.getRangeValue().getRangeTo();
        if (eqTo != null) {
          VocRangeToOperator op = eqTo.getOperator();
          switch (op) {
            case LT:
              relationship.append(" before");
              break;
            case LTEQ:
              relationship.append(" on or before");
              break;
          }
        }

      }
    }
    return relationship.toString();
  }

  private void setSingleValue(EQDOCColumnValue cv, Where pv, boolean in) throws IOException, EQDException {
    EQDOCSingleValue sv = cv.getSingleValue();
    EQDOCValue variable = sv.getVariable();
    if (variable == null) {
      throw new EQDException("variable is null");
    }
    String value = variable.getValue();
    VocValueUnit eqQualifier = variable.getUnit();
    VocRelation relative = variable.getRelation();
    if (!in) pv.setNot(true);
    TTIriRef qualifier = null;
    if (eqQualifier != null) {
      qualifier = getIMQualifier(eqQualifier);
    }
    if (relative != null && relative.equals(VocRelation.RELATIVE)) {
      if (value.equalsIgnoreCase("last")) {
        pv.setOperator(Operator.eq);
        pv.setValue("-1");
        pv.setValueTerm("last");
        pv.setCompare(new Compare());
        pv.setQualifier(qualifier);
        Compare diff = pv.getCompare();
        diff.setLeft(new ValueSource()
          .setIri(pv.getIri()).setNodeRef(pv.getNodeRef()));
        diff.setRight(new ValueSource()
          .setParameter("$searchDate"));
      } else if (value.equalsIgnoreCase("this")) {
        pv.setOperator(Operator.eq);
        pv.setCompare(new Compare());
        pv.setQualifier(qualifier);
        Compare comp = pv.getCompare();
        comp.setLeft(new ValueSource()
          .setIri(pv.getIri()).setNodeRef(pv.getNodeRef()));
        comp.setRight(new ValueSource()
          .setParameter("$searchDate"));
        pv.setValueTerm("this");
      }
    } else if (value.matches("^-?(\\d+(\\.\\d*)?|\\.\\d+)$")) {
      pv.setValue(value);
      pv.setOperator(Operator.eq);
      if (!in) pv.setNot(true);
    } else {
      String key = this.sourceContext + "/EMISINTERNAL/" + value;
      Object mapValue = this.dataMap.get(key);
      if (mapValue != null) {
        pv.addIs(new Node().setIri(getValueIriResult(mapValue).stream().findFirst().get().getIri()));
        if (!in) pv.setNot(true);
      } else throw new EQDException("variable " + value + "with " + relative + " not supported");
    }
  }


  private void setRangeValue(EQDOCRangeValue rv, Where pv) throws EQDException {
    EQDOCRangeFrom rFrom = rv.getRangeFrom();
    EQDOCRangeTo rTo = rv.getRangeTo();
    String leftProperty= pv.getIri();
    if (rFrom != null && rTo != null) {
      this.setRangeCompare(pv, rFrom, rTo, rv.getRelativeTo());
    } else if (rFrom != null) {
      this.setCompareFrom(pv, rFrom, rv.getRelativeTo(),leftProperty);
    } else if (rTo != null) {
      this.setCompareTo(pv, rTo, rv.getRelativeTo(),leftProperty);
    }
  }


  private void setCompareFrom(Where where, EQDOCRangeFrom rFrom, String relativeTo,String leftProperty) throws EQDException {
    Operator comp;
    if (rFrom.getOperator() != null) {
      comp = (Operator) this.vocabMap.get(rFrom.getOperator());
    } else {
      comp = Operator.eq;
    }
    String value = null;
    TTIriRef units = null;
    EQDOCValue eqValue = rFrom.getValue();
    VocRelation relation = null;
    if (eqValue != null) {
      value = eqValue.getValue();
      units = setQualifierGetunits(where, eqValue.getUnit());
      relation = VocRelation.ABSOLUTE;
      if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
        relation = VocRelation.RELATIVE;
      }
    }
    this.setCompare(where, where, comp, value, units, relation, relativeTo,leftProperty);
  }


  private void setCompareTo(Where pv, EQDOCRangeTo rTo, String relativeTo,String leftProperty) throws EQDException {
    Operator comp;
    if (rTo.getOperator() != null) {
      comp = (Operator) this.vocabMap.get(rTo.getOperator());
    } else {
      comp = Operator.eq;
    }

    String value = null;
    TTIriRef units = null;
    VocRelation relation = null;
    EQDOCValue eqValue = rTo.getValue();
    if (eqValue != null) {
      value = eqValue.getValue();
      units = setQualifierGetunits(pv, eqValue.getUnit());
      relation = VocRelation.ABSOLUTE;
      if (eqValue.getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
        relation = VocRelation.RELATIVE;
      }
    }

    this.setCompare(pv, pv, comp, value, units, relation, relativeTo,leftProperty);
  }


  private void setCompare(Where where, Assignable assignable, Operator comp, String value, TTIriRef units, VocRelation relation, String relativeTo,String leftProperty) throws EQDException {

    String property = leftProperty;

    if (relativeTo != null) {
      relation = VocRelation.RELATIVE;
      if (relativeTo.equals("BASELINE")) {
        relativeTo = "$achievementDate";
      } else throw new EQDException("relative to " + relativeTo + " not supported");
    }

    assignable.setOperator(comp);
    if (value != null && value.equals("This")) {
      assignable.setOperator(Operator.eq);
      assignable.setValueTerm("this");
    } else if (value != null && value.equals("Last")) {
      assignable.setOperator(Operator.eq);
      assignable.setValue("-1");
      assignable.setValueTerm("last");
    } else if (value != null) {
      if (value.equals("0") && relation == VocRelation.RELATIVE) {
        value = null;
      }
      assignable.setValue(value);
    }
    if (relation == VocRelation.RELATIVE) {
      if (leftProperty.contains("age")) {
        assignable.setUnits(units);
        return;
      }
      if (relativeTo == null) {
        relativeTo = "$searchDate";
      }
      ValueSource relationLeft = new ValueSource();
      where.setIri((String) null);
      where.setName(null);
      relationLeft.setIri(property).setNodeRef(where.getNodeRef());
      ValueSource relationRight = new ValueSource();
      relationRight.setParameter(relativeTo);
      if (assignable.getValue() == null) {
        buildCompare(assignable, units, relationLeft, relationRight);
      } else buildCompare(assignable, units, relationLeft, relationRight);
    }

  }


  private TTIriRef getIMUnits(VocValueUnit units) throws EQDException {
    switch (units) {
      case YEAR:
        return iri(IM.YEARS);
      case MONTH:
        return iri(IM.MONTHS);
      case DAY:
        return iri(IM.DAYS);
      case FISCALYEAR:
        return iri(IM.FISCAL_YEAR);
      case QUARTER:
        return iri(IM.QUARTER);
      case DATE:
        break;
      default:
        throw new EQDException("unknown unit map: " + units);
    }
    return null;
  }


  private TTIriRef getIMQualifier(VocValueUnit units) throws EQDException {
    return switch (units) {
      case FISCALYEAR -> iri(IM.FISCAL_YEAR);
      case QUARTER -> iri(IM.QUARTER);
      case MONTH -> iri(IM.MONTH);
      default -> throw new EQDException("unknown qualifier map: " + units);
    };
  }


  private void setRangeCompare(Where where, EQDOCRangeFrom rFrom, EQDOCRangeTo rTo, String relativeTo) throws EQDException {
    Range range = new Range();
    where.setRange(range);
    Value fromValue = new Value();
    range.setFrom(fromValue);
    String leftProperty= where.getIri();
    Operator comp;
    if (rFrom.getOperator() != null) {
      comp = (Operator) this.vocabMap.get(rFrom.getOperator());
    } else {
      comp = Operator.eq;
    }

    String value = rFrom.getValue().getValue();
    TTIriRef units = null;
    if (rFrom.getValue().getUnit() != null) {
      units = setQualifierGetunits(where, rFrom.getValue().getUnit());
    }

    VocRelation relation = VocRelation.ABSOLUTE;
    if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
      relation = VocRelation.RELATIVE;
    }


    this.setCompare(where, fromValue, comp, value, units, relation, relativeTo,leftProperty);
    Value toValue = new Value();
    range.setTo(toValue);
    if (rFrom.getOperator() != null) {
      comp = (Operator) this.vocabMap.get(rTo.getOperator());
    } else {
      comp = Operator.eq;
    }

    value = rTo.getValue().getValue();
    units = null;
    if (rTo.getValue().getUnit() != null) {
      units = setQualifierGetunits(where, rTo.getValue().getUnit());
    }

    relation = VocRelation.ABSOLUTE;
    if (rTo.getValue().getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
      relation = VocRelation.RELATIVE;
    }

    this.setCompare(where, toValue, comp, value, units, relation, relativeTo,leftProperty);
  }

  private List<Node> getExceptionSet(EQDOCException set) throws IOException {
    List<Node> valueSet = new ArrayList<>();
    VocCodeSystemEx scheme = set.getCodeSystem();

    for (EQDOCExceptionValue ev : set.getValues()) {
      Set<Node> values = this.getValueConcepts(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
      if (values != null) valueSet.addAll(values.stream().map((v) -> v.setExclude(true)).toList());
      else {
        log.error("Missing exception sets {} {}", ev.getValue(), ev.getDisplayName());
      }
    }

    return valueSet;
  }

  private TTIriRef getClusterSet(EQDOCValueSet vs) throws IOException {
    if (vs.getCodeSystem() == VocCodeSystemEx.SNOMED_CONCEPT && vs.getDescription() != null && !vs.getClusterCode().contains("FlattenedCodeList")) {
      return this.importMaps.getReferenceFromCoreTerm(vs.getDescription());
    }
    return null;
  }

  private void setInlineValues(EQDOCValueSet vs, Where pv, boolean in) throws IOException, EQDException {
    VocCodeSystemEx scheme = vs.getCodeSystem();
    if (vs.getDescription() != null) {
      pv.setShortLabel(pv.getShortLabel() != null ? pv.getShortLabel() + "_" + vs.getDescription() : vs.getDescription());
    }

    TTIriRef cluster = this.getClusterSet(vs);
    if (cluster != null) {
      pv.addIs((new Node()).setIri(cluster.getIri()).setName(cluster.getName()).setMemberOf(true));
      if (!in) pv.setNot(true);
    } else {
      Set<Node> setContent = new HashSet<>();
      for (EQDOCValueSetValue ev : vs.getValues()) {
        Set<Node> setMembers = this.processEQDOCValueSetValue(scheme, ev);
        if (!setMembers.isEmpty()) {
          for (Node memberOrConcept : setMembers) {
            if (memberOrConcept.isMemberOf()) {
              String setIri = memberOrConcept.getIri();
              TTEntity usedIn = EqdToIMQ.setIriToEntity.get(setIri);
              if (usedIn == null) {
                usedIn = (new TTEntity()).setIri(setIri).setCrud(iri(IM.ADD_QUADS));
                this.document.addEntity(usedIn);
                EqdToIMQ.setIriToEntity.put(setIri, usedIn);
              }

              this.addUsedIn(usedIn);
            }
          }

          setContent.addAll(setMembers);
        }
      }

      if (scheme == VocCodeSystemEx.SCT_CONST) {
        Query eclQuery = this.convertToEcl(scheme, setContent);
        TTEntity valueSet = this.createValueSet(vs, eclQuery, setContent);
        pv.addIs((new Node()).setIri(valueSet.getIri()).setName(valueSet.getName()).setMemberOf(true));
        if (!in) pv.setNot(true);
        pv.setValueLabel(valueSet.getName() + (eclQuery.notExists() ? " (+exclusions)" : ""));
      } else {
        String name = null;
        String exclusions = "";
        String previous = null;
        if (setContent.size() <= 3) {
          for (Node node : setContent) {
            if (node.isExclude()) {
              exclusions = " (exclusions)";
            }
            pv.addIs(node);
            if (!in) pv.setNot(true);
            if (node.getName() != null && name == null) {
              name = this.getShortName(node.getName(), previous);
              previous = name;
            }
          }
        } else {
          for (Node node : setContent) {
            if (node.isExclude()) {
              exclusions = " (exclusions)";
            }

            if (node.getName() != null && name == null) {
              name = this.getShortName(node.getName(), previous);
              previous = name;
            }
            if (node.isMemberOf()) {
              pv.addIs(node);
              if (!in) pv.setNot(true);
            }
          }
          Set<Node> conceptContent = setContent.stream().filter(c -> !c.isMemberOf()).collect(Collectors.toSet());
          TTEntity valueSet = this.createValueSet(vs, conceptContent);
          pv.addIs((new Node()).setIri(valueSet.getIri()).setName(valueSet.getName()).setMemberOf(true));
          if (!in) pv.setNot(true);
          pv.setValueLabel(valueSet.getName() + exclusions);
        }

        if (setContent.size() > 1 && name != null) {
          name = name + "...etc.";
        }

        if (vs.getDescription() != null) {
          name = vs.getDescription();
        }

        pv.setValueLabel(name + exclusions);
      }

    }
  }

  private Query convertToEcl(VocCodeSystemEx scheme, Set<Node> setContent) throws EQDException {
    if (scheme == VocCodeSystemEx.SCT_CONST) {
      String property = NAMESPACE.SNOMED + "127489000";
      String name = null;
      Query eclQuery = (new Query());
      Where where = new Where();
      where.setAnyRoleGroup(true);
      Where notWhere = new Where();
      where.setIri(property);
      notWhere.setIri(property);
      notWhere.setAnyRoleGroup(true);

      for (Node node : setContent) {
        if (node.isExclude()) {
          notWhere.addIs(node);
        } else where.addIs(node);
        if (node.getName() != null && name == null) {
          name = this.getShortName(node.getName(), null);
        }
      }

      if (notWhere.getIs() != null) {
        Match inMatch = new Match();
        addMatchWhere(inMatch, where);
        eclQuery.addAnd(inMatch);
        Match notMatch = new Match();
        notMatch.setNotExists(true);
        eclQuery.addAnd(notMatch);
        addMatchWhere(notMatch, notWhere);
      } else {
        addMatchWhere(eclQuery, where);
      }

      return eclQuery;
    } else {
      throw new EQDException("unrecognised scheme");
    }
  }

  private Set<Node> processEQDOCValueSetValue(VocCodeSystemEx scheme, EQDOCValueSetValue ev) throws IOException {
    Set<Node> concepts = this.getValueConcepts(scheme, ev);
    if (concepts == null) {
      String eqValue = ev.getValue();
      throw new IOException("Missing " + eqValue + " " + ev.getDisplayName());
    } else {
      if (ev.isIncludeChildren()) {
        for (Node node : concepts) {
          node.setDescendantsOrSelfOf(true);
        }
      }

      if (!ev.getException().isEmpty()) {
        for (EQDOCException exc : ev.getException()) {
          for (EQDOCExceptionValue val : exc.getValues()) {
            Set<Node> exceptionValue = this.getValueConcepts(scheme, val);
            if (exceptionValue != null) {
              for (Node node : exceptionValue) {
                if (val.isIncludeChildren()) {
                  node.setDescendantsOrSelfOf(true);
                }

                node.setExclude(true);
                concepts.add(node);
              }
            }
          }
        }
      }

      return concepts;
    }
  }

  private Set<Node> getValueConcepts(VocCodeSystemEx scheme, EQDOCExceptionValue ev) throws IOException {
    return this.getValueConcepts(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
  }

  private Set<Node> getValueConcepts(VocCodeSystemEx scheme, EQDOCValueSetValue ev) throws IOException {
    return this.getValueConcepts(scheme, ev.getValue(), ev.getDisplayName(), ev.getLegacyValue());
  }

  private Set<Node> getValueConcepts(VocCodeSystemEx scheme, String originalCode, String originalTerm, String legacyCode) throws IOException {
    if (scheme == VocCodeSystemEx.EMISINTERNAL) {
      String key = this.sourceContext + "/EMISINTERNAL/" + originalCode;
      Object mapValue = this.dataMap.get(key);
      if (mapValue != null) {
        return this.getValueIriResult(mapValue);
      } else if (isValidUUID(originalCode)) {
        return this.getValueIriResult(this.namespace + originalCode);
      } else {
        throw new IllegalArgumentException("unmapped emis internal code : " + key);
      }
    } else if (scheme != VocCodeSystemEx.SNOMED_CONCEPT && !scheme.value().contains("SCT")) {
      throw new IllegalArgumentException("code scheme not recognised : " + scheme.value());
    } else {
      List<NAMESPACE> schemes = new ArrayList<>();
      schemes.add(NAMESPACE.SNOMED);
      schemes.add(NAMESPACE.EMIS);
      schemes.add(NAMESPACE.IM);
      schemes.add(NAMESPACE.BNF);
      Set<Node> snomed = valueMap.get(originalCode);
      if (snomed == null) {
        snomed = this.getValuesFromOriginal(originalCode, originalTerm, legacyCode, schemes);
        if (snomed != null) {
          this.valueMap.put(originalCode, snomed);
        }
      }

      if (snomed != null) {
        for (Node sn : snomed) {
          if (sn.getIri() == null) {
            System.out.println("null snomed");
          }
        }
        return new HashSet<>(snomed);
      } else {
        return Collections.emptySet();
      }
    }
  }

  private Set<Node> getValueIriResult(Object mapValue) throws IOException {
    Node iri = (new Node()).setIri(mapValue.toString());
    String name = this.importMaps.getCoreName(iri.getIri());
    if (name != null) {
      iri.setName(name);
    }

    Set<Node> result = new HashSet<>();
    result.add(iri);
    return result;
  }

  private Set<Node> getValuesFromOriginal(String originalCode, String originalTerm, String legacyCode, List<NAMESPACE> schemes) {
    Set<Entity> snomed = this.getCoreFromCode(originalCode, schemes);
    if (snomed == null && legacyCode != null) {
      snomed = this.getCoreFromCode(legacyCode, schemes);
    }

    if (snomed == null && originalTerm != null) {
      snomed = this.getCoreFromLegacyTerm(originalTerm);
    }

    if (snomed == null) {
      snomed = this.getLegacyFromTermCode(originalCode);
    }

    if (snomed == null && originalTerm != null) {
      snomed = this.getCoreFromLegacyTerm(originalTerm + " (emis code id)");
    }
    if (snomed == null)
      return null;
    return snomed.stream().map(e -> {
        Node n = new Node();
        n.setIri(e.getIri());
        n.setName(e.getName());
        if (Set.of(iri(IM.CONCEPT_SET), iri(IM.VALUESET)).stream().anyMatch(e.getType()::contains))
          n.setMemberOf(true);
        return n;
      })
      .collect(Collectors.toSet());
  }

  private Set<Entity> getLegacyFromTermCode(String originalCode) {
    try {
      return this.importMaps.getLegacyFromTermCode(originalCode, NAMESPACE.EMIS);
    } catch (Exception e) {
      log.error("unable to retrieve iri from term code {}", e.getMessage());
      return Collections.emptySet();
    }
  }

  private Set<Entity> getCoreFromLegacyTerm(String originalTerm) {
    try {
      if (originalTerm.contains("s disease of lymph nodes of head, face AND/OR neck")) {
        log.info("!!");
      }
      return this.importMaps.getCoreFromLegacyTerm(originalTerm, NAMESPACE.EMIS);
    } catch (Exception e) {
      log.error("unable to retrieve from term {}", e.getMessage());
      return Collections.emptySet();
    }
  }

  private Set<Entity> getCoreFromCode(String originalCode, List<NAMESPACE> schemes) {
    return this.importMaps.getCoreFromCode(originalCode, schemes);
  }

  private void addUsedIn(TTEntity set) {
    String usedIn = activeReport;
    if (EqdToIMQ.versionMap.containsKey(usedIn)) {
      usedIn = EqdToIMQ.versionMap.get(usedIn);
    }
    queryEntity.addObject(iri(IM.USES), iri(set.getIri()));
  }

  private void createLibraryValueSet(String iri, String name) {
    TTEntity valueSet = (new TTEntity()).setIri(iri).setName(name).addType(iri(IM.CONCEPT_SET));
    valueSet.setScheme(iri(namespace));
    this.addUsedIn(valueSet);
    this.document.addEntity(valueSet);
  }

  private void updateSetName(TTEntity setName, String thisName) {
    String currentName = setName.getName();
    if (!currentName.contains(thisName)) {
      setName.setName(currentName.split(" (set)")[0] + " ... (set");
    }

  }

  private TTEntity createValueSet(EQDOCValueSet vs, Query definition, Set<Node> setContent) throws JsonProcessingException {
    String description = vs.getDescription();
    String name = description == null ? this.getNameFromSet(vs, setContent) : description;
    String entailedMembers = (new ObjectMapper()).writeValueAsString(definition);
    TTEntity duplicate = EqdToIMQ.definitionToEntity.get(entailedMembers);
    if (duplicate != null) {
      this.addUsedIn(duplicate);
      return duplicate;
    } else {
      TTEntity valueSet = new TTEntity()
        .setIri(namespace + vs.getId())
        .setName(name)
        .setScheme(iri(namespace))
        .addType(iri(IM.CONCEPT_SET))
        .set(iri(IM.DEFINITION), TTLiteral.literal(definition));
      this.addUsedIn(valueSet);
      this.document.addEntity(valueSet);
      return valueSet;
    }
  }

  private TTEntity createValueSet(EQDOCValueSet vs, Set<Node> setContent) throws JsonProcessingException {
    String description = vs.getDescription();
    String name = description == null ? this.getNameFromSet(vs, setContent) : description;
    String entailedMembers = (new ObjectMapper()).writeValueAsString(setContent);
    TTEntity duplicate = EqdToIMQ.definitionToEntity.get(entailedMembers);
    if (duplicate != null) {
      this.addUsedIn(duplicate);
      if (this.columnGroup != null) {
        this.updateSetName(duplicate, name);
      } else {
        this.updateSetName(duplicate, name);
      }

      return duplicate;
    } else {
      TTEntity valueSet = new TTEntity()
        .setIri(namespace + vs.getId()).setName(name).addType(iri(IM.CONCEPT_SET));
      valueSet.setScheme(iri(namespace));
      if (this.columnGroup != null) {
        queryEntity.addObject(iri(IM.USES), iri(valueSet.getIri()));
      } else {
        String usedIn = activeReport;
        if (EqdToIMQ.versionMap.containsKey(usedIn)) {
          usedIn = EqdToIMQ.versionMap.get(usedIn);
        }
        queryEntity.addObject(iri(IM.USES), iri(valueSet.getIri()));
      }

      for (Node node : setContent) {
        TTNode instance = new TTNode();
        valueSet.addObject(iri(IM.ENTAILED_MEMBER), instance);
        instance.set(IM.IS.toString(), iri(node.getIri()));
        if (node.isExclude()) {
          instance.set(iri(IM.EXCLUDE), TTLiteral.literal(true));
        }

        if (node.isAncestorsOf()) {
          instance.set(iri(IM.ENTAILMENT), iri(IM.ANCESTORS_OF));
        }

        if (node.isDescendantsOf()) {
          instance.set(iri(IM.ENTAILMENT), iri(IM.DESCENDANTS_OF));
        }

        if (node.isDescendantsOrSelfOf()) {
          instance.set(iri(IM.ENTAILMENT), iri(IM.DESCENDANTS_OR_SELF_OF));
        }
      }

      EqdToIMQ.definitionToEntity.put(entailedMembers, valueSet);
      this.document.addEntity(valueSet);
      return valueSet;
    }
  }

  private String getNameFromSet(EQDOCValueSet vs, Set<Node> set) {
    if (EqdToIMQ.getAutoNamedSets().get(namespace + vs.getId()) != null) {
      return EqdToIMQ.getAutoNamedSets().get(namespace + vs.getId());
    }
    if (EqdToIMQ.getInlineSets().get(namespace + vs.getId()) != null) {
      return EqdToIMQ.getInlineSets().get(namespace + vs.getId()).getName();
    }
    StringBuilder name = new StringBuilder();
    if (set.size() > 2) {
      name = new StringBuilder("Clinical codes..");
    } else {
      for (Node node : set) {
        if (!name.isEmpty()) name.append(" or ");
        name.append(node.getName());
      }
    }
    return name.toString();
  }


  private String getShortName(String name, String previous) {
    name = name.split(" \\(")[0];
    if (previous == null) return name;
    return name.contains("resolved") ? previous + " or resolved" : previous + " or " + name;
  }

  public String getAcronym(String iri) {
    if (iri == null || iri.isBlank()) return "";
    String local = iri.substring(iri.lastIndexOf("#") + 1);
    String[] parts = local.split("(?<!^)(?=[A-Z0-9])");
    StringBuilder sb = new StringBuilder();
    for (String part : parts) {
      if (part.isBlank()) continue;
      sb.append(Character.toUpperCase(part.charAt(0)));
    }
    String acronym = sb.toString().toLowerCase();
    if (acronyms.contains(acronym)) {
      int i = 1;
      while (acronyms.contains(acronym + i))
        i++;
      acronym = acronym + i;
    }
    acronyms.add(acronym);
    return acronym;
  }

  private String createKeepAs(Match match) {
    StringBuilder keepAs = new StringBuilder();
    if (match.getWhere() != null) {
      Where where = match.getWhere();
      if (where.getShortLabel() != null) {
        keepAs.append(where.getShortLabel());
      }
      if (where.getAnd() != null) {
        for (Where and : where.getAnd()) {
          if (and.getShortLabel() != null) {
            keepAs.append(and.getShortLabel());
          } else if (and.getValueLabel() != null) {
            String valueLabel = and.getValueLabel();
            keepAs.append(valueLabel, 0, Math.min(valueLabel.length(), 10));
          } else if (and.getIs() != null) {
            String isName = and.getIs().getFirst().getName().replace(" ", "");
            keepAs.append(isName, 0, Math.min(isName.length(), 10));
          }
        }
      }
      return keepAs.toString();
    }
    if (match.getOr() != null) {
      for (Match or : match.getOr()) {
        if (!keepAs.isEmpty())
          keepAs.append("_");
        keepAs.append(this.createKeepAs(or));
      }
    } else if (match.getNodeRef() != null) {
      keepAs.append(createKeepAs(nodeRefMap.get(match.getNodeRef())));
    }
    return keepAs.toString();
  }

  private void setKeepMatchNode(Match match, String affix) {
    if (match.getAnd() != null) {
      setKeepMatchNode(match.getAnd().getLast(), affix);
      return;
    } else if (match.getOr() != null) {
      match.setNode(createKeepAs(match) + affix);
      return;
    }
    StringBuilder keepAs = new StringBuilder();
    if (match.getWhere() != null) {
      keepAs.append(createKeepAs(match));
      if (keepAs.isEmpty() && match.getNode() == null) {
        matchCounter++;
        match.setNode("m_" + matchCounter);
      } else {
        match.setNode(keepAs + (affix != null ? affix : ""));
      }
    } else if (match.getNodeRef() != null) {
      match.setNode(createKeepAs(nodeRefMap.get(match.getNodeRef()))
        + (affix != null ? ("_" + affix) : ""));
    }
  }
}