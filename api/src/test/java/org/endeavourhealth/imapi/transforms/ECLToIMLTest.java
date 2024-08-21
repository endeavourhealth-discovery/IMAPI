package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.SparqlConverter;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.junit.jupiter.api.Test;


import java.util.zip.DataFormatException;

import static org.junit.Assert.assertEquals;

class ECLToIMLTest {


  //@Test
  public void ECLToIMLTest() throws DataFormatException, JsonProcessingException, QueryException, EclFormatException {
    String ecl0 = "<<*:  R http://endhealth.info/im#hasMemberParent|member parent | = http://bnf.info/bnf#BNF_040201|4.2.1 Antipsychotic drugs (BNF based value sets)|";
    String ecl1 = "(<< 10363801000001108 \n" +
      " OR << 10363901000001102 ): (<< 127489000  = << 116601002 | Prednisolone (substance) | \n" +
      " OR << 127489000 | Has active ingredient (attribute) |  = << 396458002 | Hydrocortisone (substance) | \n" +
      " OR << 127489000 | Has active ingredient (attribute) |  = << 116571008 | Betamethasone (substance) | \n" +
      " OR << 127489000 | Has active ingredient (attribute) |  = << 396012006 | Deflazacort (substance) | \n" +
      " OR << 127489000 | Has active ingredient (attribute) |  = << 372584003 | Dexamethasone (substance) | \n" +
      " OR << 127489000 | Has active ingredient (attribute) |  = << 116593003 | Methylprednisolone (substance) | \n" +
      " OR << 127489000 | Has active ingredient (attribute) |  = << 116602009 | Prednisone (substance) | \n" +
      " OR << 10363001000001101 | Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute) |  = << 116601002 | Prednisolone (substance) | \n" +
      " OR << 10363001000001101 | Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute) |  = << 396458002 | Hydrocortisone (substance) | \n" +
      " OR << 10363001000001101 | Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute) |  = << 116571008 | Betamethasone (substance) | \n" +
      " OR << 10363001000001101 | Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute) |  = << 396012006 | Deflazacort (substance) | \n" +
      " OR << 10363001000001101 | Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute) |  = << 372584003 | Dexamethasone (substance) | \n" +
      " OR << 10363001000001101 | Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute) |  = << 116602009 | Prednisone (substance) | \n" +
      " OR << 10363001000001101 | Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute) |  = << 116593003 | Methylprednisolone (substance) | )\n" +
      " AND (<< 411116001 | Has manufactured dose form (attribute) |  = << 385268001 | Oral dose form (dose form) | \n" +
      " OR << 13088501000001100 | Has NHS dm+d (dictionary of medicines and devices) VMP (Virtual Medicinal Product) ontology form and route (attribute) |  = << 21000001106 | tablet.oral ontology form and route (qualifier value) | \n" +
      " OR << 13088401000001104 | Has NHS dm+d (dictionary of medicines and devices) VMP (Virtual Medicinal Product) route of administration (attribute) |  = << 26643006 | Oral route (qualifier value) | \n" +
      " OR << 10362901000001105 | Has dispensed dose form (attribute) |  = << 385268001 | Oral dose form (dose form) | )";
    String ecl2 = "(<< 10363801000001108 | Virtual medicinal product (product) | \n" +
      " OR << 10363901000001102 | Actual medicinal product (product) | ): \n" +
      " ({(<< 127489000 | Has active ingredient (attribute) |  = << 116601002 | Prednisolone (substance) | \n" +
      " OR << 127489000 | Has active ingredient (attribute) |  = << 396458002 | Hydrocortisone (substance) |)}\n" +
      " AND (<< 411116001 | Has manufactured dose form (attribute) |  = << 385268001 | Oral dose form (dose form) | \n" +
      " OR << 13088501000001100 | Has NHS dm+d (dictionary of medicines and devices) VMP (Virtual Medicinal Product) ontology form and route (attribute) |  = << 21000001106 | tablet.oral ontology form and route (qualifier value) | \n" +
      " OR << 13088401000001104 | Has NHS dm+d (dictionary of medicines and devices) VMP (Virtual Medicinal Product) route of administration (attribute) |  = << 26643006 | Oral route (qualifier value) | \n" +
      " OR << 10362901000001105 | Has dispensed dose form (attribute) |  = << 385268001 | Oral dose form (dose form) | ))";
    String ecl3 = "< 736479009 |Dose form intended site (intended site)|";
    Query imq0 = new ECLToIMQ().getQueryFromECL(ecl0);
    String query0 = new ObjectMapper().writeValueAsString(imq0);
    String sql = new SparqlConverter(new QueryRequest().setQuery(imq0)).getSelectSparql(null);
    Query imq1 = new ECLToIMQ().getQueryFromECL(ecl1);
    String query = new ObjectMapper().writeValueAsString(imq1);
    assertEquals("{\"bool\":\"or\",\"match\":[{\"instanceOf\":{\"@id\":\"http://snomed.info/sct#10363801000001108\",\"name\":\"Virtual medicinal product (product)\",\"descendantsOrSelfOf\":true}},{\"instanceOf\":{\"@id\":\"http://snomed.info/sct#10363901000001102\",\"name\":\"Actual medicinal product (product)\",\"descendantsOrSelfOf\":true}}],\"where\":[{\"bool\":\"and\",\"where\":[{\"bool\":\"or\",\"where\":[{\"@id\":\"http://snomed.info/sct#127489000\",\"name\":\"Has active ingredient (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#116601002\",\"name\":\"Prednisolone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#127489000\",\"name\":\"Has active ingredient (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#396458002\",\"name\":\"Hydrocortisone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#127489000\",\"name\":\"Has active ingredient (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#116571008\",\"name\":\"Betamethasone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#127489000\",\"name\":\"Has active ingredient (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#396012006\",\"name\":\"Deflazacort (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#127489000\",\"name\":\"Has active ingredient (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#372584003\",\"name\":\"Dexamethasone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#127489000\",\"name\":\"Has active ingredient (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#116593003\",\"name\":\"Methylprednisolone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#127489000\",\"name\":\"Has active ingredient (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#116602009\",\"name\":\"Prednisone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#10363001000001101\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#116601002\",\"name\":\"Prednisolone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#10363001000001101\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#396458002\",\"name\":\"Hydrocortisone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#10363001000001101\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#116571008\",\"name\":\"Betamethasone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#10363001000001101\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#396012006\",\"name\":\"Deflazacort (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#10363001000001101\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#372584003\",\"name\":\"Dexamethasone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#10363001000001101\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#116602009\",\"name\":\"Prednisone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#10363001000001101\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) basis of strength substance (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#116593003\",\"name\":\"Methylprednisolone (substance)\",\"descendantsOrSelfOf\":true}]}]},{\"bool\":\"or\",\"where\":[{\"@id\":\"http://snomed.info/sct#411116001\",\"name\":\"Has manufactured dose form (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#385268001\",\"name\":\"Oral dose form (dose form)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#13088501000001100\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) VMP (Virtual Medicinal Product) ontology form and route (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#21000001106\",\"name\":\"tablet.oral ontology form and route (qualifier value)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#13088401000001104\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) VMP (Virtual Medicinal Product) route of administration (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#26643006\",\"name\":\"Oral route (qualifier value)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#10362901000001105\",\"name\":\"Has dispensed dose form (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#385268001\",\"name\":\"Oral dose form (dose form)\",\"descendantsOrSelfOf\":true}]}]}]}]}", query);
    Query imq2 = new ECLToIMQ().getQueryFromECL(ecl2);
    String query2 = new ObjectMapper().writeValueAsString(imq2);
    assertEquals("{\"bool\":\"or\",\"match\":[{\"instanceOf\":{\"@id\":\"http://snomed.info/sct#10363801000001108\",\"name\":\"Virtual medicinal product (product)\",\"descendantsOrSelfOf\":true}},{\"instanceOf\":{\"@id\":\"http://snomed.info/sct#10363901000001102\",\"name\":\"Actual medicinal product (product)\",\"descendantsOrSelfOf\":true}}],\"where\":[{\"bool\":\"and\",\"where\":[{\"@id\":\"http://endhealth.info/im#roleGroup\",\"match\":{\"where\":[{\"bool\":\"or\",\"where\":[{\"@id\":\"http://snomed.info/sct#127489000\",\"name\":\"Has active ingredient (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#116601002\",\"name\":\"Prednisolone (substance)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#127489000\",\"name\":\"Has active ingredient (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#396458002\",\"name\":\"Hydrocortisone (substance)\",\"descendantsOrSelfOf\":true}]}]}]}},{\"bool\":\"or\",\"where\":[{\"@id\":\"http://snomed.info/sct#411116001\",\"name\":\"Has manufactured dose form (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#385268001\",\"name\":\"Oral dose form (dose form)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#13088501000001100\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) VMP (Virtual Medicinal Product) ontology form and route (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#21000001106\",\"name\":\"tablet.oral ontology form and route (qualifier value)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#13088401000001104\",\"name\":\"Has NHS dm+d (dictionary of medicines and devices) VMP (Virtual Medicinal Product) route of administration (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#26643006\",\"name\":\"Oral route (qualifier value)\",\"descendantsOrSelfOf\":true}]},{\"@id\":\"http://snomed.info/sct#10362901000001105\",\"name\":\"Has dispensed dose form (attribute)\",\"anyRoleGroup\":true,\"descendantsOrSelfOf\":true,\"is\":[{\"@id\":\"http://snomed.info/sct#385268001\",\"name\":\"Oral dose form (dose form)\",\"descendantsOrSelfOf\":true}]}]}]}]}", query2);

    Query imq3 = new ECLToIMQ().getQueryFromECL(ecl3);
    String query3 = new ObjectMapper().writeValueAsString(imq3);
    assertEquals("{\"instanceOf\":{\"@id\":\"http://snomed.info/sct#736479009\",\"name\":\"Dose form intended site (intended site)\",\"descendantsOf\":true}}", query3);
    String ecl4 = "prefix im: http://endhealth.info/im#\n" +
      "^im:BNF_020201 | Thiazides and related diuretics (BNF based value sets) | OR\n" +
      "^im:BNF_0204\n" +
      "OR\n" +
      "^im:BNF_0205051\n" +
      "OR\n" +
      "^im:BNF_0205052";
    Query imq4 = new ECLToIMQ().getQueryFromECL(ecl4);
    String query4 = new ObjectMapper().writeValueAsString(imq3);
    String ecl4Test = new IMQToECL().getECLFromQuery(imq4, true);
    System.out.println(ecl4Test);

  }
}
