package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.model.imq.DisplayMode;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.junit.jupiter.api.Test;

public class QueryLogicTest {
 // @Test
  public void optimiseQuery() throws QueryException, JsonProcessingException {
    Query query= new QueryService().getQueryFromIri("http://endhealth.info/im#Q_TestQuery");
    new LogicOptimizer().resolveLogic(query, DisplayMode.LOGICAL);
    System.out.println(new ObjectMapper().writeValueAsString(query));
  }
}
