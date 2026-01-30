package org.endeavourhealth.imapi.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.dataaccess.SetRepository.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectCreatorTester {
  @Test
  public void testObjectCreator() throws JsonProcessingException {
    Query imQuery= new Query();
    imQuery
      .path(p->p
        .setOptional(true)
        .setIri(IM.HAS_SCHEME)
        .setTypeOf(IM.CONCEPT)
        .setNode("scheme"))
      .return_(s->s
        .setIri(RDFS.LABEL).as("term"))
      .return_(s -> s
        .setIri(IM.CODE).as("code"))
      .return_(s -> s
        .setIri(IM.HAS_SCHEME)
        .setNodeRef("scheme")
        .setIri(RDFS.LABEL)
        .as("schemeName"))
      .return_(s -> s
        .setIri(IM.USAGE_TOTAL)
        .as("usage"))
      .return_(s -> s
        .setIri(IM.IM_1_ID)
        .as(IM_1_ID));
    String originalQuery= new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(imQuery);

    String testJson= """
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
    Query createdQuery= ObjectCreator.create(testJson, Query.class);
    assertEquals(originalQuery,new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(createdQuery));
  }


}
