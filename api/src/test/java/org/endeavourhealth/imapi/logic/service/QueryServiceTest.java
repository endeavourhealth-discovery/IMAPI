package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.icu.impl.CollectionSet;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.sql.IMQtoSQLConverter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class QueryServiceTest {

  EntityService entityService = new EntityService();

  void testCohortQueriesToSQL() throws JsonProcessingException {
    List<TTIriRef> queries = entityService.getEntitiesByType(IM.COHORT_QUERY);
    System.out.println("Queries: " + queries.size());
    for (TTIriRef ref : queries) {
      System.out.println("Testing " + ref.getName() + " " + ref.getIri());
      TTEntity entity = entityService.getBundle(ref.getIri(), Collections.singleton(IM.DEFINITION)).getEntity();
      Query query = entity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
      try {
        String sql = new IMQtoSQLConverter().IMQtoSQL(query);
        assertNotNull(sql);
        if(!sql.startsWith("org.endeavourhealth.imapi.errorhandling.SQLConversionException")) {
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
      } catch (Exception | SQLConversionException e) {
        System.out.println("ERROR: " + e.getMessage());

//        System.out.println(e.getMessage());
//        throw new RuntimeException(e);
      }
    }
  }
}
