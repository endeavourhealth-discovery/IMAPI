package org.endeavourhealth.imapi.logic.exporters;

import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
public class IMQFeatureTableExporter {
  private static final Path EXPORT_DIR = Path.of("src/test/resources/features/transforms");
  private static final String REG_QUERIES_FILE = "IMQtoSQLConverterRegQueries.txt";
  private static final String QOF_QUERIES_FILE = "IMQtoSQLConverterQOFQueries.txt";
  private static final String SMH_QUERIES_FILE = "IMQtoSQLConverterSMHQueries.txt";

  private final EntityRepository entityRepository = new EntityRepository();

  public void exportRegisterQueries(List<TTIriRef> entities) {
    writeFile(REG_QUERIES_FILE, entities);
  }

  public void exportQOFQueries(List<TTIriRef> entities) {
    writeFile(QOF_QUERIES_FILE, entities);
  }

  public void exportSMHQueries(List<TTIriRef> entities) {
    writeFile(SMH_QUERIES_FILE, entities);
  }

  private void writeFile(String filename, List<TTIriRef> entities) {
    Path file = EXPORT_DIR.resolve(filename);
    try {
      Files.createDirectories(EXPORT_DIR);
      Files.writeString(file, toFeatureTable(entities), StandardCharsets.UTF_8);
      log.info("Exported {} rows to {}", entities.size(), file.toAbsolutePath());
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to write export file " + file, e);
    }
  }

  private String toFeatureTable(List<TTIriRef> entities) {
    StringJoiner rows = new StringJoiner(System.lineSeparator());
    rows.add("| iri | count | label |");
    for (TTIriRef entity : entities) {
      rows.add("| \"" + entity.getIri() + "\" |  | " + entity.getName() + " |");
    }
    return rows.toString();
  }
}