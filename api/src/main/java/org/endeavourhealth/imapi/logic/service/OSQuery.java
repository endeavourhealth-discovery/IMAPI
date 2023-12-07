package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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


/**
 * Class to create and runan open search text query
 */
public class OSQuery {
    private static final Logger LOG = LoggerFactory.getLogger(OSQuery.class);
    private static final String TERM_CODE_TERM = "termCode.term";
    private static final String MATCH_TERM = "matchTerm";
    private static final String SCHEME = "scheme";
    private static final String WEIGHTING = "weighting";
    private static final String STATUS = "status";
    private static final String COMMENT = "comment";
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
    public List<SearchResultSummary>  multiPhaseQuery(SearchRequest request) throws  InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {

        request.addTiming("Entry point for \""+request.getTermFilter()+"\"");
        List<SearchResultSummary> results = defaultQuery(request);
        if (!results.isEmpty()) {
            return results;
        }
        if (request.getTermFilter().contains(" ")){
          results= wrapandRun(buildNGramQuery(request),request);
          if (!results.isEmpty())
              return results;
        }

        String corrected= spellingCorrection(request);
        if (corrected!=null) {
                    request.setTermFilter(corrected);
                    results = defaultQuery(request);
        }
        return results;
    }
    private List<SearchResultSummary> defaultQuery(SearchRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        int page = request.getPage();
        int size = request.getSize();
        request.setFrom(size * (page - 1));
        String term = request.getTermFilter();
        if (null == term) {
            return boolQuery(request);
        }
        List<SearchResultSummary> results;
        if (page == 1 && term != null && (!term.contains(" "))) {
            if (term.contains(":")) {
                String namespace = EntityCache.getDefaultPrefixes().getNamespace(term.substring(0, term.indexOf(":")));
                if (namespace != null)
                    request.setTermFilter(namespace + term.split(":")[1]);
            }
        }


        request.addTiming("start building auto complete query");

        results = autoCompleteQuery(request);
        request.addTiming("end auto complete query - returning results");
        if (!results.isEmpty()) {
            return results;
        }
        return multiWordQuery(request);
    }




    private List<SearchResultSummary> codeIriQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {

        QueryBuilder qry = buildCodeIriQuery(request);
        return wrapandRun(qry, request);
    }


    private List<SearchResultSummary> autoCompleteQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        QueryBuilder qry = buildAutoCompleteQuery(request);
        hasScriptScore= true;
        return wrapandRun(qry, request);
    }

    private String getScript(SearchRequest request) {
        if (request.getOrderBy()==null)
            return null;
        boolean scriptScore=false;
        StringBuilder script= new StringBuilder();
        for (OrderBy orderBy:request.getOrderBy()) {
            if (orderBy.getAnd() != null || orderBy.getIriValue() != null || orderBy.isStartsWithTerm()) {
                if (script.isEmpty()) {
                    script.append("int score=1000000;");
                    script.append("int dif=100000;");
                    scriptScore = true;
                }
                String field;
                int arraySize;
                if (orderBy.getIriValue() != null || orderBy.getTextValue() != null) {
                    if (orderBy.getIriValue() != null) {
                        field = orderBy.getField() + ".@id";
                        arraySize = orderBy.getIriValue().size();
                    }
                    else {
                        field = orderBy.getField();
                        arraySize = orderBy.getTextValue().size();
                    }
                    script.append(" if (doc['").append(field).append("'].size()>0){ ");
                    script.append("def ids=doc['").append(field).append("'];");
                    int index = 0;
                    for (TTIriRef iriValue : orderBy.getIriValue()) {
                        index++;
                        if (index > 1)
                            script.append(" else if ");
                        else
                            script.append(" if ");
                        script.append(" (ids.contains('").append(iriValue.getIri()).append("'))");
                        script.append("    score=score-(dif*").append(index).append(");");
                    }
                    script.append("else score=score-(dif*").append(arraySize + 1).append(");");
                    script.append(" }");
                    script.append(" else score=score-(dif*9)").append(";");
                }
                else if (orderBy.isStartsWithTerm()) {
                    field = orderBy.getField() + ".keyword";
                    script.append(" if (doc['").append(field).append("'].size()>0) { ");
                    script.append(" if (doc['").append(field).append("'].value.toLowerCase().startsWith('").append(request.getTermFilter()).append("'))");
                    script.append(" score =score-dif;");
                    script.append(" else score=score-(dif*2);");
                    script.append("}");
                    script.append(" else score=score-(dif*2);");
                }
                script.append(" dif= dif/10;");
            }
        }
        if (scriptScore) {
            script.append(" return score;");
            return script.toString();
        }
        else
            return null;





    }


    private void setSorts(SearchRequest request,SearchSourceBuilder bld) {
        if (request.getOrderBy()!=null) {
            for (OrderBy orderBy: request.getOrderBy()){
                if (orderBy.getDirection()!=null){
                    bld.sort(orderBy.getField(), orderBy.getDirection()==Order.descending ? SortOrder.DESC : SortOrder.ASC);
                }
            }
        }
    }

    private List<SearchResultSummary> boolQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        QueryBuilder qry = buildBoolQuery(request);
        return wrapandRun(qry, request);
    }


    private List<SearchResultSummary> multiWordQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        QueryBuilder qry = buildMultiWordQuery(request);
        return wrapandRun(qry, request);
    }


    private String getMatchTerm(String term){
        term=term.split(" \\(")[0];
        if (term.length()<30)
            return term;
        else
            return term.substring(0,30);
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
        TermQueryBuilder tqiri = new TermQueryBuilder("iri", request.getTermFilter());
        boolBuilder.should(tqiri);
        TermQueryBuilder tciri = new TermQueryBuilder("termCode.code", request.getTermFilter());
        boolBuilder.should(tciri);

        return boolBuilder;
    }


    private void addFilters(BoolQueryBuilder qry, SearchRequest request) {
        if (request.getFilter()!=null){
            for (Filter filter:request.getFilter()){
                String field= filter.getField();
                if (filter.getIriValue()!=null){
                    TermsQueryBuilder tqr= new TermsQueryBuilder(field+".@id",
                      filter.getIriValue().stream().map(TTIriRef::getIri).collect(Collectors.toList()));
                    qry.filter(tqr);
                }
                if (filter.getTextValue()!=null){
                    TermsQueryBuilder tqr= new TermsQueryBuilder(field,filter.getTextValue());
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
    }


    private QueryBuilder buildBoolQuery(SearchRequest request) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();

        if (!request.getIsA().isEmpty()) {
            List<String> isas = new ArrayList<>(request.getIsA());
            boolQuery.must(new TermsQueryBuilder("isA.@id", isas));
        }

        if (!request.getMemberOf().isEmpty()) {
            List<String> memberOfs = new ArrayList<>(request.getMemberOf());
            boolQuery.must(new TermsQueryBuilder("memberOf.@id", memberOfs));
        }

        return boolQuery;
    }




    private QueryBuilder buildAutoCompleteQuery(SearchRequest request) {
        if (request.getOrderBy()==null){
            addDefaultSorts(request);
        }
        String requestTerm=getMatchTerm(request.getTermFilter());
        BoolQueryBuilder boolQuery ;
        if (request.getPage()==1&&!requestTerm.contains(" ")){
            boolQuery= buildCodeIriQuery(request);
            PrefixQueryBuilder pqb = new PrefixQueryBuilder("key", request.getTermFilter());
            boolQuery.should(pqb);
        }
        else
            boolQuery= new BoolQueryBuilder();

        String prefix=requestTerm.replaceAll("[ '()\\-_./]","").toLowerCase();
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

    private void addDefaultSorts(SearchRequest request){
        request.orderBy(o->o.setField("name").setStartsWithTerm(true));
        request.orderBy(o->o.setField("preferredName").setStartsWithTerm(true));
        request.orderBy(o-> o
          .setField("subsumptionCount")
          .setDirection(Order.descending));
        request.orderBy(o->o
          .setField("length")
          .setDirection(Order.ascending));
        request.orderBy(o->o
          .setField("weighting"));
    }



    private QueryBuilder buildNGramQuery(SearchRequest request){
        if (request.getOrderBy()==null){
            addDefaultSorts(request);
        }
        String requestTerm=getMatchTerm(request.getTermFilter());
        BoolQueryBuilder  boolQuery= new BoolQueryBuilder();
        MatchQueryBuilder mat= new MatchQueryBuilder(TERM_CODE_TERM,requestTerm);
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

    private List<SearchResultSummary> wrapandRun(QueryBuilder query,SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        SearchSourceBuilder bld= new SearchSourceBuilder();
        if (hasScriptScore) {
            String scriptScore = getScript(request);
            if (scriptScore != null) {
                Script script = new Script(ScriptType.INLINE, "painless", scriptScore, new HashMap<>());
                //Script script= new Script(ScriptType.STORED,null,scriptScore,new HashMap<>());
                ScriptScoreQueryBuilder sqr = QueryBuilders.scriptScoreQuery(query, script);
                bld.query(sqr);
                bld.sort("_score");
            }
            else
                bld.query(query);
        }
        else
            bld.query(query);
        setSorts(request,bld);
        if (request.getIndex() == null)
            request.setIndex("concept");

        int size = request.getSize();
        int from = request.getFrom();
        String sortField = request.getSortField();
        String sortDirection = request.getSortDirection();

        bld.size(size).from(from);
        if (null != sortField) {
            bld.sort(sortField, null != sortDirection ? SortOrder.fromString(sortDirection) : SortOrder.DESC);
        }

        String termCode = "termCode";

        List<String> defaultFields = new ArrayList<>(Arrays.asList("iri", "name", "code", termCode, "entityType", STATUS, SCHEME, WEIGHTING,"preferredName"));
        Set<String> fields = new HashSet<>(defaultFields);
        if (!request.getSelect().isEmpty()) {
            fields.addAll(request.getSelect());
        }
        String[] sources= fields.toArray(String[]::new);
        bld.fetchSource(sources, null);

        try {
            return runQuery(request, bld);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new InterruptedException("failure to run open search query due to :" + ie.getMessage());
        }
    }


    public List<SearchResultSummary> runQuery(SearchRequest request, SearchSourceBuilder bld) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        request.addTiming("About to run query");

        String queryJson = bld.toString();
        HttpResponse<String> response= getQueryResponse(request,queryJson);

        try (CachedObjectMapper om = new CachedObjectMapper()) {
            JsonNode root = om.readTree(response.body());
            request.addTiming("Query run and response received. Ready to produce search results");
            List<SearchResultSummary> searchResults = new ArrayList<>();
            return standardResponse(request, root, om, searchResults);
        }

    }

    private HttpResponse<String> getQueryResponse(SearchRequest request,String queryJson) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException {
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
            throw new OpenSearchException("Search request failed. Error =. "+response.body());
        }
        return response;


    }

    private List<SearchResultSummary> standardResponse(SearchRequest request, JsonNode root, CachedObjectMapper resultMapper, List<SearchResultSummary> searchResults) throws JsonProcessingException {
        int resultNumber = 0;
        for (JsonNode hit : root.get("hits").get("hits")) {
            resultNumber++;
            SearchResultSummary source = resultMapper.treeToValue(hit.get("_source"), SearchResultSummary.class);
            searchResults.add(source);
            source.setMatch(source.getName());
            if (source.getPreferredName()!=null) {
                source.setName(source.getPreferredName());
                source.setMatch(source.getName());
            }
            if (resultNumber < 6 && null != request.getTermFilter()) {
                fetchMatchTerm(source, request.getTermFilter());
            }
            source.setTermCode(null);
        }
        //Sort now donw in query
       // if (!searchResults.isEmpty() && null != request.getTermFilter())
         //   sort(searchResults, request.getTermFilter());
        request.addTiming("Results List built");
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
                if ((termCodeStatus != null) && (!(termCodeStatus.equals(IM.INACTIVE))) &&
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

    public List<SearchResultSummary> openSearchQuery(QueryRequest queryRequest) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException, QueryException {
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
        if (searchRequest==null)
            return null;
        List<SearchResultSummary> results = multiPhaseQuery(searchRequest);
        if (results.isEmpty())
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
        for (Match match:matches) {
            if (!matchCompatibleWithOpenSearch(match)){
                return false;
            }
        }
        return true;
    }

    private boolean matchCompatibleWithOpenSearch(Match match){
        if (match.getProperty() != null){
            for (Property property : match.getProperty()) {
                if (!propertyCompatibleWithOpenSearch(property))
                    return false;
            }
        }
        if (match.getMatch() != null){
            return matchesCompatibleWithOpenSearch(match.getMatch());
        }
        return true;
    }

    private boolean propertyCompatibleWithOpenSearch(Property property){
        if (!propertyAvailableInOpenSearch(property.getIri())){
            return false;
        }
        if (property.getMatch()!=null){
           return matchCompatibleWithOpenSearch(property.getMatch());
        }
        return true;
    }

    public ObjectNode convertOSResult(List<SearchResultSummary> searchResults, Query query) {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            ObjectNode result = om.createObjectNode();
            ArrayNode resultNodes = om.createArrayNode();
            result.set("entities", resultNodes);
            for (SearchResultSummary searchResult : searchResults) {
                ObjectNode resultNode = om.createObjectNode();
                resultNodes.add(resultNode);
                resultNode.put("@id", searchResult.getIri());

                if (query != null) {
                    if (query.getReturn() == null)
                        query.return_(s -> s.property(p -> p.setIri(RDFS.LABEL.getIri())));
                    for (Return select : query.getReturn()) {
                        convertOSResultAddNode(om, searchResult, resultNode, select);
                    }
                }
            }
            return result;
        }
    }

    private void convertOSResultAddNode(CachedObjectMapper om, SearchResultSummary searchResult, ObjectNode resultNode, Return select) {
        if (select.getProperty()!=null) {
            for (ReturnProperty prop : select.getProperty()) {
                if (prop.getIri() != null) {
                    String field = prop.getIri();
                    switch (field) {
                        case (RDFS.NAMESPACE + "label") -> resultNode.put(field, searchResult.getName());
                        case (RDFS.NAMESPACE + COMMENT) -> {
                            if (searchResult.getDescription() != null)
                                resultNode.put(field, searchResult.getDescription());
                        }
                        case (IM.NAMESPACE + "code") -> resultNode.put(field, searchResult.getCode());
                        case (IM.NAMESPACE + STATUS) -> resultNode.set(field, fromIri(searchResult.getStatus(), om));
                        case (IM.NAMESPACE + SCHEME) -> resultNode.set(field, fromIri(searchResult.getScheme(), om));
                        case (RDF.NAMESPACE + "type") -> resultNode.set(field, arrayFromIri(searchResult.getEntityType(), om));
                        case (IM.NAMESPACE + WEIGHTING) -> resultNode.put(field, searchResult.getWeighting());
                    }
                }
            }
        }
    }

    private ObjectNode fromIri(TTIriRef iri, CachedObjectMapper om){
        ObjectNode node= om.createObjectNode();
        node.put("@id",iri.getIri());
        if (iri.getName()!=null)
            node.put("name",iri.getName());
        return node;
    }

    private ArrayNode arrayFromIri(Set<TTIriRef> iris, CachedObjectMapper om){

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
                if (request.getStatusFilter().isEmpty()) request.setStatusFilter(List.of(IM.ACTIVE.getIri()));
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
                        switch (prop.getIri()) {
                            case (RDFS.NAMESPACE + COMMENT) -> request.addSelect("description");
                            case (IM.NAMESPACE + "code") -> request.addSelect("code");
                            case (IM.NAMESPACE + STATUS) -> request.addSelect(STATUS);
                            case (IM.NAMESPACE + SCHEME) -> request.addSelect(SCHEME);
                            case (RDF.NAMESPACE + "type") -> request.addSelect("entityType");
                            case (IM.NAMESPACE + WEIGHTING) -> request.addSelect(WEIGHTING);
                            default -> {
                            }
                        }
                    }
                });
        }
    }

    private static boolean processMatches(SearchRequest request, Query query, QueryRequest imRequest) throws QueryException {
        if (query.getMatch() == null)
            return true;

        for (Match match: query.getMatch()) {
            if (!processMatch(request, match, imRequest))
                return false;
        }
        return true;
    }

    private static boolean processMatch(SearchRequest request, Match match, QueryRequest imRequest) throws QueryException {
        if (match.getTypeOf() != null) {
            List<String> iris = listFromAlias(match.getTypeOf(),imRequest);
            if (iris == null || iris.isEmpty())
                return false;

            request.getTypeFilter().addAll(iris);
        }

        if (match.getInstanceOf() != null) {
            List<String> iris = listFromAlias(match.getInstanceOf(),imRequest);
            if (iris == null || iris.isEmpty())
                return false;

            request.getIsA().addAll(iris);
        }

        if (match.getProperty() != null && !processProperties(request, match))
            return false;

        if (match.getMatch() != null) {
            if (match.getBool() != Bool.or)
                return false;

            for (Match subMatch : match.getMatch()) {
                if (!processMatch(request, subMatch, imRequest))
                    return false;
            }
        }

        return true;
    }

    private static boolean processProperties(SearchRequest request, Match match) throws QueryException {
        for(Property w : match.getProperty()) {
            if (IM.HAS_SCHEME.getIri().equals(w.getIri())) {
                processSchemeProperty(request, w);
            } else if (IM.HAS_MEMBER.getIri().equals(w.getIri()) && w.isInverse()) {
                processMemberProperty(request, w);
            } else if (IM.HAS_STATUS.getIri().equals(w.getIri())) {
                processStatusProperty(request, w);
            } else {
                return false;
            }
        }

        return true;
    }

    private static void processSchemeProperty(SearchRequest request, Property w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty())
            request.setSchemeFilter(w.getIs().stream().map(Node::getIri).toList());
        else
            throw new QueryException("Scheme filter must be a list (is)");
    }

    private static void processMemberProperty(SearchRequest request, Property w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty())
            request.setMemberOf(w.getIs().stream().map(Node::getIri).toList());
        else
            throw new QueryException("Set membership filter must be a list (is)");
    }

    private static void processStatusProperty(SearchRequest request, Property w) throws QueryException {
        if (w.getIs() != null && !w.getIs().isEmpty())
            request.setStatusFilter(w.getIs().stream().map(Node::getIri).toList());
        else
            throw new QueryException("Status filter must be a list (is)");
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
                            argument.getValueIriList().stream().map(TTIriRef::getIri).toList();
                    }
                }
            }
            throw new QueryException("Query Variable " + value + " has not been assigned is the request");
        } else
            throw new QueryException("Query has a query variable but request has no arguments set");
    }

    private String spellingCorrection(SearchRequest request) throws OpenSearchException, URISyntaxException, ExecutionException, InterruptedException, JsonProcessingException {
        String oldTerm= request.getTermFilter();
        String newTerm=null;
        String suggestor = "{\n" +
              "  \"suggest\": {\n" +
              "    \"spell-check\": {\n" +
              "      \"text\": \"" + oldTerm+ "\",\n" +
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
                JsonNode suggestions= root.get("suggest").get("spell-check");
                String[] words= oldTerm.split(" ");
               for (JsonNode suggest: suggestions){
                   if (suggest.has("options")){
                       JsonNode options= suggest.get("options");
                       if (options.size()>0){
                           String original= suggest.get("text").asText();
                           JsonNode swapNode= options.get(0).get("text");
                           if (original.equals(oldTerm))
                               newTerm= swapNode.asText();
                           else {
                               int wordPos=getWordPos(words,original);
                               if (wordPos>-1){
                                   words[wordPos]= swapNode.asText();
                               }
                           }
                       }

                   }
               }
               if (newTerm==null) {
                   newTerm = String.join(" ", words);
               }
               if (newTerm.equals(oldTerm))
                    return null;
                else
                    return newTerm;
        }
    }

    private int getWordPos(String[] words,String wordToFind){
        for (int i=0; i<words.length; i++){
            if (words[i].equals(wordToFind))
                return i;
        }
        return -1;
    }


    private static boolean propertyAvailableInOpenSearch(String iri) {
        if (iri == null)
            return false;

        return List.of(
            RDFS.LABEL.getIri(),
            RDFS.COMMENT.getIri(),
            IM.CODE.getIri(),
            IM.HAS_STATUS.getIri(),
            IM.HAS_SCHEME.getIri(),
            RDF.TYPE.getIri(),
            IM.WEIGHTING.getIri(),
            IM.HAS_MEMBER.getIri()
        ).contains(iri);
    }

}
