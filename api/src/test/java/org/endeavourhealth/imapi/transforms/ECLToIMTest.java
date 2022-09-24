package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.iml.Query;

import java.util.zip.DataFormatException;

class ECLToIMTest {

	//@Test
	void convertConceptSet() throws DataFormatException, JsonProcessingException {
		//String ecl= "(* : <<10362601000001103 = <<39330711000001103)";
		String ecl= "<< 10601006 OR (<< 29857009 MINUS (<<102588006 OR (<<29857009:263502005 |Clinical course (attribute)| = 424124008 |Sudden onset AND/OR short duration (qualifier value)|)))";
		//String ecl="<< 105590001 |Substance (substance)| OR 138875005 |SNOMED CT Concept (SNOMED RT+CTV3)| OR << 260787004 |Physical object (physical object)| OR << 373873005 |Pharmaceutical / biologic product (product)| OR << 410607006 |Organism (organism)| OR << 78621006 |Physical force (physical force)|";
		//String ecl= "<<998681000000109 | Downs screening test (observable entity) |  OR \n" +
			//"<<364589006 | Birth weight (observable entity) |";
		//String ecl="<<763158003:<<127489000 = <<372665008,\n" +
			//"             <<411116001 = <<385268001";
		ECLToTT cnv= new ECLToTT();
		ECLToIML iml= new ECLToIML();
		Query query= iml.getClassExpression(ecl);
		System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(query));
		/*
		TTEntity entity = new TTEntity();
		entity.setContext(new TTManager().createDefaultContext());
		entity.set(IM.DEFINITION,cnv.getClassExpression(ecl));
		String ecl2=TTToECL.getExpressionConstraint(entity.get(IM.DEFINITION), false);
		System.out.println(ecl2);
		assertEquals(ecl,ecl2);

		 */
	}
}
