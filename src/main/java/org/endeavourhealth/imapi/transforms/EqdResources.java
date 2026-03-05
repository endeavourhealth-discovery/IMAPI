package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.endeavourhealth.imapi.logic.exporters.ImportMaps;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
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
  @Getter
  private Namespace namespace;
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
  @Setter
  @Getter TTEntity queryEntity;

  public EqdResources(TTDocument document, Properties dataMap, Namespace namespace) {
    this.dataMap = dataMap;
    this.document = document;
    this.namespace = namespace;
    this.setVocabMaps();
  }

  public EqdResources setMatchCounter(int matchCounter) {
    this.matchCounter = matchCounter;
    return this;
  }

  private static boolean isValidUUID(String str) {
    try {
      UUID uuid = UUID.fromString(str);
      return str.equals(uuid.toString());
    } catch (IllegalArgumentException var2) {
      return false;
    }
  }

  public void incrementRule() {
    ++this.rule;
  }

  public void incrementSubRule() {
    ++this.subRule;
  }

  public EqdResources setNamespace(Namespace namespace) {
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
      match.addIs(Node.iri(this.namespace + parent).setName(this.reportNames.get(parent)));
      queryEntity.addObject(iri(IM.DEPENDENT_ON),iri(namespace+parent));
      return match;
    } else {
      List<EQDOCCriteria> groupCriteria = eqGroup.getDefinition().getCriteria();
      return this.getMatchFromGroup(groupCriteria, eqGroup.getDefinition().getMemberOperator());
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
        Match match= this.convertCriteria(eqCriteria);
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
    if (eqCriteria.getPopulationCriterion() != null) {
      return this.getPopulationQuery(eqCriteria);
    } else {
      this.incrementSubRule();
      if (eqCriteria.getCriterion() != null) {
        Match match= this.convertCriterion(eqCriteria.getCriterion());
        if (eqCriteria.getCriterion().getDescription()!=null) match.setDescription(eqCriteria.getCriterion().getDescription());
        return match;
      } else {
        Map<String, EQDOCCriterion> libraryItems = EqdToIMQ.getLibraryItems();
        String libraryId = eqCriteria.getLibraryItem().getLibraryItem();
        if (!libraryItems.containsKey(libraryId)) {
          System.err.println("Library item not found: " + libraryId);
          Match libraryMatch= new Match();
          libraryMatch.addIs(new Node().setIri(this.namespace+ libraryId));
          return libraryMatch;
        } else {
          System.out.println("Library item found : " + libraryId);
          return this.convertCriterion(libraryItems.get(libraryId));
        }
      }
    }
  }

  public Match getPopulationQuery(EQDOCCriteria eqCriteria) {
    EQDOCSearchIdentifier search = eqCriteria.getPopulationCriterion();
    String searchId = search.getReportGuid();
    if (search.getVersionIndependentGuid() != null) searchId = search.getVersionIndependentGuid();
    if (EqdToIMQ.versionMap.containsKey(searchId)) {
      searchId = EqdToIMQ.versionMap.get(searchId);
    }
    Match match = new Match();
    match.addIs(new Node().setIri(namespace + searchId)
      .setIsCohort(true).setName((String) this.reportNames.get(search.getReportGuid())));
    queryEntity.addObject(iri(IM.DEPENDENT_ON),iri(namespace+searchId));
    return match;
  }

  private void injectPatientReturn(Match match) {
    if (match.getReturn()==null){
      match.addReturn(new Return()
        .setNodeRef(match.getNode())
        .setIri(Namespace.IM+"patient"));
    }
    else {
      match.getReturn().addFirst(new Return().setNodeRef(match.getNode()).setIri(Namespace.IM+"patient"));
    }
  }

  private Match convertCriterion(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {
    Match baseMatch = null;
    Match standardMatch = null;
    Match testMatch = null;
    Match linkedMatch = null;
    Match lastMatch = null;
    EQDOCFilterAttribute filter = eqCriterion.getFilterAttribute();
    boolean hasLinked = eqCriterion.getLinkedCriterion() != null;
    boolean hasStandard = (!filter.getColumnValue().isEmpty() || filter.getRestriction() != null);
    if (!eqCriterion.getBaseCriteriaGroup().isEmpty()) {
      Match baseQuery = this.convertBaseCriteriaGroups(eqCriterion);
      baseQuery.setTypeOf(new Node().setIri(Namespace.IM+"Patient"));
      injectPatientReturn(baseQuery);
      Match logicalMatch= new LogicOptimizer().getLogicalMatch(baseQuery);
      String json= new ObjectMapper().writeValueAsString(logicalMatch);
      int hash= json.hashCode();
      EqdToIMQ.getBaseQueries().put(String.valueOf(hash),baseQuery);
      baseMatch= new Match();
      baseMatch.addIs(new Node().setIri(namespace.toString()+hash).setIsResultSet(true));
      injectPatientReturn(baseMatch);
      lastMatch = baseMatch;
    }

    if (hasStandard) {
      standardMatch = this.convertStandardCriterion(eqCriterion);
      injectPatientReturn(standardMatch);
      lastMatch= standardMatch;
      if (baseMatch != null) {
        if (standardMatch.getAnd()==null&&standardMatch.getWhere()==null){
          baseMatch.setOrderBy(standardMatch.getOrderBy());
          standardMatch=null;
          setNamedMatchNode(baseMatch);
          lastMatch= baseMatch;
        }else {
          setMatchNode(baseMatch);
          standardMatch.setNodeRef(baseMatch.getNode());
        }
        lastMatch.setPath(null);
      }
      if (eqCriterion.getFilterAttribute().getRestriction() != null && eqCriterion.getFilterAttribute().getRestriction().getTestAttribute() != null) {
        testMatch = this.convertTestCriterion(eqCriterion,lastMatch);
        setNamedMatchNode(lastMatch);
        testMatch.setNodeRef(lastMatch.getNode());
        injectPatientReturn(testMatch);
        lastMatch= testMatch;
      }
    }
    if (lastMatch == null) {
      throw new EQDException("No matches found for criterion");
    }

    if (hasLinked) {
      setNamedMatchNode(lastMatch);
      linkedMatch = this.convertLinkedCriterion(eqCriterion, lastMatch);
      injectPatientReturn(linkedMatch);
    }
    List<Match> steps = new ArrayList<>();

    if (baseMatch != null) {
      steps.add(baseMatch);
    }
    if (standardMatch != null) {
      steps.add(standardMatch);
    }
    if (testMatch != null) {
      steps.add(testMatch);
    }
    if (linkedMatch != null) {
      steps.add(linkedMatch);
    }

    if (steps.size() > 1) {
      Match outerMatch = new Match();
      outerMatch.setStep(steps);
      injectPatientReturn(outerMatch);
      return outerMatch;
    }
    else return lastMatch;
  }

  private Match convertBaseCriteriaGroups(EQDOCCriterion eqCriterion) throws QueryException, EQDException, IOException {
    Match baseMatch;
    if (eqCriterion.getBaseCriteriaGroup().size() > 1) {
      baseMatch = new Match();
      for (EQDOCBaseCriteriaGroup baseGroup : eqCriterion.getBaseCriteriaGroup()) {
        Match subQuery = this.convertBaseCriteriaGroup(baseGroup);
        baseMatch.addUnion(subQuery);
      }
      setUnionReturns(baseMatch,Namespace.IM+"effectiveDate");
    } else {
      baseMatch = this.convertBaseCriteriaGroup(eqCriterion.getBaseCriteriaGroup().getFirst());
    }

    return baseMatch;
  }

  private void setUnionReturns(Match match,String returnIri) {
    match.addReturn(new Return()
      .setNodeRef(getNodeRef(match))
      .setIri(returnIri));
      for (Match subQuery : match.getUnion()) {
        injectNestedReturn(subQuery, returnIri);
      }
  }

  private Match convertBaseCriteriaGroup(EQDOCBaseCriteriaGroup baseGroup) throws QueryException, EQDException, IOException {
    return this.getMatchFromGroup(baseGroup.getDefinition().getCriteria(), baseGroup.getDefinition().getMemberOperator());
  }


  private Match convertStandardCriterion(EQDOCCriterion eqCriterion) throws IOException, EQDException {
    Match match = null;
    if (!eqCriterion.getFilterAttribute().getColumnValue().isEmpty()) {
      match = this.convertColumns(eqCriterion.getTable(), eqCriterion.getId(), eqCriterion.getFilterAttribute().getColumnValue(), null);
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
    return match;
  }
  private Match convertLinkedCriterion(EQDOCCriterion eqCriterion, Match parentMatch) throws IOException, QueryException, EQDException {
    EQDOCCriterion eqLinkedCriterion = eqCriterion.getLinkedCriterion().getCriterion();
    Match linkedMatch = this.convertCriterion(eqLinkedCriterion);
    if (eqLinkedCriterion.getDescription()!=null) linkedMatch.setDescription(eqLinkedCriterion.getDescription());
    Where relationWhere = new Where();
    if (linkedMatch.getAnd() != null) {
      linkedMatch = linkedMatch.getAnd().getFirst();
    }
    addMatchWhere(linkedMatch, relationWhere);
    EQDOCRelationship eqRelationship = eqCriterion.getLinkedCriterion().getRelationship();
    String table = eqLinkedCriterion.getTable();
    String child = this.getIMPath(table + "/" + eqRelationship.getChildColumn());
    ValueSource relationLeft= new ValueSource();
    relationLeft.setNodeRef(getNodeRef(linkedMatch));
    relationLeft.setPath(new Path().setIri(child.substring(child.lastIndexOf(" ") + 1)));
    injectTestReturn(parentMatch,relationLeft.getPath().getIri());
    ValueSource relationRight= new ValueSource();
    relationRight.setNodeRef(getNodeRef(parentMatch));
    if (eqRelationship.getParentColumn().contains("DATE")) {
      relationRight.setPath(new Path().setIri(Namespace.IM + "effectiveDate"));
    } else {
      if (!eqRelationship.getParentColumn().contains("DOB")) {
        throw new QueryException("Non date linked criteria not managed yet");
      }
      relationRight.setPath(new Path().setIri(Namespace.IM + "dateOfBirth"));
    }

    if (eqRelationship.getRangeValue() != null) {
      EQDOCRangeValue eqRange = eqRelationship.getRangeValue();
      if (eqRange.getRangeFrom() != null && eqRange.getRangeTo() != null) {
        Range range = new Range();
        relationWhere.setRange(range);
        String fromValue= eqRange.getRangeFrom().getValue().getValue();
        TTIriRef fromUnits= setQualifierGetunits(relationWhere,eqRange.getRangeFrom().getValue().getUnit());
        Operator fromOperator= ((Operator) this.vocabMap.get(eqRange.getRangeFrom().getOperator()));
        Value from = new Value();
        range.setFrom(from);
        from.setOperator(fromOperator);
        from.setValue(fromValue);

        buildCompare(from,fromUnits,relationLeft,relationRight);
        Value to = new Value();
        range.setTo(to);
        String toValue= eqRange.getRangeTo().getValue().getValue();
        Operator toOperator= ((Operator) this.vocabMap.get(eqRange.getRangeTo().getOperator()));
        TTIriRef toUnits= setQualifierGetunits(relationWhere,eqRange.getRangeTo().getValue().getUnit());
        from.setOperator(toOperator);
        from.setValue(toValue);
        buildCompare(to,toUnits,relationLeft,relationRight);

      } else if (eqRelationship.getRangeValue().getRangeFrom() != null) {
        String fromValue= eqRange.getRangeFrom().getValue().getValue();
        TTIriRef fromUnits= setQualifierGetunits(relationWhere,eqRange.getRangeFrom().getValue().getUnit());
        Operator fromOperator= ((Operator) this.vocabMap.get(eqRange.getRangeFrom().getOperator()));
        relationWhere.setOperator(fromOperator);
        relationWhere.setValue(fromValue);
        buildCompare(relationWhere,fromUnits,relationLeft,relationRight);

      } else {
        String toValue= eqRange.getRangeTo().getValue().getValue();
        Operator toOperator= ((Operator) this.vocabMap.get(eqRange.getRangeTo().getOperator()));
        TTIriRef toUnits= setQualifierGetunits(relationWhere,eqRange.getRangeTo().getValue().getUnit());
        relationWhere.setOperator(toOperator);
        relationWhere.setValue(toValue);
        buildCompare(relationWhere,toUnits,relationLeft,relationRight);
      }
    } else {
      relationWhere.setCompare(new Compare());
      relationWhere.getCompare().setLeft(relationLeft);
      relationWhere.getCompare().setRight(relationRight);
      relationWhere.setOperator(Operator.eq);
    }
    return linkedMatch;
  }

  private TTIriRef setQualifierGetunits(Where where, VocValueUnit eqUnits) throws EQDException {
    if (eqUnits==null) return null;
    TTIriRef units=getIMUnits(eqUnits);
    if (units==null) return null;
    if (units.getIri().equals(IM.FISCAL_YEAR.toString())) {
      where.setQualifier(units);
      units= null;
    }
    else if (units.getIri().equals(IM.QUARTER.toString())) {
      where.setQualifier(units);
      units= null;
    }
    return units;
  }

  private void buildCompare(Assignable assignable,    TTIriRef units,
                            ValueSource relationLeft,
                            ValueSource relationRight) {

    assignable.setCompare(new Compare());
    assignable.getCompare().setLeft(relationLeft);
    assignable.getCompare().setRight(relationRight);
    if (units!=null)
        assignable.getCompare().setUnits(units);
  }


  private Match convertColumns(String table, String eqId, List<EQDOCColumnValue> columns, Match matchToTest) throws EQDException, IOException {
    int index = 0;
    Match match = new Match();
    for (EQDOCColumnValue cv : columns) {
        ++index;
        this.convertColumn(table, eqId, cv, match, index, matchToTest);
    }
    if (match.getPath()!=null) {
      match.setTypeOf(new Node().setIri(match.getPath().getFirst().getTypeOf().getIri()));
    }

    return match;
  }


  private void convertColumn(String table, String eqId, EQDOCColumnValue cv, Match match, int index, Match matchToTest) throws EQDException, IOException {
    String tablePath = this.getIMPath(table);
    String eqColumn = String.join("/", cv.getColumn());
    String eqURL = table + "/" + eqColumn;
    this.sourceContext = eqURL;
    String columnPath = getIMPath(eqURL);
    String[] fullPath = (tablePath + " " + columnPath).trim().split(" ");

    Where where = new Where();
    if (matchToTest==null) {
      String variable = this.setMatchPath(match, fullPath);
      if (variable != null) {
        where.setNodeRef(variable);
      }
    }
    addMatchWhere(match, where);
    where.setIri(fullPath[fullPath.length - 1]);
    if (matchToTest!=null){
      injectTestReturn(matchToTest,where.getIri());
    }

    this.convertColumnValue(cv, where);
  }
  private void setMatchNode(Match match){
    if (match.getNode()==null) {
      matchCounter++;
      match.setNode("Match_" + matchCounter);
    }
  }

  private void injectNestedReturn(Match match, String iri){
    if (match.getAnd()!=null){
      injectNestedReturn(match.getAnd().getLast(),iri);
      match.addReturn (new Return()
        .setNodeRef(match.getAnd().getLast().getNode())
        .setIri(iri));
    }else if (match.getStep()!=null){
      injectNestedReturn(match.getStep().getLast(),iri);
      match.addReturn (new Return()
        .setNodeRef(match.getStep().getLast().getNode())
        .setIri(iri));
    }
    else if (match.getOr()!=null){
      match.setUnion(match.getOr());
      match.setOr(null);
      setUnionReturns(match,iri);
    }
    else {
      match.addReturn (new Return()
        .setNodeRef(getNodeRef(match))
        .setIri(iri));
    }
  }

  private void injectTestReturn(Match matchToTest, String iri) {
    boolean alreadyIn= false;
    if (matchToTest.getReturn()!=null) {
      for (Return returnProp : matchToTest.getReturn()) {
        if (returnProp.getIri().equals(iri)) {
          alreadyIn= true;
          break;
        }
      }
    }
    if (!alreadyIn) {
      matchToTest.return_(p -> p.setNodeRef(getNodeRef(matchToTest)).setIri(iri));
      if (matchToTest.getUnion()!=null){
          setUnionReturns(matchToTest,iri);

      }
    }
  }

  public String setMatchPath(Match match, String[] paths) {
    if (paths.length == 1) {
      return null;
    } else {
      String path = paths[0];
      boolean inverse = path.startsWith("^");
      String pathIri = path.replaceFirst("^", "");
      if (match.getPath() != null) {
        Path pathMatch = match.getPath().getFirst();
        if (pathMatch.getIri().equals(pathIri) && pathMatch.isInverse() == inverse) {
            if (paths.length == 3) {
              return pathMatch.getNode();
            }
            return this.getPathFromPath(pathMatch, paths, 2);
        }
      }

      Path pathMatch = new Path();
      match.addPath(pathMatch);
      pathMatch.setIri(pathIri);
      pathMatch.setInverse(inverse);
      pathMatch.setNode(getAcronym(paths[1]));;
      pathMatch.setTypeOf((new Node()).setIri(paths[1]));
      return paths.length == 3 ? pathMatch.getNode() : this.getPathFromPath(pathMatch, paths, 2);
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
      setSingleValue(cv,pv,in);
    }
    if (cv.getFunction()!=null){
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

        path = inverse + (path.startsWith("http") ? path : Namespace.IM + path);
        paths[i] = path;
      }

      return String.join(" ", paths);
    }
  }


  private String resolveIri(String iri) {
    return iri.startsWith("http") ? iri : Namespace.IM + iri;
  }

  private Match convertTestCriterion(EQDOCCriterion eqCriterion,Match matchToTest) throws EQDException, IOException {
    EQDOCTestAttribute testAtt = eqCriterion.getFilterAttribute().getRestriction().getTestAttribute();
    return this.convertColumns(eqCriterion.getTable(), null, testAtt.getColumnValue(), matchToTest);
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
    if (eqRelationship.getRangeValue()==null){
      relationship.append(" on same date as");
    }
    else {
      EQDOCRangeFrom eqFrom= eqRelationship.getRangeValue().getRangeFrom();
      if (eqFrom!=null) {
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
      }
      else {
        EQDOCRangeTo eqTo= eqRelationship.getRangeValue().getRangeTo();
        if (eqTo!=null) {
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

  private void setSingleValue(EQDOCColumnValue cv, Where pv,boolean in) throws IOException, EQDException {
    EQDOCSingleValue sv = cv.getSingleValue();
    EQDOCValue variable = sv.getVariable();
    if (variable == null) {
      throw new EQDException("variable is null");
    }
    String value = variable.getValue();
    VocValueUnit eqQualifier = variable.getUnit();
    VocRelation relative = variable.getRelation();
    if (!in) pv.setNot(true);
    TTIriRef qualifier= null;
    if (eqQualifier != null) {
      qualifier= getIMQualifier(eqQualifier);
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
          .setPath(new Path().setIri(pv.getIri())));
        diff.setRight(new ValueSource()
        .setParameter("$searchDate"));
      } else if (value.equalsIgnoreCase("this")) {
        pv.setOperator(Operator.eq);
        pv.setCompare(new Compare());
        pv.setQualifier(qualifier);
        Compare comp = pv.getCompare();
        comp.setLeft(new ValueSource()
          .setPath(new Path().setIri(pv.getIri())));
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
    if (rFrom != null&&rTo!=null) {
      this.setRangeCompare(pv, rFrom, rTo, rv.getRelativeTo());
    } else if (rFrom!=null) {
        this.setCompareFrom(pv, rFrom,rv.getRelativeTo());
    } else if (rTo != null) {
      this.setCompareTo(pv, rTo, rv.getRelativeTo());
    }
  }



  private void setCompareFrom(Where where,EQDOCRangeFrom rFrom,String relativeTo) throws EQDException {
    Operator comp;
    if (rFrom.getOperator() != null) {
      comp = (Operator) this.vocabMap.get(rFrom.getOperator());
    } else {
      comp = Operator.eq;
    }
    String value = null;
    TTIriRef units = null;
    EQDOCValue eqValue= rFrom.getValue();
    VocRelation relation = null;
    if (eqValue != null) {
      value = eqValue.getValue();
      units= setQualifierGetunits(where,eqValue.getUnit());
      relation = VocRelation.ABSOLUTE;
      if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
        relation = VocRelation.RELATIVE;
      }
    }
    this.setCompare(where,where, comp,value,units, relation,relativeTo);
  }



  private void setCompareTo(Where pv, EQDOCRangeTo rTo,String relativeTo) throws EQDException {
    Operator comp;
    if (rTo.getOperator() != null) {
      comp = (Operator) this.vocabMap.get(rTo.getOperator());
    } else {
      comp = Operator.eq;
    }

    String value = null;
    TTIriRef units = null;
    VocRelation relation = null;
    EQDOCValue eqValue= rTo.getValue();
    if (eqValue!= null) {
      value = eqValue.getValue();
      units= setQualifierGetunits(pv,eqValue.getUnit());
      relation = VocRelation.ABSOLUTE;
      if (eqValue.getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
        relation = VocRelation.RELATIVE;
      }
    }

    this.setCompare(pv,pv,comp, value, units, relation,relativeTo);
  }



  private void setCompare(Where where,Assignable assignable, Operator comp, String value, TTIriRef units, VocRelation relation,String relativeTo) throws EQDException {

    if (relativeTo!=null){
      if (relativeTo.equals("BASELINE")) {
        relativeTo= "$achievementDate";
      }
      else throw new  EQDException("relative to " + relativeTo + " not supported");
    }
    String property=where.getIri();
    if (where.getIri().contains("age")) {
      property=Namespace.IM+"dateOfBirth";
    }
    assignable.setOperator(comp);
    if (value!=null && value.equals("This")) {
      assignable.setOperator(Operator.eq);
      assignable.setValueTerm("this");
    }
    else if (value!=null && value.equals("Last")) {
      assignable.setOperator(Operator.eq);
      assignable.setValue("-1");
      assignable.setValueTerm("last");
    } else if (value!=null) {
      assignable.setValue(value);
    }
    if (relation==VocRelation.RELATIVE) {
      ValueSource relationLeft = new ValueSource();
      relationLeft.setPath(new Path().setIri(property));
      ValueSource relationRight = new ValueSource();
      relationRight.setParameter(relativeTo);
      if (assignable.getValue() == null) {
        buildCompare(assignable,units, relationLeft, relationRight);
      } else buildCompare(assignable, units, relationLeft, relationRight);
    }

  }


  private TTIriRef getIMUnits(VocValueUnit units) throws EQDException {
    switch (units) {
      case YEAR :return iri(IM.YEARS);
      case MONTH: return iri(IM.MONTHS);
      case DAY : return iri(IM.DAYS);
      case FISCALYEAR: return iri(IM.FISCAL_YEAR);
      case QUARTER: return iri(IM.QUARTER);
      case DATE :break;
      default :throw new EQDException("unknown unit map: " + units);
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



  private void setRangeCompare(Where where, EQDOCRangeFrom rFrom, EQDOCRangeTo rTo,String relativeTo) throws EQDException {
    Range range = new Range();
    where.setRange(range);
    Value fromValue = new Value();
    range.setFrom(fromValue);
    Operator comp;
    if (rFrom.getOperator() != null) {
      comp = (Operator) this.vocabMap.get(rFrom.getOperator());
    } else {
      comp = Operator.eq;
    }

    String value = rFrom.getValue().getValue();
    TTIriRef units = null;
    if (rFrom.getValue().getUnit() != null) {
      units = setQualifierGetunits(where,rFrom.getValue().getUnit());
    }

    VocRelation relation = VocRelation.ABSOLUTE;
    if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
      relation = VocRelation.RELATIVE;
    }


    this.setCompare(where,fromValue,comp,value,units, relation,relativeTo);
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
      units = setQualifierGetunits(where,rTo.getValue().getUnit());
    }

    relation = VocRelation.ABSOLUTE;
    if (rTo.getValue().getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
      relation = VocRelation.RELATIVE;
    }

    this.setCompare(where,toValue, comp, value, units, relation,relativeTo);
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
              previous=name;
            }
          }
        } else {
          for (Node node : setContent) {
            if (node.isExclude()) {
              exclusions = " (exclusions)";
            }

            if (node.getName() != null && name == null) {
              name = this.getShortName(node.getName(), previous);
              previous=name;
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
      String property = Namespace.SNOMED + "127489000";
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
      List<Namespace> schemes = new ArrayList<>();
      schemes.add(Namespace.SNOMED);
      schemes.add(Namespace.EMIS);
      schemes.add(Namespace.IM);
      schemes.add(Namespace.BNF);
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

  private Set<Node> getValuesFromOriginal(String originalCode, String originalTerm, String legacyCode, List<Namespace> schemes) {
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
      return this.importMaps.getLegacyFromTermCode(originalCode, Namespace.EMIS);
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
      return this.importMaps.getCoreFromLegacyTerm(originalTerm, Namespace.EMIS);
    } catch (Exception e) {
      log.error("unable to retrieve from term {}", e.getMessage());
      return Collections.emptySet();
    }
  }

  private Set<Entity> getCoreFromCode(String originalCode, List<Namespace> schemes) {
    return this.importMaps.getCoreFromCode(originalCode, schemes);
  }

  private void addUsedIn(TTEntity set) {
    String usedIn = activeReport;
    if (EqdToIMQ.versionMap.containsKey(usedIn)) {
      usedIn = EqdToIMQ.versionMap.get(usedIn);
    }
    queryEntity.addObject(iri(IM.USES),iri(set.getIri()));
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
    String name = description == null ? this.getNameFromSet(vs,setContent) : description;
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
    String name = description == null ? this.getNameFromSet(vs,setContent) : description;
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

  private String getNameFromSet(EQDOCValueSet vs,Set<Node> set) {
    if (EqdToIMQ.getAutoNamedSets().get(namespace+ vs.getId())!=null){
      return EqdToIMQ.getAutoNamedSets().get(namespace+ vs.getId());
    }
    if (EqdToIMQ.getInlineSets().get(namespace + vs.getId()) != null) {
      return EqdToIMQ.getInlineSets().get(namespace + vs.getId()).getName();
    }
    StringBuilder name= new StringBuilder();
    if (set.size()>2) {
      name = new StringBuilder("Clinical codes..");
    } else {
      for (Node node : set) {
        if (!name.isEmpty()) name.append(" or ");
        name.append(node.getName());
      }
    }
    return name.toString();
  }


  private String getShortName(String name,String previous) {
    name = name.split(" \\(")[0];
    if (previous==null) return name;
    return name.contains("resolved") ? previous + " or resolved" : previous + " or " + name;
  }

  public String getAcronym(String iri) {
    if (iri == null || iri.isBlank()) return "";
    String local = iri.substring(iri.lastIndexOf("#")+1);
    String[] parts = local.split("(?<!^)(?=[A-Z0-9])");
    StringBuilder sb = new StringBuilder();
    for (String part : parts) {
      if (part.isBlank()) continue;
      sb.append(Character.toUpperCase(part.charAt(0)));
    }
    String acronym= sb.toString().toLowerCase();
    if (acronyms.contains(acronym)){
      int i=1;
      while(acronyms.contains(acronym+i))
        i++;
      acronym=acronym+i;
    }
    acronyms.add(acronym);
    return acronym;
  }

  private void setNamedMatchNode(Match match) {
    StringBuilder node = new StringBuilder();
    if (match.getOrderBy()!=null){
      OrderLimit orderBy= match.getOrderBy();
      for (OrderDirection property : orderBy.getProperty()) {
        String field = property.getIri();
        if (field.toLowerCase().contains("date")) {
          if (property.getDirection() == Order.descending) node.append("Latest_");
          else node.append("Earliest_");
        } else {
          if (property.getDirection() == Order.descending) node.append("Max_");
          else node.append("Min_");
        }
      }
    }
    boolean conceptFound= false;
    if (match.getWhere()!=null){
      Where where= match.getWhere();
      if (where.getAnd()!=null){
        for (Where and:where.getAnd()){
          if (and.getShortLabel()!=null){
            node.append(and.getShortLabel());
            conceptFound=true;
          } else if (and.getValueLabel()!=null) {
            String valueLabel= and.getValueLabel();
            node.append(valueLabel, 0, Math.min(valueLabel.length(), 10));
            conceptFound=true;
          }
          else if (and.getIs()!=null){
            String isName= and.getIs().getFirst().getName().replace(" ","");
            node.append(isName,0,Math.min(isName.length(),10));
          }
        }
      }
    } else {
      if (match.getUnion()!=null){
        node.append("combined_dates");
        conceptFound=true;
      }
    }
    if (conceptFound) match.setNode(node.toString());
    else setMatchNode(match);
  }



}
