package org.endeavourhealth.imapi.logic.exporters;

import org.endeavourhealth.imapi.model.iml.Concept;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SetExporterTest {
  private final SetExporter exporter = new SetExporter();

  private static Concept concept(String im1Id) {
    Concept concept = new Concept();
    concept.setIm1Id(im1Id);
    return concept;
  }

  private static String[] lines(String output) {
    return output.split(System.lineSeparator());
  }

  @Test
  void generateForIm1_emptyMembersGivesHeaderOnly() throws Exception {
    assertThat(exporter.generateForIm1("set1", "Set One", Set.of()).toString()).isEqualTo("vsId\tvsName\tmemberDbid");
  }

  @Test
  void generateForIm1_writesOneRowPerMemberWithIm1Id() throws Exception {
    String[] lines = lines(exporter.generateForIm1("set1", "Set One", Set.of(concept("100"))).toString());
    assertThat(lines).containsExactly("vsId\tvsName\tmemberDbid", "set1\tSet One\t100");
  }

  @Test
  void generateForIm1_skipsMembersWithoutIm1Id() throws Exception {
    Set<Concept> members = Set.of(concept(null));
    assertThat(lines(exporter.generateForIm1("set1", "Set One", members).toString())).hasSize(1);
  }

  @Test
  void generateForIm1_addsLegacyRowsUsingParentIm1Id() throws Exception {
    Concept member = concept("100");
    member.addMatchedFrom(concept("200"));
    String[] lines = lines(exporter.generateForIm1("set1", "Set One", Set.of(member)).toString());
    assertThat(lines).containsExactly("vsId\tvsName\tmemberDbid", "set1\tSet One\t100", "set1\tSet One\t100");
  }

  @Test
  void generateForIm1_legacyWithoutIm1IdIsSkipped() throws Exception {
    Concept member = concept("100");
    member.addMatchedFrom(concept(null));
    assertThat(lines(exporter.generateForIm1("set1", "Set One", Set.of(member)).toString())).hasSize(2);
  }
}
