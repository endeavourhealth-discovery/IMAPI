package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class ECLToTTTest {

	@Test
	void convertConceptSet() throws DataFormatException {
		String ecl= "<< 39330711000001103 OR (<< 10363601000001109 : << 10362601000001103 = << 39330711000001103)";
		//String ecl= "<< 1324671000000103 OR (<< 1324681000000101 MINUS (<< 1324691000000104 OR << 223123123203))";
		ECLToTT cnv= new ECLToTT();
		TTEntity entity = new TTEntity();
		entity.setContext(new TTManager().createDefaultContext());
		entity.set(IM.DEFINITION,cnv.getClassExpression(ecl));
		TTToECL rev= new TTToECL();
		String ecl2=TTToECL.getExpressionConstraint(entity.get(IM.DEFINITION), false);
		System.out.println(ecl2);
		assertEquals(ecl,ecl2);
	}
}
