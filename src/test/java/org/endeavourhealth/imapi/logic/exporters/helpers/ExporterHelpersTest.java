package org.endeavourhealth.imapi.logic.exporters.helpers;

import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExporterHelpersTest {

  private static Concept conceptInSubset() {
    TTEntity subset = new TTEntity().setIri("http://example.org#subset").setName("My subset").setVersion(3);
    Concept concept = new Concept();
    concept.addIsContainedIn(subset);
    return concept;
  }

  @Test
  void constructorIsNotInstantiable() throws NoSuchMethodException {
    Constructor<ExporterHelpers> constructor = ExporterHelpers.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    assertThatThrownBy(constructor::newInstance)
      .isInstanceOf(InvocationTargetException.class)
      .hasCauseInstanceOf(IllegalStateException.class);
  }

  @Test
  void getUsage_returnsUsageAsString() {
    assertThat(ExporterHelpers.getUsage(new Concept().setUsage(42))).isEqualTo("42");
  }

  @Test
  void getUsage_returnsNullStringWhenMissing() {
    assertThat(ExporterHelpers.getUsage(new Concept())).isEqualTo("null");
  }

  @Test
  void getIsExtension_returnsNullStringWhenNoScheme() {
    assertThat(ExporterHelpers.getIsExtension(new Concept())).isEqualTo("null");
  }

  @Test
  void getIsExtension_returnsNForSnomedScheme() {
    Concept concept = new Concept().setScheme(new TTIriRef("http://snomed.info/sct#"));
    assertThat(ExporterHelpers.getIsExtension(concept)).isEqualTo("N");
  }

  @Test
  void getIsExtension_returnsYForOtherSchemes() {
    Concept concept = new Concept().setScheme(new TTIriRef("http://endhealth.info/im#"));
    assertThat(ExporterHelpers.getIsExtension(concept)).isEqualTo("Y");
  }

  @Test
  void subsetGetters_returnFirstContainingSubset() {
    Concept concept = conceptInSubset();
    assertThat(ExporterHelpers.getSubSet(concept)).isEqualTo("My subset");
    assertThat(ExporterHelpers.getSubsetIri(concept)).isEqualTo("http://example.org#subset");
    assertThat(ExporterHelpers.setSubsetVersion(concept)).isEqualTo("3");
  }

  @Test
  void subsetGetters_returnNullStringWhenNotInSubset() {
    Concept concept = new Concept();
    assertThat(ExporterHelpers.getSubSet(concept)).isEqualTo("null");
    assertThat(ExporterHelpers.getSubsetIri(concept)).isEqualTo("null");
    assertThat(ExporterHelpers.setSubsetVersion(concept)).isEqualTo("null");
  }

  @Test
  void getCode_returnsAlternativeCodeOrNullString() {
    assertThat(ExporterHelpers.getCode(new Concept().setAlternativeCode("ALT1"))).isEqualTo("ALT1");
    assertThat(ExporterHelpers.getCode(new Concept())).isEqualTo("null");
  }

  @Test
  void getStatus_returnsStatusNameOrNullString() {
    Concept concept = new Concept();
    concept.setStatus(new TTIriRef("http://endhealth.info/im#Active", "Active"));
    assertThat(ExporterHelpers.getStatus(concept)).isEqualTo("Active");
    assertThat(ExporterHelpers.getStatus(new Concept())).isEqualTo("null");
  }
}
