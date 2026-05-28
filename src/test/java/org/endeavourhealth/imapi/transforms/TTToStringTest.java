package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.OWL;
import org.endeavourhealth.interfacemanager.model.RDF;
import org.endeavourhealth.interfacemanager.model.RDFS;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    return List.of("http://www.w3.org/2001/XMLSchema#string");
  }

  public TTArray getTestArray() {
    return new TTArray()
      .add(iri("http://snomed.info/sct#128084001", "Duane's syndrome, type 3 (disorder)"))
      .add(iri("http://snomed.info/sct#298382003", "Scoliosis deformity of spine (disorder)"))
      .add(iri("http://snomed.info/sct#82354003", "Multiple system malformation syndrome (disorder)"));
  }

  public TTNode getTestNode() {
    return new TTNode().set(EnumUtils.asIri(OWL.INTERSECTION_OF), getTestArray());
  }

  public TTBundle getTestBundle() {
    return new TTBundle()
      .addPredicate(EnumUtils.asIri(IM.IS_A))
      .addPredicate(EnumUtils.asIri(IM.ROLE_GROUP))
      .addPredicate(EnumUtils.asIri(OWL.EQUIVALENT_CLASS))
      .addPredicate(EnumUtils.asIri(OWL.ON_PROPERTY))
      .addPredicate(EnumUtils.asIri(RDF.TYPE))
      .addPredicate(EnumUtils.asIri(RDFS.SUBCLASS_OF))
      .addPredicate(EnumUtils.asIri(OWL.INTERSECTION_OF))
      .addPredicate(EnumUtils.asIri(OWL.SOME_VALUES_FROM))
      .setEntity(new TTEntity()
        .set(EnumUtils.asIri(RDFS.SUBCLASS_OF), new TTArray()
          .add(new TTNode()
            .set(EnumUtils.asIri(OWL.INTERSECTION_OF), new TTArray()
              .add(iri("http://snomed.info/sct#128084001", "Duane's syndrome, type 3 (disorder)"))
              .add(iri("http://snomed.info/sct#298382003", "Scoliosis deformity of spine (disorder)"))
              .add(iri("http://snomed.info/sct#82354003", "Multiple system malformation syndrome (disorder)"))
              .add(iri("http://snomed.info/sct#85995004", "Autosomal recessive hereditary disorder (disorder)"))
              .add(new TTNode()
                .set(EnumUtils.asIri(OWL.SOME_VALUES_FROM), new TTNode()
                  .set(EnumUtils.asIri(OWL.INTERSECTION_OF), new TTArray()
                    .add(new TTNode()
                      .set(EnumUtils.asIri(OWL.SOME_VALUES_FROM), iri("http://snomed.info/sct#31739005", "Lateral abnormal curvature (morphologic abnormality)"))
                      .set(EnumUtils.asIri(OWL.ON_PROPERTY), iri("http://snomed.info/sct#116676008", "Associated morphology (attribute)"))
                      .set(EnumUtils.asIri(RDF.TYPE), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                    )
                    .add(new TTNode()
                      .set(EnumUtils.asIri(OWL.SOME_VALUES_FROM), iri("http://snomed.info/sct#289959001", "Musculoskeletal structure of spine (body structure)"))
                      .set(EnumUtils.asIri(OWL.ON_PROPERTY), iri("http://snomed.info/sct#363698007", "Finding site (attribute)"))
                      .set(EnumUtils.asIri(RDF.TYPE), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                    )
                    .add(new TTNode()
                      .set(EnumUtils.asIri(OWL.SOME_VALUES_FROM), iri("http://snomed.info/sct#308490002", "Pathological developmental process (qualifier value)"))
                      .set(EnumUtils.asIri(OWL.ON_PROPERTY), iri("http://snomed.info/sct#370135005", "Pathological process (attribute)"))
                      .set(EnumUtils.asIri(RDF.TYPE), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                    )
                  )
                )
                .set(EnumUtils.asIri(OWL.ON_PROPERTY), iri("http://endhealth.info/im#roleGroup", "role group"))
                .set(EnumUtils.asIri(RDF.TYPE), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
              )
              .add(new TTNode()
                .set(EnumUtils.asIri(OWL.SOME_VALUES_FROM), new TTNode()
                  .set(EnumUtils.asIri(OWL.INTERSECTION_OF), new TTArray()
                    .add(new TTNode()
                      .set(EnumUtils.asIri(OWL.SOME_VALUES_FROM), iri("http://snomed.info/sct#49755003", "Morphologically abnormal structure (morphologic abnormality)"))
                      .set(EnumUtils.asIri(OWL.ON_PROPERTY), iri("http://snomed.info/sct#116676008", "Associated morphology (attribute)"))
                      .set(EnumUtils.asIri(RDF.TYPE), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                    )
                    .add(new TTNode()
                      .set(EnumUtils.asIri(OWL.SOME_VALUES_FROM), iri("http://snomed.info/sct#255399007", "Congenital (qualifier value)"))
                      .set(EnumUtils.asIri(OWL.ON_PROPERTY), iri("http://snomed.info/sct#246454002", "Occurrence (attribute)"))
                      .set(EnumUtils.asIri(RDF.TYPE), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                    )
                    .add(new TTNode()
                      .set(EnumUtils.asIri(OWL.SOME_VALUES_FROM), iri("http://snomed.info/sct#127954009", "Skeletal muscle structure (body structure)"))
                      .set(EnumUtils.asIri(OWL.ON_PROPERTY), iri("http://snomed.info/sct#363698007", "Finding site (attribute)"))
                      .set(EnumUtils.asIri(RDF.TYPE), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                    )
                    .add(new TTNode()
                      .set(EnumUtils.asIri(OWL.SOME_VALUES_FROM), iri("http://snomed.info/sct#308490002", "Pathological developmental process (qualifier value)"))
                      .set(EnumUtils.asIri(OWL.ON_PROPERTY), iri("http://snomed.info/sct#370135005", "Pathological process (attribute)"))
                      .set(EnumUtils.asIri(RDF.TYPE), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
                    )
                  )
                )
                .set(EnumUtils.asIri(OWL.ON_PROPERTY), iri("http://endhealth.info/im#roleGroup", "role group"))
                .set(EnumUtils.asIri(RDF.TYPE), iri("http://www.w3.org/2002/07/owl#Restriction", "Restriction"))
              )
            )
          )
        )
      );
  }

  @Test
  void bundleToString() {
    String expected = """
      Subclass Of :\s
      Combination of :\s
        <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23128084001">Duane's syndrome, type 3</a>
        <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23298382003">Scoliosis deformity of spine</a>
        <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%2382354003">Multiple system malformation syndrome</a>
        <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%2385995004">Autosomal recessive hereditary disorder</a>
        ( Match a value :\s
          Combination of :\s
            ( Match a value : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%2331739005">Lateral abnormal curvature</a>
              On property : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23116676008">Associated morphology</a>
              Type : <a href="/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction">Restriction</a> )
            ( Match a value : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23289959001">Musculoskeletal structure of spine</a>
              On property : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23363698007">Finding site</a>
              Type : <a href="/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction">Restriction</a> )
            ( Match a value : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23308490002">Pathological developmental process</a>
              On property : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23370135005">Pathological process</a>
              Type : <a href="/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction">Restriction</a> )
          On property : <a href="/viewer/#/concept/http:%2F%2Fendhealth.info%2Fim%23roleGroup">role group</a>
          Type : <a href="/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction">Restriction</a> )
        ( Match a value :\s
          Combination of :\s
            ( Match a value : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%2349755003">Morphologically abnormal structure</a>
              On property : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23116676008">Associated morphology</a>
              Type : <a href="/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction">Restriction</a> )
            ( Match a value : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23255399007">Congenital</a>
              On property : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23246454002">Occurrence</a>
              Type : <a href="/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction">Restriction</a> )
            ( Match a value : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23127954009">Skeletal muscle structure</a>
              On property : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23363698007">Finding site</a>
              Type : <a href="/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction">Restriction</a> )
            ( Match a value : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23308490002">Pathological developmental process</a>
              On property : <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23370135005">Pathological process</a>
              Type : <a href="/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction">Restriction</a> )
          On property : <a href="/viewer/#/concept/http:%2F%2Fendhealth.info%2Fim%23roleGroup">role group</a>
          Type : <a href="/viewer/#/concept/http:%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Restriction">Restriction</a> )
      """;
    assertEquals(expected, TTToString.getBundleAsString(getTestBundle(), getTestDefaultPredicates(), 0, true, getTestBlockedIris()));
  }

  @Test
  void ttNodeToString() {
    String expected = """
      Combination of :\s
        <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23128084001">Duane's syndrome, type 3</a>
        <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%23298382003">Scoliosis deformity of spine</a>
        <a href="/viewer/#/concept/http:%2F%2Fsnomed.info%2Fsct%2382354003">Multiple system malformation syndrome</a>
      """;
    assertEquals(expected, TTToString.ttNodeToString(getTestNode(), 0, true, getTestDefaultPredicates(), getTestBlockedIris()));
  }
}
