package org.endeavourhealth.imapi.model.tripletree;

import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestHelper {
  public static TTEntity getTestEntity() {
    return new TTEntity("http://endhealth.info/im#25451000252115")
      .addPrefix("http://endhealth.info/im#", "im")
      .addPrefix("http://snomed.info/sct#", "sn")
      .addPrefix("http://www.w3.org/2000/01/rdf-schema#", "rdfs")
      .addPrefix("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf")
      .setName("Partial amputation of toe of left foot")
      .setDescription("Partial amputation of toe of left foot (procedure)")
      .setCode("787213005")
      .setScheme(new TTIriRefExtended("http://snomed.info/sct#"))
      .setType(new TTArray().add(new TTIriRefExtended(ImVocab. CONCEPT)))
      .set(new TTIriRefExtended(ImVocab. IS_A),new TTArray()
      .add(new TTIriRefExtended("http://snomed.info/sct#371186005", "Amputation of toe (procedure)"))
      .add(new TTIriRefExtended("http://snomed.info/sct#732214009", "Amputation of left lower limb"))
      )
      .set(new TTIriRefExtended(ImVocab. ROLE_GROUP),new TTArray()
      .add(new TTNode()
        .set(new TTIriRefExtended("http://snomed.info/sct#260686004", "Method"), new TTIriRefExtended("http://snomed.info/sct#129309007", "Amputation - action"))
        .set(new TTIriRefExtended("http://snomed.info/sct#405813007", "Procedure site - Direct"), new TTIriRefExtended("http://snomed.info/sct#732939008", "Part of toe of left foot"))
      )
      );
  }

  public static String getTestEntityJson() {
    return new StringJoiner(System.lineSeparator())
      .add("{")
      .add("  \"iri\" : \"http://endhealth.info/im#25451000252115\",")
      .add("  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {")
      .add("    \"iri\" : \"http://endhealth.info/im#Concept\",")
      .add("    \"name\" : \"Concept\"")
      .add("  } ],")
      .add("  \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Partial amputation of toe of left foot\",")
      .add("  \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"Partial amputation of toe of left foot (procedure)\",")
      .add("  \"http://endhealth.info/im#isA\" : [ {")
      .add("    \"iri\" : \"http://snomed.info/sct#371186005\",")
      .add("    \"name\" : \"Amputation of toe (procedure)\"")
      .add("  }, {")
      .add("    \"iri\" : \"http://snomed.info/sct#732214009\",")
      .add("    \"name\" : \"Amputation of left lower limb\"")
      .add("  } ],")
      .add("  \"http://endhealth.info/im#code\" : \"787213005\",")
      .add("  \"http://endhealth.info/im#roleGroup\" : [ {")
      .add("    \"http://snomed.info/sct#260686004\" : [ {")
      .add("      \"iri\" : \"http://snomed.info/sct#129309007\",")
      .add("      \"name\" : \"Amputation - action\"")
      .add("    } ],")
      .add("    \"http://snomed.info/sct#405813007\" : [ {")
      .add("      \"iri\" : \"http://snomed.info/sct#732939008\",")
      .add("      \"name\" : \"Part of toe of left foot\"")
      .add("    } ]")
      .add("  } ],")
      .add("  \"http://endhealth.info/im#scheme\" : [ {")
      .add("    \"iri\" : \"http://snomed.info/sct#\"")
      .add("  } ]")
      .add("}")
      .toString();
  }

  public static String getTestEntityJsonSimple() {
    return new StringJoiner(System.lineSeparator())
      .add("{")
      .add("  \"iri\" : \"http://endhealth.info/im#25451000252115\",")
      .add("  \"type\" : [ {")
      .add("    \"iri\" : \"http://endhealth.info/im#Concept\",")
      .add("    \"name\" : \"Concept\"")
      .add("  } ],")
      .add("  \"label\" : \"Partial amputation of toe of left foot\",")
      .add("  \"comment\" : \"Partial amputation of toe of left foot (procedure)\",")
      .add("  \"isA\" : [ {")
      .add("    \"iri\" : \"http://snomed.info/sct#371186005\",")
      .add("    \"name\" : \"Amputation of toe (procedure)\"")
      .add("  }, {")
      .add("    \"iri\" : \"http://snomed.info/sct#732214009\",")
      .add("    \"name\" : \"Amputation of left lower limb\"")
      .add("  } ],")
      .add("  \"code\" : \"787213005\",")
      .add("  \"roleGroup\" : [ {")
      .add("    \"260686004\" : [ {")
      .add("      \"iri\" : \"http://snomed.info/sct#129309007\",")
      .add("      \"name\" : \"Amputation - action\"")
      .add("    } ],")
      .add("    \"405813007\" : [ {")
      .add("      \"iri\" : \"http://snomed.info/sct#732939008\",")
      .add("      \"name\" : \"Part of toe of left foot\"")
      .add("    } ]")
      .add("  } ],")
      .add("  \"scheme\" : [ {")
      .add("    \"iri\" : \"http://snomed.info/sct#\"")
      .add("  } ]")
      .add("}")
      .toString();
  }

  public static String getTestEntityJsonPrefixWithContext() {
    return new StringJoiner(System.lineSeparator())
      .add("{")
      .add("  \"context\" : {")
      .add("    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",")
      .add("    \"im\" : \"http://endhealth.info/im#\",")
      .add("    \"rdfs\" : \"http://www.w3.org/2000/01/rdf-schema#\",")
      .add("    \"sn\" : \"http://snomed.info/sct#\",")
      .add("    \"entities\" : {")
      .add("      \"iri\" : \"http://envhealth.info/im#entities\",")
      .add("      \"container\" : \"set\"")
      .add("    }")
      .add("  },")
      .add("  \"iri\" : \"im:25451000252115\",")
      .add("  \"rdf:type\" : [ {")
      .add("    \"iri\" : \"im:Concept\",")
      .add("    \"name\" : \"Concept\"")
      .add("  } ],")
      .add("  \"rdfs:label\" : \"Partial amputation of toe of left foot\",")
      .add("  \"rdfs:comment\" : \"Partial amputation of toe of left foot (procedure)\",")
      .add("  \"im:isA\" : [ {")
      .add("    \"iri\" : \"sn:371186005\",")
      .add("    \"name\" : \"Amputation of toe (procedure)\"")
      .add("  }, {")
      .add("    \"iri\" : \"sn:732214009\",")
      .add("    \"name\" : \"Amputation of left lower limb\"")
      .add("  } ],")
      .add("  \"im:code\" : \"787213005\",")
      .add("  \"im:roleGroup\" : [ {")
      .add("    \"sn:260686004\" : [ {")
      .add("      \"iri\" : \"sn:129309007\",")
      .add("      \"name\" : \"Amputation - action\"")
      .add("    } ],")
      .add("    \"sn:405813007\" : [ {")
      .add("      \"iri\" : \"sn:732939008\",")
      .add("      \"name\" : \"Part of toe of left foot\"")
      .add("    } ]")
      .add("  } ],")
      .add("  \"im:scheme\" : [ {")
      .add("    \"iri\" : \"sn:\"")
      .add("  } ]")
      .add("}")
      .toString();
  }

  public static String getTestEntityJsonSimplePropertiesAndPrefixWithContext() {
    return new StringJoiner(System.lineSeparator())
      .add("{")
      .add("  \"context\" : {")
      .add("    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",")
      .add("    \"im\" : \"http://endhealth.info/im#\",")
      .add("    \"rdfs\" : \"http://www.w3.org/2000/01/rdf-schema#\",")
      .add("    \"sn\" : \"http://snomed.info/sct#\",")
      .add("    \"entities\" : {")
      .add("      \"iri\" : \"http://envhealth.info/im#entities\",")
      .add("      \"container\" : \"set\"")
      .add("    }")
      .add("  },")
      .add("  \"iri\" : \"im:25451000252115\",")
      .add("  \"type\" : [ {")
      .add("    \"iri\" : \"im:Concept\",")
      .add("    \"name\" : \"Concept\"")
      .add("  } ],")
      .add("  \"label\" : \"Partial amputation of toe of left foot\",")
      .add("  \"comment\" : \"Partial amputation of toe of left foot (procedure)\",")
      .add("  \"isA\" : [ {")
      .add("    \"iri\" : \"sn:371186005\",")
      .add("    \"name\" : \"Amputation of toe (procedure)\"")
      .add("  }, {")
      .add("    \"iri\" : \"sn:732214009\",")
      .add("    \"name\" : \"Amputation of left lower limb\"")
      .add("  } ],")
      .add("  \"code\" : \"787213005\",")
      .add("  \"roleGroup\" : [ {")
      .add("    \"260686004\" : [ {")
      .add("      \"iri\" : \"sn:129309007\",")
      .add("      \"name\" : \"Amputation - action\"")
      .add("    } ],")
      .add("    \"405813007\" : [ {")
      .add("      \"iri\" : \"sn:732939008\",")
      .add("      \"name\" : \"Part of toe of left foot\"")
      .add("    } ]")
      .add("  } ],")
      .add("  \"scheme\" : [ {")
      .add("    \"iri\" : \"sn:\"")
      .add("  } ]")
      .add("}")
      .toString();
  }

  public static void checkEntity(TTNode entity) {
    assertTrue(entity.has(new TTIriRefExtended(RdfsVocab.LABEL)));
    assertTrue(entity.get(new TTIriRefExtended(RdfsVocab.LABEL)).isLiteral());
    assertEquals("Partial amputation of toe of left foot", entity
      .getAsLiteral(new TTIriRefExtended(RdfsVocab.LABEL))
      .getValue()
    );

    // Type(s)
    assertEquals(1, entity.get(new TTIriRefExtended(RdfVocab.TYPE)).size());
    assertTrue(entity.get(new TTIriRefExtended(RdfVocab.TYPE)).get(0).isIriRef());
    assertEquals(new TTIriRefExtended(ImVocab. CONCEPT),entity.get(new TTIriRefExtended(RdfVocab.TYPE)).get(0).asIriRef());

    // Is A(s)
    assertEquals(2, entity.get(new TTIriRefExtended(ImVocab. IS_A)).size());
    assertEquals(new TTIriRefExtended("http://snomed.info/sct#371186005", "Amputation of toe (procedure)"), entity.get(new TTIriRefExtended(ImVocab.
    IS_A)).get(0).asIriRef());
    assertEquals(new TTIriRefExtended("http://snomed.info/sct#732214009", "Amputation of left lower limb"), entity.get(new TTIriRefExtended(ImVocab.
    IS_A)).get(1).asIriRef());

    // Role Group(s)
    assertEquals(1, entity.get(new TTIriRefExtended(ImVocab. ROLE_GROUP)).size());
    assertTrue(entity.get(new TTIriRefExtended(ImVocab. ROLE_GROUP)).get(0).isNode());

    TTNode roleGroup = entity.get(new TTIriRefExtended(ImVocab. ROLE_GROUP)).get(0).asNode();

    assertTrue(roleGroup.has(new TTIriRefExtended("http://snomed.info/sct#260686004", "Method")));
    assertTrue(roleGroup.get(new TTIriRefExtended("http://snomed.info/sct#260686004", "Method")).isIriRef());
    assertEquals(new TTIriRefExtended("http://snomed.info/sct#129309007", "Amputation - action"), roleGroup.get(new TTIriRefExtended("http://snomed.info/sct#260686004", "Method")).asIriRef());

    assertTrue(roleGroup.has(new TTIriRefExtended("http://snomed.info/sct#405813007", "Procedure site - Direct")));
    assertTrue(roleGroup.get(new TTIriRefExtended("http://snomed.info/sct#405813007", "Procedure site - Direct")).isIriRef());
    assertEquals(new TTIriRefExtended("http://snomed.info/sct#732939008", "Part of toe of left foot"), roleGroup.get(new TTIriRefExtended("http://snomed.info/sct#405813007", "Procedure site - Direct")).asIriRef());
  }
}
