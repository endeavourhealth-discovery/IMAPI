package org.endeavourhealth.imapi.logic.exporters;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SetTextFileExporterTest {
  private static final String SNOMED = "http://snomed.info/sct#";
  private final SetTextFileExporter exporter = new SetTextFileExporter();

  private static Concept concept(String code, String name) {
    Concept concept = new Concept().setCode(code).setName(name).setScheme(new TTIriRef(SNOMED, "Snomed"));
    concept.setStatus(new TTIriRef("http://endhealth.info/im#Active", "Active"));
    return concept;
  }

  private String text(String type, Set<Concept> members, boolean im1, boolean subsets, boolean legacy, List<String> subsumption) throws Exception {
    return new String(exporter.generateFile(type, members, "MySet", im1, subsets, legacy, subsumption, null), StandardCharsets.UTF_8);
  }

  private static String[] lines(String s) {
    return s.split(System.lineSeparator());
  }

  @Test
  void tsv_basicHeaderAndRow() throws Exception {
    String[] lines = lines(text("tsv", Set.of(concept("123", "Asthma").setUsage(7)), false, false, false, null));
    assertThat(lines[0]).isEqualTo("code\tterm\tstatus\tscheme\tusage\textension\tset");
    assertThat(lines[1]).isEqualTo("123\tAsthma\tActive\tSnomed\t7\tN\tMySet");
  }

  @Test
  void csv_quotesValues() throws Exception {
    String[] lines = lines(text("csv", Set.of(concept("123", "Asthma")), false, false, false, null));
    assertThat(lines[0]).isEqualTo("code,term,status,scheme,usage,extension,set");
    assertThat(lines[1]).isEqualTo("\"123\",\"Asthma\",\"Active\",\"Snomed\",\"null\",\"N\",\"MySet\"");
  }

  @Test
  void im1IdColumnAddedWhenRequested() throws Exception {
    Concept concept = concept("123", "Asthma");
    concept.setIm1Id("999");
    String[] lines = lines(text("tsv", Set.of(concept), true, false, false, null));
    assertThat(lines[0]).endsWith("\tim1id");
    assertThat(lines[1]).endsWith("\t999");
  }

  @Test
  void subsumedColumnAddedWhenSubsumptionRequested() throws Exception {
    Concept concept = concept("123", "Asthma");
    concept.setSubsumed(true);
    String[] lines = lines(text("tsv", Set.of(concept), false, false, false, List.of("x")));
    assertThat(lines[0]).startsWith("subsumed\tcode");
    assertThat(lines[1]).startsWith("Y\t123");
  }

  @Test
  void subsetColumnsOnlyIncludedWhenMembersHaveSubsets() throws Exception {
    String withoutSubsets = text("tsv", Set.of(concept("123", "Asthma")), false, true, false, null);
    assertThat(lines(withoutSubsets)[0]).doesNotContain("subset");

    Concept inSubset = concept("123", "Asthma");
    inSubset.addIsContainedIn(new TTEntity().setIri("http://example.org#sub").setName("Sub").setVersion(2));
    String[] lines = lines(text("tsv", Set.of(inSubset), false, true, false, null));
    assertThat(lines[0]).endsWith("\tsubset\tsubsetIri\tsubsetVersion");
    assertThat(lines[1]).endsWith("\tSub\thttp://example.org#sub\t2");
  }

  @Test
  void legacyRowsWrittenPerMatchedFromConcept() throws Exception {
    Concept concept = concept("123", "Asthma");
    concept.addMatchedFrom(new Concept().setCode("L1").setName("Legacy").setScheme(new TTIriRef("http://read#")).setCodeId("cid").setAlternativeCode("alt"));
    String[] lines = lines(text("tsv", Set.of(concept), false, false, true, null));
    assertThat(lines[0]).endsWith("\tlegacyCode\tlegacyTerm\tlegacyScheme\tcodeId\tlegacyAltCode");
    assertThat(lines[1]).endsWith("\tL1\tLegacy\thttp://read#\tcid\talt");
  }

  @Test
  void nonSnomedSchemeIsMarkedAsExtension() throws Exception {
    Concept concept = new Concept().setCode("1").setName("n").setScheme(new TTIriRef("http://endhealth.info/im#", "IM"));
    assertThat(lines(text("tsv", Set.of(concept), false, false, false, null))[1]).contains("\tY\t");
  }

  @Test
  void unknownFileTypeReturnsEmptyArray() throws Exception {
    assertThat(exporter.generateFile("pdf", Set.of(), "MySet", false, false, false, null, null)).isEmpty();
  }

  @Test
  void xlsx_containsExpansionSheetAndEclSheet() throws Exception {
    byte[] bytes = exporter.generateFile("xlsx", Set.of(concept("123", "Asthma")), "MySet", false, false, false, null, "<< 195967001");
    try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
      Sheet ecl = workbook.getSheet("ECLDefinition");
      assertThat(ecl.getRow(1).getCell(0).getStringCellValue()).isEqualTo("<< 195967001");
      Sheet expansion = workbook.getSheet("Expansion");
      assertThat(expansion.getRow(0).getCell(0).getStringCellValue()).isEqualTo("code");
      assertThat(expansion.getRow(1).getCell(1).getStringCellValue()).isEqualTo("Asthma");
    }
  }

  @Test
  void xlsx_withoutEclHasNoEclSheet() throws Exception {
    byte[] bytes = exporter.generateFile("xlsx", Set.of(concept("123", "Asthma")), "MySet", false, false, false, null, "");
    try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
      assertThat(workbook.getSheet("ECLDefinition")).isNull();
      assertThat(workbook.getSheet("Expansion")).isNotNull();
    }
  }
}
