package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OpenSearchService {
  public static final String AUTHORIZATION = "Authorization";
  public static final String BASIC = "Basic ";
  public static final String ERROR_CALLING_OPEN_SEARCH = "Error calling OpenSearch";
  private static final Logger LOG = LoggerFactory.getLogger(OpenSearchService.class);
  private final Client client = ClientBuilder.newClient();
  private final String osUrl = System.getenv("OPENSEARCH_URL");
  private final String osAuth = System.getenv("OPENSEARCH_AUTH");
  private final String index = System.getenv("OPENSEARCH_INDEX");

  public EntityDocument getOSDocument(String iri) throws OpenSearchException {
    if (osUrl == null)
      throw new OpenSearchException("Environmental variable OPENSEARCH_AUTH token is not set");

    try (CachedObjectMapper om = new CachedObjectMapper()) {
      SearchSourceBuilder bld = new SearchSourceBuilder()
        .size(1)
        .query(
          new TermQueryBuilder("iri", iri)
        );

      WebTarget target = client.target(osUrl).path(index + "/_search");

      Response response = target
        .request()
        .header(AUTHORIZATION, BASIC + osAuth)
        .header("Content-Type", "application/json")
        .post(Entity.entity(
          bld.toString(),
          MediaType.APPLICATION_JSON
        ));

      if (response.getStatus() != 200) {
        String responseData = response.readEntity(String.class);
        LOG.error(responseData);
        throw new OpenSearchException(ERROR_CALLING_OPEN_SEARCH);
      }

      if (!response.hasEntity())
        return null;

      JsonNode root = om.readTree(response.readEntity(String.class));

      JsonNode hits = root.at("/hits/total/value");
      if (hits == null || hits.asInt() == 0)
        return null;

      String entityJson = root.at("/hits/hits/0/_source").toString();
      return om.readValue(entityJson, EntityDocument.class);

    } catch (Exception e) {
      throw new OpenSearchException("Error retrieving document from OpenSearch", e);
    }
  }

  public void fileDocument(EntityDocument entityDocument) throws OpenSearchException {
    LOG.debug("Loading OS document");
    EntityDocument osDoc = getOSDocument(entityDocument.getIri());
    if (osDoc == null)
      addOSDocument(entityDocument);
    else {
      entityDocument.setId(osDoc.getId());
      updateOSDocument(entityDocument);
    }
  }

  public void addOSDocument(EntityDocument entityDocument) throws OpenSearchException {
    entityDocument.setId(getMaxDocument() + 1);
    updateOSDocument(entityDocument);
  }

  public void updateOSDocument(EntityDocument entityDocument) throws OpenSearchException {
    LOG.debug("Sending OS document");
    WebTarget target = client.target(osUrl).path(index + "/_doc/" + entityDocument.getId());

    try (CachedObjectMapper om = new CachedObjectMapper()) {
      Response response = target
        .request()
        .header(AUTHORIZATION, BASIC + osAuth)
        .put(Entity.entity(
          om.writeValueAsString(entityDocument),
          MediaType.APPLICATION_JSON
        ));

      if (response.getStatus() != 200 && response.getStatus() != 201) {
        String responseData = response.readEntity(String.class);
        LOG.error(responseData);
        throw new IllegalStateException(ERROR_CALLING_OPEN_SEARCH);
      }
    } catch (Exception e) {
      throw new OpenSearchException("Error sending document to OpenSearch", e);
    }
    LOG.debug("OS document sent");
  }

  private int getMaxDocument() throws OpenSearchException {
    LOG.debug("Fetching next OS Document ID");
    WebTarget target = client.target(osUrl).path(index + "/_search");

    String json = """
      {
        "aggs" : {
          "max_id" : {
            "max" : {
              "field" : "id"
            }
          }
        },
        "size":0
      }
      """;

    Response response = target
      .request()
      .header(AUTHORIZATION, BASIC + osAuth)
      .post(Entity.entity(json, MediaType.APPLICATION_JSON));

    if (response.getStatus() != 200) {
      String responseData = response.readEntity(String.class);
      if (responseData.contains("index_not_found_exception")) {
        LOG.info("Index not found, starting from zero");
        return 0;
      } else {
        LOG.error(responseData);
        throw new OpenSearchException(ERROR_CALLING_OPEN_SEARCH);
      }
    } else {
      try (CachedObjectMapper om = new CachedObjectMapper()) {
        String responseData = response.readEntity(String.class);
        JsonNode root = om.readTree(responseData);
        int maxId = root.get("aggregations").get("max_id").get("value").asInt();
        if (maxId > 0)
          LOG.info("Max document ID {}", maxId);
        return maxId;
      } catch (Exception e) {
        throw new OpenSearchException("Error getting max document id from Opensearch", e);
      }
    }
  }
}

