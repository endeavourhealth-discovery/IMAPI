package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTToECLTest {

    @Test
    void getConceptSetECL_nullIri() throws DataFormatException {
        TTEntity definition = new TTEntity();
        String actual = TTToECL.getConceptSetECL(definition, null, false);
        Assertions.assertNull(actual);
    }

    @Test
    void getConceptSetECL_includeNameTrue() throws DataFormatException {
        TTEntity definition = new TTEntity()
                .setIri("http://endhealth.info/im#CSET_BartsCVSSMeds")
                .setName("Concept Set- Barts Covid vaccine study medication concepts");

        definition.set(IM.HAS_MEMBER, new TTArray()
                .add(iri("http://snomed.info/sct#39330711000001103", "COVID-19 vaccine (product)"))
                .add(new TTNode().set(OWL.INTERSECTIONOF, new TTArray()
                        .add(iri("http://snomed.info/sct#10363601000001109", "UK product (product)"))
                        .add(new TTNode().set(iri("http://snomed.info/sct#10362601000001103","Has VMP (attribute)"), iri("http://snomed.info/sct#39330711000001103","COVID-19 vaccine (product)")))
                ))
        );
        String expected = "<<39330711000001103 | COVID-19 vaccine (product) " +
                "OR (<<10363601000001109 | UK product (product) " +
                ": <<10362601000001103 | Has VMP (attribute) " +
                "= <<39330711000001103 | COVID-19 vaccine (product))";
        String actual = TTToECL.getConceptSetECL(definition, null, true);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getConceptSetECL_includeNameFalse() throws DataFormatException {
        TTEntity definition = new TTEntity()
                .setIri("http://endhealth.info/im#CSET_BartsCVSSMeds")
                .setName("Concept Set- Barts Covid vaccine study medication concepts");

        definition.set(IM.HAS_MEMBER, new TTArray()
                .add(iri("http://snomed.info/sct#39330711000001103", "COVID-19 vaccine (product)"))
                .add(new TTNode().set(OWL.INTERSECTIONOF, new TTArray()
                        .add(iri("http://snomed.info/sct#10363601000001109", "UK product (product)"))
                        .add(new TTNode().set(iri("http://snomed.info/sct#10362601000001103","Has VMP (attribute)"), iri("http://snomed.info/sct#39330711000001103","COVID-19 vaccine (product)")))
                ))
        );
        String expected = "<<39330711000001103 " +
                "OR (<<10363601000001109 " +
                ": <<10362601000001103 " +
                "= <<39330711000001103)";
        String actual = TTToECL.getConceptSetECL(definition, null, false);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getConceptSetECL_hasNotMember() throws DataFormatException {
        TTEntity definition = new TTEntity()
                .setIri("http://endhealth.info/im#CSET_BartsCVSSMeds")
                .setName("Concept Set- Barts Covid vaccine study medication concepts");
        String actual = TTToECL.getConceptSetECL(definition, null, false);
        Assertions.assertNull(actual);
    }

}
