package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.reasoner.SetMemberGenerator;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.logic.service.SetService;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.junit.jupiter.api.Test;

public class ComplexECLTest {
  //@Test
  public void expanderTest() throws QueryException, JsonProcessingException {
    new SetMemberGenerator().generateMembers(Namespace.IM+"CSET_OralCorticosteroids", Graph.IM);
  }
  //@Test
  public void eclToIMQ() throws QueryException {
    ECLToIMQ eclToIMQ = new ECLToIMQ();
    Query query= eclToIMQ.convertECL(getEclComplex());
    SearchResponse results= new EclService().eclSearch(new EclSearchRequest().setEclQuery(query));
  }

  public String getEclSimple(){
    return "<<763158003";
  }

  public String getEclComplex(){
    return """
      <<((<< 10363801000001108|Virtual medicinal product (product)|
       OR << 10363901000001102|Actual medicinal product (product)|
      ): (<< 127489000|Has active ingredient (attribute)| =  (<< 116601002|Prednisolone (substance)|
       or << 396458002|Hydrocortisone (substance)|
       or << 116571008|Betamethasone (substance)|
       or << 396012006|Deflazacort (substance)|
       or << 372584003|Dexamethasone (substance)|
       or << 116593003|Methylprednisolone (substance)|
       or << 116602009|Prednisone (substance)|)
       or << 10363001000001101|Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute)| =  (<< 116601002|Prednisolone (substance)|
       or << 396458002|Hydrocortisone (substance)|
       or << 116571008|Betamethasone (substance)|
       or << 396012006|Deflazacort (substance)|
       or << 372584003|Dexamethasone (substance)|
       or << 116602009|Prednisone (substance)|
       or << 116593003|Methylprednisolone (substance)|))
       , (<< 10362901000001105|Has dispensed dose form (attribute)| = << 385268001|Oral dose form (dose form)|
       or << 13088501000001100|Has NHS dm+d (dictionary of medicines and devices) VMP (Virtual Medicinal Product) ontology form and route (attribute)| = << 21000001106|tablet.oral ontology form and route (qualifier value)|
       or << 13088401000001104|Has NHS dm+d (dictionary of medicines and devices) VMP (Virtual Medicinal Product) route of administration (attribute)| = << 26643006|Oral route (qualifier value)|
       or << 411116001|Has manufactured dose form (attribute)| = << 385268001|Oral dose form (dose form)|))
      """;

  }
}
