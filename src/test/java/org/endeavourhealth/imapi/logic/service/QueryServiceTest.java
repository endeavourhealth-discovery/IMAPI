package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class QueryServiceTest {

  EntityService entityService = new EntityService();
  QueryService queryService = new QueryService();

  //  @Test
  void testCohortQueriesToSQL() throws JsonProcessingException {
    List<TTIriRef> queries = entityService.getEntitiesByType(IM.QUERY);
    System.out.println("Queries: " + queries.size());
    for (TTIriRef ref : queries) {
      System.out.println("Testing " + ref.getName() + " " + ref.getIri());
      try {
        String sql = queryService.getSQLFromIMQIri(ref.getIri(), new HashMap<>());
        assertNotNull(sql);
        if (!sql.startsWith("org.endeavourhealth.imapi.errorhandling.SQLConversionException")) {
          System.out.println("OK");
        } else {
          System.err.println("SQLConversionException: " + sql);
        }
//        String sqlExplain = "EXPLAIN " + sql.replaceAll("\\$referenceDate", "NOW()");
//        try {
////          Statement st = conn.createStatement();
////          ResultSet rs = st.executeQuery(sqlExplain);
//          System.out.println("OK");
//        } catch (Exception e) {
//          System.out.println("ERROR");
//          System.out.println(sqlExplain);
//          System.out.println(e.getMessage());
//          throw e;
//        } finally {
//          rs.close();
//          st.close();
//        }
      } catch (Exception e) {
        System.out.println("ERROR: " + e.getMessage());

//        System.out.println(e.getMessage());
//        throw new RuntimeException(e);
      }
    }
  }
}
