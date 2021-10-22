package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.HashMap;

class TTToStringTest {
    @org.junit.jupiter.api.Test
    void nodeToString() {
        TTBundle bundle = new TTBundle();

        bundle.addPredicate(IM.IS_A);
        bundle.addPredicate(IM.ROLE_GROUP);
        bundle.addPredicate(OWL.EQUIVALENTCLASS);
        bundle.addPredicate(OWL.ONPROPERTY);
        bundle.addPredicate(OWL.SOMEVALUESFROM);
        bundle.addPredicate(RDF.TYPE);
        bundle.addPredicate(RDFS.SUBCLASSOF);

        TTEntity entity = new TTEntity();

        TTNode pathological = new TTNode();
        pathological.addObject(RDF.TYPE, OWL.RESTRICTION);
        pathological.addObject(OWL.ONPROPERTY, TTIriRef.iri("http://snomed.info/sct#370135005", "Pathological process (attribute)"));
        pathological.addObject(OWL.SOMEVALUESFROM, TTIriRef.iri("http://snomed.info/sct#308490002", "Pathological development process (qualifier value)"));
        TTNode eye = new TTNode();
        eye.addObject(RDF.TYPE, OWL.RESTRICTION);
        eye.addObject(OWL.ONPROPERTY, TTIriRef.iri("http://snomed.info/sct#363698007", "Finding site (attribute)"));
        eye.addObject(OWL.SOMEVALUESFROM, TTIriRef.iri("http://snomed.info/sct#81745001", "Structure of eye proper (body structure)"));
        TTArray intersectionArray = new TTArray();
        intersectionArray.add(eye);
        intersectionArray.add(pathological);
        TTNode intersection = new TTNode();
        entity.set(RDFS.SUBCLASSOF, intersection);
    }
}
