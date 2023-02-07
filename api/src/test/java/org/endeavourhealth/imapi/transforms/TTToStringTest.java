package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TTToStringTest {

    public Map<String, String> getTestDefaultPredicates() {
        Map<String, String> defaults = new HashMap<>();
        defaults.put("http://endhealth.info/im#isA", "Is a");
        defaults.put("http://endhealth.info/im#roleGroup", "Match");
        defaults.put("http://www.w3.org/2002/07/owl#equivalentClass", "Is equivalent to");
        defaults.put("http://www.w3.org/2002/07/owl#intersectionOf", "Combination of");
        defaults.put("http://www.w3.org/2002/07/owl#someValuesFrom", "Match a value");
        defaults.put("http://www.w3.org/2002/07/owl#onProperty", "On property");
        return defaults;
    }

    public List<String> getTestBlockedIris() {
        return Arrays.asList("http://www.w3.org/2001/XMLSchema#string");
    }

    public TTIriRef getTestIri() {
        return iri("http://snomed.info/sct#370135005", "Pathological process (attribute)");
    }

    public TTArray getTestArray() {
        return new TTArray()
                .add(iri("http://snomed.info/sct#128084001", "Duane's syndrome, type 3 (disorder)"))
                .add(iri("http://snomed.info/sct#298382003", "Scoliosis deformity of spine (disorder)"))
                .add(iri("http://snomed.info/sct#82354003", "Multiple system malformation syndrome (disorder)"));
    }

    public TTNode getTestNode() {
        return new TTNode().set(iri(OWL.INTERSECTIONOF.getIri(), "intersectionOf"), getTestArray());
    }

    public TTBundle getTestBundle() {
        return new TTBundle()
                .addPredicate(iri(IM.IS_A.getIri(), "isA"))
                .addPredicate(iri(IM.ROLE_GROUP.getIri(), "roleGroup"))
                .addPredicate(iri(OWL.EQUIVALENTCLASS.getIri(), "equivalentClass"))
                .addPredicate(iri(OWL.ONPROPERTY.getIri(), "onProperty"))
                .addPredicate(iri(RDF.TYPE.getIri(), "type"))
                .addPredicate(iri(RDFS.SUBCLASSOF.getIri(), "subClassOf"))
                .addPredicate(iri(OWL.INTERSECTIONOF.getIri(), "intersectionOf"))
                .addPredicate(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"))
                .setEntity(new TTEntity()
                        .set(iri(RDFS.SUBCLASSOF.getIri(), "subClassOf"), new TTArray()
                                .add(new TTNode()
                                        .set(iri(OWL.INTERSECTIONOF.getIri(), "intersectionOf"), new TTArray()
                                                .add(iri("http://snomed.info/sct#128084001", "Duane's syndrome, type 3 (disorder)"))
                                                .add(iri("http://snomed.info/sct#298382003", "Scoliosis deformity of spine (disorder)"))
                                                .add(iri("http://snomed.info/sct#82354003", "Multiple system malformation syndrome (disorder)"))
                                                .add(iri("http://snomed.info/sct#85995004", "Autosomal recessive hereditary disorder (disorder)"))
                                                .add(new TTNode()
                                                        .set(OWL.SOMEVALUESFROM, new TTNode()
                                                                .set(OWL.INTERSECTIONOF, new TTArray()
                                                                        .add(new TTNode()
                                                                                .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#31739005", "Lateral abnormal curvature (morphologic abnormality)"))
                                                                                .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#116676008", "Associated morphology (attribute)"))
                                                                                .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                                                                        )
                                                                        .add(new TTNode()
                                                                                .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#289959001", "Musculoskeletal structure of spine (body structure)"))
                                                                                .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#363698007", "Finding site (attribute)"))
                                                                                .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                                                                        )
                                                                        .add(new TTNode()
                                                                                .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#308490002", "Pathological developmental process (qualifier value)"))
                                                                                .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#370135005", "Pathological process (attribute)"))
                                                                                .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                                                                        )
                                                                )
                                                        )
                                                        .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://endhealth.info/im#roleGroup", "role group"))
                                                        .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                                                )
                                                .add(new TTNode()
                                                        .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), new TTNode()
                                                                .set(iri(OWL.INTERSECTIONOF.getIri(), "intersectionOf"), new TTArray()
                                                                        .add(new TTNode()
                                                                                .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#49755003", "Morphologically abnormal structure (morphologic abnormality)"))
                                                                                .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#116676008", "Associated morphology (attribute)"))
                                                                                .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                                                                        )
                                                                        .add(new TTNode()
                                                                                .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#255399007", "Congenital (qualifier value)"))
                                                                                .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#246454002", "Occurrence (attribute)"))
                                                                                .set(RDF.TYPE, iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                                                                        )
                                                                        .add(new TTNode()
                                                                                .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#127954009", "Skeletal muscle structure (body structure)"))
                                                                                .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#363698007", "Finding site (attribute)"))
                                                                                .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                                                                        )
                                                                        .add(new TTNode()
                                                                                .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#308490002", "Pathological developmental process (qualifier value)"))
                                                                                .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#370135005", "Pathological process (attribute)"))
                                                                                .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                                                                        )
                                                                )
                                                        )
                                                        .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://endhealth.info/im#roleGroup", "role group"))
                                                        .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                                                )
                                        )
                                )
                        )
                );
    }

    @Test
    void bundleToString() throws Exception {
        String expected = "subClassOf : \n" +
                "Combination of : \n" +
                "  <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23128084001\">Duane's syndrome, type 3</a>\n" +
                "  <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23298382003\">Scoliosis deformity of spine</a>\n" +
                "  <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%2382354003\">Multiple system malformation syndrome</a>\n" +
                "  <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%2385995004\">Autosomal recessive hereditary disorder</a>\n" +
                "  ( Match a value : \n" +
                "    Combination of : \n" +
                "      ( Match a value : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%2331739005\">Lateral abnormal curvature</a>\n" +
                "        On property : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23116676008\">Associated morphology</a>\n" +
                "        type : <a href=\"" + "/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction\">Restriction</a> )\n" +
                "      ( Match a value : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23289959001\">Musculoskeletal structure of spine</a>\n" +
                "        On property : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23363698007\">Finding site</a>\n" +
                "        type : <a href=\"" + "/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction\">Restriction</a> )\n" +
                "      ( Match a value : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23308490002\">Pathological developmental process</a>\n" +
                "        On property : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23370135005\">Pathological process</a>\n" +
                "        type : <a href=\"" + "/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction\">Restriction</a> )\n" +
                "    On property : <a href=\"" + "/viewer/#/concept/http:%2F%2Fendhealth.info%2Fim%23roleGroup\">role group</a>\n" +
                "    type : <a href=\"" + "/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction\">Restriction</a> )\n" +
                "  ( Match a value : \n" +
                "    Combination of : \n" +
                "      ( Match a value : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%2349755003\">Morphologically abnormal structure</a>\n" +
                "        On property : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23116676008\">Associated morphology</a>\n" +
                "        type : <a href=\"" + "/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction\">Restriction</a> )\n" +
                "      ( Match a value : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23255399007\">Congenital</a>\n" +
                "        On property : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23246454002\">Occurrence</a>\n" +
                "        type : <a href=\"" + "/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction\">Restriction</a> )\n" +
                "      ( Match a value : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23127954009\">Skeletal muscle structure</a>\n" +
                "        On property : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23363698007\">Finding site</a>\n" +
                "        type : <a href=\"" + "/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction\">Restriction</a> )\n" +
                "      ( Match a value : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23308490002\">Pathological developmental process</a>\n" +
                "        On property : <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23370135005\">Pathological process</a>\n" +
                "        type : <a href=\"" + "/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction\">Restriction</a> )\n" +
                "    On property : <a href=\"" + "/viewer/#/concept/http:%2F%2Fendhealth.info%2Fim%23roleGroup\">role group</a>\n" +
                "    type : <a href=\"" + "/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction\">Restriction</a> )\n";
        assertEquals(expected, TTToString.getBundleAsString(getTestBundle(), getTestDefaultPredicates(), 0, true, getTestBlockedIris()));
    }

    @Test
    void ttNodeToString() throws Exception {
        String expected = "Combination of : \n" +
                "  <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23128084001\">Duane's syndrome, type 3</a>\n" +
                "  <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23298382003\">Scoliosis deformity of spine</a>\n" +
                "  <a href=\"" + "/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%2382354003\">Multiple system malformation syndrome</a>\n";
        assertEquals(expected, TTToString.ttNodeToString(getTestNode(), 0, true, getTestDefaultPredicates(), getTestBlockedIris()));
    }
}
