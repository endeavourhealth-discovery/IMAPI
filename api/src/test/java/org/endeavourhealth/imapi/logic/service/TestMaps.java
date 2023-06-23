package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.map.MapObject;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.iml.ListMode;
import org.endeavourhealth.imapi.model.iml.TargetUpdateMode;
import org.endeavourhealth.imapi.vocabulary.FHIR;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.MAP;

public class TestMaps {



	public static MapObject patientDSTU2() throws JsonProcessingException {
	TTEntity patientMapEntity= new TTEntity();
		patientMapEntity
			.setIri(MAP.NAMESPACE+"FHIR_2_PatientToIM")
			.setName("FHIR DSTU2 Patient to IM Patient transformMap")
			.setDescription("Maps a FHIR DSTU2 Patient resource to IM Patient entity");
		MapObject patientMap = new MapObject();
		patientMap
			.setIri(patientMapEntity.getIri())
			.setSourceType(FHIR.DSTU2+"Patient")
			.setTargetType(IM.NAMESPACE+"Patient")
			.propertyMap(r->r
				.setSource("id")
				.setSourceVariable("fhirId")
				.setTarget("iri")
				.function(f->f
    				.setIri(IM.NAMESPACE+ "Concatenate")
		    		.argument(a->a
							.setValueData("urn:uuid:"))
				    .argument(a->a
							.setValueVariable("fhirId"))))
			.propertyMap(m->m
					.setSource("identifier")
						.propertyMap(m1->m1
							.setSource("value")
							.where(w->w
								.setIri(IM.NAMESPACE+"system")
									.setValue("http://fhir.nhs.net/Id/nhs-number")))
							.setTarget("nhsNumber"))
			.propertyMap(m->m
				.setSource("name")
				.propertyMap(m1->m1
					.setSource("family")
					.setTarget("familyName"))
				.propertyMap(m1->m1
					.setSource("given")
					.setListMode(ListMode.FIRST)
					.setTarget("callingName"))
				.propertyMap(m1->m1
					.setSource("given")
					.setSourceVariable("fhirGiven")
					.setListMode(ListMode.ALL)
					.setTarget("forenames")
					.function(f->f
							.setIri(IM.NAMESPACE+"StringJoin")
							.argument(a->a
								.setParameter("delimiter")
								.setValueData(" "))
							.argument(a->a
								.setParameter("elements")
								.setValueVariable("fhirGiven")))))
			.propertyMap(m->m
				.setSource("address")
				.setTarget("homeAddress")
				.objectMap(m1->m1
						.where(w->w
							.setIri(IM.NAMESPACE+"use")
								.setValue("home"))))
					.setTargetType(IM.NAMESPACE+"Address")
					.propertyMap(m2->m2
						.setSource("line")
						.setTarget("addressLine"))
					.propertyMap(m2->m2
						.setSource("postalCode")
						.setTarget("postCode"))
					.propertyMap(m2->m2
						.setSource("city")
						.setTarget("addressLine")
						.setTargetUpdateMode(TargetUpdateMode.ADDTOLIST))
			.propertyMap(m->m
				.setSource("telecom")
				.propertyMap(m1->m1
						.setSource("value")
						.where(w->w
							.setBoolMatch(Bool.and)
							.where(w1->w1
								.setIri(IM.NAMESPACE+"system")
										.setValue("phone"))
							.where(w1->w1
								.setIri(IM.NAMESPACE+"use")
									.setValue("mobile")))
						.setTarget("mobileTelephoneNumber")))
			.propertyMap(m->m
				.setSource("telecom")
				.propertyMap(m1->m1
					.setSource("value"))
					.where(w->w
						.setBoolMatch(Bool.and)
						.where(w1->w1
							.setIri(IM.NAMESPACE+"system")
								.setValue("phone")
							)
						.where(w1->w1
							.setIri(IM.NAMESPACE+"use")
								.setValue("home")))
					.setTarget("homeTelephoneNumber"))
			.propertyMap(m->m
				.setSource("birthDate")
				.setTarget("dateOfBirth"))
			.propertyMap(m->m
				.setSource("managingOrganization")
				.propertyMap(m1->m1
					.setSource("reference")
					.setTarget("provider")))
			.propertyMap(m->m
				.setTarget("administrativeGender")
				.objectMap(m1->m1
					.setTargetType(IM.NAMESPACE+"CodeableConcept")
					.propertyMap(m2->m2
						.setSource("gender")
						.setTarget("originalCode"))
					.propertyMap(m2->m2
						.setTarget("originalScheme")
						.setValueData("http://hl7.org/fhir/administrative-gender"))));
		patientMapEntity.set(IM.DEFINITION, TTLiteral.literal(patientMap));
		EntityCache.addEntity(patientMapEntity);
			return patientMap;
	}
}
