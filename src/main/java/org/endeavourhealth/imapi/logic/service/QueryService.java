package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.iml.Indicator;
import org.endeavourhealth.imapi.model.iml.MatchMap;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.sql.IMQtoSQLConverterKotlin;
import org.endeavourhealth.imapi.model.sql.SubQueryDependency;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.queryengine.QueryDescriptor;
import org.endeavourhealth.imapi.queryengine.QueryValidator;
import org.endeavourhealth.imapi.vocabulary.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArray;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

@Component
@Slf4j
public class QueryService {

  private final EntityRepository entityRepository = new EntityRepository();
  private final DataModelRepository dataModelRepository = new DataModelRepository();

  public Query describeQuery(Query query, DisplayMode displayMode) throws QueryException, JsonProcessingException {
    return new QueryDescriptor().describeQuery(query, displayMode);
  }

  public Query describeMatch(Query query) throws QueryException {
    return new QueryDescriptor().describeSingleMatch(query);
  }

  public Query describeQuery(String queryIri, DisplayMode displayMode) throws JsonProcessingException, QueryException {
    return new QueryDescriptor().describeQuery(queryIri, displayMode);
  }

  public String getSQLFromIMQ(QueryRequest queryRequest) throws SQLConversionException, JsonProcessingException {
    if (queryRequest.getQuery().getQueryType() == IMQType.INDICATOR) {
      TTBundle bundle = entityRepository.getBundle(queryRequest.getQuery().getIri(), Set.of(iri(IM.NUMERATOR).getIri(), iri(IM.DENOMINATOR).getIri(), iri(IM.HAS_DATASET).getIri()));
      String denominator = bundle.getEntity().get(IM.DENOMINATOR).getElements().getFirst().asIriRef().getIri();
      String numerator = bundle.getEntity().get(IM.NUMERATOR).getElements().getFirst().asIriRef().getIri();
      String dataset = bundle.getEntity().get(IM.HAS_DATASET).getElements().getFirst().asIriRef().getIri();
      return new IMQtoSQLConverterKotlin(queryRequest, new ObjectMapper(), denominator, numerator, dataset).getSql();
    }
    QueryRequest queryRequestForSql = getQueryRequestForSqlConversion(queryRequest);
    return new IMQtoSQLConverterKotlin(queryRequestForSql).getSql();
  }

  public String getSQLFromIMQIri(String queryIri, DatabaseOption lang) throws JsonProcessingException, SQLConversionException {
    QueryRequest queryRequest = new QueryRequest().setQuery(new Query().setIri(queryIri)).setLanguage(lang);
    QueryRequest queryRequestForSql = getQueryRequestForSqlConversion(queryRequest);
    return new IMQtoSQLConverterKotlin(queryRequestForSql).getSql();
  }

  public String getSQLPatientTraceFromIMQIri(String queryIri, String patientId, DatabaseOption lang) throws JsonProcessingException, SQLConversionException {
    QueryRequest queryRequest = new QueryRequest().setQuery(new Query().setIri(queryIri)).setLanguage(lang);
    QueryRequest queryRequestForSql = getQueryRequestForSqlConversion(queryRequest);
    return new IMQtoSQLConverterKotlin(queryRequestForSql, new ObjectMapper(), null, null, null, patientId).getSql();
  }

  public QueryRequest getQueryRequestForSqlConversion(QueryRequest queryRequest) throws SQLConversionException, JsonProcessingException {
    if (null == queryRequest.getQuery()) throw new SQLConversionException("Query in query request cannot be null");

    if (null != queryRequest.getLanguage() && !queryRequest.getLanguage().equals(DatabaseOption.MYSQL) && !queryRequest.getLanguage().equals(DatabaseOption.POSTGRESQL)) {
      throw new SQLConversionException("'" + queryRequest.getLanguage() + "' is not currently supported for query to SQL. Supported languages are MYSQL and POSTGRESQL.");
    }
    Query query;
    if (queryRequest.getQuery().getIri() != null && !queryRequest.getQuery().getIri().isEmpty()) {
      TTEntity queryEntity = entityRepository.getEntityPredicates(queryRequest.getQuery().getIri(), asHashSet(IM.DEFINITION, RDF.TYPE)).getEntity();
      if (queryEntity.isType(iri(IM.INDICATOR)))
        return new QueryRequest().setQuery(queryRequest.getQuery().setQueryType(IMQType.INDICATOR)).setArgument(queryRequest.getArgument());

      if (!queryEntity.has(iri(IM.DEFINITION)))
        throw new SQLConversionException("Query: " + queryRequest.getQuery().getIri() + " not found.");
      query = queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
      query.setIri(queryEntity.getIri());
    } else {
      query = queryRequest.getQuery();
    }
    try {
      new LogicOptimizer().resolveLogic(query, DisplayMode.LOGICAL);
      LogicOptimizer.optimiseAgeWheres(query);
      LogicOptimizer.optimiseNegativeIntervalWheres(query);
    } catch (Exception e) {
      throw new SQLConversionException(e.getMessage(), e);
    }
    if (query == null) return null;
    if (null == query.getIri()) query.setIri(UUID.randomUUID().toString());
    query.setQueryType(query.getColumnGroup() != null ? IMQType.DATASET : IMQType.COHORT);
    return new QueryRequest().setQuery(query).setArgument(queryRequest.getArgument()); // need to add update info instead of queryString
  }

  public Query getDefaultQuery() throws JsonProcessingException {
    List<TTEntity> children = entityRepository.getFolderChildren(NAMESPACE.IM + "Q_DefaultCohorts", asArray(SHACL.ORDER, RDF.TYPE, RDFS.LABEL, IM.DEFINITION));
    if (children.isEmpty()) {
      return new Query().setTypeOf(NAMESPACE.IM + "Patient");
    }
    TTEntity cohort = findFirstQuery(children);
    Query defaultQuery = new Query();
    if (cohort != null) {
      Query cohortQuery = cohort.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
      defaultQuery.setTypeOf(cohortQuery.getTypeOf());
      defaultQuery.setIs(new Node().setIri(cohort.getIri()).setMemberOf(true));
      return defaultQuery;
    } else return null;
  }

  private TTEntity findFirstQuery(List<TTEntity> children) {
    for (TTEntity child : children) {
      if (child.isType(iri(IM.QUERY)) && child.get(iri(IM.DEFINITION)) != null) {
        return child;
      }

    }
    for (TTEntity child : children) {
      if (child.isType(iri(IM.FOLDER))) {
        List<TTEntity> subchildren = entityRepository.getFolderChildren(NAMESPACE.IM + "DefaultCohorts", asArray(SHACL.ORDER, RDF.TYPE, RDFS.LABEL, IM.DEFINITION));
        if (subchildren == null || subchildren.isEmpty()) {
          return null;
        }
        TTEntity cohort = findFirstQuery(subchildren);
        if (cohort != null) return cohort;
      }
    }
    return null;
  }

  public Query flattenQuery(Query query) {
    LogicOptimizer.optimizeQuery(query);
    return query;
  }

  public Query optimiseECLQuery(Query query) {
    LogicOptimizer.optimiseECLQuery(query);
    return query;
  }

  public Query getQueryFromIri(String iri) throws JsonProcessingException, QueryException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION.toString())).getEntity();
    Query query = queryEntity.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
    new QueryDescriptor().describeQuery(query, DisplayMode.ORIGINAL);
    return query;
  }

  public List<ArgumentReference> findMissingArguments(QueryRequest queryRequest) throws QueryException, JsonProcessingException {
    List<ArgumentReference> missingArguments = new ArrayList<>();
    Query query = describeQuery(queryRequest.getQuery().getIri(), DisplayMode.LOGICAL);
    Set<Argument> arguments = queryRequest.getArgument();
    if (null == arguments) arguments = new HashSet<>();
    recursivelyCheckQueryArguments(query, missingArguments, arguments);
    if (!missingArguments.isEmpty()) {
      for (ArgumentReference argument : missingArguments) {
        TTIriRef dataType = dataModelRepository.getPathDatatype(argument.getReferenceIri().getIri());
        if (null != dataType) argument.setDataType(dataType);
      }
    }
    return missingArguments;
  }

  private void recursivelyCheckQueryArguments(Query query, List<ArgumentReference> missingArguments, Set<Argument> arguments) {
    checkMatchArguments(query, missingArguments, arguments);
  }

  private void checkMatchArguments(Query query, List<ArgumentReference> missingArguments, Set<Argument> arguments) {
    if (null != query.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(query.getParameter()))) {
      addMissingArgument(missingArguments, query.getParameter(), query.getIri());
    }
    if (null != query.getIs()) {
      Node instance = query.getIs();
      if (null != instance.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(instance.getParameter()))) {
        addMissingArgument(missingArguments, instance.getParameter(), instance.getIri());
      }
    }
    if (null != query.getAnd()) {
      List<Query> queries = query.getAnd();
      queries.forEach(andMatch -> checkMatchArguments(andMatch, missingArguments, arguments));
    }
    if (null != query.getOr()) {
      List<Query> queries = query.getOr();
      queries.forEach(orMatch -> checkMatchArguments(orMatch, missingArguments, arguments));
    }
    if (null != query.getWhere()) {
      recursivelyCheckWhereArguments(query.getWhere(), missingArguments, arguments);
    }
  }

  private void recursivelyCheckWhereArguments(Where where, List<ArgumentReference> missingArguments, Set<Argument> arguments) {
    if (null != where.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(where.getParameter()))) {
      missingArguments.add(new ArgumentReference().setParameter(where.getParameter()).setReferenceIri(iri(where.getIri())));
    }
    if (null != where.getAnd()) {
      where.getAnd().forEach(and -> recursivelyCheckWhereArguments(and, missingArguments, arguments));
    }
    if (null != where.getOr()) {
      where.getOr().forEach(or -> recursivelyCheckWhereArguments(or, missingArguments, arguments));
    }
    if (null != where.getIs() && !where.isNot()) {
      where.getIs().forEach(is -> {
        if (null != is.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(is.getParameter()))) {
          addMissingArgument(missingArguments, is.getParameter(), is.getIri());
        }
      });
    }
    if (null != where.getIs() && where.isNot()) {
      where.getIs().forEach(notIs -> {
        if (null != notIs.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(notIs.getParameter()))) {
          addMissingArgument(missingArguments, notIs.getParameter(), notIs.getIri());
        }
      });
    }
  }

  private void addMissingArgument(List<ArgumentReference> missingArguments, String parameter, String referenceIri) {
    if (missingArguments.stream().noneMatch(missingArgument -> missingArgument.getParameter().equals(parameter))) {
      missingArguments.add(new ArgumentReference().setParameter(parameter).setReferenceIri(iri(referenceIri)));
    }
  }

  public TTIriRef getArgumentType(String referenceIri) {
    if (null == referenceIri) {
      throw new IllegalArgumentException("referenceIri is null");
    }
    return dataModelRepository.getPathDatatype(referenceIri);
  }


  private void getNodeRefs(Where where, Set<String> nodeRefs) {
    if (where.getNodeRef() != null) {
      nodeRefs.add(where.getNodeRef());
    }
    for (List<Where> whereList : Arrays.asList(where.getAnd(), where.getOr())) {
      if (whereList != null) {
        for (Where subWhere : whereList)
          getNodeRefs(subWhere, nodeRefs);
      }
    }
  }

  public Query expandCohort(String cohortIri, DisplayMode displayMode) throws JsonProcessingException, QueryException {
    Query query = new QueryRepository().expandCohort(cohortIri, displayMode);
    query = new QueryDescriptor().describeQuery(query, displayMode);
    return query;
  }


  public Indicator describeIndicator(String iri) throws JsonProcessingException, QueryException {
    TTEntity entity = entityRepository.getEntityPredicates(iri, asHashSet(RDFS.LABEL, RDFS.COMMENT,
      IM.IS_SUBINDICATOR_OF, IM.DENOMINATOR, IM.NUMERATOR, IM.HAS_DATASET)).getEntity();
    Indicator indicator = new Indicator();
    indicator.setIri(entity.getIri());
    indicator.setName(entity.getName());
    indicator.setDescription(entity.getDescription());
    if (entity.get(IM.DENOMINATOR) != null) {
      indicator.setDenominator(entity.get(IM.DENOMINATOR).asIriRef());
    }
    if (entity.get(IM.NUMERATOR) != null) {
      indicator.setnumerator(entity.get(IM.NUMERATOR).asIriRef());
    }
    if (entity.get(IM.HAS_DATASET) != null) {
      indicator.setDataset(entity.get(IM.HAS_DATASET).asIriRef());
    }
    if (entity.get(IM.IS_SUBINDICATOR_OF) != null) {
      indicator.setIsSubIndicatorOf(entity.get(IM.IS_SUBINDICATOR_OF).getElements()
        .stream().map(TTValue::asIriRef).collect(Collectors.toList()));
    }
    return indicator;
  }

  public Collection<SubQueryDependency> getOrderedSubqueries(String queryIri, Boolean isIndicator) {
    if (isIndicator) return getIndicatorSubqueries(queryIri);
    return entityRepository.getOrderedSubqueries(queryIri);
  }

  public Collection<SubQueryDependency> getIndicatorSubqueries(String queryIri) {
    return entityRepository.getIndicatorSubqueries(queryIri);
  }


  public Query validateQuery(Query query) {
    QueryValidator validator = new QueryValidator();
    query.setInvalid(false);
    try {
      validator.validateQuery(query);
    } catch (Exception e) {
      query.setInvalid(true);
      query.setErrorMessage(e.getMessage());
    }
    return query;
  }

  public Set<TTEntity> getSemanticMapsForMatch(MatchMap matchMap) {
    Query query = matchMap.getMatch();
    Return column= matchMap.getReturn();
    Set<String> sourceIris = new HashSet<>();
    Set<String> properties= new HashSet<>();
    properties.add(getPropertyFromColumn(query,column));
    if (query.getWhere() != null) {
      collectSourcesFromWhere(query.getWhere(), sourceIris);
    }
    if (query.getThen() != null && query.getThen().getWhere() != null) {
      collectSourcesFromWhere(query.getThen().getWhere(), sourceIris);
    }
    if (sourceIris.isEmpty()){
      DataModelRepository dataModelRepository = new DataModelRepository();
      TTIriRef valueSet= dataModelRepository.getPropertyValueSet(query.getTypeOf().getIri(),properties);
      if (valueSet!=null) {
        sourceIris.add(valueSet.getIri());
      }
    }
    if (!sourceIris.isEmpty()) {
      Set<String> fullSet = new HashSet<>(sourceIris);
      SetService setService = new SetService();
      for (String sourceIri : sourceIris) {
        Set<TTIriRef>  subsets= setService.getSubsets(sourceIri);
        if (!subsets.isEmpty()) {
          fullSet.addAll(subsets.stream().map(TTIriRef::getIri).collect(Collectors.toSet()));
        }
      }
      return new QueryRepository().getSemanticMapsForSourceEntities(properties,fullSet);
    }
    return null;
  }

  private String getPropertyFromColumn(HasPaths pathable, Return column) {
    if (column.getNodeRef()!=null) {
      String nodeRef = column.getNodeRef();
      if (pathable.getPath() != null) {
        for (Path path : pathable.getPath()) {
          if (path.getNode().equals(nodeRef)) return path.getIri();
          if (path.getPath() != null) {
            String property = getPropertyFromColumn(path, column);
            if (property != null) return property;
          }
        }
      }
    }
    return column.getIri();
  }


  private void collectSourcesFromWhere(Where where, Set<String> sourceIris) {
    if (where.getIs() != null) {
      for (Node is : where.getIs()) {
        sourceIris.add(is.getIri());
      }
    }
    for (List<Where> whereList : Arrays.asList(where.getAnd(), where.getOr())) {
      if (whereList != null) {
        for (Where subWhere : whereList) {
          collectSourcesFromWhere(subWhere, sourceIris);
        }
      }
    }
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
    return acronym;
  }

  public Query generateMatchForSemanticMap(MatchMap matchMap) {
    Query match= matchMap.getMatch();
    if (match.getReturn() == null) return match;
    Query mappedMatch=match;
    String baseType = matchMap.getBaseType();
    if (match.getTypeOf() != null && !match.getTypeOf().getIri().equals(baseType)) {
      mappedMatch= new Query();
      mappedMatch.addAnd(match);
      mappedMatch.setReturn(match.getReturn());
      match.setReturn(null);

      mappedMatch.setName(match.getName());
    }
    for (Return column : match.getReturn()) {
      if (column.getCase() == null && column.getSemanticMap() != null) {
        String semanticMapIri = column.getSemanticMap().getIri();
        TTEntity mapSource = new QueryRepository().getMapSourceProperties(semanticMapIri);
        String sourceValueProperty;
        String sourceEntityProperty;
        TTArray valueProperty = mapSource.get(IM.SOURCE_VALUE_PROPERTY);
        if (valueProperty != null) {
          sourceValueProperty = valueProperty.asIriRef().getIri();
        } else {
          sourceValueProperty = null;
        }
        TTArray entityProperty = mapSource.get(IM.SOURCE_ENTITY_PROPERTY);
        if (entityProperty != null) {
          sourceEntityProperty = entityProperty.asIriRef().getIri();
        } else {
          sourceEntityProperty = null;
        }
        if (sourceEntityProperty == null) {
          return matchMap.getMatch();
        }
        if (match.getTypeOf() != null && !match.getTypeOf().getIri().equals(baseType)) {
          setComplexSemanticMatch(match, mapSource, sourceEntityProperty, sourceValueProperty, semanticMapIri, column);
        } else {
          setSimpleSemanticMatch(match, mapSource, sourceEntityProperty, sourceValueProperty, semanticMapIri, column);
        }
        setSemanticMatchCase(mappedMatch,mapSource,column);
      }
    }
    return mappedMatch;
  }

  private Path getOrSetPathFromProperty(Query query, String property,String typeOf) {
    if (query.getPath()!=null){
      for (Path path:query.getPath()) {
        if (path.getIri().equals(property)) return path;
      }
    }
    Path path = new Path();
    query.addPath(path);
    path.setIri(property);
    path.setTypeOf(typeOf);
    return path;
  }

  private void setSimpleSemanticMatch(Query match, TTEntity mapSource,String sourceEntityProperty, String sourceValueProperty, String semanticMapIri,Return column) {
    Path path = getOrSetPathFromProperty(match,sourceEntityProperty,IM.CONCEPT.toString());
    match.addPath(path);
    setMapPath(match,path,sourceValueProperty,semanticMapIri);
  }

  private void setSemanticMatchCase(Query match, TTEntity mapSource,Return caseReturn) {
    caseReturn.setIri((String) null);
    String defaultText= mapSource.get(IM.DEFAULT_TEXT).asLiteral().getValue();
    When when= new When();
    when.setNodeRef("mapEntry")
      .setIri(IM.TARGET_TEXT.toString())
      .setNotNull(true);
    when
      .then(t->t
        .setIri(IM.TARGET_TEXT.toString()));
    Case case_ = new Case();
    caseReturn.setCase(case_);
    case_.addWhen(when);
    case_.else_(
      e->e.setValue(defaultText)
      );
  }


  private void setComplexSemanticMatch (Query match,TTEntity mapSource,String entityProperty,
                                         String sourceValueProperty,String semanticMapIri,Return column){

    match.addReturn(new Return().setIri(entityProperty));
    if (sourceValueProperty!=null) {
      match.addReturn(new Return().setIri(sourceValueProperty));
    }
    Query then= new Query();
    match.setThen(then);
    setMapPath(then,then,sourceValueProperty,semanticMapIri);
  }

  private void setMapPath(Query match,HasPaths rootPath,String sourceValueProperty,String semanticMapIri) {
    Path mapEntryPath = new Path(); // outer join on map entry
    rootPath.addPath(mapEntryPath);
    mapEntryPath.setOptional(true);
    mapEntryPath.setIri(IM.HAS_MAP_ENTRY);
    mapEntryPath.setTypeOf(IM.MAP_ENTRY);
    mapEntryPath.setNode("mapEntry");
    Where where = new Where();
    Where mapWhere = new Where();  //matches only on the map entries needed
    mapWhere.setNodeRef("mapEntry")
      .setIri(IM.IN_SEMANTIC_MAP.toString())
      .is(is -> is.setIri(semanticMapIri));
    if (sourceValueProperty != null) {
      mapEntryPath.setWhere(where);
      where
        .addAnd(mapWhere)
        .and(w1 -> w1    //range match rules
          .or(w2 -> w2
            .and(w3 -> w3
              .setIri(sourceValueProperty)
              .setNotNull(true))
            .and(w3 -> w3
              .setIri(sourceValueProperty)
              .range(r -> r
                .from(f -> f
                  .setNodeRef("mapEntry")
                  .setIri(IM.RANGE_FROM.toString())
                  .setOperator(Operator.gte))
                .to(t -> t
                  .setNodeRef("mapEntry")
                  .setIri(IM.RANGE_TO.toString())
                  .setOperator(Operator.lte)))))
          .or(w2 -> w2.  //match withut range match
            and(w3 -> w3
            .setIri(sourceValueProperty)
            .setIsNull(true))
            .and(w3 -> w3
              .setNodeRef("mapEntry")
              .setIsNull(true)
              .setIri(IM.RANGE_FROM.toString()))));
    } else {
      mapEntryPath.setWhere(mapWhere); //any match
    }
    if (sourceValueProperty != null) {
      match.setOrderBy(new OrderLimit());
      OrderLimit orderLimit = match.getOrderBy();
      orderLimit.addProperty(new OrderDirection()
        .setNodeRef("mapEntry")
        .setIri(SHACL.ORDER.toString())
        .setDirection(Order.ascending));
      orderLimit.setLimit(1);
      match.setAs("mapEntry");
    }

  }


}
