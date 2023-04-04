package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.RDF;
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
import java.util.zip.DataFormatException;

/**
 * Class to create and runan open search text query
 */
public class OSQuery {
    private static final Logger LOG = LoggerFactory.getLogger(OSQuery.class);
    private final Set<String> resultCache = new HashSet<>();
    private final String termCodeTerm = "termCode.term";
    private final String matchTerm = "matchTerm";
    private final String scheme = "scheme";
    private final String weighting = "weighting";
    private final String status = "status";
    private final String comment = "comment";

    public Set<String> getResultCache() {
        return resultCache;
    }


    public OSQuery addResult(String item) {
        resultCache.add(item);
        return this;
    }


    private List<SearchResultSummary> codeIriQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        SearchSourceBuilder bld= new SearchSourceBuilder();
        QueryBuilder qry = buildCodeIriQuery(request);
        bld.query(qry);
        return wrapandRun(bld, request);
    }


    private List<SearchResultSummary> autoCompleteQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        SearchSourceBuilder bld= new SearchSourceBuilder();
        QueryBuilder qry = buildAutoCompleteQuery(request);
        bld.query(qry);
        bld.sort("length");
        return wrapandRun(bld, request);
    }

    private List<SearchResultSummary> boolQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        SearchSourceBuilder bld= new SearchSourceBuilder();
        QueryBuilder qry = buildBoolQuery(request);
        bld.query(qry);
        return wrapandRun(bld, request);
    }

    private List<SearchResultSummary> iriTermQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        SearchSourceBuilder bld= new SearchSourceBuilder();
        QueryBuilder qry = buildIriTermQuery(request);
        bld.query(qry);
        bld.sort("length");
        return wrapandRun(bld, request);
    }

    private List<SearchResultSummary> multiWordQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
        SearchSourceBuilder bld= new SearchSourceBuilder();
        QueryBuilder qry = buildMultiWordQuery(request);
        bld.query(qry);
        return wrapandRun(bld, request);
    }

    /**
     * A rapid response page oriented query that bundles together code/iri key prefix term and the multi-word sequentially
     * <p> Each time a result is found, if more than one page it may be returned leaving the calling method to determine whether to add more results.
     * <p>Paging is supported</p>
     * subsequent calls.
     *
     * @param request Search request object
     * @return search request object
     * @throws DataFormatException if problem with data format of query
     */
    public List<SearchResultSummary>  multiPhaseQuery(SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {

        String term = request.getTermFilter();
        int page = request.getPage();
        int size = request.getSize();

        request.setFrom(size * (page - 1));

        List<SearchResultSummary> results;

        request.setPage(1);
        if (term != null && term.length() < 3)
            return codeIriQuery(request);

        if (page == 1 && term != null && (!term.contains(" "))) {
            if (term.contains(":")) {
                String namespace = EntityCache.getDefaultPrefixes().getNamespace(term.substring(0, term.indexOf(":")));
                if (namespace != null)
                    request.setTermFilter(namespace + term.split(":")[1]);
            }
            results = iriTermQuery(request);
            if (!results.isEmpty())
                return results;
        }

        if (null == term) {
            return boolQuery(request);
        }

        results = autoCompleteQuery(request);
        if (!results.isEmpty()) {
            return results;
        }
        return multiWordQuery(request);
    }

    private String getMatchTerm(String term){
        term=term.replaceAll(" ","");
        if (term.length()<25)
            return term;
        else
            return term.substring(0,24);
    }

    private QueryBuilder buildCodeIriQuery(SearchRequest request) {
        BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
        String term = request.getTermFilter();
        if (term.contains(":")) {
            String namespace = EntityCache.getDefaultPrefixes().getNamespace(term.substring(0, term.indexOf(":")));
            if (namespace != null)
                request.setTermFilter(namespace + term.split(":")[1]);
        }

        TermQueryBuilder tqb = new TermQueryBuilder("code", request.getTermFilter());
        tqb.boost(2F);
        boolBuilder.should(tqb);
        TermQueryBuilder tqiri = new TermQueryBuilder("iri", request.getTermFilter());
        tqiri.boost(2F);
        boolBuilder.should(tqiri);
        TermQueryBuilder tciri = new TermQueryBuilder("termCode.code", request.getTermFilter());
        tqiri.boost(1F);
        boolBuilder.should(tciri);
        tqb = new TermQueryBuilder("key", request.getTermFilter().toLowerCase());
        boolBuilder.should(tqb).minimumShouldMatch(1);
        addFilters(boolBuilder, request);
        return boolBuilder;
    }


    private void addFilters(BoolQueryBuilder qry, SearchRequest request) {
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
    }

    private QueryBuilder buildBoolQuery(SearchRequest request) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        if(null == request.getIsA() || request.getIsA().isEmpty()) {
            return boolQuery;
        }
        List<String> isas = new ArrayList<>(request.getIsA());
        boolQuery.must(new TermsQueryBuilder("isA.@id", isas));
        return boolQuery;
    }




    private QueryBuilder buildAutoCompleteQuery(SearchRequest request) {
        String requestTerm=getMatchTerm(request.getTermFilter());

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        MatchPhraseQueryBuilder mpq = new MatchPhraseQueryBuilder(matchTerm, requestTerm).boost(1.5F);
        boolQuery.should(mpq);
        MatchPhrasePrefixQueryBuilder mpp = new MatchPhrasePrefixQueryBuilder(matchTerm, requestTerm).boost(1.5F);
        boolQuery.should(mpp).minimumShouldMatch(1);
        addFilters(boolQuery, request);
        return boolQuery;

    }

    private QueryBuilder buildIriTermQuery(SearchRequest request) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        TermQueryBuilder tqb = new TermQueryBuilder("code", request.getTermFilter());
        tqb.boost(4F);
        boolQuery.should(tqb);
        TermQueryBuilder tqiri = new TermQueryBuilder("iri", request.getTermFilter());
        tqiri.boost(4F);
        boolQuery.should(tqiri);
        PrefixQueryBuilder pqb = new PrefixQueryBuilder("key", request.getTermFilter());
        pqb.boost(2F);
        boolQuery.should(pqb);
        MatchPhraseQueryBuilder mpq = new MatchPhraseQueryBuilder(termCodeTerm, request.getTermFilter()).boost(1.5F);
        boolQuery.should(mpq);
        MatchPhraseQueryBuilder mpc = new MatchPhraseQueryBuilder("termCode.code", request.getTermFilter()).boost(2.5F);
        boolQuery.should(mpc);
        MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder(matchTerm, request.getTermFilter()).boost(0.5F);
        boolQuery.should(mfs).minimumShouldMatch(1);
        addFilters(boolQuery, request);
        return boolQuery;

    }

    private QueryBuilder buildMultiWordQuery(SearchRequest request) {
        BoolQueryBuilder qry = new BoolQueryBuilder();
        MatchPhrasePrefixQueryBuilder pqry = new MatchPhrasePrefixQueryBuilder(termCodeTerm, request.getTermFilter());
        qry.should(pqry);
        BoolQueryBuilder wqry = new BoolQueryBuilder();
        qry.should(wqry);
        int wordPos = 0;
        for (String term : request.getTermFilter().split(" ")) {
            wordPos++;
            MatchPhrasePrefixQueryBuilder mfs = new MatchPhrasePrefixQueryBuilder(termCodeTerm, term);
            mfs.boost(wordPos == 1 ? 4 : 1);
            wqry.must(mfs);
        }
        qry.minimumShouldMatch(1);
        addFilters(qry, request);
        return qry;

    }

    private List<SearchResultSummary> wrapandRun(SearchSourceBuilder bld,SearchRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException {
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

        Set<String> fields = Set.of("iri", "name", "code", termCode, "entityType", status, scheme, weighting,"preferredName");
        if (!request.getSelect().isEmpty()) {
            for (String select : request.getSelect()) {
                if (!fields.contains(select))
                    fields.add(select);
            }
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
       String queryJson = bld.toString();
       /*
       String queryJson= "{\n" +
          "  \"query\" : {\n" +
          "    \"match_all\": {}\n" +
          "  }\n" +
          "}";

        */

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
            List<SearchResultSummary> searchResults = new ArrayList<>();
            return standardResponse(request, root, om, searchResults);
        }
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
        if (!searchResults.isEmpty() && null != request.getTermFilter())
            sort(searchResults, request.getTermFilter());
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
                TTIriRef status = tc.getStatus();
                if ((status != null) && (!(status.equals(IM.INACTIVE))) &&
                    (tc.getTerm() != null && tc.getTerm().toLowerCase().startsWith(searchTerm))) {
                    searchResult.setMatch(tc.getTerm());
                    break;
                }
            }
        }
    }

    private void sort(List<SearchResultSummary> initialList, String term) {
        term = term.toLowerCase(Locale.ROOT);
        String finalTerm = term;
        initialList.sort(Comparator.comparing((SearchResultSummary sr) -> !sr.getMatch().toLowerCase(Locale.ROOT).startsWith(finalTerm))
                .thenComparing(sr -> sr.getMatch().length()));

    }

    /**
     * An open search query using IMQ- Returns null if query cannot be done via open search.
     * <p>Application logic should try IM direct if null is returned</p>
     *
     * @param queryRequest Query request object containing any textSearch term, paging and a query.
     * @return ObjectNode as determined by select statement
     * @throws DataFormatException if content of query definition is invalid
     */

    public ObjectNode openSearchQuery(QueryRequest queryRequest) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException, DataFormatException {
        if (queryRequest.getTextSearch() == null)
            return null;
        Query query = queryRequest.getQuery();

        if (query.getSelect() != null && !validateSelects(query)) {
            return null;
        }



        if (query.getMatch() != null && !validateFroms(query.getMatch())) {
            return null;
        }

        SearchRequest searchRequest = convertIMToOS(queryRequest);
        if (searchRequest==null)
            return null;
        List<SearchResultSummary> results = multiPhaseQuery(searchRequest);
        if (results.isEmpty())
            return null;
        else
            return convertOSResult(results, query);

    }

    private static boolean validateSelects(Query query) {
        for (Select select : query.getSelect()) {
            if (select.getId() != null && !propIsSupported(select.getId()))
                return false;
            if (select.getSelect() != null)
                return false;
        }
        return true;
    }

    private boolean validateFroms(List<Match> matches) {
        for (Match match:matches) {
            if (match.getWhere() != null)
                for (Where where : match.getWhere()) {
                    if (!validateWhere(where))
                        return false;
                }
            if (match.getMatch() != null)
                    if (!validateFroms(match.getMatch()))
                        return false;
            return true;
        }
        return true;
    }

    private boolean validateWhere(Where where){
        if (where.getWhere()!=null && !propIsSupported(where.getId())){
            return false;
        }
        if (where.getWhere()!=null){
            for (Where subWhere:where.getWhere())
                if (!validateWhere(subWhere))
                    return false;
        }
        return true;


    }
    private ObjectNode convertOSResult(List<SearchResultSummary> searchResults, Query query) {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            ObjectNode result = om.createObjectNode();
            ArrayNode resultNodes = om.createArrayNode();
            result.set("entities", resultNodes);
            for (SearchResultSummary searchResult : searchResults) {
                ObjectNode resultNode = om.createObjectNode();
                resultNodes.add(resultNode);
                resultNode.put("@id", searchResult.getIri());
                if (query.getSelect()==null)
                    query.select(s->s.setIri(RDFS.LABEL.getIri()));
                for (Select select : query.getSelect()) {
                    convertOSResultAddNode(om, searchResult, resultNode, select);
                }
            }
            return result;
        }
    }

    private void convertOSResultAddNode(CachedObjectMapper om, SearchResultSummary searchResult, ObjectNode resultNode, Select select) {
        Element prop = select;
        if (prop.getId() != null) {
            String field = prop.getId();
            switch (field) {
                case (RDFS.NAMESPACE + "label"):
                    resultNode.put(field, searchResult.getName());
                    break;
                case (RDFS.NAMESPACE + comment):
                    if (searchResult.getDescription() != null)
                        resultNode.put(field, searchResult.getDescription());
                    break;
                case (IM.NAMESPACE + "code"):
                    resultNode.put(field, searchResult.getCode());
                    break;
                case (IM.NAMESPACE + status):
                    resultNode.set(field, fromIri(searchResult.getStatus(), om));
                    break;
                case (IM.NAMESPACE + scheme):
                    resultNode.set(field, fromIri(searchResult.getScheme(), om));
                    break;
                case (RDF.NAMESPACE + "type"):
                    resultNode.set(field, arrayFromIri(searchResult.getEntityType(), om));
                    break;
                case (IM.NAMESPACE + weighting):
                    resultNode.put(field, searchResult.getWeighting());
                    break;
                default:

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


    private SearchRequest convertIMToOS(QueryRequest imRequest) throws DataFormatException {

        SearchRequest request = new SearchRequest();
        if (imRequest.getPage() != null) {
            request.setPage(imRequest.getPage().getPageNumber());
            request.setSize(imRequest.getPage().getPageSize());
        }
        request.setTermFilter(imRequest.getTextSearch());
        Query query = imRequest.getQuery();
        if (!validateFromList(request, query,imRequest))
            return null;
        request.addSelect("iri");
        request.addSelect("name");
        if (query.isActiveOnly())
            request.setStatusFilter(List.of(IM.ACTIVE.getIri()));
        processSelects(request, query);

        return request;
    }

    private void processSelects(SearchRequest request, Query query) {
        if (query.getSelect() != null) {
            for (Select select : query.getSelect()) {
                Element prop = select;
                if (prop.getId() != null) {
                    switch (prop.getId()) {
                        case (RDFS.NAMESPACE + comment):
                            request.addSelect("description");

                            break;
                        case (IM.NAMESPACE + "code"):
                            request.addSelect("code");
                            break;
                        case (IM.NAMESPACE + status):
                            request.addSelect(status);
                            break;
                        case (IM.NAMESPACE + scheme):
                            request.addSelect(scheme);
                            break;
                        case (RDF.NAMESPACE + "type"):
                            request.addSelect("entityType");
                            break;
                        case (IM.NAMESPACE + weighting):
                            request.addSelect(weighting);
                            break;
                        default:
                    }
                }
            }
        }
    }

    private static boolean validateFromList(SearchRequest request, Query query,QueryRequest imRequest) throws DataFormatException {
        for (Match match: query.getMatch()) {
            if (!addFromTypes(request, match, imRequest))
                return false;
        }
        return true;
    }

    private static boolean addFromTypes(SearchRequest request, Match match, QueryRequest imRequest) throws DataFormatException {
        if (match.getType()!= null){
                request.addType(match.getType());
                if (match.getMatch()!=null)
                    return false;
                if (match.getWhere()!=null)
                    return false;
                return true;
        }
        else if (match.isDescendantsOrSelfOf()) {
                return processSubTypes(request, match, imRequest);
        }
        else if (match.getId()!=null)
                throw new DataFormatException("Text searches on sets or single instances not supported. Are you looking for types (match.sourceType= type, or subtypes match.isIncludeSubtypes(true");

        else if (match.getMatch() != null) {
            if (match.getBoolMatch()!=Bool.or){
                return false;
            }
            for (Match subMatch : match.getMatch()) {
                if (!addFromTypes(request, subMatch, imRequest))
                    return false;
            }
            return true;

        }
        return false;
    }

    private static boolean processSubTypes(SearchRequest request, Match match, QueryRequest imRequest) throws DataFormatException {
        List<String> isas= listFromAlias(match,imRequest);
        if (isas==null)
            return false;
        request.setIsA(isas);
        return true;
    }

    private static List<String> listFromAlias(Match match, QueryRequest queryRequest) throws DataFormatException {
        if (match.getParameter() == null) {
            List<String> iriList = new ArrayList<>();
            iriList.add(match.getId());
            return iriList;
        }

        String value = match.getParameter();
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
            throw new DataFormatException("Query Variable " + value + " has not been assigned in the request");
        } else
            throw new DataFormatException("Query has a query variable but request has no arguments set");
    }


    private static boolean propIsSupported(String iri) {
        if (iri == null)
            return false;
        switch (iri) {
            case (RDFS.NAMESPACE + "label"):
            case (RDFS.NAMESPACE + "comment"):
            case (IM.NAMESPACE + "code"):
            case (IM.NAMESPACE + "status"):
            case (IM.NAMESPACE + "scheme"):
            case (RDF.NAMESPACE + "type"):
            case (IM.NAMESPACE + "weighting"):
                return true;
            default:
                return false;
        }

    }

}
