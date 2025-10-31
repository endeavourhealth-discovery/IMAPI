package org.endeavourhealth.imapi.transformation.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.DefaultApplicationArguments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for QOFToIMQCliApplication
 * 
 * Tests the complete CLI workflow including argument parsing,
 * validation, and end-to-end transformation execution.
 */
public class QOFToIMQCliIntegrationTest {

    @TempDir
    private Path tempDir;

    private Path testInputFile;
    private Path testOutputDir;

    @BeforeEach
    void setUp() throws IOException {
        testOutputDir = tempDir.resolve("output");
        Files.createDirectories(testOutputDir);
        testInputFile = tempDir.resolve("test-input.json");
    }

    /**
     * Create a minimal valid QOF document for testing
     */
    private void createTestQOFDocument() throws IOException {
        String qofContent = """
            {
              "name": "Test QOF Document",
              "description": "Test description",
              "selections": [
                {
                  "name": "Selection1",
                  "rules": []
                }
              ],
              "registers": [
                {
                  "name": "Register1",
                  "source": "TestSource"
                }
              ],
              "extractionFields": [
                {
                  "name": "Field1",
                  "path": "path.to.field"
                }
              ],
              "indicators": []
            }
            """;
        Files.writeString(testInputFile, qofContent);
    }

    /**
     * Create multiple test QOF documents for batch processing
     */
    private void createBatchTestDocuments(int count) throws IOException {
        for (int i = 0; i < count; i++) {
            String filename = String.format("qof-document-%d.json", i);
            Path filePath = tempDir.resolve(filename);
            
            String qofContent = String.format("""
                {
                  "name": "Test QOF Document %d",
                  "description": "Test description %d",
                  "selections": [
                    {
                      "name": "Selection1",
                      "rules": []
                    }
                  ],
                  "registers": [
                    {
                      "name": "Register1",
                      "source": "TestSource"
                    }
                  ],
                  "extractionFields": [
                    {
                      "name": "Field1",
                      "path": "path.to.field"
                    }
                  ],
                  "indicators": []
                }
                """, i, i);
            
            Files.writeString(filePath, qofContent);
        }
    }

    @Test
    @DisplayName("CLI validates missing required arguments")
    void testCliValidatesMissingArguments() {
        String[] args = {};
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertFalse(config.isValid());
        assertTrue(config.getValidationErrors().stream()
            .anyMatch(e -> e.contains("Input path is required")));
    }

    @Test
    @DisplayName("CLI validates nonexistent input file")
    void testCliValidatesNonexistentInputFile() {
        String[] args = {
            "--input=/nonexistent/path/file.json",
            "--output=" + testOutputDir.toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertFalse(config.isValid());
        assertTrue(config.getValidationErrors().stream()
            .anyMatch(e -> e.contains("does not exist")));
    }

    @Test
    @DisplayName("CLI rejects directory path in single-file mode")
    void testCliRejectsDirInSingleFileMode() throws IOException {
        Path inputDir = tempDir.resolve("input");
        Files.createDirectories(inputDir);

        String[] args = {
            "--input=" + inputDir.toAbsolutePath(),
            "--output=" + testOutputDir.toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertFalse(config.isValid());
        assertTrue(config.getValidationErrors().stream()
            .anyMatch(e -> e.contains("must be a file")));
    }

    @Test
    @DisplayName("CLI accepts file path in single-file mode")
    void testCliAcceptsFileInSingleFileMode() throws IOException {
        createTestQOFDocument();

        String[] args = {
            "--input=" + testInputFile.toAbsolutePath(),
            "--output=" + testOutputDir.resolve("output.json").toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertTrue(config.isValid());
        assertFalse(config.isBatchMode());
    }

    @Test
    @DisplayName("CLI accepts directory path in batch mode")
    void testCliAcceptsDirInBatchMode() throws IOException {
        Path inputDir = tempDir.resolve("input");
        Files.createDirectories(inputDir);
        createBatchTestDocuments(1);

        String[] args = {
            "--batch",
            "--input=" + inputDir.toAbsolutePath(),
            "--output=" + testOutputDir.toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertTrue(config.isBatchMode());
    }

    @Test
    @DisplayName("CLI rejects file path in batch mode")
    void testCliRejectsFileInBatchMode() throws IOException {
        createTestQOFDocument();

        String[] args = {
            "--batch",
            "--input=" + testInputFile.toAbsolutePath(),
            "--output=" + testOutputDir.toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertFalse(config.isValid());
        assertTrue(config.getValidationErrors().stream()
            .anyMatch(e -> e.contains("must be a directory")));
    }

    @Test
    @DisplayName("CLI creates missing output directory in batch mode")
    void testCliCreatesMissingOutputDir() throws IOException {
        Path inputDir = tempDir.resolve("input");
        Files.createDirectories(inputDir);
        Path missingOutputDir = testOutputDir.resolve("nested/output");

        String[] args = {
            "--batch",
            "--input=" + inputDir.toAbsolutePath(),
            "--output=" + missingOutputDir.toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertTrue(config.isValid());
        assertTrue(Files.exists(missingOutputDir));
    }

    @Test
    @DisplayName("CLI parses batch mode correctly")
    void testCliParsesBatchMode() {
        String[] args = {
            "--batch",
            "--input=/path/to/input",
            "--output=/path/to/output"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertEquals("/path/to/input", config.getInputPath());
        assertEquals("/path/to/output", config.getOutputPath());
        assertTrue(config.isBatchMode());
    }

    @Test
    @DisplayName("CLI parses file pattern correctly")
    void testCliParsesFilePattern() {
        String[] args = {
            "--batch",
            "--input=/path/to/input",
            "--output=/path/to/output",
            "--pattern=qof-*.json"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertEquals("qof-*.json", config.getFilePattern());
    }

    @Test
    @DisplayName("CLI defaults to *.json pattern")
    void testCliDefaultsToJsonPattern() {
        String[] args = {
            "--batch",
            "--input=/path/to/input",
            "--output=/path/to/output"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertEquals("*.json", config.getFilePattern());
    }

    @Test
    @DisplayName("CLI parses verbose flag correctly")
    void testCliParsesVerboseFlag() {
        String[] args = {
            "--input=/path/to/input.json",
            "--output=/path/to/output.json",
            "--verbose"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertTrue(config.isVerbose());
    }

    @Test
    @DisplayName("CLI parses parallel processing flag")
    void testCliParsesParallelFlag() {
        String[] args = {
            "--batch",
            "--input=/path/to/input",
            "--output=/path/to/output",
            "--parallel"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertTrue(config.isParallelProcessing());
    }

    @Test
    @DisplayName("CLI loads configuration from file")
    void testCliLoadsConfigFromFile() throws IOException {
        Path configFile = tempDir.resolve("config.json");
        String configContent = """
            {
              "input": "/path/from/config",
              "output": "/path/from/config",
              "batch": true,
              "pattern": "config-*.json",
              "verbose": true,
              "parallelProcessing": true
            }
            """;
        Files.writeString(configFile, configContent);

        String[] args = {
            "--config=" + configFile.toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertEquals("/path/from/config", config.getInputPath());
        assertEquals("/path/from/config", config.getOutputPath());
        assertTrue(config.isBatchMode());
        assertEquals("config-*.json", config.getFilePattern());
        assertTrue(config.isVerbose());
        assertTrue(config.isParallelProcessing());
    }

    @Test
    @DisplayName("CLI command-line args override config file")
    void testCliCommandLineOverridesConfig() throws IOException {
        Path inputDir = tempDir.resolve("input");
        Files.createDirectories(inputDir);
        
        Path configFile = tempDir.resolve("config.json");
        String configContent = """
            {
              "input": "/path/from/config",
              "output": "/path/from/config",
              "batch": false
            }
            """;
        Files.writeString(configFile, configContent);

        String[] args = {
            "--config=" + configFile.toAbsolutePath(),
            "--input=" + inputDir.toAbsolutePath(),
            "--batch"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        // Command-line arguments should override config file
        assertEquals(inputDir.toAbsolutePath().toString(), config.getInputPath());
        assertTrue(config.isBatchMode());
    }

    @Test
    @DisplayName("CLI handles missing config file gracefully")
    void testCliHandlesMissingConfigFile() {
        String[] args = {
            "--config=/nonexistent/config.json"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertFalse(config.isValid());
        assertTrue(config.getValidationErrors().stream()
            .anyMatch(e -> e.contains("Configuration file not found")));
    }

    @Test
    @DisplayName("CLI handles malformed config file")
    void testCliHandlesMalformedConfigFile() throws IOException {
        Path configFile = tempDir.resolve("bad-config.json");
        Files.writeString(configFile, "{ invalid json");

        String[] args = {
            "--config=" + configFile.toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertFalse(config.isValid());
        assertTrue(config.getValidationErrors().stream()
            .anyMatch(e -> e.contains("Failed to parse configuration file")));
    }

    @Test
    @DisplayName("CLI validates JSON input file requirement")
    void testCliValidatesJsonInput() throws IOException {
        Path nonJsonFile = tempDir.resolve("file.txt");
        Files.writeString(nonJsonFile, "not json");

        String[] args = {
            "--input=" + nonJsonFile.toAbsolutePath(),
            "--output=" + testOutputDir.resolve("output.json").toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertFalse(config.isValid());
        assertTrue(config.getValidationErrors().stream()
            .anyMatch(e -> e.contains("must be a JSON file")));
    }

    @Test
    @DisplayName("CLI creates parent directory for output file")
    void testCliCreatesParentDirForOutput() throws IOException {
        createTestQOFDocument();
        Path nestedOutput = testOutputDir.resolve("nested/path/output.json");

        String[] args = {
            "--input=" + testInputFile.toAbsolutePath(),
            "--output=" + nestedOutput.toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertTrue(config.isValid());
        assertTrue(Files.exists(nestedOutput.getParent()));
    }

    @Test
    @DisplayName("CLI configuration is valid for basic single-file transform")
    void testCliValidSingleFileConfig() throws IOException {
        createTestQOFDocument();

        String[] args = {
            "--input=" + testInputFile.toAbsolutePath(),
            "--output=" + testOutputDir.resolve("output.json").toAbsolutePath()
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertTrue(config.isValid());
        assertEquals(testInputFile.toAbsolutePath().toString(), config.getInputPath());
        assertFalse(config.isBatchMode());
        assertFalse(config.isVerbose());
        assertFalse(config.isParallelProcessing());
    }

    @Test
    @DisplayName("CLI configuration is valid for batch mode")
    void testCliValidBatchConfig() throws IOException {
        Path inputDir = tempDir.resolve("input");
        Files.createDirectories(inputDir);
        createBatchTestDocuments(1);

        String[] args = {
            "--batch",
            "--input=" + inputDir.toAbsolutePath(),
            "--output=" + testOutputDir.toAbsolutePath(),
            "--pattern=qof-*.json",
            "--verbose"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertTrue(config.isValid());
        assertTrue(config.isBatchMode());
        assertEquals("qof-*.json", config.getFilePattern());
        assertTrue(config.isVerbose());
    }
}