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

    public SearchResponse multiPhaseQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {

        request.addTiming("Entry point for \"" + request.getTermFilter() + "\"");
        SearchResponse results;
        results = defaultQuery(request);
        if (null != results) {
            return results;
        }
        if (request.getTermFilter().contains(" ")) {
            results = wrapandRun(buildNGramQuery(request), request);
            if (null != results)
                return results;
        }

        String corrected = spellingCorrection(request);
        if (corrected != null) {
            request.setTermFilter(corrected);
            results = defaultQuery(request);
            if (null != results)
                return results;
        }

        return results;
    }

    private SearchResponse defaultQuery(SearchRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        int page = request.getPage();
        int size = request.getSize();
        request.setFrom(size * (page - 1));
        String term = request.getTermFilter();
        if (null == term) {
            return boolQuery(request);
        }
        SearchResponse results;
        if (page == 1 && term != null && (!term.contains(" "))) {
            if (term.contains(":")) {
                String namespace = EntityCache.getDefaultPrefixes().getNamespace(term.substring(0, term.indexOf(":")));
                if (namespace != null)
                    request.setTermFilter(namespace + term.split(":")[1]);
                request.getSchemeFilter().clear();
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

    private SearchResponse codeIriQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {

        QueryBuilder qry = buildCodeIriQuery(request);
        return wrapandRun(qry, request);
    }


    private SearchResponse autoCompleteQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        QueryBuilder qry = buildAutoCompleteQuery(request);
        hasScriptScore = true;
        return wrapandRun(qry, request);
    }

    private Map<String, Object> getScript(SearchRequest request) throws JsonProcessingException {
        if (request.getOrderBy() == null)
            return null;
        Map<String, Object> params = null;
        for (OrderBy orderBy : request.getOrderBy()) {
            if (orderBy.getAnd() != null || orderBy.getIriValue() != null || orderBy.isStartsWithTerm()) {
                if (params == null) {
                    params = new HashMap<>();
                    params.put("term", request.getTermFilter().toLowerCase());
                    Map<String, Object> ordersMap = new HashMap<>();
                    params.put("orders", ordersMap);
                    ordersMap.put("orderBy", new ArrayList<>());
                }
                Map<String, Object> ordersMap = (Map) params.get("orders");
                Map<String, Object> orderByMap = new HashMap<>();
                ((List) ordersMap.get("orderBy")).add(orderByMap);
                orderByMap.put("field", orderBy.getField());
                if (orderBy.getIriValue() != null) {
                    orderByMap.put("iriValue", new ArrayList<>());
                    for (TTIriRef iriValue : orderBy.getIriValue()) {
                        ((List) orderByMap.get("iriValue")).add(iriValue.getIri());
                    }
                }
                if (orderBy.getTextValue() != null) {
                    orderByMap.put("textValue", new ArrayList<>());
                    for (String textValue : orderBy.getTextValue()) {
                        ((List) orderByMap.get("textValue")).add(textValue);
                    }
                }
                if (orderBy.isStartsWithTerm()) {
                    orderByMap.put("startsWithTerm", true);
                }
            }
        }
        return params;


    }


    private void setSorts(SearchRequest request, SearchSourceBuilder bld) {
        if (request.getOrderBy() != null) {
            for (OrderBy orderBy : request.getOrderBy()) {
                if (orderBy.getDirection() != null) {
                    bld.sort(orderBy.getField(), orderBy.getDirection() == Order.descending ? SortOrder.DESC : SortOrder.ASC);
                }
            }
        }
    }

    private SearchResponse boolQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        QueryBuilder qry = buildBoolQuery(request);
        return wrapandRun(qry, request);
    }


    private SearchResponse multiWordQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
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

    private BoolQueryBuilder buildCodeIriQuery(SearchRequest request) {
        BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
        String term = request.getTermFilter();
        if (term.contains(":")) {
            String namespace = EntityCache.getDefaultPrefixes().getNamespace(term.substring(0, term.indexOf(":")));
            if (namespace != null)
                request.setTermFilter(namespace + term.split(":")[1]);
        }

        TermQueryBuilder tqb = new TermQueryBuilder("code", request.getTermFilter());
        boolBuilder.should(tqb);
        TermQueryBuilder tqac = new TermQueryBuilder("alternativeCode", request.getTermFilter());
        boolBuilder.should(tqac);
        TermQueryBuilder tqiri = new TermQueryBuilder("iri", request.getTermFilter());
        boolBuilder.should(tqiri);
        TermQueryBuilder tciri = new TermQueryBuilder("termCode.code", request.getTermFilter());
        boolBuilder.should(tciri);

        return boolBuilder;
    }


    private void addFilters(BoolQueryBuilder qry, SearchRequest request) {
        if (request.getFilter() != null) {
            for (Filter filter : request.getFilter()) {
                String field = filter.getField();
                if (filter.getIriValue() != null) {
                    TermsQueryBuilder tqr = new TermsQueryBuilder(field + ".@id",
                            filter.getIriValue().stream().map(TTIriRef::getIri).collect(Collectors.toList()));
                    qry.filter(tqr);
                }
                if (filter.getTextValue() != null) {
                    TermsQueryBuilder tqr = new TermsQueryBuilder(field, filter.getTextValue());
                    qry.filter(tqr);
                }
            }
        }
        if (!request.getSchemeFilter().isEmpty()) {
            List<String> schemes = new ArrayList<>(request.getSchemeFilter());
            TermsQueryBuilder tqr = new TermsQueryBuilder("scheme.@id", schemes);
            qry.filter(tqr);
        }
        if (!request.getStatusFilter().isEmpty()) {
            List<String> statuses = new ArrayList<>(request.getStatusFilter());
            TermsQueryBuilder tqr = new TermsQueryBuilder("status.@id", statuses);
            qry.filter(tqr);
        }
        if (!request.getTypeFilter().isEmpty()) {
            List<String> types = new ArrayList<>(request.getTypeFilter());
            TermsQueryBuilder tqr = new TermsQueryBuilder("entityType.@id", types);
            qry.filter(tqr);
        }
        if (!request.getIsA().isEmpty()) {
            List<String> isas = new ArrayList<>(request.getIsA());
            TermsQueryBuilder tqr = new TermsQueryBuilder("isA.@id", isas);
            qry.filter(tqr);
        }
        if (!request.getMemberOf().isEmpty()) {
            List<String> memberOfs = new ArrayList<>(request.getMemberOf());
            TermsQueryBuilder tqr = new TermsQueryBuilder("memberOf.@id", memberOfs);
            qry.filter(tqr);
        }
        if (!request.getBindingFilter().isEmpty()) {
            BoolQueryBuilder bqr = buildBindingBoolQuery(request.getBindingFilter());
            qry.filter(bqr);
        }
    }


    private QueryBuilder buildBoolQuery(SearchRequest request) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();

        if (!request.getSchemeFilter().isEmpty()) {
            List<String> schemes = new ArrayList<>(request.getSchemeFilter());
            boolQuery.must(new TermsQueryBuilder("scheme.@id", schemes));
        }
        if (!request.getStatusFilter().isEmpty()) {
            List<String> statuses = new ArrayList<>(request.getStatusFilter());
            boolQuery.must(new TermsQueryBuilder("status.@id", statuses));
        }
        if (!request.getTypeFilter().isEmpty()) {
            List<String> types = new ArrayList<>(request.getTypeFilter());
            boolQuery.must(new TermsQueryBuilder("entityType.@id", types));
        }

        if (!request.getIsA().isEmpty()) {
            List<String> isas = new ArrayList<>(request.getIsA());
            boolQuery.must(new TermsQueryBuilder("isA.@id", isas));
        }

        if (!request.getMemberOf().isEmpty()) {
            List<String> memberOfs = new ArrayList<>(request.getMemberOf());
            boolQuery.must(new TermsQueryBuilder("memberOf.@id", memberOfs));
        }

        if (!request.getBindingFilter().isEmpty()) {
            for (SearchBinding binding : request.getBindingFilter()) {
                BoolQueryBuilder inner = new BoolQueryBuilder();
                inner.must(new TermsQueryBuilder("binding.node.@id", binding.getNode().getIri()));
                inner.must(new TermsQueryBuilder("binding.path.@id", binding.getPath().getIri()));
                boolQuery.must(inner);
            }
        }



        return boolQuery;
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


    private QueryBuilder buildAutoCompleteQuery(SearchRequest request) {
        String requestTerm = getMatchTerm(request.getTermFilter());
        BoolQueryBuilder boolQuery;
        if (request.getPage() == 1 && !requestTerm.contains(" ")) {
            boolQuery = buildCodeIriQuery(request);
            PrefixQueryBuilder pqb = new PrefixQueryBuilder("key", request.getTermFilter());
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
        addFilters(boolQuery, request);

        return boolQuery;

    }

    private void addDefaultSorts(SearchRequest request) {
        request.orderBy(o -> o.setField("name").setStartsWithTerm(true));
        request.orderBy(o -> o.setField("preferredName").setStartsWithTerm(true));
        request.orderBy(o -> o
                .setField("subsumptionCount")
                .setDirection(Order.descending));
        request.orderBy(o -> o
          .setField("usageTotal")
          .setDirection(Order.descending));
        request.orderBy(o -> o
                .setField("length")
                .setDirection(Order.ascending));
        ;
    }


    private QueryBuilder buildNGramQuery(SearchRequest request) {
        String requestTerm = getMatchTerm(request.getTermFilter());
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        MatchQueryBuilder mat = new MatchQueryBuilder(TERM_CODE_TERM, requestTerm);
        mat.analyzer("standard");
        mat.operator(Operator.AND);
        mat.fuzziness(Fuzziness.TWO);
        boolQuery.must(mat);
        addFilters(boolQuery, request);
        return boolQuery;

    }

    private QueryBuilder buildMultiWordQuery(SearchRequest request) {
        BoolQueryBuilder qry = new BoolQueryBuilder();
        MatchPhrasePrefixQueryBuilder pqry = new MatchPhrasePrefixQueryBuilder(TERM_CODE_TERM, request.getTermFilter());
        pqry.analyzer("standard");
        qry.should(pqry);
        BoolQueryBuilder wqry = new BoolQueryBuilder();
        qry.should(wqry);
        int wordPos = 0;
        for (String term : request.getTermFilter().split(" ")) {
            wordPos++;
            MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder(TERM_CODE_TERM, term);
            mfs.boost(wordPos == 1 ? 4 : 1);
            mfs.analyzer("standard");
            wqry.must(mfs);
        }
        qry.minimumShouldMatch(1);
        addFilters(qry, request);
        return qry;

    }

    private SearchResponse wrapandRun(QueryBuilder query, SearchRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        SearchResponse response = wrapandRun(query, request, false);
        SearchResponse highestUsageResponse = wrapandRun(query, request, true);
        if (!highestUsageResponse.getEntities().isEmpty())
            response.setHighestUsage(highestUsageResponse.getEntities().get(0).getUsageTotal());
        return response;
    }

    private SearchResponse wrapandRun(QueryBuilder query, SearchRequest request, boolean highestUsage) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        SearchSourceBuilder bld = new SearchSourceBuilder();
        if (hasScriptScore) {
            Map<String, Object> params = getScript(request);
            if (params != null) {
                Script script = new Script(ScriptType.STORED, null, "orderBy", params);
                ScriptScoreQueryBuilder sqr = QueryBuilders.scriptScoreQuery(query, script);
                bld.query(sqr);
            } else
                bld.query(query);
        } else
            bld.query(query);
            addDefaultSorts(request);
        if (!highestUsage) setSorts(request, bld);
        else bld.sort("usageTotal", SortOrder.DESC);
        if (request.getIndex() == null)
            request.setIndex("concept");

        int size = request.getSize();
        int from = request.getFrom();
        if (highestUsage) {
            size = 1;
            from = 0;
        }
        bld.size(size).from(from);
        String termCode = "termCode";

        List<String> defaultFields = new ArrayList<>(Arrays.asList("iri", "name", "code", "alternativeCode", termCode, "entityType", STATUS, SCHEME, USAGE_TOTAL, "preferredName"));
        Set<String> fields = new HashSet<>(defaultFields);
        if (!request.getSelect().isEmpty()) {
            fields.addAll(request.getSelect());
        }
        String[] sources = fields.toArray(String[]::new);
        bld.fetchSource(sources, null);

        try {
            return runQuery(request, bld);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new InterruptedException("failure to run open search query due to :" + ie.getMessage());
        }
    }


    public SearchResponse runQuery(SearchRequest request, SearchSourceBuilder bld) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
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

    private HttpResponse<String> getQueryResponse(SearchRequest request, String queryJson) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {
        String url = System.getenv("OPENSEARCH_URL");
        if (url == null)
            throw new OpenSearchException("Environmental variable OPENSEARCH_URL is not set");


        String index = request.getIndex();
        if (index == null)
            index = System.getenv("OPENSEARCH_INDEX");

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

    private SearchResponse standardResponse(SearchRequest request, JsonNode root, CachedObjectMapper resultMapper, SearchResponse searchResults) throws JsonProcessingException {
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
            if (resultNumber < 6 && null != request.getTermFilter()) {
                fetchMatchTerm(source, request.getTermFilter());
            }
            source.setTermCode(null);
        }
        Integer totalCount = resultMapper.treeToValue(root.get("hits").get("total").get("value"), Integer.class);
        if (null != totalCount) searchResults.setCount(totalCount);
        if (request.getPage() != 0) searchResults.setPage(request.getPage());
        //Sort now donw in query
        // if (!searchResults.isEmpty() && null != request.getTermFilter())
        //   sort(searchResults, request.getTermFilter());
        request.addTiming("Results List built");
        if (null != request.getTermFilter()) searchResults.setTerm(request.getTermFilter());
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

        SearchRequest searchRequest = queryRequestToSearchRequest(queryRequest);
        if (searchRequest == null)
            return null;
        SearchResponse results = multiPhaseQuery(searchRequest);
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


    private SearchRequest queryRequestToSearchRequest(QueryRequest imRequest) throws QueryException {

        SearchRequest request = new SearchRequest();
        if (imRequest.getPage() != null) {
            request.setPage(imRequest.getPage().getPageNumber());
            request.setSize(imRequest.getPage().getPageSize());
        }
        request.setTermFilter(imRequest.getTextSearch());

        Query query = imRequest.getQuery();
        if (query != null) {
            if (!processMatches(request, query, imRequest))
                return null;
            if (query.isActiveOnly()) {
                if (request.getStatusFilter().isEmpty()) request.setStatusFilter(List.of(IM.ACTIVE));
            }
            processSelects(request, query);
        }

        request.addSelect("iri");
        request.addSelect("name");

        return request;
    }

    private void processSelects(SearchRequest request, Query query) {
        if (query.getReturn() != null) {
            query.getReturn().stream()
                    .map(Return::getProperty)
                    .flatMap(Collection::stream)
                    .forEach(prop -> {
                        if (prop.getIri() != null) {
                            if (prop.getIri().equals(RDFS.COMMENT)) request.addSelect("description");
                            else if (prop.getIri().equals(IM.CODE)) request.addSelect("code");
                            else if (prop.getIri().equals(IM.HAS_STATUS)) request.addSelect(STATUS);
                            else if (prop.getIri().equals(IM.ALTERNATIVE_CODE)) request.addSelect("alternativeCode");
                            else if (prop.getIri().equals(IM.HAS_SCHEME)) request.addSelect(SCHEME);
                            else if (prop.getIri().equals(RDF.TYPE)) request.addSelect("entityType");
                            else if (prop.getIri().equals(IM.USAGE_TOTAL)) request.addSelect(USAGE_TOTAL);
                        }
                    });
        }
    }

    private static boolean processMatches(SearchRequest request, Query query, QueryRequest imRequest) throws QueryException {
        if (query.getMatch() == null)
            return true;

        for (Match match : query.getMatch()) {
            if (!processMatch(request, match, imRequest))
                return false;
        }
        return true;
    }

    private static boolean processMatch(SearchRequest request, Match match, QueryRequest imRequest) throws QueryException {
        if (match.getTypeOf() != null) {
            List<String> iris = listFromAlias(match.getTypeOf(), imRequest);
            if (iris == null || iris.isEmpty())
                return false;

            request.getTypeFilter().addAll(iris);
        }

        if (match.getInstanceOf() != null) {
            List<String> iris = listFromAlias(match.getInstanceOf(), imRequest);
            if (iris == null || iris.isEmpty())
                return false;

            request.getIsA().addAll(iris);
        }

        if (match.getWhere() != null && !processProperties(request, match))
            return false;

        if (match.getMatch() != null) {
            if (match.getBoolMatch() != Bool.or)
                return false;

            for (Match subMatch : match.getMatch()) {
                if (!processMatch(request, subMatch, imRequest))
                    return false;
            }
        }

        return true;
    }

    private static boolean processProperties(SearchRequest request, Match match) throws QueryException {
        for (Where w : match.getWhere()) {
            if (IM.HAS_SCHEME.equals(w.getIri())) {
                processSchemeProperty(request, w);
            } else if (IM.HAS_MEMBER.equals(w.getIri()) && w.isInverse()) {
                processMemberProperty(request, w);
            } else if (IM.HAS_STATUS.equals(w.getIri())) {
                processStatusProperty(request, w);
            } else if (RDF.TYPE.equals(w.getIri())) {
                processTypeProperty(request, w);
            } else if (IM.BINDING.equals(w.getIri())) {
                processBindingProperty(request, w);
            }else if (IM.IS_A.equals(w.getIri())) {
                processIsAProperty(request, w);
            }  else {
                return false;
            }
        }

        return true;
    }

    private static void processSchemeProperty(SearchRequest request, Where w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty())
            request.setSchemeFilter(w.getIs().stream().map(Node::getIri).collect(Collectors.toList()));
        else
            throw new QueryException("Scheme filter must be a list (is)");
    }

    private static void processMemberProperty(SearchRequest request, Where w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty())
            request.setMemberOf(w.getIs().stream().map(Node::getIri).collect(Collectors.toList()));
        else
            throw new QueryException("Set membership filter must be a list (is)");
    }

    private static void processStatusProperty(SearchRequest request, Where w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty())
            request.setStatusFilter(w.getIs().stream().map(Node::getIri).collect(Collectors.toList()));
        else
            throw new QueryException("Status filter must be a list (is)");
    }

    private static void processTypeProperty(SearchRequest request, Where w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty())
            request.setTypeFilter(w.getIs().stream().map(Node::getIri).collect(Collectors.toList()));
        else
            throw new QueryException("Status filter must be a list (is)");
    }

    private static void processBindingProperty(SearchRequest request, Where w) throws QueryException {
        if (null != w.getMatch() && null != w.getMatch().getWhere() && !w.getMatch().getWhere().isEmpty()) {
            Optional<Where> path = w.getMatch().getWhere().stream().filter(nestedWhere -> SHACL.PATH.equals(nestedWhere.getIri())).findFirst();
            Optional<Where> node = w.getMatch().getWhere().stream().filter(nestedWhere -> SHACL.NODE.equals(nestedWhere.getIri())).findFirst();
            if (path.isPresent() && null != path.get().getIs() && !path.get().getIs().isEmpty() && node.isPresent() && null != node.get().getIs() && !node.get().getIs().isEmpty()) {
                Optional<Node> pathNode = path.get().getIs().stream().findFirst();
                Optional<Node> nodeNode = node.get().getIs().stream().findFirst();
                if (pathNode.isPresent() && nodeNode.isPresent()) {
                    List<SearchBinding> bindingFilter = new ArrayList<>();
                    bindingFilter.add(new SearchBinding().setNode(new TTIriRef(nodeNode.get().getIri())).setPath(new TTIriRef(pathNode.get().getIri())));
                    request.setBindingFilter(bindingFilter);
                }
            }
        } else
            throw new QueryException("Binding must have a path and a node");
    }

    private static void processIsAProperty(SearchRequest request, Where w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty())
            request.setIsA(w.getIs().stream().map(Node::getIri).collect(Collectors.toList()));
        else
            throw new QueryException("Is a filter must be a list (is)");

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

    private String spellingCorrection(SearchRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        String oldTerm = request.getTermFilter();
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
