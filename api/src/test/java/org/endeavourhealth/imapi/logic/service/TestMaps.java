package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.iml.DataMap;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTVariable;
import org.endeavourhealth.imapi.model.iml.ListMode;
import org.endeavourhealth.imapi.model.iml.TargetUpdateMode;
import org.endeavourhealth.imapi.vocabulary.FHIR;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.MAP;

public class TestMaps {
	public static DataMap fhirDstu2() throws JsonProcessingException {

		TTEntity patientMapEntity= new TTEntity();
		patientMapEntity
			.setIri(MAP.NAMESPACE+"FHIR_2_PatientToIM")
			.setName("FHIR DSTU2 Patient to IM Patient transformMap")
			.setDescription("Maps a FHIR DSTU2 Patient resource to IM Patient entity");
		DataMap patientMap = new DataMap();
		patientMap
			.setIri(patientMapEntity.getIri())
			.setSource(new TTVariable()
				.setIri(FHIR.DSTU2+"Patient")
				.setVariable("fhirPatient"))
			.rule(r->r
				.setCreate(new TTVariable()
				.setIri(IM.NAMESPACE+"Patient")
			.setVariable("imPatient")));
		patientMap.rule(r-> r
				.setSourceProperty("id")
				.setSourceVariable("fhirId")
			.function(f->f
				.setIri(IM.NAMESPACE+ "Concatenate")
				.argument(a->a
					.setValueData("urn:uuid:"))
				.argument(a->a
					.setValueVariable("fhirId")))
			.setTargetProperty("@id")
				.setTargetVariable("imId"));
		patientMap.rule(r->r
					.setSourceProperty("identifier")
					.flatMap(m1->m1
						.rule(r1->r1
							.setSourceProperty("value")
							.setSourceVariable("fhirNhsNumber")
							.where(w->w
								.setPath("system")
								.value(v->v
									.setValue("http://fhir.nhs.net/Id/nhs-number")))
							.setTargetProperty("nhsNumber"))))
						.rule(r->r
							.setSourceProperty("name")
							.setSourceVariable("fhirName")
							.flatMap(m1->m1
								.rule(r1->r1
									.setSourceProperty("family")
									.setTargetProperty("familyName"))
								.rule(r1->r1
								.setSourceProperty("given")
								.setListMode(ListMode.FIRST)
								.setTargetProperty("callingName"))
							.rule(r1->r1
						  	.setSourceProperty("given")
								.setSourceVariable("fhirGiven")
								.setListMode(ListMode.ALL)
								.setTargetProperty("forenames")
								.function(f->f
										.setIri(IM.NAMESPACE+"StringJoin")
										.argument(a->a
											.setParameter("delimiter")
											.setValueData(" "))
										.argument(a->a
											.setParameter("elements")
											.setValueVariable("fhirGiven"))))));
		patientMap.rule(r->r
			.setSourceProperty("address")
			.setTargetProperty("homeAddress")
			.valueMap(m1->m1
				.rule(r1->r1
					.where(w->w
						.setPath("use")
						.value(v->v
							.setValue("home")))
					.setCreate(new TTVariable()
						.setIri(IM.NAMESPACE+"Address")))
				.rule(r1->r1
					.setSourceProperty("line")
					.setTargetProperty("addressLine"))
				.rule(r1->r1
					.setSourceProperty("postalCode")
					.setTargetProperty("postCode"))
					.rule(r1->r1
						.setSourceProperty("city")
						.setTargetProperty("addressLine")
						.setTargetUpdateMode(TargetUpdateMode.ADDTOLIST))));
		patientMap.rule(r->r
			.setSourceProperty("telecom")
				.flatMap(m->m
					.rule(r1->r1
						.setSourceProperty("value")
						.where(w->w
							.and(w1->w1
								.setPath("system")
								.value(v->v
										.setValue("phone")
									))
							.and(w1->w1
								.setPath("use")
								.value(v->v
									.setValue("mobile"))))
						.setTargetProperty("mobileTelephoneNumber"))));
		patientMap.rule(r->r
			.setSourceProperty("telecom")
			.flatMap(m->m
				.rule(r1->r1
					.setSourceProperty("value")
					.where(w->w
						.and(w1->w1
							.setPath("system")
							.value(v->v
								.setValue("phone")
							))
						.and(w1->w1
							.setPath("use")
							.value(v->v
								.setValue("home"))))
					.setTargetProperty("homeTelephoneNumber"))));
		patientMap.rule(r->r
			.setSourceProperty("birthDate")
			.setTargetProperty("dataOfBirth"));
		patientMapEntity.set(IM.DEFINITION, TTLiteral.literal(patientMap));
		EntityCache.addEntity(patientMapEntity);
			return patientMap;
	}
}
