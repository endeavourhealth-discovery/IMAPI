package org.endeavourhealth.imapi.model.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.model.imq.DisplayMode;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.junit.jupiter.api.Test;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class QueryRequestTest {

  QueryService queryService = new QueryService();

//  @Test
//  void testSameQueryHashCode() throws JsonProcessingException, QueryException {
//    String queryIri = "http://smartlifehealth.info/smh#441fa4d1-3160-4b9d-88e6-02f914c1fe83";
//    Query query = queryService.describeQuery(queryIri, DisplayMode.LOGICAL, Graph.SMARTLIFE);
//    QueryRequest queryRequest = new QueryRequest().setQuery(query);
//    int hashCode = queryService.getQueryRequestHashCode(queryRequest);
//    System.out.println(hashCode);
//    System.out.println(queryRequest.hashCode());
//
//    Query sameQuery = queryService.describeQuery(queryIri, DisplayMode.LOGICAL, Graph.SMARTLIFE);
//    QueryRequest sameQueryRequest = new QueryRequest().setQuery(sameQuery);
//    int sameHashCode = queryService.getQueryRequestHashCode(sameQueryRequest);
//    System.out.println(sameHashCode);
//    System.out.println(sameQueryRequest.hashCode());
//
//    assertEquals(hashCode, sameHashCode);
//    assertEquals(queryRequest.hashCode(), sameQueryRequest.hashCode());
//  }
//
//  @Test
//  void testDifferentQueryHashCode() throws JsonProcessingException, QueryException {
//    String queryIri = "http://smartlifehealth.info/smh#441fa4d1-3160-4b9d-88e6-02f914c1fe83";
//    Query query = queryService.describeQuery(queryIri, DisplayMode.LOGICAL, Graph.SMARTLIFE);
//    QueryRequest queryRequest = new QueryRequest().setQuery(query);
//    int hashCode = queryService.getQueryRequestHashCode(queryRequest);
//    System.out.println(hashCode);
//    System.out.println(queryRequest.hashCode());
//
//    String diffIri = "http://endhealth.info/im#Q_RegisteredGMS";
//    Query diffQuery = queryService.describeQuery(diffIri, DisplayMode.LOGICAL, Graph.IM);
//    QueryRequest diffQueryRequest = new QueryRequest().setQuery(diffQuery);
//    int diffHashCode = queryService.getQueryRequestHashCode(diffQueryRequest);
//    System.out.println(diffHashCode);
//    System.out.println(diffQueryRequest.hashCode());
//
//    assertNotEquals(hashCode, diffHashCode);
//    assertNotEquals(queryRequest.hashCode(), diffQueryRequest.hashCode());
//  }

}
