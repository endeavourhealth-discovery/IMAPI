package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.iml.Query;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ECLToIMTest {

//	@Test
	void convertConceptSet() throws DataFormatException, JsonProcessingException {
		for (String ecl: List.of(
			"<  404684003 |Clinical finding| \n" +
				"\t: {  \n" +
				"\t\t  363698007 |Finding site|  = <<  39057004 |Pulmonary valve structure| \n" +
				"\t\t, 116676008 |Associated morphology|  = <<  415582006 |Stenosis| \n" +
				"\t}, \n" +
				"\t{  \n" +
				"\t\t  363698007 |Finding site|  = <<  53085002 |Right ventricular structure| \n" +
				"\t\t, 116676008 |Associated morphology|  = <<  56246009 |Hypertrophy| \n" +
				"\t}"
	)) {
			ECLToIML iml = new ECLToIML();
			Query query = iml.getQueryFromECL(ecl);
			String ecl2 = IMLToECL.getECLFromQuery(query, true);
			assertEquals(ecl.replaceAll(" ", ""), ecl2.replaceAll(" ", "").replaceAll("\n", ""));
		}

	}
    @Test
    void groupBasedTest() throws DataFormatException, JsonProcessingException {
        String ecl = "<  404684003 |Clinical finding| \n" +
            "\t: {  \n" +
            "\t\t  363698007 |Finding site|  = <<  39057004 |Pulmonary valve structure| \n" +
            "\t\t, 116676008 |Associated morphology|  = <<  415582006 |Stenosis| \n" +
            "\t}, \n" +
            "\t{  \n" +
            "\t\t  363698007 |Finding site|  = <<  53085002 |Right ventricular structure| \n" +
            "\t\t, 116676008 |Associated morphology|  = <<  56246009 |Hypertrophy| \n" +
            "\t}";
        ECLToIML iml = new ECLToIML();
        Query query = iml.getQueryFromECL(ecl);
        String ecl2 = IMLToECL.getECLFromQuery(query, true);
        assertEquals(ecl.replaceAll(" ", ""), ecl2.replaceAll(" ", "").replaceAll("\n", ""));

    }
}
