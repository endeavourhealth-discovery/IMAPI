package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.extensions.TTIriRefExtensionsKt;
import org.endeavourhealth.imapi.model.map.MapObject;
import org.endeavourhealth.imapi.model.tripletree.TTDocumentJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.interfacemanager.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

class TransformServiceTest {

  //@Test
  void transform() throws Exception {
    String root = new File(System.getProperty("user.dir")).getParent();
    String testSources = root + "\\TestTransforms\\TestSources";
    String testTargets = root + "\\TestTransforms\\TestTargets";
    String testMaps = root + "\\TestTransforms\\TestMaps";
    //Creates an example transform map and adds to ebntity cache
    TestMaps.patientDSTU2();
    ObjectMapper om = new ObjectMapper();
    //Adds map to the IM cache so it can be accessed by the service
    TTEntityJava mapEntity = EntityCache.getEntity(NamespaceVocab.MAP + "FHIR_2_PatientToIM").getEntity();
    MapObject map = mapEntity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION)).asLiteral().objectValue(MapObject.class);
    writeObject(testMaps, "DSTUToIMPatient", map);
    System.out.println("Map written to" + testMaps + "\\" + mapEntity.getName());

    //Create transform request;
    TransformRequest request = new TransformRequest();
    request.setTransformMap(TTIriRefExtensionsKt.iri(new TTIriRef(), mapEntity.getIri()));
    request.setSourceFormat("JSON");
    request.setTargetFormat("JSON-LD");

    //Add patient resource to the request;
    JsonNode patient = om.readValue(getPatient(), JsonNode.class);
    request.addSource(FhirVocab.DSTU2 + "Patient", patient);
    writeObject(testSources, "DSTUPatient", patient);
    System.out.println("Source written to" + testSources + "\\" + "DSTUPatient");

    //Perform transform
    Set<Object> results = new TransformService().runTransform(request);

    //Displays result
    try (TTManager manager = new TTManager()) {
      TTDocumentJava document = manager.createDocument();
      document.setEntities(results.stream().map(r -> (TTEntityJava) r).collect(Collectors.toList()));
      manager.saveDocument(new File(testTargets + "\\IMPatient.json"));
    }
    System.out.println("Target written to " + testTargets + "\\IMPatient");
  }

  private String getPatient() {
    return """
      {
         "active": true,
         "address": [
          {
             "city": "STOCKPORT",
             "district": "",
             "line": [
              29,
              "",
              "GREENWAY"
            ],
             "postalCode": "SK6 4HH",
             "text": "29,,GREENWAY,,STOCKPORT,SK6 4HH",
             "use": "home"
          }
        ],
         "birthDate": "2011-09-07",
         "careProvider": [
          {
             "reference": "Organization/328"
          },
          {
             "reference": "Practitioner/1272"
          }
        ],
         "extension": [
          {
             "url": "http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension",
             "valueCodeableConcept": {
               "coding": [
                {
                   "code": "K",
                   "display": "Bangladeshi",
                   "system": "http://endeavourhealth.org/fhir/StructureDefinition/primarycare-ethnic-category-extension"
                }
              ]
            }
          }
        ],
         "gender": "F",
         "id": 1,
         "identifier": [
          {
             "system": "http://fhir.nhs.net/Id/nhs-number",
             "use": "official",
             "value": 3127565459
          },
          {
             "system": "http://endeavourhealth.org/identifier/patient-number",
             "use": "secondary",
             "value": 1
          }
        ],
         "managingOrganization": {
           "reference": "Organization/328"
        },
         "meta": {
           "profile": [
            "http://endeavourhealth.org/fhir/StructureDefinition/primarycare-patient"
          ]
        },
         "name": [
          {
             "family": [
              "Albergaria"
            ],
             "given": [
              "Lindsey"
            ],
             "text": "Albergaria, Lindsey (Ms)",
             "use": "official"
          }
        ],
         "resourceType": "Patient",
         "telecom": [
          {
             "system": "phone",
             "use": "mobile",
             "value": "07456223456"
          },
          {
             "system": "phone",
             "use": "home",
             "value": "01945 668768"
          }
        ]
      }""";
  }

  private void writeObject(String path, String fileName, Object object) throws IOException {
    try (FileWriter wr = new FileWriter(path + "\\" + fileName + ".json")) {
      wr.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object));
    }
  }
}