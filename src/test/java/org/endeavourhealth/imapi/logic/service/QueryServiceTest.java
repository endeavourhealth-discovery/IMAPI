package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.imq.DatabaseOption;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.EntityType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class QueryServiceTest {

  final EntityService entityService = new EntityService();
  final QueryService queryService = new QueryService();

  //  @Test
  void testCohortQueriesToSQL() {
    List<TTIriRef> queries = entityService.getEntitiesByType(EntityType.QUERY);
    System.out.println("Queries: " + queries.size());
    for (TTIriRef ref : queries) {
      System.out.println("Testing " + ref.getName() + " " + ref.getIri());
      try {
        String sql = queryService.getSQLFromIMQIri(ref.getIri(), DatabaseOption.MYSQL);
        assertNotNull(sql);
        if (!sql.startsWith("org.endeavourhealth.imapi.errorhandling.SQLConversionException")) {
          System.out.println("OK");
        } else {
          System.err.println("SQLConversionException: " + sql);
        }
//        String sqlExplain = "EXPLAIN " + sql.replaceAll("\\$searchDate", "NOW()");
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
