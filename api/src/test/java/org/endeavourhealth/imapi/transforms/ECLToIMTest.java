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
			"<123455:\n" +
				"<<762951001 = <33373737\n" +
				"MINUS <<10363901000001102"
	)) {
			ECLToIML iml = new ECLToIML();
			Query query = iml.getQueryFromECL(ecl);
			String ecl2 = IMLToECL.getECLFromQuery(query, true);
			assertEquals(ecl.replaceAll(" ", ""), ecl2.replaceAll(" ", "").replaceAll("\n", ""));
		}

	}
}
