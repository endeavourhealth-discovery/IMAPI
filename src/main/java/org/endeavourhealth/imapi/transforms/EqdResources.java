package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.endeavourhealth.imapi.logic.exporters.ImportMaps;
import org.endeavourhealth.imapi.logic.service.QueryDescriptor;
import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.Graph;
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
  private final Map<Integer, String> baseMatchMap = new HashMap<>();
  private final QueryDescriptor descriptor = new QueryDescriptor();
  @Getter
  Map<String, String> reportNames = new HashMap<>();
  @Getter
  private Namespace namespace;
  @Setter
  private Properties criteriaMaps;
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
  @Getter
  @Setter
  private int baseCounter = 0;
  private String sourceContext;
  @Getter
  @Setter
  private QueryType queryType;
  private int counter = 0;
  @Setter
  @Getter
  private int rule = 0;
  @Setter
  @Getter
  private int subRule = 0;

  public EqdResources(TTDocument document, Properties dataMap, Namespace namespace) {
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
      match.addInstanceOf((new Node()).setIri(this.namespace + parent).setMemberOf(true)).setName((String) this.reportNames.get(parent));
      return match;
    } else {
      List<EQDOCCriteria> groupCriteria = eqGroup.getDefinition().getCriteria();
      return this.getMatchFromGroup(groupCriteria, eqGroup.getDefinition().getMemberOperator());
    }
  }

  private Match getMatchFromGroup(List<EQDOCCriteria> groupCriteria, VocMemberOperator memberOp) throws QueryException, EQDException, IOException {
    this.subRule = 0;
    if (groupCriteria.size() <= 1) {
      EQDOCCriteria eqCriteria = (EQDOCCriteria) groupCriteria.get(0);
      if (isNegatedCriteria(eqCriteria)) {
        Match match = new Match();
        match.addNot(convertCriteria(eqCriteria));
        return match;
      } else return convertCriteria(groupCriteria.get(0));
    } else {
      Match boolMatch = new Match();
      if (memberOp == null) {
        memberOp = VocMemberOperator.OR;
      }


      for (EQDOCCriteria eqCriteria : groupCriteria) {
        if (isNegatedCriteria(eqCriteria)) {
          boolMatch.addNot(this.convertCriteria(eqCriteria));
        } else if (memberOp == VocMemberOperator.AND) {
          boolMatch.addAnd(this.convertCriteria(eqCriteria));
        } else boolMatch.addOr(this.convertCriteria(eqCriteria));
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
          return new Match().setIri(this.namespace + libraryId);
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
    match.addInstanceOf(new Node().setIri(namespace + searchId).setName((String) this.reportNames.get(search.getReportGuid())).setMemberOf(true));
    return match;
  }

  private Match convertCriterion(EQDOCCriterion eqCriterion) throws IOException, QueryException, EQDException {
    Match baseMatch = null;
    Match standardMatch = null;
    Match testMatch = null;
    Match linkedMatch;
    EQDOCFilterAttribute filter = eqCriterion.getFilterAttribute();
    boolean hasLinked = eqCriterion.getLinkedCriterion() != null;
    boolean hasStandard= (!filter.getColumnValue().isEmpty() || filter.getRestriction() != null);
    if (!eqCriterion.getBaseCriteriaGroup().isEmpty()) {
      baseMatch = this.convertBaseCriteriaGroups(eqCriterion);
      /*String baseName= new QueryDescriptor().getDescriptions(baseMatch);
      String baseDefinition = new ObjectMapper().writeValueAsString(baseMatch);
      Integer baseHash = baseDefinition.hashCode();
      if (baseMatchMap.get(baseHash) == null) {
        baseCounter++;
        TTEntity baseEntity = new TTEntity()
          .setIri(this.namespace + "BaseCriteria_" + baseHash)
          .setName(baseName)
          .setScheme(iri(namespace))
          .addType(iri(IM.QUERY));
        baseEntity.set(IM.DEFINITION, TTLiteral.literal(baseMatch));
        baseMatchMap.put(baseHash, baseEntity.getIri());
        document.addEntity(baseEntity);
      }
      baseMatch = new Match();
      baseMatch.addInstanceOf(new Node().setIri(baseMatchMap.get(baseHash)).setName(baseName).setMemberOf(true));
       */
    }


    if (hasStandard) {
      standardMatch = this.convertStandardCriterion(eqCriterion, baseMatch == null ? null : "IndexEvent");
      if (eqCriterion.getFilterAttribute().getRestriction() != null && eqCriterion.getFilterAttribute().getRestriction().getTestAttribute() != null) {
        testMatch = this.convertTestCriterion(eqCriterion);
        injectReturn(standardMatch, testMatch);
        standardMatch.setThen(testMatch);
      }
    }
    if (baseMatch!=null){
      if (standardMatch!=null) baseMatch.setThen(standardMatch);
    }
    if (hasLinked) {
      counter++;
      String as="Match_"+counter;
      if (testMatch != null) setReturn(testMatch,as);
      else if (standardMatch != null) setReturn(standardMatch,as);
      else setReturn(baseMatch,as);
      String nodeRef= standardMatch != null ? standardMatch.getReturn().getAs(): baseMatch!=null ? baseMatch.getReturn().getAs() : "error";
      linkedMatch = this.convertLinkedCriterion(eqCriterion, nodeRef);
      if (testMatch != null) testMatch.setThen(linkedMatch);
      else if (standardMatch != null) standardMatch.setThen(linkedMatch);
      else baseMatch.setThen(linkedMatch);
    }
    if (baseMatch != null) return baseMatch;
    else return standardMatch;
  }

  private Match convertBaseCriteriaGroups(EQDOCCriterion eqCriterion) throws QueryException, EQDException, IOException {
    int originalCounter = counter;
    counter = 0;
    String baseContent = (new ObjectMapper()).writeValueAsString(eqCriterion.getBaseCriteriaGroup());
    Match baseMatch;
    if (eqCriterion.getBaseCriteriaGroup().size() > 1) {
      baseMatch = new Match();
      baseMatch.setIsUnion(true);
      for (EQDOCBaseCriteriaGroup baseGroup : eqCriterion.getBaseCriteriaGroup()) {
        Match subQuery = this.convertBaseCriteriaGroup(baseGroup);
        baseMatch.addOr(subQuery);
      }

    } else {
      baseMatch = this.convertBaseCriteriaGroup(eqCriterion.getBaseCriteriaGroup().get(0));
    }

    counter = originalCounter;
    return baseMatch;
  }

  private void setReturn(Match match,String as) {
    if (match.getReturn() == null) {
      if (match.getOr() != null) {
        int orIndex = 0;
        for (Match subQuery : match.getOr()) {
          orIndex++;
          if (subQuery.getReturn() == null) {
            match.setReturn((new Return()).setAs(as + orIndex).property((p) -> p.setIri(Namespace.IM + "effectiveDate")));
          }
        }
      }
      match.setReturn((new Return()).setAs(as).property((p) -> p.setIri(Namespace.IM + "effectiveDate")));
    }
  }

  private Match convertBaseCriteriaGroup(EQDOCBaseCriteriaGroup baseGroup) throws QueryException, EQDException, IOException {
    return this.getMatchFromGroup(baseGroup.getDefinition().getCriteria(), baseGroup.getDefinition().getMemberOperator());
  }


  private Match convertStandardCriterion(EQDOCCriterion eqCriterion, String nodeRef) throws IOException, EQDException {
    Match match = null;
    if (!eqCriterion.getFilterAttribute().getColumnValue().isEmpty()) {
      match = this.convertColumns(eqCriterion.getTable(), eqCriterion.getId(), eqCriterion.getFilterAttribute().getColumnValue(), false);
    }

    if (eqCriterion.getFilterAttribute().getRestriction() != null) {
      if (match == null) {
        match = new Match();
      }

      this.setRestriction(eqCriterion, match);
    }

    if (match == null) {
      throw new EQDException("No match found for standard criterion");
    } else {
      if (nodeRef != null) {
        match.setNodeRef(nodeRef);
        match.setPath(null);
      }

      return match;
    }
  }

  private Match convertColumns(String table, String eqId, List<EQDOCColumnValue> columns, boolean isTest) throws EQDException, IOException {
    int index = 0;
    Match match = new Match();

    for (EQDOCColumnValue cv : columns) {
        ++index;
        this.convertColumn(table, eqId, cv, match, index, isTest);
    }

    return match;
  }


  private void convertColumn(String table, String eqId, EQDOCColumnValue cv, Match match, int index, boolean isTest) throws EQDException, IOException {
    String tablePath = this.getIMPath(table);
    String eqColumn = String.join("/", cv.getColumn());
    String eqURL = table + "/" + eqColumn;
    this.sourceContext = eqURL;
    String columnPath = getIMPath(eqURL);
    String[] fullPath = (tablePath + " " + columnPath).trim().split(" ");

    Where where = new Where();
    if (!isTest) {
      String variable = this.setMatchPath(match, fullPath);
      if (variable != null) {
        where.setNodeRef(variable);
      }
    }
    addMatchWhere(match, where,null);
    where.setIri(fullPath[fullPath.length - 1]);
    if ((Namespace.IM + "value").equals(where.getIri())) {
      if (match.getPath() != null) {
        match.getPath().getFirst().setTypeOf((new Node()).setIri(Namespace.IM + "Observation"));
      }
    }

    this.convertColumnValue(cv, where);
  }

  private String setMatchPath(Match match, String[] paths) {
    if (paths.length == 1) {
      return null;
    } else {
      String path = paths[0];
      boolean inverse = path.startsWith("^");
      String pathIri = path.replaceFirst("^", "");
      if (match.getPath() != null) {
        for (Path pathMatch : match.getPath()) {
          if (pathMatch.getIri().equals(pathIri) && pathMatch.isInverse() == inverse) {
            if (paths.length == 3) {
              return pathMatch.getVariable();
            }

            return this.getPathFromPath(pathMatch, paths, 2);
          }
        }
      }

      Path pathMatch = new Path();
      match.addPath(pathMatch);
      pathMatch.setIri(pathIri);
      pathMatch.setInverse(inverse);
      counter++;
      pathMatch.setVariable(paths[1].substring(paths[1].lastIndexOf("#") + 1) + counter);
      pathMatch.setTypeOf((new Node()).setIri(paths[1]));
      return paths.length == 3 ? pathMatch.getVariable() : this.getPathFromPath(pathMatch, paths, 2);
    }
  }

  public String getNodeRef(HasPaths path) {
    if (path.getPath() != null) {
      for (Path pathMatch : path.getPath()) {
        if (pathMatch.getVariable() != null && pathMatch.getPath() == null) {
          return pathMatch.getVariable();
        } else return getNodeRef(pathMatch);
      }
      return "";
    }
    return "";
  }

  private void injectReturn(Match parentMatch, Match childMatch) throws EQDException, IOException, QueryException {
    Return ret = null;
    String asLabel;
    if (parentMatch.getReturn() != null) {
      ret = parentMatch.getReturn();
      asLabel = ret.getAs();
    } else {
      asLabel = descriptor.getShortDescription(parentMatch).toLowerCase();
      if (asLabel == "") {
        counter++;
        asLabel = "Match_" + counter;
      }
      ret = new Return();
      ret.setAs(asLabel);
      parentMatch.setReturn(ret);
    }
    String nodeRef = getNodeRef(parentMatch);
    Where where = childMatch.getWhere();
    if (where != null) {
      if (where.getIri() != null) {
        ret.addProperty(new ReturnProperty().setNodeRef(nodeRef).setIri(where.getIri()));
      } else {
        for (Where subWhere : where.getAnd()) {
          if (subWhere.getIri() != null) {
            ret.addProperty(new ReturnProperty().setNodeRef(nodeRef).setIri(subWhere.getIri()));
          }
        }
      }
    }
  }


  private String getPathFromPath(Path pathMatch, String[] paths, int offset) {
    String path = paths[offset];
    boolean inverse = path.startsWith("^");
    String pathIri = path.replaceFirst("^", "");
    if (pathMatch.getPath() != null) {
      for (Path subPathMatch : pathMatch.getPath()) {
        if (subPathMatch.getIri().equals(pathIri) && subPathMatch.isInverse() == inverse) {
          if (paths.length == offset + 3) {
            return subPathMatch.getVariable();
          }

          return this.getPathFromPath(subPathMatch, paths, offset + 2);
        }
      }
    }

    Path subPathMatch = new Path();
    pathMatch.addPath(subPathMatch);
    subPathMatch.setIri(pathIri);
    subPathMatch.setInverse(inverse);
    counter++;
    subPathMatch.setVariable(paths[offset + 1].substring(paths[offset + 1].lastIndexOf("#") + 1) + counter);
    subPathMatch.setTypeOf((new Node()).setIri(paths[offset + 1]));
    return paths.length == offset + 3 ? pathMatch.getVariable() : this.getPathFromPath(pathMatch, paths, offset + 2);
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
        if (in) pv.addIs(iri);
        else pv.addNotIs(iri);
        pv.setValueLabel(valueLabel);
        this.createLibraryValueSet(iri.getIri(), vsetName);
      }
    } else if (cv.getRangeValue() != null) {
      this.setRangeValue(cv.getRangeValue(), pv);
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
    } else if (target.startsWith("{")) {
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

  private Match convertTestCriterion(EQDOCCriterion eqCriterion) throws EQDException, IOException {
    EQDOCTestAttribute testAtt = eqCriterion.getFilterAttribute().getRestriction().getTestAttribute();
    return this.convertColumns(eqCriterion.getTable(), (String) null, testAtt.getColumnValue(), true);
  }

  private void setRestriction(EQDOCCriterion eqCriterion, Match restricted) throws EQDException, IOException {
    EQDOCFilterRestriction restrict = eqCriterion.getFilterAttribute().getRestriction();
    Order direction;
    if (((EQDOCColumnOrder.Columns) restrict.getColumnOrder().getColumns().get(0)).getDirection() == VocOrderDirection.ASC) {
      direction = Order.ascending;
    } else {
      direction = Order.descending;
    }
    String linkColumn = eqCriterion.getFilterAttribute().getRestriction().getColumnOrder().getColumns().get(0).getColumn().get(0);
    String table = eqCriterion.getTable();
    String orderBy = this.getIMPath(table + "/" + linkColumn);
    if (restrict.getColumnOrder().getRecordCount() != 1000) {
      counter++;
      String asLabel = "Match_" + counter;
      restricted.setReturn(new Return());
      restricted.getReturn().setAs(asLabel);
      String nodeRef = getNodeRef(restricted);
      restricted.getReturn().orderBy((o) -> o
        .addProperty(new OrderDirection()
          .setNodeRef(nodeRef)
          .setIri(orderBy)
          .setDirection(direction))
        .setLimit(restrict.getColumnOrder().getRecordCount()));
    }
  }

  private void addMatchWhere(Match match, Where where,Integer index) {
    if (match.getWhere() == null) {
      match.setWhere(where);
    } else if (match.getWhere().getIri() != null) {
      Where boolWhere = new Where();
      boolWhere.addAnd(match.getWhere());
      match.setWhere(boolWhere);
      if (index!=null) boolWhere.getAnd().add(index,where);
      else boolWhere.addAnd(where);
    } else {
      if (index!=null) match.getWhere().getAnd().add(index,where);
      else match.getWhere().addAnd(where);
    }
  }

  private Match convertLinkedCriterion(EQDOCCriterion eqCriterion, String nodeRef) throws IOException, QueryException, EQDException {
    EQDOCCriterion eqLinkedCriterion = eqCriterion.getLinkedCriterion().getCriterion();
    Match match = this.convertCriterion(eqLinkedCriterion);
    if (eqLinkedCriterion.getDescription()!=null) match.setDescription(eqLinkedCriterion.getDescription());
    match.setReturn(null);
    Where relationProperty = new Where();
    Match linkTarget= match;
    if (match.getAnd() != null) {
      linkTarget = match.getAnd().get(0);
    }
    addMatchWhere(linkTarget, relationProperty,0);
    EQDOCRelationship eqRelationship = eqCriterion.getLinkedCriterion().getRelationship();
    String table = eqLinkedCriterion.getTable();
    String child = this.getIMPath(table + "/" + eqRelationship.getChildColumn());
    relationProperty.setNodeRef(nodeRef);
    relationProperty.setIri(child.substring(child.lastIndexOf(" ") + 1));
    String parentProperty;
    if (eqRelationship.getParentColumn().contains("DATE")) {
      parentProperty = Namespace.IM + "effectiveDate";
    } else {
      if (!eqRelationship.getParentColumn().contains("DOB")) {
        throw new QueryException("Non date linked criteria not managed yet");
      }

      parentProperty = Namespace.IM + "dateOfBirth";
    }

    if (eqRelationship.getRangeValue() != null) {
      EQDOCRangeValue eqRange = eqRelationship.getRangeValue();
      if (eqRange.getRangeFrom() != null && eqRange.getRangeTo() != null) {
        Range range = new Range();
        relationProperty.setRange(range);
        Value from = new Value();
        range.setFrom(from);
        from.setOperator((Operator) this.vocabMap.get(eqRange.getRangeFrom().getOperator())).setValue(eqRange.getRangeFrom().getValue().getValue());
        if (eqRange.getRangeFrom().getValue().getUnit() != null) {
          this.setUnitsOrArgument(relationProperty, eqRange.getRangeFrom().getValue().getUnit().value());
        }

        Value to = new Value();
        range.setTo(to);
        to.setOperator((Operator) this.vocabMap.get(eqRange.getRangeTo().getOperator())).setValue(eqRange.getRangeTo().getValue().getValue());
        if (eqRange.getRangeTo().getValue().getUnit() != null) {
          this.setUnitsOrArgument(to, eqRange.getRangeTo().getValue().getUnit().value());
        }
      } else if (eqRelationship.getRangeValue().getRangeFrom() != null) {
        relationProperty.setOperator((Operator) this.vocabMap.get(eqRange.getRangeFrom().getOperator())).setValue(eqRange.getRangeFrom().getValue().getValue());
        if (eqRange.getRangeFrom().getValue().getUnit() != null) {
          this.setUnitsOrArgument(relationProperty, eqRange.getRangeFrom().getValue().getUnit().value());
        }
      } else {
        relationProperty.setOperator((Operator) this.vocabMap.get(eqRange.getRangeTo().getOperator())).setValue(eqRange.getRangeTo().getValue().getValue());
        if (eqRange.getRangeTo().getValue().getUnit() != null) {
          this.setUnitsOrArgument(relationProperty, eqRange.getRangeTo().getValue().getUnit().value());
        }
      }
    } else {
      relationProperty.setOperator(Operator.eq);
    }

    relationProperty.setRelativeTo((new RelativeTo()).setNodeRef(nodeRef).setIri(parentProperty));
    if (match.getDescription()!=null){
      match.setDescription(match.getDescription()+" (where "+getRelationship(eqRelationship)+")");
    }
    if (eqLinkedCriterion.isNegation()){
      Match linkedMatch= new Match();
      linkedMatch.addNot(match);
      return linkedMatch;
    } else return match;
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


  private void setRangeValue(EQDOCRangeValue rv, Where pv) throws EQDException {
    EQDOCRangeFrom rFrom = rv.getRangeFrom();
    EQDOCRangeTo rTo = rv.getRangeTo();
    if (rFrom != null) {
      if (rTo == null) {
        this.setCompareFrom(pv, rFrom);
      } else {
        this.setRangeCompare(pv, rFrom, rTo);
      }
    }

    if (rTo != null && rFrom == null) {
      if (rTo.getValue() != null && rTo.getValue().getValue() != null && rTo.getValue().getValue().equals("This")) {
        if (rTo.getValue().getUnit() != VocValueUnit.FISCALYEAR) {
          throw new EQDException("unknown units with 'This' value : " + rTo.getValue().getUnit());
        }

        this.setFiscalYear(pv, rTo);
      } else {
        this.setCompareTo(pv, rTo);
      }
    }

    if (rv.getRelativeTo() != null && rv.getRelativeTo().equals("BASELINE")) {
      pv.setRelativeTo((new RelativeTo()).setParameter("$baselineDate"));
    }

  }

  private void setFiscalYear(Where where, EQDOCRangeTo rTo) throws EQDException {
    if (rTo.getOperator() == VocRangeToOperator.LT) {
      where.setValueParameter("$startOfFiscalYear");
      where.setOperator(Operator.lt);
    } else {
      if (rTo.getOperator() != VocRangeToOperator.LTEQ) {
        throw new EQDException("Unknown fiscal year operator " + rTo.getOperator().value());
      }

      where.setValueParameter("$endOfFiscalYear");
      where.setOperator(Operator.lte);
    }

  }

  private void setCompareFrom(Where where, EQDOCRangeFrom rFrom) throws EQDException {
    Operator comp;
    if (rFrom.getOperator() != null) {
      comp = (Operator) this.vocabMap.get(rFrom.getOperator());
    } else {
      comp = Operator.eq;
    }

    String value = null;
    String units = null;
    VocRelation relation = null;
    if (rFrom.getValue() != null) {
      value = rFrom.getValue().getValue();
      if (rFrom.getValue().getUnit() != null) {
        units = rFrom.getValue().getUnit().value();
      }

      relation = VocRelation.ABSOLUTE;
      if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
        relation = VocRelation.RELATIVE;
      }
    }

    this.setCompare(where, comp, value, units, relation);
  }

  private void setCompare(Where where, Value pv, Operator comp, String value, String units, VocRelation relation) throws EQDException {
    if (relation == VocRelation.RELATIVE) {
      where.setRelativeTo((new RelativeTo()).setParameter("$referenceDate"));
    }

    pv.setOperator(comp);
    pv.setValue(value);
    if (units != null) {
      this.setUnitsOrArgument(pv, units);
    }

  }

  private void setUnitsOrArgument(Assignable assignable, String units) throws EQDException {
    switch (units) {
      case "YEAR" -> assignable.setUnit(iri(IM.YEARS));
      case "MONTH" -> assignable.setUnit(iri(IM.MONTHS));
      case "DAY" -> assignable.setUnit(iri(IM.DAYS));
      case "DATE" -> { /* No units to set */ }
      default -> throw new EQDException("unknown unit map: " + units);
    }

  }

  private void setCompare(Where where, Operator comp, String value, String units, VocRelation relation) throws EQDException {
    if (relation == VocRelation.RELATIVE) {
      where.setRelativeTo((new RelativeTo()).setParameter("$referenceDate"));
    }

    if (comp != null) {
      where.setOperator(comp);
    }

    if (value != null) {
      where.setValue(value);
    }

    if (units != null) {
      this.setUnitsOrArgument(where, units);
    }

  }

  private void setCompareTo(Where pv, EQDOCRangeTo rTo) throws EQDException {
    Operator comp;
    if (rTo.getOperator() != null) {
      comp = (Operator) this.vocabMap.get(rTo.getOperator());
    } else {
      comp = Operator.eq;
    }

    String value = null;
    String units = null;
    VocRelation relation = null;
    if (rTo.getValue() != null) {
      value = rTo.getValue().getValue();
      if (rTo.getValue().getUnit() != null) {
        units = rTo.getValue().getUnit().value();
      }

      relation = VocRelation.ABSOLUTE;
      if (rTo.getValue().getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
        relation = VocRelation.RELATIVE;
      }
    }

    this.setCompare(pv, comp, value, units, relation);
  }

  private void setRangeCompare(Where where, EQDOCRangeFrom rFrom, EQDOCRangeTo rTo) throws EQDException {
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
    String units = null;
    if (rFrom.getValue().getUnit() != null) {
      units = rFrom.getValue().getUnit().value();
    }

    VocRelation relation = VocRelation.ABSOLUTE;
    if (rFrom.getValue().getRelation() != null && rFrom.getValue().getRelation() == VocRelation.RELATIVE) {
      relation = VocRelation.RELATIVE;
    }

    this.setCompare(where, fromValue, comp, value, units, relation);
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
      units = rTo.getValue().getUnit().value();
    }

    relation = VocRelation.ABSOLUTE;
    if (rTo.getValue().getRelation() != null && rTo.getValue().getRelation() == VocRelation.RELATIVE) {
      relation = VocRelation.RELATIVE;
    }

    this.setCompare(where, toValue, comp, value, units, relation);
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
    return vs.getCodeSystem() == VocCodeSystemEx.SNOMED_CONCEPT && vs.getDescription() != null && !vs.getClusterCode().contains("FlattenedCodeList") ? this.importMaps.getReferenceFromCoreTerm(vs.getDescription()) : null;
  }

  private void setInlineValues(EQDOCValueSet vs, Where pv, boolean in) throws IOException, EQDException {
    VocCodeSystemEx scheme = vs.getCodeSystem();
    if (vs.getDescription() != null) {
      pv.setShortLabel(pv.getShortLabel() != null ? pv.getShortLabel() + "_" + vs.getDescription() : vs.getDescription());
    }

    TTIriRef cluster = this.getClusterSet(vs);
    if (cluster != null) {
      if (in)
        pv.addIs((new Node()).setIri(cluster.getIri()).setName(cluster.getName()).setMemberOf(true));
      else pv.addNotIs((new Node()).setIri(cluster.getIri()).setName(cluster.getName()).setMemberOf(true));
    } else {
      Set<Node> setContent = new HashSet<>();
      for (EQDOCValueSetValue ev : vs.getValues()) {
        Set<Node> setMembers = this.processEQDOCValueSetValue(scheme, ev);
        if (!setMembers.isEmpty()) {
          for (Node memberOrConcept : setMembers) {
            if (memberOrConcept.isMemberOf()) {
              String setIri = memberOrConcept.getIri();
              TTEntity usedIn = (TTEntity) EqdToIMQ.setIriToEntity.get(setIri);
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
        if (in)
          pv.addIs((new Node()).setIri(valueSet.getIri()).setName(valueSet.getName()).setMemberOf(true));
        else pv.addNotIs((new Node()).setIri(valueSet.getIri()).setName(valueSet.getName()).setMemberOf(true));
        pv.setValueLabel(valueSet.getName() + (eclQuery.getNot() != null ? " (+exclusions)" : ""));
      } else {
        String name = null;
        String exclusions = "";
        if (setContent.size() <= 3) {
          for (Node node : setContent) {
            if (node.isExclude()) {
              exclusions = " (exclusions)";
            }
            if (in) pv.addIs(node);
            else pv.addNotIs(node);
            if (node.getName() != null && name == null) {
              name = this.getShortName(node.getName(), (String) null);
            }
          }
        } else {
          for (Node node : setContent) {
            if (node.isExclude()) {
              exclusions = " (exclusions)";
            }

            if (node.getName() != null && name == null) {
              name = this.getShortName(node.getName(), (String) null);
            }

            if (node.isMemberOf()) {
              if (in) pv.addIs(node);
              else pv.addNotIs(node);
            }
          }
          Set<Node> conceptContent = setContent.stream().filter(c -> !c.isMemberOf()).collect(Collectors.toSet());
          TTEntity valueSet = this.createValueSet(vs, conceptContent);
          if (in) pv.addIs((new Node()).setIri(valueSet.getIri()).setName(valueSet.getName()).setMemberOf(true));
          else pv.addNotIs((new Node()).setIri(valueSet.getIri()).setName(valueSet.getName()).setMemberOf(true));
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
        addMatchWhere(inMatch, where,null);
        eclQuery.addAnd(inMatch);
        Match notMatch = new Match();
        eclQuery.addNot(notMatch);
        addMatchWhere(notMatch, notWhere,null);
      } else {
        addMatchWhere(eclQuery, where,null);
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
    set.addObject(iri(IM.USED_IN), iri(this.namespace + usedIn));
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
    String name = description == null ? this.getNameFromSet(setContent) : description;
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
    String name = description == null ? this.getNameFromSet(setContent) : description;
    String entailedMembers = (new ObjectMapper()).writeValueAsString(setContent);
    TTEntity duplicate = (TTEntity) EqdToIMQ.definitionToEntity.get(entailedMembers);
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
        valueSet.addObject(iri(IM.USED_IN), iri(this.columnGroup.getIri()));
      } else {
        String usedIn = activeReport;
        if (EqdToIMQ.versionMap.containsKey(usedIn)) {
          usedIn = EqdToIMQ.versionMap.get(usedIn);
        }
        valueSet.addObject(iri(IM.USED_IN), iri(this.namespace + usedIn));
      }

      for (Node node : setContent) {
        TTNode instance = new TTNode();
        valueSet.addObject(iri(IM.ENTAILED_MEMBER), instance);
        instance.set(IM.INSTANCE_OF.toString(), iri(node.getIri()));
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

  private String getNameFromSet(Set<Node> set) {
    String name = (String) set.stream().limit(1L).map(IriLD::getName).collect(Collectors.joining(","));
    if (set.size() > 1) {
      name = name + " ... etc";
    }

    return name;
  }

  private String getShortName(String name, String previous) {
    name = name.split(" \\(")[0];
    if (previous == null) {
      return name;
    } else {
      return name.contains("resolved") ? previous + " or resolved" : previous + " or " + name;
    }
  }


}
