package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.map.MapObject;
import org.endeavourhealth.imapi.model.requests.TransformRequest;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.FHIR;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.MAP;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

class TransformServiceTest {

  private String testSources;
  private String testTargets;
  private String testMaps;


  //@Test
  void transform() throws Exception {
    String root = new File(System.getProperty("user.dir")).getParent();
    testSources = root + "\\TestTransforms\\TestSources";
    testTargets = root + "\\TestTransforms\\TestTargets";
    testMaps = root + "\\TestTransforms\\TestMaps";
    //Creates an example transform map and adds to ebntity cache
    TestMaps.patientDSTU2();
    ObjectMapper om = new ObjectMapper();
    //Adds map to the IM cache so it can be accessed by the service
    TTEntity mapEntity = EntityCache.getEntity(Namespace.MAP + "FHIR_2_PatientToIM").getEntity();
    MapObject map = mapEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(MapObject.class);
    writeObject(testMaps, "DSTUToIMPatient", map);
    System.out.println("Map written to" + testMaps + "\\" + mapEntity.getName());

    //Create transform request;
    TransformRequest request = new TransformRequest();
    request.setTransformMap(TTIriRef.iri(mapEntity.getIri()));
    request.setSourceFormat("JSON");
    request.setTargetFormat("JSON-LD");

    //Add patient resource to the request;
    JsonNode patient = om.readValue(getPatient(), JsonNode.class);
    request.addSource(FHIR.DSTU2 + "Patient", patient);
    writeObject(testSources, "DSTUPatient", patient);
    System.out.println("Source written to" + testSources + "\\" + "DSTUPatient");

    //Perform transform
    Set<Object> results = new TransformService().runTransform(request);

    //Displays result
    TTManager manager = new TTManager();
    TTDocument document = manager.createDocument();
    document.setEntities(results.stream().map(r -> (TTEntity) r).collect(Collectors.toList()));
    manager.saveDocument(new File(testTargets + "\\IMPatient.json"));
    System.out.println("Target written to " + testTargets + "\\IMPatient");
  }

  private String getPatient() {
    String patient = "{\n" +
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
      "\t\t\t \"value\": \"07456223456\"\n" +
      "\t\t},\n" +
      "\t\t{\n" +
      "\t\t\t \"system\": \"phone\",\n" +
      "\t\t\t \"use\": \"home\",\n" +
      "\t\t\t \"value\": \"01945 668768\"\n" +
      "\t\t}\n" +
      "\t]\n" +
      "}";
    return patient;
  }

  private void writeObject(String path, String fileName, Object object) throws JsonProcessingException, IOException {
    try (FileWriter wr = new FileWriter(path + "\\" + fileName + ".json")) {
      wr.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object));
    }
  }
}