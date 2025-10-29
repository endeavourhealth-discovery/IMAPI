package org.endeavourhealth.imapi.transform.qofimq;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for file discovery and schema/structural validation.
 */
public class QofImqFileServiceValidatorTest {

  @TempDir
  Path tempDir;

  private QofImqProperties props(Path inputRoot) {
    QofImqProperties p = new QofImqProperties();
    p.setInputPath(inputRoot.toString());
    p.setMaxFileSizeBytes(1024 * 1024);
    return p;
  }

  @Test
  void discoverJsonFiles_filtersAndSorts() throws IOException {
    // Arrange
    Path a = Files.createDirectories(tempDir.resolve("a"));
    Path b = Files.createDirectories(tempDir.resolve("b"));
    Path f1 = Files.writeString(a.resolve("one.json"), "{}\n");
    Path f2 = Files.writeString(b.resolve("two.JSON"), "{}\n"); // case-insensitive
    Path f3 = Files.writeString(b.resolve("readme.txt"), "ignore");

    QofImqFileService fs = new QofImqFileService(props(tempDir));

    // Act
    List<Path> files = fs.discoverJsonFiles();

    // Assert
    assertEquals(2, files.size());
    assertTrue(files.get(0).toString().endsWith("one.json"));
    assertTrue(files.get(1).toString().endsWith("two.JSON"));
    assertFalse(files.contains(f3));
  }

  @Test
  void validator_reportsErrorsForInvalidShapes_andNoneForValid() throws IOException {
    // Arrange
    Path valid = Files.writeString(tempDir.resolve("valid.json"), "{\n  \"field\": \"QOF:age\", \n  \"op\": \"EQUALS\", \n  \"value\": 42\n}\n");
    Path invalid = Files.writeString(tempDir.resolve("invalid.json"), "{\n  \"operator\": \"AND\"\n}\n");

    QofImqProperties p = props(tempDir);
    QofImqValidator validator = new QofImqValidator();

    // Act
    List<ValidationError> ok = validator.validateFile(valid);
    List<ValidationError> bad = validator.validateFile(invalid);

    // Assert
    assertNotNull(ok);
    assertTrue(ok.isEmpty(), "Expected no errors for valid file");

    assertNotNull(bad);
    assertFalse(bad.isEmpty(), "Expected errors for invalid file");
    // At least one error should mention missing operands or pointer
    boolean mentionsOperands = bad.stream().anyMatch(e ->
      (e.getMessage() != null && e.getMessage().toLowerCase().contains("operand"))
        || (e.getJsonPointer() != null && !e.getJsonPointer().isBlank())
    );
    assertTrue(mentionsOperands, "Expected validation to include pointer or operand message");
  }
}
