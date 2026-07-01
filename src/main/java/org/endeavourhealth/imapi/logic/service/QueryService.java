package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.sql.IMQtoSQLConverterKotlin;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTValueJava;
import org.endeavourhealth.imapi.queryengine.QueryDescriptor;
import org.endeavourhealth.imapi.queryengine.QueryValidator;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class QueryService {

  private final EntityRepository entityRepository = new EntityRepository();
  private final DataModelRepository dataModelRepository = new DataModelRepository();

  public Query describeQuery(Query query, DisplayMode displayMode) throws QueryException, JsonProcessingException {
    return new QueryDescriptor().describeQuery(query, displayMode);
  }

  public Match describeMatch(Match match) throws QueryException {
    return new QueryDescriptor().describeSingleMatch(match);
  }

  public Query describeQuery(String queryIri, DisplayMode displayMode) throws JsonProcessingException, QueryException {
    return new QueryDescriptor().describeQuery(queryIri, displayMode);
  }

  public String getSQLFromIMQ(QueryRequest queryRequest) throws SQLConversionException, JsonProcessingException {
    if (queryRequest.getQuery().getQueryType() == IMQType.INDICATOR) {
      TTBundle bundle = entityRepository.getBundle(queryRequest.getQuery().getIri(), Set.of(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.NUMERATOR).getIri(), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DENOMINATOR).getIri(), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_DATASET).getIri()));
      String denominator = bundle.getEntity().get(ImVocab.DENOMINATOR).getElements().getFirst().asIriRef().getIri();
      String numerator = bundle.getEntity().get(ImVocab.NUMERATOR).getElements().getFirst().asIriRef().getIri();
      String dataset = bundle.getEntity().get(ImVocab.HAS_DATASET).getElements().getFirst().asIriRef().getIri();
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

  public QueryRequest getQueryRequestForSqlConversion(QueryRequest queryRequest) throws SQLConversionException, JsonProcessingException {
    if (null == queryRequest.getQuery()) throw new SQLConversionException("Query in query request cannot be null");

    if (null != queryRequest.getLanguage() && !queryRequest.getLanguage().equals(DatabaseOption.MYSQL) && !queryRequest.getLanguage().equals(DatabaseOption.POSTGRESQL)) {
      throw new SQLConversionException("'" + queryRequest.getLanguage() + "' is not currently supported for query to SQL. Supported languages are MYSQL and POSTGRESQL.");
    }
    Query query;
    if (queryRequest.getQuery().getIri() != null && !queryRequest.getQuery().getIri().isEmpty()) {
      TTEntityJava queryEntity = entityRepository.getEntityPredicates(queryRequest.getQuery().getIri(), EnumUtils.asHashSet(ImVocab.DEFINITION, RdfVocab.TYPE)).getEntity();
      if (queryEntity.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.INDICATOR)))
        return new QueryRequest().setQuery(queryRequest.getQuery().setQueryType(IMQType.INDICATOR)).setArgument(queryRequest.getArgument());

      if (!queryEntity.has(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION)))
        throw new SQLConversionException("Query: " + queryRequest.getQuery().getIri() + " not found.");
      query = queryEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION)).asLiteral().objectValue(Query.class);
      query.setIri(queryEntity.getIri());
    } else {
      query = queryRequest.getQuery();
    }
    try {
      new LogicOptimizer().resolveLogic(query, DisplayMode.LOGICAL);
    } catch (Exception e) {
      throw new SQLConversionException(e.getMessage(), e);
    }
    if (query == null) return null;
    if (null == query.getIri()) query.setIri(UUID.randomUUID().toString());
    query.setQueryType(query.getColumnGroup() != null ? IMQType.DATASET : IMQType.COHORT);
    return new QueryRequest().setQuery(query).setArgument(queryRequest.getArgument()); // need to add update info instead of queryString
  }

  public Query getDefaultQuery() throws JsonProcessingException {
    List<TTEntityJava> children = entityRepository.getFolderChildren(NamespaceVocab.
      IM + "Q_DefaultCohorts", EnumUtils.asArray(ShaclVocab.ORDER, RdfVocab.TYPE, RdfsVocab.LABEL, ImVocab.DEFINITION));
    if (children.isEmpty()) {
      return new Query().setTypeOf(NamespaceVocab.IM + "Patient");
    }
    TTEntityJava cohort = findFirstQuery(children);
    Query defaultQuery = new Query();
    if (cohort != null) {
      Query cohortQuery = cohort.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION)).asLiteral().objectValue(Query.class);
      defaultQuery.setTypeOf(cohortQuery.getTypeOf());
      defaultQuery.addIs(new Node().setIri(cohort.getIri()).setMemberOf(true));
      return defaultQuery;
    } else return null;
  }

  private TTEntityJava findFirstQuery(List<TTEntityJava> children) {
    for (TTEntityJava child : children) {
      if (child.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.QUERY)) && child.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION)) != null) {
        return child;
      }

    }
    for (TTEntityJava child : children) {
      if (child.isType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.FOLDER))) {
        List<TTEntityJava> subchildren = entityRepository.getFolderChildren(NamespaceVocab.
          IM + "DefaultCohorts", EnumUtils.asArray(ShaclVocab.ORDER, RdfVocab.TYPE, RdfsVocab.LABEL, ImVocab.DEFINITION));
        if (subchildren == null || subchildren.isEmpty()) {
          return null;
        }
        TTEntityJava cohort = findFirstQuery(subchildren);
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
    TTEntityJava queryEntity = entityRepository.getEntityPredicates(iri, Set.of(ImVocab.DEFINITION.toString())).getEntity();
    Query query = queryEntity.get(ImVocab.DEFINITION).asLiteral().objectValue(Query.class);
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

  private void checkMatchArguments(Match match, List<ArgumentReference> missingArguments, Set<Argument> arguments) {
    if (null != match.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(match.getParameter()))) {
      addMissingArgument(missingArguments, match.getParameter(), match.getIri());
    }
    if (null != match.getIs()) {
      List<Node> instances = match.getIs();
      instances.forEach(instance -> {
        if (null != instance.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(instance.getParameter()))) {
          addMissingArgument(missingArguments, instance.getParameter(), instance.getIri());
        }
      });
    }
    if (null != match.getAnd()) {
      List<Match> matches = match.getAnd();
      matches.forEach(andMatch -> checkMatchArguments(andMatch, missingArguments, arguments));
    }
    if (null != match.getOr()) {
      List<Match> matches = match.getOr();
      matches.forEach(orMatch -> checkMatchArguments(orMatch, missingArguments, arguments));
    }
    if (null != match.getWhere()) {
      recursivelyCheckWhereArguments(match.getWhere(), missingArguments, arguments);
    }
  }

  private void recursivelyCheckWhereArguments(Where where, List<ArgumentReference> missingArguments, Set<Argument> arguments) {
    if (null != where.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(where.getParameter()))) {
      missingArguments.add(new ArgumentReference().setParameter(where.getParameter()).setReferenceIri(TTIriRefExtensionsKt.iri(new TTIriRef(), where.getIri())));
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
      missingArguments.add(new ArgumentReference().setParameter(parameter).setReferenceIri(TTIriRefExtensionsKt.iri(new TTIriRef(), referenceIri)));
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

  public Query expandCohort(String queryIri, String cohortIri, DisplayMode displayMode) throws JsonProcessingException, QueryException {
    Query query = new QueryRepository().expandCohort(queryIri, cohortIri, displayMode);
    query = new QueryDescriptor().describeQuery(query, displayMode);
    return query;
  }


  public Indicator describeIndicator(String iri) throws JsonProcessingException, QueryException {
    TTEntityJava entity = entityRepository.getEntityPredicates(iri, EnumUtils.asHashSet(RdfsVocab.LABEL, RdfsVocab.COMMENT,
      ImVocab.IS_SUBINDICATOR_OF, ImVocab.DENOMINATOR, ImVocab.NUMERATOR, ImVocab.HAS_DATASET)).getEntity();
    Indicator indicator = new Indicator();
    indicator.setIri(entity.getIri());
    indicator.setName(entity.getName());
    indicator.setDescription(entity.getDescription());
    if (entity.get(ImVocab.DENOMINATOR) != null) {
      indicator.setDenominator(entity.get(ImVocab.DENOMINATOR).asIriRef());
    }
    if (entity.get(ImVocab.NUMERATOR) != null) {
      indicator.setnumerator(entity.get(ImVocab.NUMERATOR).asIriRef());
    }
    if (entity.get(ImVocab.HAS_DATASET) != null) {
      indicator.setDataset(entity.get(ImVocab.HAS_DATASET).asIriRef());
    }
    if (entity.get(ImVocab.IS_SUBINDICATOR_OF) != null) {
      indicator.setIsSubIndicatorOf(entity.get(ImVocab.IS_SUBINDICATOR_OF).getElements()
        .stream().map(TTValueJava::asIriRef).collect(Collectors.toList()));
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
}
