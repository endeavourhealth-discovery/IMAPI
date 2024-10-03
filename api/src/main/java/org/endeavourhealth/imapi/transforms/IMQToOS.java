package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.functionscore.ScriptScoreQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.endeavourhealth.imapi.dataaccess.SparqlConverter;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.*;
import java.util.stream.Collectors;

public class IMQToOS {
  private static final String SCHEME = "scheme";
  private static final String USAGE_TOTAL = "usageTotal";
  private static final String STATUS = "status";
  private QueryRequest request;
  private BoolQueryBuilder boolBuilder;
  private SearchSourceBuilder sourceBuilder;
  private Query query;

  public SearchSourceBuilder buildQuery(QueryRequest imRequest, Query oneQuery, QUERY_TYPE type, Fuzziness fuzziness) throws QueryException {
    request = imRequest;
    query = oneQuery;
    request.setTextSearch(request.getTextSearch()
      .replace("\"", "")
      .replace("{", "")
      .replace("}", ""));
    switch (type) {
      case AUTOCOMPLETE -> {
        return autocompleteQuery(request, query);
      }
      case MULTIWORD -> {
        return multiWordQuery(request, query);
      }
      case NGRAM -> {
        return nGramQuery(request, query, fuzziness);
      }
    }
    throw new QueryException("Valid query type needed");
  }

  public SearchSourceBuilder buildQuery(QueryRequest imRequest, Query oneQuery, QUERY_TYPE type) throws QueryException {
    return buildQuery(imRequest, oneQuery, type, Fuzziness.ZERO);
  }

  private SearchSourceBuilder nGramQuery(QueryRequest imRequest, Query oneQuery, Fuzziness fuzziness) throws QueryException {

    boolBuilder = new BoolQueryBuilder();
    if (!addMatches())
      return null;
    sourceBuilder = new SearchSourceBuilder();
    if (!addReturns())
      return null;
    addScriptAndPage();
    String text = request.getTextSearch();
    MatchQueryBuilder mat = new MatchQueryBuilder("termCode.term", text);
    mat.analyzer("standard");
    mat.operator(Operator.AND);
    mat.fuzziness(fuzziness);
    boolBuilder.must(mat);

    return sourceBuilder;
  }

  private SearchSourceBuilder multiWordQuery(QueryRequest imRequest, Query query) throws QueryException {
    if (!addMatches())
      return null;
    sourceBuilder = new SearchSourceBuilder();
    if (!addReturns())
      return null;
    addScriptAndPage();

    String text = request.getTextSearch();
    int wordPos = 0;
    text = text.replace("(", "").replace(")", "").replace("-", "");
    for (String term : text.split(" ")) {
      wordPos++;
      MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder("termCode.term", term);
      mfs.boost(wordPos == 1 ? 4 : 1);
      mfs.analyzer("standard");
      boolBuilder.must(mfs);
    }

    return sourceBuilder;
  }

  private SearchSourceBuilder autocompleteQuery(QueryRequest imRequest, Query query) throws QueryException {

    boolBuilder = new BoolQueryBuilder();
    String term = request.getTextSearch();
    if (term.contains(":") && (!term.contains(" "))) {
      String namespace = EntityCache.getDefaultPrefixes().getNamespace(term.substring(0, term.indexOf(":")));
      if (namespace != null)
        term = namespace + term.split(":")[1];
    }
    addCodesAndIri(term);

    String prefix = term.replaceAll("[ '()\\-_./]", "").toLowerCase();
    TermQueryBuilder tqac = new TermQueryBuilder("preferredName.keyword", term).caseInsensitive(true);
    tqac.boost(2000000F);
    boolBuilder.should(tqac);
    PrefixQueryBuilder pqb = new PrefixQueryBuilder("matchTerm", prefix);
    pqb.boost(1000000F);
    boolBuilder.should(pqb);
    MatchPhrasePrefixQueryBuilder mpt = new MatchPhrasePrefixQueryBuilder("termCode.term", term)
      .analyzer("standard")
      .slop(1);
    boolBuilder.should(mpt);
    boolBuilder.minimumShouldMatch(1);
    if (!addMatches())
      return null;

    sourceBuilder = new SearchSourceBuilder();
    if (!addReturns())
      return null;
    addScriptAndPage();
    return sourceBuilder;
  }

  private boolean addMatches() throws QueryException {
    if (query == null)
      return true;
    if (query.isActiveOnly()) {
      addFilterWithId("status", Set.of(IM.ACTIVE));
    }
    if (query.getMatch() == null)
      return true;
    for (Match match : query.getMatch()) {
      if (!addMatch(match))
        return false;
    }

    return true;
  }

  private void addCodesAndIri(String term) {
    TermQueryBuilder tqb = new TermQueryBuilder("code", term);
    boolBuilder.should(tqb);
    TermQueryBuilder tqac = new TermQueryBuilder("alternativeCode", term);
    boolBuilder.should(tqac);
    TermQueryBuilder tqiri = new TermQueryBuilder("iri", term);
    boolBuilder.should(tqiri);
    TermQueryBuilder tciri = new TermQueryBuilder("termCode.code", term);
    boolBuilder.should(tciri);


  }

  private void addScriptAndPage() {
    Script script = new Script(getScoreScript());
    ScriptScoreQueryBuilder sqr = QueryBuilders.scriptScoreQuery(boolBuilder, script);
    sourceBuilder.query(sqr);
    if (request.getPage() != null) {
      sourceBuilder.size(request.getPage().getPageSize()).from(request.getPage().getPageSize() * (request.getPage().getPageNumber() - 1));
    } else {
      sourceBuilder.size(1000).from(0);
    }
  }

  private String getScoreScript() {
    return "def usage=0;" +
      "if (doc['usageTotal'].size()>0) {" +
      "  def value = doc['usageTotal'].value;" +
      "  if (value<1000) {usage = 0;}" +
      "  else if (value <30000) {usage = 1;}" +
      "  else if (value <250000) {usage = 2;}" +
      "  else if (value <1000000) {usage = 4;}" +
      "  else if (value <3000000) {usage = 7;}" +
      "  else {usage = 9;}" +
      "  }" +
      "if (_score>2000000) {_score=10;} else if (_score>1000000) {_score=4;} else _score=0;_score+ usage;";
  }

  private boolean addReturns() {
    Set<String> sources = new HashSet<>(List.of("name", "iri", "preferredName", "code", "usageTotal", "entityType", "scheme", "status"));
    if (query == null)
      return true;
    if (query.getReturn() != null) {
      for (Return ret : query.getReturn()) {
        for (ReturnProperty prop : ret.getProperty()) {
          if (prop.getIri() != null) {
            switch (prop.getIri()) {
              case RDFS.COMMENT:
                sources.add("description");
                break;
              case RDFS.LABEL:
                sources.add("name");
                break;
              case IM.CODE:
                sources.add("code");
                break;
              case IM.HAS_STATUS:
                sources.add(STATUS);
                break;
              case IM.ALTERNATIVE_CODE:
                sources.add("alternativeCode");
                break;
              case IM.HAS_SCHEME:
                sources.add(SCHEME);
                break;
              case RDF.TYPE:
                sources.add("entityType");
                break;
              case IM.USAGE_TOTAL:
                sources.add(USAGE_TOTAL);
                break;
              case IM.BINDING:
                sources.add("binding");
                break;
              case IM.HAS_TERM_CODE:
                sources.add("termCode");
                break;
              case RDFS.DOMAIN:
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

  private void addFilterWithId(String property, Set<String> values) {
    TermsQueryBuilder tqr = new TermsQueryBuilder(property + ".@id", values);
    boolBuilder.filter(tqr);
  }

  private void addFilter(String property, Set<String> values) {
    TermsQueryBuilder tqr = new TermsQueryBuilder(property, values);
    boolBuilder.filter(tqr);
  }

  private boolean addMatch(Match match) throws QueryException {
    if (match.getTypeOf() != null) {
      addFilterWithId("entityType", Set.of(getIriFromAlias(match.getTypeOf())));
    }

    if (match.getInstanceOf() != null) {
      setFromAliases(match.getInstanceOf());

    }

    if (!addProperties(match))
      return false;

    if (match.getMatch() != null) {
      return false;
    }

    return true;
  }

  private boolean addProperties(Match match) throws QueryException {
    if (match.getWhere() == null)
      return true;
    for (Where where : match.getWhere()) {
      String w = where.getIri();
      if (IM.HAS_SCHEME.equals(w)) {
        if (!addIsFilter("scheme", where))
          return false;
      } else if (IM.HAS_MEMBER.equals(w) && where.isInverse()) {
        if (!addIsFilter("memberOf", where))
          return false;
      } else if (IM.HAS_STATUS.equals(w)) {
        if (!addIsFilter("status", where))
          return false;
      } else if (RDF.TYPE.equals(w)) {
        if (!addIsFilter("entityType", where))
          return false;
      } else if (IM.BINDING.equals(w)) {
        if (!addBinding(where))
          return false;
      } else if (IM.IS_A.equals(w)) {
        if (!addIsFilter("isA", where))
          return false;
      } else {
        return false;
      }
    }
    return true;
  }

  private boolean addIsFilter(String property, Where where) {
    if (where.getIs() != null) {
      Set<String> isList = where.getIs().stream().map(is -> {
        if (is.getIri() != null && !is.getIri().isEmpty()) return is.getIri();
        else {
          try {
            return getIriFromAlias(is);
          } catch (QueryException e) {
            throw new RuntimeException(e);
          }
        }
      }).collect(Collectors.toSet());
      addFilterWithId(property, isList);
      return true;
    }
    return false;
  }

  private String getIriFromAlias(Node node) throws QueryException {
    if (null == node.getIri()) {
      if (null != node.getParameter()) {
        return SparqlConverter.resolveReference(node.getParameter(), request);
      } else
        throw new QueryException("Match clause has no iri or parameter for type of");
    } else return node.getIri();
  }

  private boolean addBinding(Where where) throws QueryException {
    try {
      String path = null;
      String node = null;
      Match match = where.getMatch();
      for (Where binding : match.getWhere()) {
        if (binding.getIri().equals(SHACL.PATH)) {
          path = getIriFromAlias(binding.getIs().get(0));
        } else if (binding.getIri().equals(SHACL.NODE)) {
          node = getIriFromAlias(binding.getIs().get(0));
        }
      }
      if (path == null || node == null)
        throw new QueryException("Invalid binding in where clause. Should have match where path and match where node");
      addFilter("binding", Set.of(path + " " + node));
      return true;
    } catch (Exception e) {
      throw new QueryException("Invalid binding in where clause. Should have match where path and match where node");
    }
  }

  private void setFromAliases(List<Node> types) throws QueryException {
    Map<String, Set<String>> instanceFilters = new HashMap<>();
    for (Node type : types) {
      setFromAlias(type, instanceFilters);
    }
    if (!instanceFilters.isEmpty()) {
      for (Map.Entry<String, Set<String>> entry : instanceFilters.entrySet()) {
        addFilterWithId(entry.getKey(), entry.getValue());
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
              argument.getValueIriList().stream().forEach(ttIriRef -> addToInstanceFilters(type, ttIriRef.getIri(), instanceFilters));
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
          addToInstanceFilters(type, element.get("@id").asText(), instanceFilters);
        }

        return;
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

  public enum QUERY_TYPE {
    AUTOCOMPLETE,
    MULTIWORD,
    NGRAM
  }


}
