package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.functionscore.ScriptScoreQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Class to create and runan open search text query
 */
public class OSQuery {
    private static final Logger LOG = LoggerFactory.getLogger(OSQuery.class);
    private static final String TERM_CODE_TERM = "termCode.term";
    private static final String MATCH_TERM = "matchTerm";
    private static final String SCHEME = "scheme";
    private static final String USAGE_TOTAL = "usageTotal";
    private static final String STATUS = "status";
    private boolean hasScriptScore;


    /**
     * A rapid response page oriented query that bundles together code/iri key prefix term and the multi-word sequentially
     * <p> Each time a result is found, if more than one page it may be returned leaving the calling method to determine whether to add more results.
     * <p>Paging is supported</p>
     * subsequent calls.
     *
     * @param request Search request object
     * @return search request object
     * @throws QueryException if problem with data format of query
     */

    public SearchResponse multiPhaseQuery(QueryRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException, QueryException {

        request.addTiming("Entry point for \"" + request.getTextSearch() + "\"");
        SearchResponse results;
        results = defaultQuery(request);
        if (null != results) {
            return results;
        }
        if (request.getTextSearch().contains(" ")) {
            results = wrapandRun(buildNGramQuery(request), request);
            if (null != results)
                return results;
        }

        String corrected = spellingCorrection(request);
        if (corrected != null) {
            request.setTextSearch(corrected);
            results = defaultQuery(request);
            if (null != results)
                return results;
        }

        return results;
    }

    private SearchResponse defaultQuery(QueryRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException, QueryException {
        Page page = request.getPage();
        String term = request.getTextSearch();
        if (null == term) {
            return boolQuery(request);
        }
        SearchResponse results;
        if (page.getPageNumber() == 1 && term != null && (!term.contains(" "))) {
            if (term.contains(":")) {
                String namespace = EntityCache.getDefaultPrefixes().getNamespace(term.substring(0, term.indexOf(":")));
                if (namespace != null)
                    request.setTextSearch(namespace + term.split(":")[1]);
            }
        }


        request.addTiming("start building auto complete query");

        results = autoCompleteQuery(request);
        request.addTiming("end auto complete query - returning results");
        if (null != results) {
            return results;
        }
        return multiWordQuery(request);
    }

    private SearchResponse codeIriQuery(QueryRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {

        QueryBuilder qry = buildCodeIriQuery(request);
        return wrapandRun(qry, request);
    }


    private SearchResponse autoCompleteQuery(QueryRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException, QueryException {
        QueryBuilder qry = buildAutoCompleteQuery(request);
        hasScriptScore = true;
        return wrapandRun(qry, request);
    }

    private SearchResponse boolQuery(QueryRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException, QueryException {
        QueryBuilder qry = buildBoolQuery(request);
        return wrapandRun(qry, request);
    }


    private SearchResponse multiWordQuery(QueryRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException, QueryException {
        QueryBuilder qry = buildMultiWordQuery(request);
        return wrapandRun(qry, request);
    }


    private String getMatchTerm(String term) {
        term = term.split(" \\(")[0];
        if (term.length() < 30)
            return term;
        else
            return term.substring(0, 30);
    }

    private BoolQueryBuilder buildCodeIriQuery(QueryRequest request) {
        BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
        String term = request.getTextSearch();
        if (term.contains(":")) {
            String namespace = EntityCache.getDefaultPrefixes().getNamespace(term.substring(0, term.indexOf(":")));
            if (namespace != null)
                request.setTextSearch(namespace + term.split(":")[1]);
        }

        TermQueryBuilder tqb = new TermQueryBuilder("code", request.getTextSearch());
        boolBuilder.should(tqb);
        TermQueryBuilder tqac = new TermQueryBuilder("alternativeCode", request.getTextSearch());
        boolBuilder.should(tqac);
        TermQueryBuilder tqiri = new TermQueryBuilder("iri", request.getTextSearch());
        boolBuilder.should(tqiri);
        TermQueryBuilder tciri = new TermQueryBuilder("termCode.code", request.getTextSearch());
        boolBuilder.should(tciri);

        return boolBuilder;
    }

    private QueryBuilder buildBoolQuery(QueryRequest request) throws QueryException {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        processMatches(request, boolQuery);
        return boolQuery;
    }

    private void processMatches(QueryRequest queryRequest, BoolQueryBuilder boolQuery) throws QueryException {
        if (null == queryRequest.getQuery().getMatch()) throw new QueryException("Query requires a match for OS conversion");
        for (Match match : queryRequest.getQuery().getMatch()) {
            processMatch(match, boolQuery, queryRequest);
        }
    }

    private void processMatch(Match match, BoolQueryBuilder boolQueryBuilder, QueryRequest queryRequest) throws QueryException {
        if (null != match.getTypeOf()) {
            List<String> iris = listFromAlias(match.getTypeOf(), queryRequest);
            if (iris == null || iris.isEmpty())
                throw new QueryException("Failed to find query types");
            boolQueryBuilder.must(new TermsQueryBuilder("entityType.@id", iris));
        }
        if (null != match.getInstanceOf()) {
            List<String> iris = listFromAlias(match.getInstanceOf(), queryRequest);
            if (iris == null || iris.isEmpty())
                throw new QueryException("Failed to find query isas");
            boolQueryBuilder.must(new TermsQueryBuilder("isA.@id", iris));
        }
        if (null != match.getWhere()) {
            processProperties(match, boolQueryBuilder);
        }
        if (null != match.getMatch()) {
            if (match.getBoolMatch() != Bool.or) throw new QueryException("Match must be Bool.or");
            for (Match subMatch : match.getMatch()) {
                processMatch(subMatch, boolQueryBuilder, queryRequest);
            }
        }
    }

    private void processProperties(Match match, BoolQueryBuilder boolQueryBuilder) throws QueryException {
        for (Where where : match.getWhere()) {
            if (IM.HAS_SCHEME.equals(where.getIri())) {
                processSchemeProperty(boolQueryBuilder, where);
            } else if (IM.HAS_MEMBER.equals(where.getIri()) && where.isInverse()) {
                processMemberProperty(boolQueryBuilder, where);
            } else if (IM.HAS_STATUS.equals(where.getIri())) {
                processStatusProperty(boolQueryBuilder, where);
            } else if (RDF.TYPE.equals(where.getIri())) {
                processTypeProperty(boolQueryBuilder, where);
            } else if (IM.BINDING.equals(where.getIri())) {
                processBindingProperty(boolQueryBuilder, where);
            }else if (IM.IS_A.equals(where.getIri())) {
                processIsAProperty(boolQueryBuilder, where);
            }  else {
                throw new QueryException("Unexpected where encountered: " + where.getIri());
            }
        }
    }

    private static void processSchemeProperty(BoolQueryBuilder boolQueryBuilder, Where w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty()) {
            List<String> schemes = w.getIs().stream().map(Node::getIri).collect(Collectors.toList());
            boolQueryBuilder.must(new TermsQueryBuilder("scheme.@id", schemes));
        } else
            throw new QueryException("Scheme filter must be a list (is)");
    }

    private static void processMemberProperty(BoolQueryBuilder boolQueryBuilder, Where w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty()) {
            List<String> memberOfs = w.getIs().stream().map(Node::getIri).collect(Collectors.toList());
            boolQueryBuilder.must(new TermsQueryBuilder("memberOf.@id", memberOfs));
        } else
            throw new QueryException("Set membership filter must be a list (is)");
    }

    private static void processStatusProperty(BoolQueryBuilder boolQueryBuilder, Where w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty()) {
            List<String> statuses = w.getIs().stream().map(Node::getIri).collect(Collectors.toList());
            boolQueryBuilder.must(new TermsQueryBuilder("status.@id", statuses));
        } else
            throw new QueryException("Status filter must be a list (is)");
    }

    private static void processTypeProperty(BoolQueryBuilder boolQueryBuilder, Where w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty()) {
            List<String> types = w.getIs().stream().map(Node::getIri).collect(Collectors.toList());
            boolQueryBuilder.must(new TermsQueryBuilder("entityType.@id", types));
        } else
            throw new QueryException("Type filter must be a list (is)");
    }

    private static void processBindingProperty(BoolQueryBuilder boolQueryBuilder, Where w) throws QueryException {
        if (null != w.getMatch() && null != w.getMatch().getWhere() && !w.getMatch().getWhere().isEmpty()) {
            Optional<Where> path = w.getMatch().getWhere().stream().filter(nestedWhere -> SHACL.PATH.equals(nestedWhere.getIri())).findFirst();
            Optional<Where> node = w.getMatch().getWhere().stream().filter(nestedWhere -> SHACL.NODE.equals(nestedWhere.getIri())).findFirst();
            if (path.isPresent() && null != path.get().getIs() && !path.get().getIs().isEmpty() && node.isPresent() && null != node.get().getIs() && !node.get().getIs().isEmpty()) {
                Optional<Node> pathNode = path.get().getIs().stream().findFirst();
                Optional<Node> nodeNode = node.get().getIs().stream().findFirst();
                if (pathNode.isPresent() && nodeNode.isPresent()) {
                    List<SearchBinding> bindingFilter = new ArrayList<>();
                    bindingFilter.add(new SearchBinding().setNode(new TTIriRef(nodeNode.get().getIri())).setPath(new TTIriRef(pathNode.get().getIri())));
                    for (SearchBinding binding : bindingFilter) {
                        BoolQueryBuilder inner = new BoolQueryBuilder();
                        inner.must(new TermsQueryBuilder("binding.node.@id", binding.getNode().getIri()));
                        inner.must(new TermsQueryBuilder("binding.path.@id", binding.getPath().getIri()));
                        boolQueryBuilder.must(inner);
                    }
                }
            }
        } else
            throw new QueryException("Binding must have a path and a node");
    }

    private static void processIsAProperty(BoolQueryBuilder boolQueryBuilder, Where w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty()) {
            List<String> isas = w.getIs().stream().map(Node::getIri).collect(Collectors.toList());
            boolQueryBuilder.must(new TermsQueryBuilder("isA.@id", isas));
        } else
            throw new QueryException("Is a filter must be a list (is)");

    }

    private BoolQueryBuilder buildBindingBoolQuery(List<SearchBinding> bindings) {
        BoolQueryBuilder result = new BoolQueryBuilder();
        result.minimumShouldMatch(1);

        for (SearchBinding binding : bindings) {
            BoolQueryBuilder inner = new BoolQueryBuilder();
            inner.must(new TermsQueryBuilder("binding.node.@id", binding.getNode().getIri()));
            inner.must(new TermsQueryBuilder("binding.path.@id", binding.getPath().getIri()));
            result.should(inner);
        }

        return result;
    }


    private QueryBuilder buildAutoCompleteQuery(QueryRequest request) throws QueryException {
        String requestTerm = getMatchTerm(request.getTextSearch());
        BoolQueryBuilder boolQuery;
        if (request.getPage().getPageNumber() == 1 && !requestTerm.contains(" ")) {
            boolQuery = buildCodeIriQuery(request);
            PrefixQueryBuilder pqb = new PrefixQueryBuilder("key", request.getTextSearch());
            boolQuery.should(pqb);
        } else
            boolQuery = new BoolQueryBuilder();

        String prefix = requestTerm.replaceAll("[ '()\\-_./]", "").toLowerCase();
        PrefixQueryBuilder pqb = new PrefixQueryBuilder(MATCH_TERM, prefix);
        boolQuery.should(pqb);
        MatchPhrasePrefixQueryBuilder mpt = new MatchPhrasePrefixQueryBuilder(TERM_CODE_TERM, requestTerm)
                .analyzer("standard")
                .boost(1.5F)
                .slop(1);
        boolQuery.should(mpt);
        boolQuery.minimumShouldMatch(1);
        processMatches(request, boolQuery);

        return boolQuery;

    }

    private void addDefaultSorts(SearchSourceBuilder bld) {
//        bld.sort("name");
//        bld.sort("preferredName");
        bld.sort("subsumptionCount",SortOrder.DESC);
        bld.sort("usageTotal", SortOrder.DESC);
        bld.sort("length",SortOrder.ASC);
    }


    private QueryBuilder buildNGramQuery(QueryRequest request) throws QueryException {
        String requestTerm = getMatchTerm(request.getTextSearch());
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        MatchQueryBuilder mat = new MatchQueryBuilder(TERM_CODE_TERM, requestTerm);
        mat.analyzer("standard");
        mat.operator(Operator.AND);
        mat.fuzziness(Fuzziness.TWO);
        boolQuery.must(mat);
        processMatches(request, boolQuery);
        return boolQuery;

    }

    private QueryBuilder buildMultiWordQuery(QueryRequest request) throws QueryException {
        BoolQueryBuilder qry = new BoolQueryBuilder();
        MatchPhrasePrefixQueryBuilder pqry = new MatchPhrasePrefixQueryBuilder(TERM_CODE_TERM, request.getTextSearch());
        pqry.analyzer("standard");
        qry.should(pqry);
        BoolQueryBuilder wqry = new BoolQueryBuilder();
        qry.should(wqry);
        int wordPos = 0;
        for (String term : request.getTextSearch().split(" ")) {
            wordPos++;
            MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder(TERM_CODE_TERM, term);
            mfs.boost(wordPos == 1 ? 4 : 1);
            mfs.analyzer("standard");
            wqry.must(mfs);
        }
        qry.minimumShouldMatch(1);
        processMatches(request, qry);
        return qry;

    }

    private SearchResponse wrapandRun(QueryBuilder query, QueryRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchResponse response = wrapandRun(query, request, false);
        SearchResponse highestUsageResponse = wrapandRun(query, request, true);
        if (!highestUsageResponse.getEntities().isEmpty())
            response.setHighestUsage(highestUsageResponse.getEntities().get(0).getUsageTotal());
        return response;
    }

    private SearchResponse wrapandRun(QueryBuilder query, QueryRequest request, boolean highestUsage) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        SearchSourceBuilder bld = new SearchSourceBuilder();
        bld.query(query);
        if (!highestUsage) addDefaultSorts(bld);
        else bld.sort("usageTotal", SortOrder.DESC);
        int size = request.getPage().getPageSize();
        int from = request.getPage().getPageSize() * (request.getPage().getPageNumber() - 1);
        if (highestUsage) {
            size = 1;
            from = 0;
        }
        bld.size(size).from(from);
        String termCode = "termCode";

        List<String> defaultFields = new ArrayList<>(Arrays.asList("iri", "name", "code", "alternativeCode", termCode, "entityType", STATUS, SCHEME, USAGE_TOTAL, "preferredName"));
        Set<String> fields = new HashSet<>(defaultFields);
        String[] sources = fields.toArray(String[]::new);
        bld.fetchSource(sources, null);
        try {
            return runQuery(request, bld);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new InterruptedException("failure to run open search query due to :" + ie.getMessage());
        }
    }


    public SearchResponse runQuery(QueryRequest request, SearchSourceBuilder bld) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        request.addTiming("About to run query");

        String queryJson = bld.toString();

        HttpResponse<String> response = getQueryResponse(request, queryJson);

        try (CachedObjectMapper om = new CachedObjectMapper()) {
            JsonNode root = om.readTree(response.body());
            request.addTiming("Query run and response received. Ready to produce search results");
            SearchResponse searchResults = new SearchResponse();
            return standardResponse(request, root, om, searchResults);
        }

    }

    private HttpResponse<String> getQueryResponse(QueryRequest request, String queryJson) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {
        String url = System.getenv("OPENSEARCH_URL");
        if (url == null)
            throw new OpenSearchException("Environmental variable OPENSEARCH_URL is not set");

        String index = System.getenv("OPENSEARCH_INDEX");

        if (System.getenv("OPENSEARCH_AUTH") == null)
            throw new OpenSearchException("Environmental variable OPENSEARCH_AUTH token is not set");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(url + index + "/_search"))

                .header("Authorization", "Basic " + System.getenv("OPENSEARCH_AUTH"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(queryJson))
                .build();
        request.addTiming("Query sent...");

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(res -> res)
                .get();

        if (299 < response.statusCode()) {
            LOG.debug("Open search request failed with code: {}", response.statusCode());
            throw new OpenSearchException("Search request failed. Error =. " + response.body());
        }
        return response;


    }

    public int runQueryCount(SearchRequest request, SearchSourceBuilder bld) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        String queryJson = bld.toString();
        String url = System.getenv("OPENSEARCH_URL");
        if (url == null)
            throw new OpenSearchException("Environmental variable OPENSEARCH_URL is not set");

        String index = request.getIndex();
        if (index == null)
            index = System.getenv("OPENSEARCH_INDEX");

        if (System.getenv("OPENSEARCH_AUTH") == null)
            throw new OpenSearchException("Environmental variable OPENSEARCH_AUTH token is not set");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(url + index + "/_count"))

                .header("Authorization", "Basic " + System.getenv("OPENSEARCH_AUTH"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(queryJson))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client
                .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(res -> res)
                .get();

        if (299 < response.statusCode()) {
            LOG.debug("Open search request failed with code: {}", response.statusCode());
            throw new OpenSearchException("Search request failed. Error connecting to opensearch. ");
        }

        try (CachedObjectMapper om = new CachedObjectMapper()) {
            JsonNode root = om.readTree(response.body());
            return root.get("count").asInt();
        }
    }

    private SearchResponse standardResponse(QueryRequest request, JsonNode root, CachedObjectMapper resultMapper, SearchResponse searchResults) throws JsonProcessingException {
        int resultNumber = 0;
        for (JsonNode hit : root.get("hits").get("hits")) {
            resultNumber++;
            SearchResultSummary source = resultMapper.treeToValue(hit.get("_source"), SearchResultSummary.class);
            searchResults.addEntity(source);
            source.setMatch(source.getName());
            if (source.getPreferredName() != null) {
                source.setName(source.getPreferredName());
                source.setMatch(source.getName());
            }
            if (resultNumber < 6 && null != request.getTextSearch()) {
                fetchMatchTerm(source, request.getTextSearch());
            }
            source.setTermCode(null);
        }
        Integer totalCount = resultMapper.treeToValue(root.get("hits").get("total").get("value"), Integer.class);
        if (null != totalCount) searchResults.setCount(totalCount);
        if (request.getPage().getPageNumber() != 0) searchResults.setPage(request.getPage().getPageNumber());
        //Sort now donw in query
        // if (!searchResults.isEmpty() && null != request.getTermFilter())
        //   sort(searchResults, request.getTermFilter());
        request.addTiming("Results List built");
        if (null != request.getTextSearch()) searchResults.setTerm(request.getTextSearch());
        return searchResults;
    }

    private void fetchMatchTerm(SearchResultSummary searchResult, String searchTerm) {
        if (searchResult.getName() == null)
            return;
        if (searchResult.getName().toLowerCase().startsWith(searchTerm.toLowerCase()))
            return;
        if (searchResult.getCode() != null && searchResult.getCode().equals(searchTerm))
            return;
        searchTerm = searchTerm.toLowerCase();
        if (searchResult.getTermCode() != null) {
            for (SearchTermCode tc : searchResult.getTermCode()) {
                TTIriRef termCodeStatus = tc.getStatus();
                if ((termCodeStatus != null) && (!(termCodeStatus.getIri().equals(IM.INACTIVE))) &&
                        (tc.getTerm() != null && tc.getTerm().toLowerCase().startsWith(searchTerm))) {
                    searchResult.setMatch(tc.getTerm());
                    break;
                }
            }
        }
    }


    /**
     * An open search query using IMQ- Returns null if query cannot be done via open search.
     * <p>Application logic should try IM direct if null is returned</p>
     *
     * @param queryRequest Query request object containing any textSearch term, paging and a query.
     * @return ObjectNode as determined by select statement
     * @throws QueryException if content of query definition is invalid
     */

    public SearchResponse openSearchQuery(QueryRequest queryRequest) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException, QueryException {
        if (queryRequest.getTextSearch() == null)
            return null;

        Query query = queryRequest.getQuery();

        if (query != null) {
            if (query.getReturn() != null && !returnsCompatibleWithOpenSearch(query.getReturn())) {
                return null;
            }

            if (query.getMatch() != null && !matchesCompatibleWithOpenSearch(query.getMatch())) {
                return null;
            }
        }

        SearchResponse results = multiPhaseQuery(queryRequest);
        if (results.getEntities().isEmpty())
            return null;
        else
            return results;

    }

    private static boolean returnsCompatibleWithOpenSearch(List<Return> returns) {
        if (returns != null) {
            for (Return r : returns) {
                if (!returnCompatibleWithOpenSearch(r))
                    return false;
            }
        }
        return true;
    }

    private static boolean returnCompatibleWithOpenSearch(Return r) {
        for (ReturnProperty p : r.getProperty()) {
            if (p != null) {
                if (p.getIri() != null && !propertyAvailableInOpenSearch(p.getIri()))
                    return false;

                if (p.getReturn() != null && !returnCompatibleWithOpenSearch(p.getReturn()))
                    return false;
            }
        }
        return true;
    }

    private boolean matchesCompatibleWithOpenSearch(List<Match> matches) {
        for (Match match : matches) {
            if (!matchCompatibleWithOpenSearch(match)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchCompatibleWithOpenSearch(Match match) {
        if (match.getWhere() != null) {
            for (Where property : match.getWhere()) {
                if (!propertyCompatibleWithOpenSearch(property))
                    return false;
            }
        }
        if (match.getMatch() != null) {
            return matchesCompatibleWithOpenSearch(match.getMatch());
        }
        return true;
    }

    private boolean propertyCompatibleWithOpenSearch(Where property) {
        if (!propertyAvailableInOpenSearch(property.getIri())) {
            return false;
        }
        if (property.getMatch() != null) {
            return matchCompatibleWithOpenSearch(property.getMatch());
        }
        return true;
    }

    public ObjectNode convertOSResult(SearchResponse searchResults, Query query) {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            ObjectNode result = om.createObjectNode();
            ArrayNode resultNodes = om.createArrayNode();
            result.set("entities", resultNodes);
            for (SearchResultSummary searchResult : searchResults.getEntities()) {
                ObjectNode resultNode = om.createObjectNode();
                resultNodes.add(resultNode);
                resultNode.put("@id", searchResult.getIri());

                if (query != null) {
                    if (query.getReturn() == null)
                        query.return_(s -> s.property(p -> p.setIri(RDFS.LABEL)));
                    for (Return select : query.getReturn()) {
                        convertOSResultAddNode(om, searchResult, resultNode, select);
                    }
                }
            }
            return result;
        }
    }

    private void convertOSResultAddNode(CachedObjectMapper om, SearchResultSummary searchResult, ObjectNode resultNode, Return select) {
        if (select.getProperty() != null) {
            for (ReturnProperty prop : select.getProperty()) {
                if (prop.getIri() != null) {
                    String field = prop.getIri();
                    switch (field) {
                        case IM.CODE -> resultNode.put(field, searchResult.getCode());
                        case IM.HAS_STATUS -> resultNode.set(field, fromIri(searchResult.getStatus(), om));
                        case IM.HAS_SCHEME -> resultNode.set(field, fromIri(searchResult.getScheme(), om));
                        case IM.USAGE_TOTAL -> resultNode.put(field, searchResult.getUsageTotal());
                        case RDFS.LABEL -> resultNode.put(field, searchResult.getName());
                        case RDFS.COMMENT -> {
                            if (searchResult.getDescription() != null)
                                resultNode.put(field, searchResult.getDescription());
                        }
                        case RDF.TYPE -> resultNode.set(field, arrayFromIri(searchResult.getEntityType(), om));
                    }
                }
            }
        }
    }

    private ObjectNode fromIri(TTIriRef iri, CachedObjectMapper om) {
        ObjectNode node = om.createObjectNode();
        node.put("@id", iri.getIri());
        if (iri.getName() != null)
            node.put("name", iri.getName());
        return node;
    }

    private ArrayNode arrayFromIri(Set<TTIriRef> iris, CachedObjectMapper om) {

        ArrayNode arrayNode = om.createArrayNode();
        for (TTIriRef iri : iris) {
            ObjectNode node = om.createObjectNode();
            node.put("@id", iri.getIri());
            if (iri.getName() != null)
                node.put("name", iri.getName());
            arrayNode.add(node);
        }
        return arrayNode;
    }

    private static List<String> listFromAlias(Node type, QueryRequest queryRequest) throws QueryException {
        if (type.getParameter() == null) {
            return List.of(type.getIri());
        }

        String value = type.getParameter();
        value = value.replace("$", "");
        if (null != queryRequest.getArgument()) {
            for (Argument argument : queryRequest.getArgument()) {
                if (argument.getParameter().equals(value)) {
                    if (null != argument.getValueData())
                        return null;
                    else if (null != argument.getValueIri()) {
                        List<String> iriList = new ArrayList<>();
                        iriList.add(argument.getValueIri().getIri());
                        return iriList;
                    } else if (null != argument.getValueIriList()) {
                        return
                                argument.getValueIriList().stream().map(TTIriRef::getIri).collect(Collectors.toList());
                    }
                }
            }
            throw new QueryException("Query Variable " + value + " has not been assigned is the request");
        } else
            throw new QueryException("Query has a query variable but request has no arguments set");
    }

    private String spellingCorrection(QueryRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        String oldTerm = request.getTextSearch();
        String newTerm = null;
        String suggestor = "{\n" +
                "  \"suggest\": {\n" +
                "    \"spell-check\": {\n" +
                "      \"text\": \"" + oldTerm + "\",\n" +
                "      \"term\": {\n" +
                "        \"field\": \"termCode.term\",\n" +
                "        \"sort\": \"score\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        HttpResponse<String> response = getQueryResponse(request, suggestor);
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            JsonNode root = om.readTree(response.body());
            JsonNode suggestions = root.get("suggest").get("spell-check");
            String[] words = oldTerm.split(" ");
            for (JsonNode suggest : suggestions) {
                if (suggest.has("options")) {
                    JsonNode options = suggest.get("options");
                    if (options.size() > 0) {
                        String original = suggest.get("text").asText();
                        JsonNode swapNode = options.get(0).get("text");
                        if (original.equals(oldTerm))
                            newTerm = swapNode.asText();
                        else {
                            int wordPos = getWordPos(words, original);
                            if (wordPos > -1) {
                                words[wordPos] = swapNode.asText();
                            }
                        }
                    }

                }
            }
            if (newTerm == null) {
                newTerm = String.join(" ", words);
            }
            if (newTerm.equals(oldTerm))
                return null;
            else
                return newTerm;
        }
    }

    private int getWordPos(String[] words, String wordToFind) {
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(wordToFind))
                return i;
        }
        return -1;
    }


    private static boolean propertyAvailableInOpenSearch(String iri) {
        if (iri == null)
            return false;

        return List.of(
                RDFS.LABEL,
                RDFS.COMMENT,
                IM.CODE,
                IM.HAS_STATUS,
                IM.HAS_SCHEME,
                RDF.TYPE,
                IM.USAGE_TOTAL,
                IM.HAS_MEMBER,
                IM.BINDING,
                SHACL.NODE,
                SHACL.PATH
        ).contains(iri);
    }

}
