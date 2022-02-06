package org.endeavourhealth.imapi.model.tripletree;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
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
            .setScheme(iri("http://snomed.info/sct#"))
            .setType(new TTArray().add(IM.CONCEPT))
            .set(IM.IS_A, new TTArray()
                .add(iri("http://snomed.info/sct#371186005", "Amputation of toe (procedure)"))
                .add(iri("http://snomed.info/sct#732214009", "Amputation of left lower limb"))
            )
            .set(IM.ROLE_GROUP, new TTArray()
                .add(new TTNode()
                    .set(iri("http://snomed.info/sct#260686004", "Method"), iri("http://snomed.info/sct#129309007", "Amputation - action"))
                    .set(iri("http://snomed.info/sct#405813007", "Procedure site - Direct"), iri("http://snomed.info/sct#732939008", "Part of toe of left foot"))
                )
            );
    }

    public static String getTestEntityJson() {
        return new StringJoiner(System.lineSeparator())
            .add("{")
            .add("  \"@id\" : \"http://endhealth.info/im#25451000252115\",")
            .add("  \"http://endhealth.info/im#isA\" : [ {")
            .add("    \"@id\" : \"http://snomed.info/sct#371186005\",")
            .add("    \"name\" : \"Amputation of toe (procedure)\"")
            .add("  }, {")
            .add("    \"@id\" : \"http://snomed.info/sct#732214009\",")
            .add("    \"name\" : \"Amputation of left lower limb\"")
            .add("  } ],")
            .add("  \"http://endhealth.info/im#code\" : \"787213005\",")
            .add("  \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"Partial amputation of toe of left foot (procedure)\",")
            .add("  \"http://endhealth.info/im#roleGroup\" : [ {")
            .add("    \"http://snomed.info/sct#260686004\" : [ {")
            .add("      \"@id\" : \"http://snomed.info/sct#129309007\",")
            .add("      \"name\" : \"Amputation - action\"")
            .add("    } ],")
            .add("    \"http://snomed.info/sct#405813007\" : [ {")
            .add("      \"@id\" : \"http://snomed.info/sct#732939008\",")
            .add("      \"name\" : \"Part of toe of left foot\"")
            .add("    } ]")
            .add("  } ],")
            .add("  \"http://endhealth.info/im#scheme\" : [ {")
            .add("    \"@id\" : \"http://snomed.info/sct#\"")
            .add("  } ],")
            .add("  \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : [ {")
            .add("    \"@id\" : \"http://endhealth.info/im#Concept\"")
            .add("  } ],")
            .add("  \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Partial amputation of toe of left foot\"")
            .add("}")
            .toString();
    }

    public static String getTestEntityJsonSimple() {
        return new StringJoiner(System.lineSeparator())
                .add("{")
                .add("  \"@id\" : \"http://endhealth.info/im#25451000252115\",")
                .add("  \"isA\" : [ {")
                .add("    \"@id\" : \"http://snomed.info/sct#371186005\",")
                .add("    \"name\" : \"Amputation of toe (procedure)\"")
                .add("  }, {")
                .add("    \"@id\" : \"http://snomed.info/sct#732214009\",")
                .add("    \"name\" : \"Amputation of left lower limb\"")
                .add("  } ],")
                .add("  \"code\" : \"787213005\",")
                .add("  \"comment\" : \"Partial amputation of toe of left foot (procedure)\",")
                .add("  \"roleGroup\" : [ {")
                .add("    \"260686004\" : [ {")
                .add("      \"@id\" : \"http://snomed.info/sct#129309007\",")
                .add("      \"name\" : \"Amputation - action\"")
                .add("    } ],")
                .add("    \"405813007\" : [ {")
                .add("      \"@id\" : \"http://snomed.info/sct#732939008\",")
                .add("      \"name\" : \"Part of toe of left foot\"")
                .add("    } ]")
                .add("  } ],")
                .add("  \"scheme\" : [ {")
                .add("    \"@id\" : \"http://snomed.info/sct#\"")
                .add("  } ],")
                .add("  \"type\" : [ {")
                .add("    \"@id\" : \"http://endhealth.info/im#Concept\"")
                .add("  } ],")
                .add("  \"label\" : \"Partial amputation of toe of left foot\"")
                .add("}")
                .toString();
    }

    public static String getTestEntityJsonPrefix() {
        return new StringJoiner(System.lineSeparator())
                .add("{")
                .add("  \"@id\" : \"im:25451000252115\",")
                .add("  \"im:isA\" : [ {")
                .add("    \"@id\" : \"sn:371186005\",")
                .add("    \"name\" : \"Amputation of toe (procedure)\"")
                .add("  }, {")
                .add("    \"@id\" : \"sn:732214009\",")
                .add("    \"name\" : \"Amputation of left lower limb\"")
                .add("  } ],")
                .add("  \"im:code\" : \"787213005\",")
                .add("  \"rdfs:comment\" : \"Partial amputation of toe of left foot (procedure)\",")
                .add("  \"im:roleGroup\" : [ {")
                .add("    \"sn:260686004\" : [ {")
                .add("      \"@id\" : \"sn:129309007\",")
                .add("      \"name\" : \"Amputation - action\"")
                .add("    } ],")
                .add("    \"sn:405813007\" : [ {")
                .add("      \"@id\" : \"sn:732939008\",")
                .add("      \"name\" : \"Part of toe of left foot\"")
                .add("    } ]")
                .add("  } ],")
                .add("  \"im:scheme\" : [ {")
                .add("    \"@id\" : \"sn:\"")
                .add("  } ],")
                .add("  \"rdf:type\" : [ {")
                .add("    \"@id\" : \"im:Concept\"")
                .add("  } ],")
                .add("  \"rdfs:label\" : \"Partial amputation of toe of left foot\"")
                .add("}")
                .toString();
    }

    public static String getTestEntityJsonPrefixWithContext() {
        return new StringJoiner(System.lineSeparator())
                .add("{")
                .add("  \"@context\" : {")
                .add("    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",")
                .add("    \"im\" : \"http://endhealth.info/im#\",")
                .add("    \"rdfs\" : \"http://www.w3.org/2000/01/rdf-schema#\",")
                .add("    \"sn\" : \"http://snomed.info/sct#\",")
                .add("    \"entities\" : {")
                .add("      \"@id\" : \"http://envhealth.info/im#entities\",")
                .add("      \"@container\" : \"@set\"")
                .add("    }")
                .add("  },")
                .add("  \"@id\" : \"im:25451000252115\",")
                .add("  \"im:isA\" : [ {")
                .add("    \"@id\" : \"sn:371186005\",")
                .add("    \"name\" : \"Amputation of toe (procedure)\"")
                .add("  }, {")
                .add("    \"@id\" : \"sn:732214009\",")
                .add("    \"name\" : \"Amputation of left lower limb\"")
                .add("  } ],")
                .add("  \"im:code\" : \"787213005\",")
                .add("  \"rdfs:comment\" : \"Partial amputation of toe of left foot (procedure)\",")
                .add("  \"im:roleGroup\" : [ {")
                .add("    \"sn:260686004\" : [ {")
                .add("      \"@id\" : \"sn:129309007\",")
                .add("      \"name\" : \"Amputation - action\"")
                .add("    } ],")
                .add("    \"sn:405813007\" : [ {")
                .add("      \"@id\" : \"sn:732939008\",")
                .add("      \"name\" : \"Part of toe of left foot\"")
                .add("    } ]")
                .add("  } ],")
                .add("  \"im:scheme\" : [ {")
                .add("    \"@id\" : \"sn:\"")
                .add("  } ],")
                .add("  \"rdf:type\" : [ {")
                .add("    \"@id\" : \"im:Concept\"")
                .add("  } ],")
                .add("  \"rdfs:label\" : \"Partial amputation of toe of left foot\"")
                .add("}")
                .toString();
    }

    public static String getTestEntityJsonSimplePropertiesAndPrefixWithContext() {
        return new StringJoiner(System.lineSeparator())
                .add("{")
                .add("  \"@context\" : {")
                .add("    \"rdf\" : \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\",")
                .add("    \"im\" : \"http://endhealth.info/im#\",")
                .add("    \"rdfs\" : \"http://www.w3.org/2000/01/rdf-schema#\",")
                .add("    \"sn\" : \"http://snomed.info/sct#\",")
                .add("    \"entities\" : {")
                .add("      \"@id\" : \"http://envhealth.info/im#entities\",")
                .add("      \"@container\" : \"@set\"")
                .add("    }")
                .add("  },")
                .add("  \"@id\" : \"im:25451000252115\",")
                .add("  \"isA\" : [ {")
                .add("    \"@id\" : \"sn:371186005\",")
                .add("    \"name\" : \"Amputation of toe (procedure)\"")
                .add("  }, {")
                .add("    \"@id\" : \"sn:732214009\",")
                .add("    \"name\" : \"Amputation of left lower limb\"")
                .add("  } ],")
                .add("  \"code\" : \"787213005\",")
                .add("  \"comment\" : \"Partial amputation of toe of left foot (procedure)\",")
                .add("  \"roleGroup\" : [ {")
                .add("    \"260686004\" : [ {")
                .add("      \"@id\" : \"sn:129309007\",")
                .add("      \"name\" : \"Amputation - action\"")
                .add("    } ],")
                .add("    \"405813007\" : [ {")
                .add("      \"@id\" : \"sn:732939008\",")
                .add("      \"name\" : \"Part of toe of left foot\"")
                .add("    } ]")
                .add("  } ],")
                .add("  \"scheme\" : [ {")
                .add("    \"@id\" : \"sn:\"")
                .add("  } ],")
                .add("  \"type\" : [ {")
                .add("    \"@id\" : \"im:Concept\"")
                .add("  } ],")
                .add("  \"label\" : \"Partial amputation of toe of left foot\"")
                .add("}")
                .toString();
    }

    public static void checkEntity(TTNode entity) {
        assertTrue(entity.has(RDFS.LABEL));
        assertTrue(entity.get(RDFS.LABEL).isLiteral());
        assertEquals("Partial amputation of toe of left foot", entity
            .getAsLiteral(RDFS.LABEL)
            .getValue()
        );

        // Type(s)
        assertEquals(1, entity.get(RDF.TYPE).size());
        assertTrue(entity.get(RDF.TYPE).get(0).isIriRef());
        assertEquals(IM.CONCEPT, entity.get(RDF.TYPE).get(0).asIriRef());

        // Is A(s)
        assertEquals(2, entity.get(IM.IS_A).size());
        assertEquals(iri("http://snomed.info/sct#371186005","Amputation of toe (procedure)"), entity.get(IM.IS_A).get(0).asIriRef());
        assertEquals(iri("http://snomed.info/sct#732214009","Amputation of left lower limb"), entity.get(IM.IS_A).get(1).asIriRef());

        // Role Group(s)
        assertEquals(1, entity.get(IM.ROLE_GROUP).size());
        assertTrue(entity.get(IM.ROLE_GROUP).get(0).isNode());

        TTNode roleGroup = entity.get(IM.ROLE_GROUP).get(0).asNode();

        assertTrue(roleGroup.has(iri("http://snomed.info/sct#260686004","Method")));
        assertTrue(roleGroup.get(iri("http://snomed.info/sct#260686004","Method")).isIriRef());
        assertEquals(iri("http://snomed.info/sct#129309007","Amputation - action"), roleGroup.get(iri("http://snomed.info/sct#260686004","Method")).asIriRef());

        assertTrue(roleGroup.has(iri("http://snomed.info/sct#405813007","Procedure site - Direct")));
        assertTrue(roleGroup.get(iri("http://snomed.info/sct#405813007","Procedure site - Direct")).isIriRef());
        assertEquals(iri("http://snomed.info/sct#732939008","Part of toe of left foot"), roleGroup.get(iri("http://snomed.info/sct#405813007","Procedure site - Direct")).asIriRef());
    }
}
