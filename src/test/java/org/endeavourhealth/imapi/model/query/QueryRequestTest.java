package org.endeavourhealth.imapi.model.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.queryengine.QueryValidator;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;
import org.junit.jupiter.api.Test;

public class QueryRequestTest {
  QueryService queryService = new QueryService();

  //@Test
  public void validate() throws QueryException, JsonProcessingException {
    Query query= queryService.getQueryFromIri(NAMESPACE.IM+"CSET_OralCorticosteroids");
    new QueryValidator().validateQuery(query);
  }

}
