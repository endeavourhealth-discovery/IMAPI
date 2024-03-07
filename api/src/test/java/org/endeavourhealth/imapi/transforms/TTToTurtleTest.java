package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

class TTToTurtleTest {

	@Test
	void transformEntity() {
		TTEntity entity= new TTEntity();
		TTContext context= new TTContext();
		entity.setContext(context);
		context.add(IM.NAMESPACE,"im");
		context.add(SNOMED.NAMESPACE,"sn");
		context.add(OWL.NAMESPACE,"owl");
		entity.setIri(IM.NAMESPACE+"VaccineSet");
		entity.set(iri(IM.DEFINITION), new TTArray().add(TTIriRef.iri(SNOMED.NAMESPACE+"39330711000001103")));
		TTNode inter=new TTNode();
		inter.set(iri(OWL.INTERSECTION_OF),new TTArray()
			.add(iri(SNOMED.NAMESPACE+"10363601000001109"))
			.add(new TTNode().set(TTIriRef.iri(
				SNOMED.NAMESPACE+"10362601000001103"),
				iri(SNOMED.NAMESPACE+"39330711000001103"))));
		entity.get(iri(IM.DEFINITION)).add(inter);
		TTToTurtle converter= new TTToTurtle();
		String turtle=converter.transformEntity(entity);
		System.out.println(turtle);
		assertNotNull(turtle);
	}
}
