package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.iml.Query;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ECLToIMTest {

	//@Test
	void convertConceptSet() throws DataFormatException, JsonProcessingException {
		for (String ecl: List.of(
			"(*:\n" +
				" 246093002 |Component (attribute)| = 84698008 |Cholesterol (substance)| ,\n" +
				"  260686004 |Method (attribute)| = 129266000 |Measurement - action (qualifier value)| )\n" +
				"OR\n" +
				"(\n" +
				"365793008 | Finding of cholesterol level (finding)|)\n" +
				"OR\n" +
				"( 365794002 | Finding of serum cholesterol level (finding)|)"
	)) {
			ECLToIML iml = new ECLToIML();
			Query query = iml.getQueryFromECL(ecl);
			String ecl2 = IMLToECL.getECLFromQuery(query, true);
			assertEquals(ecl.replaceAll(" ", ""), ecl2.replaceAll(" ", "").replaceAll("\n", ""));
		}

	}
}
