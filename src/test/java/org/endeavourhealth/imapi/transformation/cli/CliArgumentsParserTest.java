package org.endeavourhealth.imapi.transformation.cli;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CliArgumentsParser
 */
public class CliArgumentsParserTest {

    @Test
    @DisplayName("Parse basic single file arguments")
    public void testParseSingleFileArguments() {
        String[] args = {
            "--input=/path/to/input.json",
            "--output=/path/to/output.json"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertEquals("/path/to/input.json", config.getInputPath());
        assertEquals("/path/to/output.json", config.getOutputPath());
        assertFalse(config.isBatchMode());
        assertEquals("*.json", config.getFilePattern());
    }

    @Test
    @DisplayName("Parse batch mode arguments")
    public void testParseBatchModeArguments() {
        String[] args = {
            "--batch",
            "--input=/path/to/input",
            "--output=/path/to/output",
            "--pattern=qof-*.json"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertEquals("/path/to/input", config.getInputPath());
        assertEquals("/path/to/output", config.getOutputPath());
        assertTrue(config.isBatchMode());
        assertEquals("qof-*.json", config.getFilePattern());
    }

    @Test
    @DisplayName("Parse verbose flag")
    public void testParseVerboseFlag() {
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
    @DisplayName("Parse verbose short flag")
    public void testParseVerboseShortFlag() {
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
    @DisplayName("Parse parallel processing flag")
    public void testParseParallelFlag() {
        String[] args = {
            "--input=/path/to/input",
            "--output=/path/to/output",
            "--batch",
            "--parallel"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertTrue(config.isParallelProcessing());
    }

    @Test
    @DisplayName("Validate missing input path")
    public void testValidateMissingInputPath() {
        String[] args = {
            "--output=/path/to/output.json"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertFalse(config.isValid());
        assertTrue(config.getValidationErrors().stream()
            .anyMatch(e -> e.contains("Input path is required")));
    }

    @Test
    @DisplayName("Validate missing output path")
    public void testValidateMissingOutputPath() {
        String[] args = {
            "--input=/path/to/input.json"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertFalse(config.isValid());
        assertTrue(config.getValidationErrors().stream()
            .anyMatch(e -> e.contains("Output path is required")));
    }

    @Test
    @DisplayName("Validate nonexistent input path")
    public void testValidateNonexistentInputPath() {
        String[] args = {
            "--input=/nonexistent/path/to/input.json",
            "--output=/path/to/output.json"
        };
        DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
        CliArgumentsParser parser = new CliArgumentsParser(appArgs);
        CliConfiguration config = parser.parse();

        assertFalse(config.isValid());
        assertTrue(config.getValidationErrors().stream()
            .anyMatch(e -> e.contains("does not exist")));
    }

    @Test
    @DisplayName("Load configuration from file")
    public void testLoadConfigurationFromFile() throws IOException {
        // Create temporary config file
        Path tempDir = Files.createTempDirectory("cli-test");
        Path configFile = tempDir.resolve("config.json");
        
        String configContent = """
            {
              "input": "/path/to/input",
              "output": "/path/to/output",
              "batch": true,
              "pattern": "test-*.json",
              "verbose": true,
              "parallelProcessing": true
            }
            """;
        
        Files.writeString(configFile, configContent);

        try {
            String[] args = {"--config=" + configFile.toAbsolutePath()};
            DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
            CliArgumentsParser parser = new CliArgumentsParser(appArgs);
            CliConfiguration config = parser.parse();

            assertEquals("/path/to/input", config.getInputPath());
            assertEquals("/path/to/output", config.getOutputPath());
            assertTrue(config.isBatchMode());
            assertEquals("test-*.json", config.getFilePattern());
            assertTrue(config.isVerbose());
            assertTrue(config.isParallelProcessing());
        } finally {
            // Cleanup
            Files.deleteIfExists(configFile);
            Files.deleteIfExists(tempDir);
        }
    }

    @Test
    @DisplayName("Command-line arguments override config file")
    public void testCommandLineOverridesConfigFile() throws IOException {
        // Create temporary config file
        Path tempDir = Files.createTempDirectory("cli-test");
        Path configFile = tempDir.resolve("config.json");
        Path testFile = tempDir.resolve("test.json");
        Files.createFile(testFile);
        
        String configContent = """
            {
              "input": "/path/from/config",
              "output": "/path/from/config",
              "batch": false
            }
            """;
        
        Files.writeString(configFile, configContent);

        try {
            String[] args = {
                "--config=" + configFile.toAbsolutePath(),
                "--input=" + testFile.toAbsolutePath()
            };
            DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
            CliArgumentsParser parser = new CliArgumentsParser(appArgs);
            CliConfiguration config = parser.parse();

            // Command-line argument should override config file
            assertEquals(testFile.toAbsolutePath().toString(), config.getInputPath());
        } finally {
            // Cleanup
            Files.deleteIfExists(testFile);
            Files.deleteIfExists(configFile);
            Files.deleteIfExists(tempDir);
        }
    }
}