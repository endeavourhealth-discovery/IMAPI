package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
public class OpenSearchService {
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

//                    new MatchQueryBuilder("iri", iri)

//                    new BoolQueryBuilder()
//                        .should(
//                            new TermQueryBuilder("iri", iri)
//                        )
                );

            WebTarget target = client.target(osUrl).path(index + "/_search");

            Response response = target
                .request()
                .header("Authorization", "Basic " + osAuth)
                .header("Content-Type", "application/json")
                .post(Entity.entity(
                    bld.toString(),
                    MediaType.APPLICATION_JSON
                ));

            if (response.getStatus() != 200) {
                String responseData = response.readEntity(String.class);
                LOG.error(responseData);
                throw new OpenSearchException("Error calling OpenSearch");
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
                .header("Authorization", "Basic " + osAuth)
                .put(Entity.entity(
                    om.writeValueAsString(entityDocument),
                    MediaType.APPLICATION_JSON
                ));

            if (response.getStatus() != 200 && response.getStatus() != 201) {
                String responseData = response.readEntity(String.class);
                LOG.error(responseData);
                throw new IllegalStateException("Error calling OpenSearch");
            }
        } catch (Exception e) {
            throw new OpenSearchException("Error sending document to OpenSearch", e);
        }
        LOG.debug("OS document sent");
    }

    private int getMaxDocument() throws OpenSearchException {
        LOG.debug("Fetching next OS Document ID");
        WebTarget target = client.target(osUrl).path(index + "/_search");

        Response response = target
            .request()
            .header("Authorization", "Basic " + osAuth)
            .post(Entity.entity("{\n" +
                "    \"aggs\" : {\n" +
                "      \"max_id\" : {\n" +
                "        \"max\" : { \n" +
                "          \"field\" : \"id\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"size\":0\n" +
                "  }", MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200) {
            String responseData = response.readEntity(String.class);
            if (responseData.contains("index_not_found_exception")) {
                LOG.info("Index not found, starting from zero");
                return 0;
            } else {
                LOG.error(responseData);
                throw new OpenSearchException("Error calling OpenSearch");
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

