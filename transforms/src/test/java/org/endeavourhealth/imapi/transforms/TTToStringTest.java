package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TTToStringTest {
    public TTIriRef getTestIri() {
        return iri("http://snomed.info/sct#370135005", "Pathological process (attribute)");
    }

    public TTArray getTestArray() {
        return new TTArray()
                .add(iri("http://snomed.info/sct#128084001", "Duane's syndrome, type 3 (disorder)"))
                .add(iri("http://snomed.info/sct#298382003", "Scoliosis deformity of spine (disorder)"))
                .add(iri("http://snomed.info/sct#82354003","Multiple system malformation syndrome (disorder)"));
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
                                    .set(iri(OWL.INTERSECTIONOF.getIri(),"intersectionOf"), new TTArray()
                                            .add(iri("http://snomed.info/sct#128084001", "Duane's syndrome, type 3 (disorder)"))
                                            .add(iri("http://snomed.info/sct#298382003", "Scoliosis deformity of spine (disorder)"))
                                            .add(iri("http://snomed.info/sct#82354003","Multiple system malformation syndrome (disorder)"))
                                            .add(iri("http://snomed.info/sct#85995004","Autosomal recessive hereditary disorder (disorder)"))
                                            .add(new TTNode()
                                                    .set(OWL.SOMEVALUESFROM, new TTNode()
                                                            .set(OWL.INTERSECTIONOF, new TTArray()
                                                                    .add(new TTNode()
                                                                        .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#31739005","Lateral abnormal curvature (morphologic abnormality)"))
                                                                        .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#116676008","Associated morphology (attribute)"))
                                                                        .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction","Restriction"))
                                                                    )
                                                                    .add(new TTNode()
                                                                            .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#289959001","Musculoskeletal structure of spine (body structure)"))
                                                                            .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#363698007","Finding site (attribute)"))
                                                                            .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction","Restriction"))
                                                                    )
                                                                    .add(new TTNode()
                                                                            .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#308490002","Pathological developmental process (qualifier value)"))
                                                                            .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#370135005","Pathological process (attribute)"))
                                                                            .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction","Restriction"))
                                                                    )
                                                            )
                                                    )
                                                    .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://endhealth.info/im#roleGroup","role group"))
                                                    .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction","Restriction"))
                                            )
                                            .add(new TTNode()
                                                    .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), new TTNode()
                                                            .set(iri(OWL.INTERSECTIONOF.getIri(), "intersectionOf"), new TTArray()
                                                                    .add(new TTNode()
                                                                            .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#49755003","Morphologically abnormal structure (morphologic abnormality)"))
                                                                            .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#116676008","Associated morphology (attribute)"))
                                                                            .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction","Restriction"))
                                                                    )
                                                                    .add(new TTNode()
                                                                            .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#255399007","Congenital (qualifier value)"))
                                                                            .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#246454002","Occurrence (attribute)"))
                                                                            .set(RDF.TYPE, iri("http://www.w3.org/2002/07/owl#Restriction","Restriction"))
                                                                    )
                                                                    .add(new TTNode()
                                                                            .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#127954009","Skeletal muscle structure (body structure)"))
                                                                            .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#363698007","Finding site (attribute)"))
                                                                            .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction","Restriction"))
                                                                    )
                                                                    .add(new TTNode()
                                                                            .set(iri(OWL.SOMEVALUESFROM.getIri(), "someValuesFrom"), iri("http://snomed.info/sct#308490002","Pathological developmental process (qualifier value)"))
                                                                            .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://snomed.info/sct#370135005","Pathological process (attribute)"))
                                                                            .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction","Restriction"))
                                                                    )
                                                            )
                                                    )
                                                    .set(iri(OWL.ONPROPERTY.getIri(), "onProperty"), iri("http://endhealth.info/im#roleGroup","role group"))
                                                    .set(iri(RDF.TYPE.getIri(), "type"), iri("http://www.w3.org/2002/07/owl#Restriction","Restriction"))
                                            )
                                    )
                            )
                    )
            );
    }

    @Test
    public void bundleToString() throws Exception {
        String expected = "subClassOf:\n" +
                "  Combination of:\n" +
                "    Duane's syndrome, type 3\n" +
                "    Scoliosis deformity of spine\n" +
                "    Multiple system malformation syndrome\n" +
                "    Autosomal recessive hereditary disorder\n" +
                "    ( With a value:\n" +
                "      Combination of:\n" +
                "        ( With a value : Lateral abnormal curvature\n" +
                "          On property : Associated morphology\n" +
                "          type : Restriction)\n" +
                "        ( With a value : Musculoskeletal structure of spine\n" +
                "          On property : Finding site\n" +
                "          type : Restriction)\n" +
                "        ( With a value : Pathological developmental process\n" +
                "          On property : Pathological process\n" +
                "          type : Restriction)\n" +
                "      On property : role group\n" +
                "      type : Restriction)\n" +
                "    ( With a value:\n" +
                "      Combination of:\n" +
                "        ( With a value : Morphologically abnormal structure\n" +
                "          On property : Associated morphology\n" +
                "          type : Restriction)\n" +
                "        ( With a value : Congenital\n" +
                "          On property : Occurrence\n" +
                "          type : Restriction)\n" +
                "        ( With a value : Skeletal muscle structure\n" +
                "          On property : Finding site\n" +
                "          type : Restriction)\n" +
                "        ( With a value : Pathological developmental process\n" +
                "          On property : Pathological process\n" +
                "          type : Restriction)\n" +
                "      On property : role group\n" +
                "      type : Restriction)\n";
        assertEquals(expected, TTToString.getBundleAsString(getTestBundle()));
    }

    @Test
    public void ttNodeToString() throws Exception {
        String expected = "Combination of:\n" +
                "  Duane's syndrome, type 3\n" +
                "  Scoliosis deformity of spine\n" +
                "  Multiple system malformation syndrome\n";
        assertEquals(expected, TTToString.ttNodeToString(getTestNode(), "array", 0, new HashMap<>()));
    }
}
