package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TTCompareTest {

	@Test
	void testEquals() {
		TTNode from= new TTNode();
		TTNode to= new TTNode();
		from.set(RDFS.LABEL, TTLiteral.literal("from"));
		to.set(RDFS.LABEL, TTLiteral.literal("from"));
		TTNode sub1= new TTNode();
		sub1.set(RDFS.LABEL,TTLiteral.literal("sub1"));
		sub1.set(IM.PARAMETER,new TTNode());
		from.set(IM.CODE,sub1);
		TTNode sub2= new TTNode();
		sub2.set(RDFS.LABEL,TTLiteral.literal("sub1"));
		sub2.set(IM.PARAMETER,new TTNode());
		to.set(IM.CODE,sub2);

		System.out.println(TTCompare.equals(from,to));
		assertTrue(TTCompare.equals(from,to));

	}
}
