package org.endeavourhealth.imapi.dataaccess

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.endeavourhealth.imapi.dataaccess.opensearch.Fuzziness
import org.endeavourhealth.imapi.dataaccess.opensearch.SearchSource
import org.endeavourhealth.imapi.errorhandling.DataMissingException
import org.endeavourhealth.imapi.logic.CachedObjectMapper
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException
import org.endeavourhealth.imapi.model.iml.Page
import org.endeavourhealth.imapi.model.imq.QueryException
import org.endeavourhealth.imapi.model.imq.Return
import org.endeavourhealth.imapi.model.imq.TextSearchStyle
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.responses.SearchResponse
import org.endeavourhealth.imapi.model.search.SearchResultSummary
import org.endeavourhealth.imapi.model.tripletree.TTEntity
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.RDF
import org.endeavourhealth.imapi.vocabulary.RDFS
import org.endeavourhealth.imapi.vocabulary.VocabUtils
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.function.Function

class OSQuery {
  private val log = LoggerFactory.getLogger(javaClass)
  private val converter = IMQToOS()

  @Throws(OpenSearchException::class)
  private fun runAndAddResults(fullResults: ObjectNode, builder: SearchSource, found: MutableSet<String>) {
    val results = runQuery(builder)
    if (results.get("hits").get("hits").isEmpty) return
    fullResults.put(
      "totalCount",
      fullResults.get("totalCount").asInt() + results.get("hits").get("total").get("value").asInt()
    )
    val hitsObject = fullResults.get("hits") as ObjectNode
    val hitsArray = hitsObject.get("hits") as ArrayNode
    for (hit in results.get("hits").get("hits")) {
      val entityIri = hit.get("_source").get("iri").textValue()
      if (!found.contains(entityIri)) {
        hitsArray.add(hit)
        found.add(entityIri)
        hitsObject.put("total", hitsObject.get("total").asInt() + 1)
      }
    }
  }

  private fun getTotal(results: JsonNode): Int {
    return results.get("hits").get("total").asInt()
  }

  @Throws(OpenSearchException::class, QueryException::class)
  private fun getOSResults(request: QueryRequest): ObjectNode {
    val query = request.query
    if (request.page == null) {
      request.setPage(
        Page()
          .setPageSize(20)
          .setPageNumber(1)
      )
    }
    val mapper = ObjectMapper()
    val fullResults = mapper.createObjectNode()
    fullResults.put("totalCount", 0)
    val hitsNode = fullResults.putObject("hits") // creates and assigns "hits" node
    hitsNode.put("total", 0)
    hitsNode.putArray("hits")
    var builder: SearchSource?
    val found: MutableSet<String> = hashSetOf()
    builder = converter.buildQuery(request, query, TextSearchStyle.exact)
    runAndAddResults(fullResults, builder, found)
    if (getTotal(fullResults) == 1) return fullResults

    builder = converter.buildQuery(request, query, TextSearchStyle.autocomplete)
    if (builder == null) return fullResults
    runAndAddResults(fullResults, builder, found)
    if (request.textSearchStyle == TextSearchStyle.autocomplete && fullResults.get("totalCount")
        .asInt() > 0
    ) return fullResults
    if (getTotal(fullResults) >= request.page.pageSize) return fullResults

    builder = converter.buildQuery(request, query, TextSearchStyle.ngram, Fuzziness.ZERO)
    if (builder == null) return fullResults
    runAndAddResults(fullResults, builder, found)
    if (getTotal(fullResults) >= request.page.pageSize) return fullResults
    builder = converter.buildQuery(request, query, TextSearchStyle.multiword)
    runAndAddResults(fullResults, builder, found)
    if (getTotal(fullResults) >= request.page.pageSize) return fullResults
    builder = converter.buildQuery(request, query, TextSearchStyle.ngram, Fuzziness.TWO)
    runAndAddResults(fullResults, builder, found)
    if (getTotal(fullResults) >= request.page.pageSize) return fullResults
    if (fullResults.get("hits").get("total").asInt() == 0) {
      val corrected = spellingCorrection(request)
      if (corrected != null) {
        request.setTextSearch(corrected)
        builder = IMQToOS().buildQuery(request, query, TextSearchStyle.autocomplete, Fuzziness.ZERO)
        if (builder == null) return fullResults
        runAndAddResults(fullResults, builder, found)
      }
    }
    return fullResults
  }

  @Throws(OpenSearchException::class)
  private fun spellingCorrection(request: QueryRequest): String? {
    val oldTerm = request.textSearch
    val suggester = """
      {
        "suggest": {
          "spell-check": {
            "text": "$oldTerm",
            "term": {
              "field": "termCode.term",
              "sort": "score"
            }
          }
        }
      }
      """
    val response = getResponse(suggester)
    try {
      CachedObjectMapper().use { om ->
        val root = om.readTree(response.body())
        val suggestions = root.get("suggest").get("spell-check")
        val words = oldTerm.split(" ").toMutableList()
        var newTerm = processSuggestions(suggestions, oldTerm, words)
        if (newTerm == null) {
          newTerm = words.joinToString(" ")
        }
        return if (newTerm == oldTerm) null
        else newTerm
      }
    } catch (e: JsonProcessingException) {
      throw OpenSearchException("Could not parse OpenSearch response", e)
    }
  }

  private fun processSuggestions(
    suggestions: JsonNode,
    oldTerm: String,
    words: MutableList<String>
  ): String? {
    var newTerm: String? = null

    for (suggest in suggestions) {
      val options = suggest.get("options")

      if (options != null && !options.isEmpty) {
        val original = suggest.get("text").asText()
        val swapNode = options.get(0).get("text")
        if (original == oldTerm) newTerm = swapNode.asText()
        else {
          val wordPos = words.indexOf(original)
          if (wordPos > -1) {
            words[wordPos] = swapNode.asText()
          }
        }
      }
    }
    return newTerm
  }

  @Throws(OpenSearchException::class)
  private fun runQuery(bld: SearchSource): JsonNode {
    val elastic = bld.toString()
    val response = getResponse(elastic)
    try {
      CachedObjectMapper().use { om ->
        return om.readTree(response.body())
      }
    } catch (e: JsonProcessingException) {
      throw OpenSearchException("Query execution failed", e)
    }
  }

  @Throws(OpenSearchException::class)
  private fun getResponse(queryJson: String): HttpResponse<String> {
    val url =
      System.getenv("OPENSEARCH_URL") ?: throw OpenSearchException("Environmental variable OPENSEARCH_URL is not set")

    val index = System.getenv("OPENSEARCH_INDEX")
      ?: throw OpenSearchException("Environmental variable OPENSEARCH_AUTH token is not set")

    val httpRequest: HttpRequest?
    try {
      httpRequest = HttpRequest.newBuilder()
        .uri(URI("$url$index/_search"))
        .header("Authorization", "Basic " + System.getenv("OPENSEARCH_AUTH"))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(queryJson))
        .build()
    } catch (e: URISyntaxException) {
      throw OpenSearchException("Invalid OpenSearch URI", e)
    }

    val response: HttpResponse<String>
    HttpClient.newHttpClient().use { client ->
      try {
        response = client
          .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
          .thenApply(Function { res -> res })
          .get()
      } catch (e: InterruptedException) {
        Thread.currentThread().interrupt()
        throw OpenSearchException("OpenSearch call failed", e)
      } catch (e: Exception) {
        throw OpenSearchException("OpenSearch call failed", e)
      }
    }
    if (299 < response.statusCode()) {
      log.debug("Open search request failed with code: ${response.statusCode()}")
      throw OpenSearchException("Search request failed. Error =. ${response.body()}")
    }
    return response
  }

  @Throws(OpenSearchException::class, DataMissingException::class)
  fun OSQueryAsSearchResponse(request: QueryRequest): SearchResponse? {
    try {
      val root = OSQueryAsJsonNode(request) ?: return null
      return if (root.has("entities")) {
        processIMQueryResponse(root, request)
      } else SearchResponse()
    } catch (e: JsonProcessingException) {
      throw OpenSearchException("Could not parse OpenSearch response", e)
    }
  }

  @Throws(DataMissingException::class)
  private fun getSummary(entity: TTEntity): SearchResultSummary {
    if (null == entity.status || null == entity.scheme) {
      return EntityRepository().getEntitySummaryByIri(entity.iri)
    } else {
      val summary = SearchResultSummary()
      summary.setIri(entity.iri)
      summary.setName(entity.getName())
      summary.setPreferredName(entity.getPreferredName())
      summary.setType(entity.getTypes())
      summary.setCode(entity.getCode())
      summary.setStatus(entity.status)
      summary.setScheme(entity.scheme)
      summary.setUsageTotal(entity.getUsageTotal())
      summary.setBestMatch(entity.getBestMatch())
      if (summary.preferredName != null) {
        summary.setName(summary.preferredName)
      }
      return summary
    }
  }

  @Throws(JsonProcessingException::class, DataMissingException::class)
  private fun processIMQueryResponse(root: JsonNode, request: QueryRequest): SearchResponse {
    CachedObjectMapper().use { resultMapper ->
      val searchResults = SearchResponse()
      searchResults.setHighestUsage(0)
      searchResults.setTotalCount(0)
      for (hit in root.get("entities")) {
        val entity = resultMapper.treeToValue(hit, TTEntity::class.java)
        val summary = getSummary(entity)
        searchResults.addEntity(summary)
        summary.usageTotal?.let { usageTotal ->
          searchResults.highestUsage?.let { highestUsage ->
            if (usageTotal > highestUsage) searchResults.setHighestUsage(
              summary.usageTotal
            )
          }
        }
      }
      val totalCount = resultMapper.treeToValue(root.get("totalCount"), Int::class.java)
      if (null != totalCount) searchResults.setTotalCount(totalCount)
      if (request.page != null) {
        searchResults.setPage(request.page.pageNumber)
      }
      request.addTiming("Results List built")
      searchResults.setTerm(request.textSearch)
      return searchResults
    }
  }

  @Throws(OpenSearchException::class)
  fun OSQueryAsJsonNode(request: QueryRequest): JsonNode {
    try {
      val root: JsonNode = getOSResults(request)
      CachedObjectMapper().use { om ->
        if (!root.get("hits").get("hits").isEmpty) {
          val searchResults = om.createObjectNode()
          searchResults.set<JsonNode>("totalCount", root.get("totalCount"))
          val resultNodes = om.createArrayNode()
          searchResults.set<JsonNode>("entities", resultNodes)
          processNodeResults(request, root, om, resultNodes)
          request.addTiming("Results List built")
          return searchResults
        }
        request.addTiming("no results returned")
      }
      return ObjectMapper().createObjectNode()
    } catch (e: Exception) {
      throw OpenSearchException(e.message, e)
    }
  }

  companion object {
    @Throws(JsonProcessingException::class)
    private fun processNodeResults(
      request: QueryRequest,
      root: JsonNode,
      om: CachedObjectMapper,
      resultNodes: ArrayNode
    ) {
      for (hit in root.get("hits").get("hits")) {
        val resultNode = om.createObjectNode()
        resultNodes.add(resultNode)
        val osResult = om.treeToValue(hit.get("_source"), ObjectNode::class.java)

        resultNode.set<JsonNode>("iri", osResult.get("iri"))
        resultNode.set<JsonNode>(RDFS.LABEL.toString(), osResult.get("name"))
        processBestMatch(hit, resultNode)
        processNodeResultReturn(request, osResult, resultNode)
      }
    }

    private fun processBestMatch(hit: JsonNode, resultNode: ObjectNode) {
      val innerHits = hit.get("inner_hits")
      if (innerHits != null) {
        if (innerHits.get("termCode") != null && !innerHits.get("termCode").get("hits").get("hits").isEmpty) {
          val name = resultNode.get(RDFS.LABEL.toString()).asText()
          val bestHit = innerHits.get("termCode").get("hits").get("hits").get(0).get("_source").get("term")
          var bestTerm = bestHit.asText()
          if (!bestTerm.endsWith(")") && name.endsWith(")") && name.contains("(")) {
            bestTerm = bestTerm + " " + name.substring(name.lastIndexOf("("))
          }
          resultNode.put(IM.BEST_MATCH.toString(), bestTerm)
        }
      }
    }

    private fun processNodeResultReturn(request: QueryRequest, osResult: ObjectNode, resultNode: ObjectNode) {
      if (null == request.query.getReturn()) {
        processSourceReturns(osResult, resultNode)
        return
      }
      val select = request.query.getReturn()
      processNodeResultReturnProperty(osResult, resultNode, select)
    }

    private fun processSourceReturns(osResult: ObjectNode, resultNode: ObjectNode) {
      val sources: MutableSet<String> = VocabUtils.asHashSet(
        RDFS.LABEL,
        IM.PREFERRED_NAME,
        IM.CODE,
        IM.USAGE_TOTAL,
        RDF.TYPE,
        IM.HAS_SCHEME,
        IM.HAS_STATUS
      )
      sources.add("iri")
      for (field in sources) {
        val osField = field.substring(field.lastIndexOf("#") + 1)
        if (osResult.get(osField) != null) {
          resultNode.set<JsonNode?>(field, osResult.get(osField))
        }
      }
    }

    private fun processNodeResultReturnProperty(
      osResult: ObjectNode,
      resultNode: ObjectNode,
      select: MutableList<Return>
    ) {
      for (prop in select) {
        prop.iri?.let { field ->
          val osField = field.substringAfterLast("#")
          osResult.get(osField)?.let { value ->
            resultNode.set<JsonNode>(field, value)
          }
        }
      }
    }
  }
}
