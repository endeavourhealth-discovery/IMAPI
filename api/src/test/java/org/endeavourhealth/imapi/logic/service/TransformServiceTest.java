package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.maps.GraphMap;
import org.endeavourhealth.imapi.model.maps.TransformRequest;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.FHIR;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TransformServiceTest {

	//@Test
	void transform() throws Exception {
		//Creates an example transform map
		GraphMap map= TestMaps.fhirDstu2();

		//Adds map to the IM cache so it can be accessed by the service
		TTEntity mapEntity= new TTEntity();
		mapEntity.setIri(map.getIri());
		mapEntity.set(IM.DEFINITION, TTLiteral.literal(map));
		EntityCache.addEntity(mapEntity);

		//Create transform request;
		TransformRequest request= new TransformRequest();
		request.setTransformMap(TTIriRef.iri(map.getIri()));
		request.setSourceFormat("JSON");
		request.setTargetFormat("JSON-LD");

		//Add patient resource to the request;
		request.addSource(FHIR.DSTU2+"Patient",new ObjectMapper().readValue(getPatient(), JsonNode.class));

		//Perform transform
		Set<Object> result= new TransformService().runTransform(request);

		//Displays result
		System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result));
	}

	private String getPatient(){
		String patient="{\n" +
			"\t \"active\": true,\n" +
			"\t \"address\": [\n" +
			"\t\t{\n" +
			"\t\t\t \"city\": \"STOCKPORT\",\n" +
			"\t\t\t \"district\": \"\",\n" +
			"\t\t\t \"line\": [\n" +
			"\t\t\t\t29,\n" +
			"\t\t\t\t\"\",\n" +
			"\t\t\t\t\"GREENWAY\"\n" +
			"\t\t\t],\n" +
			"\t\t\t \"postalCode\": \"SK6 4HH\",\n" +
			"\t\t\t \"text\": \"29,,GREENWAY,,STOCKPORT,SK6 4HH\",\n" +
			"\t\t\t \"use\": \"home\"\n" +
			"\t\t}\n" +
			"\t],\n" +
			"\t \"birthDate\": \"2011-09-07\",\n" +
			"\t \"careProvider\": [\n" +
			"\t\t{\n" +
			"\t\t\t \"reference\": \"Organization/328\"\n" +
			"\t\t},\n" +
			"\t\t{\n" +
			"\t\t\t \"reference\": \"Practitioner/1272\"\n" +
			"\t\t}\n" +
			"\t],\n" +
			"\t \"extension\": [\n" +
			"\t\t{\n" +
			"\t\t\t \"url\": \"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension\",\n" +
			"\t\t\t \"valueCodeableConcept\": {\n" +
			"\t\t\t\t \"coding\": [\n" +
			"\t\t\t\t\t{\n" +
			"\t\t\t\t\t\t \"code\": \"K\",\n" +
			"\t\t\t\t\t\t \"display\": \"Bangladeshi\",\n" +
			"\t\t\t\t\t\t \"system\": \"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension\"\n" +
			"\t\t\t\t\t}\n" +
			"\t\t\t\t]\n" +
			"\t\t\t}\n" +
			"\t\t}\n" +
			"\t],\n" +
			"\t \"gender\": \"F\",\n" +
			"\t \"id\": 1,\n" +
			"\t \"identifier\": [\n" +
			"\t\t{\n" +
			"\t\t\t \"system\": \"http://fhir.nhs.net/Id/nhs-number\",\n" +
			"\t\t\t \"use\": \"official\",\n" +
			"\t\t\t \"value\": 3127565459\n" +
			"\t\t},\n" +
			"\t\t{\n" +
			"\t\t\t \"system\": \"http://endeavourhealth.org/identifier/patient-number\",\n" +
			"\t\t\t \"use\": \"secondary\",\n" +
			"\t\t\t \"value\": 1\n" +
			"\t\t}\n" +
			"\t],\n" +
			"\t \"managingOrganization\": {\n" +
			"\t\t \"reference\": \"Organization/328\"\n" +
			"\t},\n" +
			"\t \"meta\": {\n" +
			"\t\t \"profile\": [\n" +
			"\t\t\t\"http://endeavourhealth.org/fhir/StructureDefinition/primarycare-patient\"\n" +
			"\t\t]\n" +
			"\t},\n" +
			"\t \"name\": [\n" +
			"\t\t{\n" +
			"\t\t\t \"family\": [\n" +
			"\t\t\t\t\"Albergaria\"\n" +
			"\t\t\t],\n" +
			"\t\t\t \"given\": [\n" +
			"\t\t\t\t\"Lindsey\"\n" +
			"\t\t\t],\n" +
			"\t\t\t \"text\": \"Albergaria, Lindsey (Ms)\",\n" +
			"\t\t\t \"use\": \"official\"\n" +
			"\t\t}\n" +
			"\t],\n" +
			"\t \"resourceType\": \"Patient\",\n" +
			"\t \"telecom\": [\n" +
			"\t\t{\n" +
			"\t\t\t \"system\": \"phone\",\n" +
			"\t\t\t \"use\": \"mobile\",\n" +
			"\t\t\t \"value\": \"0544-647-3274\"\n" +
			"\t\t},\n" +
			"\t\t{\n" +
			"\t\t\t \"system\": \"phone\",\n" +
			"\t\t\t \"use\": \"mobile\",\n" +
			"\t\t\t \"value\": \"0426-420-6546\"\n" +
			"\t\t}\n" +
			"\t]\n" +
			"}";
		return patient;
	}
}