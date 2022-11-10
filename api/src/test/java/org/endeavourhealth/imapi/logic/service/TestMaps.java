package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.maps.EntityMap;
import org.endeavourhealth.imapi.model.maps.SourceTargetMap;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTVariable;
import org.endeavourhealth.imapi.vocabulary.FHIR;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.MAP;

public class TestMaps {
	public static EntityMap fhirDstu2() throws JsonProcessingException {

		TTEntity patientMapEntity= new TTEntity();
		patientMapEntity
			.setIri(MAP.NAMESPACE+"FHIR_2_PatientToIM")
			.setName("FHIR DSTU2 Patient to IM Patient transformMap")
			.setDescription("Maps a FHIR DSTU2 Patient resource to IM Patient entity");
		EntityMap patientMap = new EntityMap();
		patientMap
			.setIri(patientMapEntity.getIri())
			.setSource(new TTVariable()
				.setIri(FHIR.DSTU2+"Patient")
				.setVariable("fhirPatient"))
			.addTarget(new TTVariable()
				.setIri(IM.NAMESPACE+"Patient")
			.setVariable("imPatient"));
		patientMap.propertyMap(pm->pm
			.setTargetEntity(new TTVariable())
			.sourcePath(sp-> sp
				.setPath("id")
				.setVariable("fhirId"))
			.function(f->f
				.setIri(IM.NAMESPACE+ "Concatenate")
				.argument(a->a
					.setValueData("urn:uuid"))
				.argument(a->a
					.setValueVariable("fhirId"))));

		patientMap.propertyMap(pm->pm
			.sourcePath(sp->sp
				.setPath("identifier value")
				.setVariable("fhirNhsNumber"))
				.where(chk->chk
							.setPath("identifier.system")
							.value(v-> v.setValue("https://fhir.nhs.uk/Id/nhs-number")))
					.setTargetPath("nhsNumber"))
			.propertyMap(pm->pm
				.sourcePath(sp->sp
						.setPath("name")
						.setVariable("fhirName"))
				.valueMap(te->te
					.propertyMap(pm1->pm1
						.sourcePath(sp1->sp1
							.setPath("family"))
						.setTargetPath("familyName"))
					.propertyMap(pm1->pm1
						.sourcePath(sp1->sp1
							.setPath("given")
									.setListMode("First"))
						.setTargetPath("callingName"))
					.propertyMap(pm1->pm1
						.sourcePath(sp1->sp1
							.setPath("given")
							.setListMode("All"))
						.setTargetPath("forenames")
						.function(f->f
										.setIri(IM.NAMESPACE+"StringJoin")
										.argument(a->a
											.setParameter("delimiter")
											.setValueData(" "))
										.argument(a->a
											.setParameter("elements")
											.setValueVariable("forenames"))))));
		patientMapEntity.set(IM.DEFINITION, TTLiteral.literal(patientMap));
		EntityCache.addEntity(patientMapEntity);
			return patientMap;
	}
}
