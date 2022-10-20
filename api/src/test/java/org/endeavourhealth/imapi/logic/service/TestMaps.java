package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.iml.TransformMap;
import org.endeavourhealth.imapi.model.tripletree.TTAlias;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.FHIR;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.MAP;

public class TestMaps {
	public TransformMap fhirPatient(){

		TransformMap transformMap = new TransformMap()
			.setIri(MAP.NAMESPACE+"FHIR_2_PatientToIM")
			.setName("FHIR DSTU2 Patient to IM Patient transformMap")
			.setDescription("Maps a FHIR DSTU2 Patient resource to IM Patient entity")
			.addSource(TTAlias.iri(FHIR.NAMESPACE+"Patient_DSTU2").setAlias("fhirPatient"))
			.setSourceFormat(TTIriRef.iri(IM.NAMESPACE+"JSON"))
			.addTarget(TTAlias.iri(IM.NAMESPACE+"Patient").setAlias("imPatient"))
			.group(g->g
				.setName("mainGroup")
				.setSource(TTAlias.iri(FHIR.NAMESPACE+"Patient_DSTU2").setAlias("src"))
				.setTarget(TTAlias.iri(IM.NAMESPACE+"Patient").setAlias("tgt")));
			return transformMap;
	}
}
