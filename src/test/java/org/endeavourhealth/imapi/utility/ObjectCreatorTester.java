package org.endeavourhealth.imapi.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.interfacemanager.model.ImVocab;
import org.endeavourhealth.interfacemanager.model.RdfsVocab;
import org.junit.jupiter.api.Test;

import static org.endeavourhealth.imapi.dataaccess.SetRepository.IM_1_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectCreatorTester {
  @Test
  public void testObjectCreator() throws JsonProcessingException {
    Query imQuery = new Query();
    imQuery
      .path(p -> p
        .setOptional(true)
        .setIri(ImVocab.HAS_SCHEME)
        .setTypeOf(ImVocab.CONCEPT)
        .setNode("scheme"))
      .return_(s -> s
        .setIri(RdfsVocab.LABEL).as("term"))
      .return_(s -> s
        .setIri(ImVocab.CODE).as("code"))
      .return_(s -> s
        .setIri(ImVocab.HAS_SCHEME)
        .setNodeRef("scheme")
        .setIri(RdfsVocab.LABEL)
        .as("schemeName"))
      .return_(s -> s
        .setIri(ImVocab.USAGE_TOTAL)
        .as("usage"))
      .return_(s -> s
        .setIri(ImVocab.IM_1_ID)
        .as(IM_1_ID));
    String originalQuery = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(imQuery);

    String testJson = """
      {
        path : [ {
        iri : im:scheme,
          node : scheme,
          optional : true,
          typeOf : {
            iri : im:Concept
          }
        } ],
        return : [ {
          iri : rdfs:label,
          as : term
        }, {
          iri : im:code,
          as : code
        }, {
          iri : rdfs:label,
          as : schemeName,
          nodeRef : scheme
        }, {
          iri : im:usageTotal,
          as : usage
        }, {
          iri : im:im1Id,
          as : im1Id
        } ]
      }
      """;
    Query createdQuery = ObjectCreator.create(testJson, Query.class);
    assertEquals(originalQuery, new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(createdQuery));
  }


}
