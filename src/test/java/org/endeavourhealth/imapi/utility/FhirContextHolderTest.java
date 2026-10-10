package org.endeavourhealth.imapi.utility;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Exercises the FHIR paths IMAPI uses (build a ValueSet, encode it to JSON, read it back). icu4j is excluded from the
 * build because only the FHIR validation/rendering message code needs it; this fails with a NoClassDefFoundError if a
 * path IMAPI does use turns out to need it.
 */
class FhirContextHolderTest {

  @Test
  void contextIsSharedAndCreatedOnce() {
    assertThat(FhirContextHolder.r4()).isSameAs(FhirContextHolder.r4());
  }

  @Test
  void valueSetWithComposeAndExpansionRoundTripsThroughJson() {
    ValueSet valueSet = new ValueSet();
    valueSet.setUrl("http://example.org/vs");
    valueSet.setName("Example");
    valueSet.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
    valueSet.getCompose().addInclude().setSystem("http://snomed.info/sct").addConcept().setCode("22298006").setDisplay("Myocardial infarction");
    valueSet.getExpansion().addContains().setSystem("http://snomed.info/sct").setCode("22298006").setDisplay("Myocardial infarction");

    FhirContext ctx = FhirContextHolder.r4();
    String json = ctx.newJsonParser().encodeResourceToString(valueSet);
    ValueSet parsed = ctx.newJsonParser().parseResource(ValueSet.class, json);

    assertThat(json).contains("\"resourceType\":\"ValueSet\"").contains("22298006");
    assertThat(parsed.getExpansion().getContains()).hasSize(1);
    assertThat(parsed.getCompose().getIncludeFirstRep().getConceptFirstRep().getCode()).isEqualTo("22298006");
  }

  @Test
  void parametersAndCodeableConceptEncode() {
    Parameters parameters = new Parameters();
    parameters.addParameter().setName("code").setValue(new CodeableConcept().addCoding(new Coding("http://snomed.info/sct", "22298006", "Myocardial infarction")));

    String json = FhirContextHolder.r4().newJsonParser().setPrettyPrint(true).encodeResourceToString(parameters);

    assertThat(json).contains("\"resourceType\": \"Parameters\"").contains("Myocardial infarction");
  }
}
