package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.iml.TransformMap;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.FHIR;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.MAP;

public class TestMaps {
	public static TransformMap fhirPatient(){

		TransformMap transformMap = new TransformMap()
			.setIri(MAP.NAMESPACE+"FHIR_2_PatientToIM")
			.setName("FHIR DSTU2 Patient to IM Patient transformMap")
			.setDescription("Maps a FHIR DSTU2 Patient resource to IM Patient entity")
			.addSource(TTAlias.iri(FHIR.DSTU2+"Patient").setAlias("fhirPatient"))
			.setSourceFormat(TTIriRef.iri(IM.NAMESPACE+"JSON"))
			.addTarget(TTAlias.iri(IM.NAMESPACE+"Patient").setAlias("imPatient"))
			.group(g->g
				.setName("mainGroup")
				.setSource(TTAlias.iri(FHIR.DSTU2+"Patient").setAlias("src"))
				.setTarget(TTAlias.iri(IM.NAMESPACE+"Patient").setAlias("tgt"))
				.rule(r->r
					.source(s->s
						.setVariable("id")
						.setContext("$.id"))
					.target(t->t
						.setContext("iri")
						.setVariable("patientId")
						.function(f->f
							.setIri(IM.NAMESPACE+ "Concatenate")
							.argument(a->a
								.setValueData("urn:uuid"))
							.argument(a->a
								.setValueVariable("id")
								))))
				.rule(r->r
					.source(s->s
						.setVariable("srcNHS")
						.setContext("$.identifier[*].value")
						.check(chk->chk
							.setPath("$.identifier[*].system")
							.value(v-> v.setValue("https://fhir.nhs.uk/Id/nhs-number"))))
					.target(t->t
						.setContext("nhsNumber")
						.setVariable("nhsNumber"))));
			return transformMap;
	}
}
