package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.functionscore.ScriptScoreQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;

import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

public class IMQToOS {
  private static final String SCHEME = "scheme";
  private static final String USAGE_TOTAL = "usageTotal";
  private static final String STATUS = "status";
  private QueryRequest request;
  private Query query;
  @Getter
  private boolean ignoreInvalid;

  public IMQToOS setIgnoreInvalid(boolean ignoreInvalid) {
    this.ignoreInvalid = ignoreInvalid;
    return this;
  }

  public SearchSourceBuilder buildQuery(QueryRequest imRequest, Query oneQuery, TextSearchStyle type, Fuzziness fuzziness) throws QueryException {
    request = imRequest;
    query = oneQuery;
    request.setTextSearch(request.getTextSearch()
      .replace("\"", "")
      .replace("{", "")
      .replace("}", ""));
    switch (type) {
      case autocomplete -> {
        return autocompleteQuery();
      }
      case multiword -> {
        return multiWordQuery();
      }
      case ngram -> {
        return nGramQuery(fuzziness);
      }
    }
    throw new QueryException("Valid query type needed");
  }

  public SearchSourceBuilder buildQuery(QueryRequest imRequest, Query oneQuery, TextSearchStyle type) throws QueryException {
    return buildQuery(imRequest, oneQuery, type, Fuzziness.ZERO);
  }

  private SearchSourceBuilder nGramQuery(Fuzziness fuzziness) throws QueryException {

    BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
    if (!addMatches(boolBuilder))
      return null;
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    if (!addReturns(sourceBuilder))
      return null;
    String text = request.getTextSearch();
    MatchQueryBuilder mat = new MatchQueryBuilder("termCode.term", text);
    mat.analyzer("standard");
    mat.operator(Operator.AND);
    mat.fuzziness(fuzziness);
    Map<String, Object> params = new HashMap<>();
    params.put("term", text);
    Script script = new Script(ScriptType.INLINE,
      Script.DEFAULT_SCRIPT_LANG,
      "double s= 100000; if (doc['termCode.term.keyword'].value.toLowerCase().startsWith(params.term)) s=200000; return s - doc['termCode.length'].value",
      Collections.emptyMap(),
      params);

    NestedQueryBuilder nested = buildNested(mat, script);
    boolBuilder.must(nested);
    sourceBuilder.query(boolBuilder);
    addPages(sourceBuilder);
    return sourceBuilder;
  }

  private SearchSourceBuilder multiWordQuery() throws QueryException {
    BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
    if (!addMatches(boolBuilder))
      return null;
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    if (!addReturns(sourceBuilder))
      return null;

    String text = request.getTextSearch();
    text = text.replace("(", "").replace(")", "").replace("-", "");
    MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term", text);
    mfs.boost(4);
    mfs.analyzer("standard");
    mfs.slop(text.split(" ").length - 1);
    Map<String, Object> params = new HashMap<>();
    params.put("term", text);
    Script script = new Script(ScriptType.INLINE,
      Script.DEFAULT_SCRIPT_LANG,
      "double s= 100000; if (doc['termCode.term.keyword'].value.toLowerCase().startsWith(params.term)) s=200000; return s - doc['termCode.length'].value",
      Collections.emptyMap(),
      params);
    NestedQueryBuilder nested = buildNested(mfs, script);
    boolBuilder.must(nested);
    sourceBuilder.query(boolBuilder);
    addPages(sourceBuilder);
    return sourceBuilder;
  }

  private NestedQueryBuilder buildNested(QueryBuilder query, Script script) {
    ScriptScoreQueryBuilder ssb = new ScriptScoreQueryBuilder(
      query,
      script
    );
    String[] includes = {"termCode.term"};
    return new NestedQueryBuilder("termCode", ssb, ScoreMode.Max)
      .innerHit(new InnerHitBuilder()
        .setFetchSourceContext(new FetchSourceContext(true, includes, null)));
  }

  private SearchSourceBuilder autocompleteQuery() throws QueryException {
    BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
    String term = request.getTextSearch();
    if (term.contains(":") && (!term.contains(" "))) {
      String namespace = EntityCache.getDefaultPrefixes().getNamespace(term.substring(0, term.indexOf(":")));
      if (namespace != null) {
        String[] splits = term.split(":");
        if (splits.length == 2) term = namespace + term.split(":")[1];
      }
    }
    addCodesAndIri(boolBuilder, term);

    String prefix = term.replaceAll("[ '()\\-_./]", "").toLowerCase();
    String field = "termCode.keyTerm";
    if (prefix.length() > 31)
      field = "termCode.term.keyword";
    PrefixQueryBuilder pqb = new PrefixQueryBuilder(field, prefix).caseInsensitive(true);
    Script script = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, "100000 - doc['termCode.length'].value", Collections.emptyMap());
    NestedQueryBuilder nested = buildNested(pqb, script);
    boolBuilder.should(nested);
    boolBuilder.minimumShouldMatch(1);
    if (!addMatches(boolBuilder))
      return null;
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    if (!addReturns(sourceBuilder))
      return null;
    sourceBuilder.query(boolBuilder);
    addPages(sourceBuilder);
    return sourceBuilder;
  }

  private boolean addMatches(BoolQueryBuilder boolBuilder) throws QueryException {
    if (query == null)
      return true;
    if (query.isActiveOnly()) {
      addFilterWithId("status", asHashSet(IM.ACTIVE), Bool.and, boolBuilder);
    }
    if (query.getAnd() == null && query.getOr() == null) {
      if (!addMatch(boolBuilder, query))
        return ignoreInvalid;
    } else {
      List<Match> ands = query.getAnd();
      List<Match> ors = query.getOr();
      for (List<Match> matches : Arrays.asList(ands, ors)) {
        if (matches != null) {
          for (Match match : matches) {
            if (!addMatch(boolBuilder, match)) {
              return ignoreInvalid;
            }
          }
        }
      }
    }
    return true;
  }

  private void addCodesAndIri(BoolQueryBuilder boolBuilder, String term) {
    TermQueryBuilder tqb = new TermQueryBuilder("code", term);
    boolBuilder.should(tqb);
    TermQueryBuilder tqac = new TermQueryBuilder("alternativeCode", term);
    boolBuilder.should(tqac);
    TermQueryBuilder tqiri = new TermQueryBuilder("iri", term);
    boolBuilder.should(tqiri);
    TermQueryBuilder tciri = new TermQueryBuilder("termCode.code", term);
    boolBuilder.should(tciri);


  }

  private void addPages(SearchSourceBuilder sourceBuilder) {
    if (request.getPage() != null) {
      sourceBuilder.size(request.getPage().getPageSize()).from(request.getPage().getPageSize() * (request.getPage().getPageNumber() - 1));
    } else {
      sourceBuilder.size(1000).from(0);
    }
  }

  private boolean addReturns(SearchSourceBuilder sourceBuilder) {
    Set<String> sources = new HashSet<>(List.of("name", "iri", "preferredName", "code", "usageTotal", "type", "scheme", "status"));
    if (query == null)
      return true;
    if (query.getReturn() != null) {
      Return ret = query.getReturn();
      if (ret.getProperty() != null) {
        for (ReturnProperty prop : ret.getProperty()) {
          if (prop.getIri() != null) {
            switch (OpenSearch.from(prop.getIri())) {
              case OpenSearch.DESCRIPTION:
                sources.add("description");
                break;
              case OpenSearch.NAME:
                sources.add("name");
                break;
              case OpenSearch.CODE:
                sources.add("code");
                break;
              case OpenSearch.STATUS:
                sources.add(STATUS);
                break;
              case OpenSearch.ALTERNATIVE_CODE:
                sources.add("alternativeCode");
                break;
              case OpenSearch.SCHEME:
                sources.add(SCHEME);
                break;
              case OpenSearch.TYPE:
                sources.add("type");
                break;
              case OpenSearch.USAGE_TOTAL:
                sources.add(USAGE_TOTAL);
                break;
              case OpenSearch.BINDING:
                sources.add("binding");
                break;
              case OpenSearch.TERM_CODE:
                sources.add("termCode");
                break;
              case OpenSearch.DOMAIN:
                break;
              default:
                return false;
            }
          }
        }
      }
    }
    String[] sourceArray = sources.toArray(String[]::new);
    sourceBuilder.fetchSource(sourceArray, null);
    return true;
  }

  private void addFilterWithId(String property, Set<String> values, Bool bool, BoolQueryBuilder boolBldr) {
    TermsQueryBuilder tqr = new TermsQueryBuilder(property + ".iri", values);
    if (Bool.and == bool) boolBldr.filter(tqr);
    else if (Bool.or == bool) boolBldr.should(tqr);
  }

  private void addFilter(Set<String> values, Bool bool, BoolQueryBuilder boolBldr) {
    TermsQueryBuilder tqr = new TermsQueryBuilder("binding", values);
    if (Bool.and == bool) boolBldr.filter(tqr);
    else if (Bool.or == bool) boolBldr.should(tqr);
  }

  private boolean addMatch(BoolQueryBuilder boolBuilder, Match match) throws QueryException {
    if (match.getAnd() != null || match.getOr() != null)
      return false;
    if (match.getTypeOf() != null) {
      addFilterWithId("type", getIriFromAlias(match.getTypeOf()), Bool.and, boolBuilder);
    }

    if (match.getInstanceOf() != null) {
      setFromAliases(boolBuilder, match.getInstanceOf());
    }
    return addProperties(boolBuilder, match);
  }

  private boolean addProperties(BoolQueryBuilder boolBuilder, Match match) throws QueryException {
    if (match.getPath() != null) {
      for (Path pathMatch : match.getPath()) {
        String w = pathMatch.getIri();
        if (IM.BINDING.equals(w)) {
          return addBinding(match, match.getOr() != null ? Bool.or : Bool.and, boolBuilder);
        }
      }
    }
    if (match.getWhere() == null)
      return true;
    Where where = match.getWhere();
    if (isBooleanWhere(where)) {
      BoolQueryBuilder nestedBool = new BoolQueryBuilder();
      for (List<Where> nested : Arrays.asList(where.getOr(), where.getAnd())) {
        if (nested != null) {
          for (Where nestedWhere : nested) {
            if (!addProperty(nestedWhere, where.getAnd() != null ? Bool.and : Bool.or, nestedBool)) return false;
          }
        }
      }
      boolBuilder.filter(nestedBool);
    } else return addProperty(where, Bool.and, boolBuilder);

    return true;
  }

  private boolean addProperty(Where where, Bool bool, BoolQueryBuilder boolBldr) throws QueryException {
    String w = where.getIri();
    if (IM.HAS_SCHEME.equals(w)) {
      return addIsFilter("scheme", where, bool, boolBldr);
    } else if (IM.IS_MEMBER_OF.equals(w)) {
      return addIsFilter("memberOf", where, bool, boolBldr);
    } else if (IM.HAS_MEMBER.equals(w) && where.isInverse()) {
      return addIsFilter("memberOf", where, bool, boolBldr);
    } else if (IM.HAS_STATUS.equals(w)) {
      return addIsFilter("status", where, bool, boolBldr);
    } else if (RDF.TYPE.equals(w)) {
      return addIsFilter("type", where, bool, boolBldr);
    } else if (IM.IS_A.equals(w)) {
      return addIsFilter("isA", where, bool, boolBldr);
    } else if (IM.CONTENT_TYPE.equals(w)) {
      return addIsFilter("contentType", where, bool, boolBldr);
    }
    return false;
  }

  private boolean isBooleanWhere(Where where) {
    return where.getAnd() != null || where.getOr() != null;
  }

  private boolean addIsFilter(String property, Where where, Bool bool, BoolQueryBuilder boolBldr) throws QueryException {
    if (where.getIs() != null) {
      Set<String> isList = new HashSet<>();
      for (Node is : where.getIs()) {
        isList.addAll(getIriFromAlias(is));
      }
      addFilterWithId(property, isList, bool, boolBldr);
      return true;
    } else if (where.getIsNull()) {
      boolBldr.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(property)));
      return true;
    } else if (where.getIsNotNull()) {
      boolBldr.should(QueryBuilders.boolQuery().must(QueryBuilders.existsQuery(property)));
      return true;
    }
    return false;
  }

  public Set<String> resolveReference(String value, QueryRequest queryRequest) throws QueryException {
    Set<String> iris = new HashSet<>();
    value = value.replace("$", "");
    if (null != queryRequest.getArgument()) {
      for (Argument argument : queryRequest.getArgument()) {
        if (argument.getParameter().equals(value)) {
          if (null != argument.getValueData()) {
            iris.add(argument.getValueData());
          } else if (null != argument.getValueIri()) {
            if (argument.getValueIri().getIri() != null)
              iris.add(argument.getValueIri().getIri());
            else
              throw new QueryException("Argument parameter " + value + " valueIri cannot be null or set requestAskIri");
          } else if (null != argument.getValueIriList()) {
            if (argument.getValueIriList().isEmpty())
              throw new QueryException("Argument parameter " + value + " valueIriList cannot be empty");
            for (TTIriRef ttIriRef : argument.getValueIriList()) {
              iris.add(ttIriRef.getIri());
            }
          } else if (null != argument.getValueDataList()) {
            if (argument.getValueDataList().isEmpty())
              throw new QueryException("Argument parameter " + value + " valueDataList cannot be empty");
            iris.addAll(argument.getValueDataList());
          } else {
            if (null == argument.getValueObject()) {
              return iris;
            }
            iris.add(argument.getValueObject().toString());
          }
        }
        return iris;
      }
    }
    return null;
  }


  private Set<String> getIriFromAlias(Node node) throws QueryException {
    Set<String> iris = new HashSet<>();
    if (null == node.getIri()) {
      if (null != node.getParameter()) {
        Set<String> resolved = resolveReference(node.getParameter(), request);
        if (resolved != null)
          iris.addAll(resolved);
        else throw new QueryException("unable to resolve reference " + node.getParameter() + " in where clause");
      } else
        throw new QueryException("Match clause has no iri or parameter for type of");
    } else iris.add(node.getIri());
    return iris;
  }

  private boolean addBinding(Match match, Bool bool, BoolQueryBuilder boolBldr) throws QueryException {
    try {
      String node = null;
      String path = null;
      if (match.getWhere().getIri() != null) {
        Where binding = match.getWhere();
        if (binding.getIri().equals(SHACL.PATH)) {
          path = binding.getIs().getFirst().getIri();
        } else if (binding.getIri().equals(SHACL.NODE)) {
          node = binding.getIs().getFirst().getIri();
        }
      } else if (match.getWhere().getAnd() != null) {
        for (Where binding : match.getWhere().getAnd()) {
          if (binding.getIri().equals(SHACL.PATH)) {
            path = binding.getIs().getFirst().getIri();
          } else if (binding.getIri().equals(SHACL.NODE)) {
            node = binding.getIs().getFirst().getIri();
          }
        }
      }
      if (path == null || node == null)
        throw new QueryException("Invalid binding in where clause. Should have match where path and match where node");
      addFilter(Set.of(path + " " + node), bool, boolBldr);
      return true;
    } catch (Exception e) {
      throw new QueryException("Invalid binding in where clause. Should have match where path and match where node");
    }
  }

  private void setFromAliases(BoolQueryBuilder boolBuilder, List<Node> types) throws QueryException {
    Map<String, Set<String>> instanceFilters = new HashMap<>();
    for (Node type : types) {
      setFromAlias(type, instanceFilters);
    }
    if (!instanceFilters.isEmpty()) {
      for (Map.Entry<String, Set<String>> entry : instanceFilters.entrySet()) {
        addFilterWithId(entry.getKey(), entry.getValue(), Bool.and, boolBuilder);
      }
    }
  }

  private void setFromAlias(Node type, Map<String, Set<String>> instanceFilters) throws QueryException {
    if (type.getIri() != null) {
      addToInstanceFilters(type, type.getIri(), instanceFilters);
    } else if (type.getParameter() != null) {
      String value = type.getParameter();
      value = value.replace("$", "");
      if (null != request.getArgument()) {
        for (Argument argument : request.getArgument()) {
          if (argument.getParameter().equals(value)) {
            if (null != argument.getValueData())
              return;
            else if (null != argument.getValueIri()) {
              addToInstanceFilters(type, argument.getValueIri().getIri(), instanceFilters);
              return;
            } else if (null != argument.getValueIriList()) {
              argument.getValueIriList().forEach(ttIriRef -> addToInstanceFilters(type, ttIriRef.getIri(), instanceFilters));
              return;
            }
          }
        }
        throw new QueryException("Query Variable " + value + " has not been assigned is the request");
      } else
        throw new QueryException("Query has a query variable but request has no arguments set");
    } else if (query.getParentResult() != null) {
      JsonNode parentResult = query.getParentResult();
      if (parentResult.get("entities") != null) {
        for (Iterator<JsonNode> it = parentResult.get("entities").elements(); it.hasNext(); ) {
          JsonNode element = it.next();
          addToInstanceFilters(type, element.get("iri").asText(), instanceFilters);
        }
      } else
        throw new QueryException("Match clause has no iri, parameter or no parent results");

    } else {
      throw new QueryException("Match clause has no iri, parameter or node reference");
    }
  }

  private void addToInstanceFilters(Node type, String iri, Map<String, Set<String>> instanceFilters) {
    if (type.isDescendantsOrSelfOf())
      instanceFilters.computeIfAbsent("isA", i -> new HashSet<>()).add(iri);
    else if (type.isMemberOf())
      instanceFilters.computeIfAbsent("isMemberOf", i -> new HashSet<>()).add(iri);
    else
      instanceFilters.computeIfAbsent("iri", i -> new HashSet<>()).add(iri);

  }


}
