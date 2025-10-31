package org.endeavourhealth.imapi.transformation.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationLoaderTest {

    private ConfigurationLoader loader;
    private TransformationConfiguration config;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        loader = new ConfigurationLoader();
        config = new TransformationConfiguration();
    }

    // ===== Basic Loading Tests =====

    @Test
    void testLoadConfigurationWithNullFile() {
        assertTrue(loader.loadConfiguration(null, config));
    }

    @Test
    void testLoadConfigurationWithEmptyFile() {
        assertTrue(loader.loadConfiguration("", config));
    }

    @Test
    void testLoadConfigurationWithNonexistentFile() {
        assertFalse(loader.loadConfiguration("/nonexistent/file.json", config));
    }

    // ===== JSON Configuration Tests =====

    @Test
    void testLoadJsonConfiguration() throws IOException {
        String jsonContent = "{\n" +
                "  \"inputPath\": \"/input/test.json\",\n" +
                "  \"outputPath\": \"/output/test.json\",\n" +
                "  \"batchMode\": true,\n" +
                "  \"verbose\": true\n" +
                "}";

        Path jsonFile = tempDir.resolve("config.json");
        Files.write(jsonFile, jsonContent.getBytes());

        assertTrue(loader.loadConfiguration(jsonFile.toString(), config));
        assertEquals("/input/test.json", config.getInputPath());
        assertEquals("/output/test.json", config.getOutputPath());
        assertTrue(config.isBatchMode());
        assertTrue(config.isVerbose());
    }

    @Test
    void testLoadJsonConfigurationWithNestedTransformation() throws IOException {
        String jsonContent = "{\n" +
                "  \"transformation\": {\n" +
                "    \"inputPath\": \"/input/test.json\",\n" +
                "    \"outputPath\": \"/output/test.json\",\n" +
                "    \"batch\": true\n" +
                "  }\n" +
                "}";

        Path jsonFile = tempDir.resolve("config.json");
        Files.write(jsonFile, jsonContent.getBytes());

        assertTrue(loader.loadConfiguration(jsonFile.toString(), config));
        assertEquals("/input/test.json", config.getInputPath());
        assertEquals("/output/test.json", config.getOutputPath());
        assertTrue(config.isBatchMode());
    }

    @Test
    void testLoadJsonConfigurationWithPerformance() throws IOException {
        String jsonContent = "{\n" +
                "  \"performance\": {\n" +
                "    \"maxParallelThreads\": 4,\n" +
                "    \"cacheSize\": 5000,\n" +
                "    \"batchSize\": 50\n" +
                "  }\n" +
                "}";

        Path jsonFile = tempDir.resolve("config.json");
        Files.write(jsonFile, jsonContent.getBytes());

        assertTrue(loader.loadConfiguration(jsonFile.toString(), config));
        assertEquals(4, config.getMaxParallelThreads());
        assertEquals(5000, config.getCacheSize());
        assertEquals(50, config.getBatchSize());
    }

    @Test
    void testLoadJsonConfigurationWithErrorHandling() throws IOException {
        String jsonContent = "{\n" +
                "  \"errorHandling\": {\n" +
                "    \"continueOnError\": false,\n" +
                "    \"maxRetries\": 5,\n" +
                "    \"retryDelayMs\": 2000\n" +
                "  }\n" +
                "}";

        Path jsonFile = tempDir.resolve("config.json");
        Files.write(jsonFile, jsonContent.getBytes());

        assertTrue(loader.loadConfiguration(jsonFile.toString(), config));
        assertFalse(config.isContinueOnError());
        assertEquals(5, config.getMaxRetries());
        assertEquals(2000, config.getRetryDelayMs());
    }

    @Test
    void testLoadJsonConfigurationWithOutput() throws IOException {
        String jsonContent = "{\n" +
                "  \"output\": {\n" +
                "    \"overwrite\": true,\n" +
                "    \"createBackup\": false,\n" +
                "    \"encoding\": \"ISO-8859-1\"\n" +
                "  }\n" +
                "}";

        Path jsonFile = tempDir.resolve("config.json");
        Files.write(jsonFile, jsonContent.getBytes());

        assertTrue(loader.loadConfiguration(jsonFile.toString(), config));
        assertTrue(config.isOverwriteOutput());
        assertFalse(config.isCreateBackup());
        assertEquals("ISO-8859-1", config.getOutputEncoding());
    }

    @Test
    void testLoadJsonConfigurationWithLogging() throws IOException {
        String jsonContent = "{\n" +
                "  \"logging\": {\n" +
                "    \"level\": \"DEBUG\"\n" +
                "  }\n" +
                "}";

        Path jsonFile = tempDir.resolve("config.json");
        Files.write(jsonFile, jsonContent.getBytes());

        assertTrue(loader.loadConfiguration(jsonFile.toString(), config));
        assertEquals("DEBUG", config.getLogLevel());
    }

    @Test
    void testLoadJsonConfigurationWithProfile() throws IOException {
        String jsonContent = "{\n" +
                "  \"profile\": \"dev\"\n" +
                "}";

        Path jsonFile = tempDir.resolve("config.json");
        Files.write(jsonFile, jsonContent.getBytes());

        assertTrue(loader.loadConfiguration(jsonFile.toString(), config));
        assertEquals(TransformationConfiguration.Profile.DEV, config.getProfile());
    }

    // ===== YAML Configuration Tests =====

    @Test
    void testLoadYamlConfiguration() throws IOException {
        String yamlContent = "inputPath: /input/test.json\n" +
                "outputPath: /output/test.json\n" +
                "batch: true\n" +
                "verbose: true\n";

        Path yamlFile = tempDir.resolve("config.yaml");
        Files.write(yamlFile, yamlContent.getBytes());

        assertTrue(loader.loadConfiguration(yamlFile.toString(), config));
        assertEquals("/input/test.json", config.getInputPath());
        assertEquals("/output/test.json", config.getOutputPath());
        assertTrue(config.isBatchMode());
        assertTrue(config.isVerbose());
    }

    @Test
    void testLoadYmlConfiguration() throws IOException {
        String yamlContent = "inputPath: /input/test.json\n" +
                "outputPath: /output/test.json\n";

        Path yamlFile = tempDir.resolve("config.yml");
        Files.write(yamlFile, yamlContent.getBytes());

        assertTrue(loader.loadConfiguration(yamlFile.toString(), config));
        assertEquals("/input/test.json", config.getInputPath());
        assertEquals("/output/test.json", config.getOutputPath());
    }

    @Test
    void testLoadYamlConfigurationWithNestedStructure() throws IOException {
        String yamlContent = "transformation:\n" +
                "  inputPath: /input/test.json\n" +
                "  outputPath: /output/test.json\n" +
                "  batch: true\n" +
                "performance:\n" +
                "  maxParallelThreads: 4\n" +
                "  cacheSize: 5000\n";

        Path yamlFile = tempDir.resolve("config.yaml");
        Files.write(yamlFile, yamlContent.getBytes());

        assertTrue(loader.loadConfiguration(yamlFile.toString(), config));
        assertEquals("/input/test.json", config.getInputPath());
        assertEquals("/output/test.json", config.getOutputPath());
        assertTrue(config.isBatchMode());
        assertEquals(4, config.getMaxParallelThreads());
        assertEquals(5000, config.getCacheSize());
    }

    // ===== Profile-Specific Configuration Tests =====

    @Test
    void testLoadProfileConfiguration() throws IOException {
        String baseContent = "inputPath: /input/base.json\noutputPath: /output/base.json\n";
        String prodContent = "inputPath: /input/prod.json\noutputPath: /output/prod.json\n";

        Path baseFile = tempDir.resolve("config.yaml");
        Path prodFile = tempDir.resolve("config-prod.yaml");
        Files.write(baseFile, baseContent.getBytes());
        Files.write(prodFile, prodContent.getBytes());

        assertTrue(loader.loadProfileConfiguration(baseFile.toString(), "prod", config));
        assertEquals("/input/prod.json", config.getInputPath());
        assertEquals("/output/prod.json", config.getOutputPath());
    }

    @Test
    void testLoadProfileConfigurationFallsBackToBase() throws IOException {
        String baseContent = "inputPath: /input/base.json\noutputPath: /output/base.json\n";

        Path baseFile = tempDir.resolve("config.yaml");
        Files.write(baseFile, baseContent.getBytes());

        // Profile-specific file doesn't exist, should use base
        assertTrue(loader.loadProfileConfiguration(baseFile.toString(), "dev", config));
        assertEquals("/input/base.json", config.getInputPath());
        assertEquals("/output/base.json", config.getOutputPath());
    }

    // ===== Validation Tests =====

    @Test
    void testValidateConfigurationFileValid() throws IOException {
        String jsonContent = "{ \"inputPath\": \"/input\" }";
        Path jsonFile = tempDir.resolve("config.json");
        Files.write(jsonFile, jsonContent.getBytes());

        assertTrue(loader.validateConfigurationFile(jsonFile.toString()));
    }

    @Test
    void testValidateConfigurationFileNotFound() {
        assertFalse(loader.validateConfigurationFile("/nonexistent/file.json"));
    }

    @Test
    void testValidateConfigurationFileNull() {
        assertTrue(loader.validateConfigurationFile(null));
    }

    @Test
    void testValidateConfigurationFileEmpty() {
        assertTrue(loader.validateConfigurationFile(""));
    }

    @Test
    void testValidateConfigurationFileInvalidJson() throws IOException {
        String invalidContent = "{ invalid json }";
        Path jsonFile = tempDir.resolve("invalid.json");
        Files.write(jsonFile, invalidContent.getBytes());

        assertFalse(loader.validateConfigurationFile(jsonFile.toString()));
    }

    @Test
    void testValidateConfigurationFileInvalidYaml() throws IOException {
        // YAML that's invalid
        String invalidContent = "invalid: [unclosed";
        Path yamlFile = tempDir.resolve("invalid.yaml");
        Files.write(yamlFile, invalidContent.getBytes());

        assertFalse(loader.validateConfigurationFile(yamlFile.toString()));
    }

    @Test
    void testValidateConfigurationFileUnknownFormat() throws IOException {
        String content = "some content";
        Path file = tempDir.resolve("config.xml");
        Files.write(file, content.getBytes());

        assertFalse(loader.validateConfigurationFile(file.toString()));
    }

    // ===== Template Generation Tests =====

    @Test
    void testGenerateYamlTemplate() {
        String template = ConfigurationLoader.generateYamlTemplate();
        assertNotNull(template);
        assertTrue(template.contains("inputPath"));
        assertTrue(template.contains("outputPath"));
        assertTrue(template.contains("batch"));
        assertTrue(template.contains("profile"));
    }

    @Test
    void testGenerateJsonTemplate() {
        String template = ConfigurationLoader.generateJsonTemplate();
        assertNotNull(template);
        assertTrue(template.contains("\"transformation\""));
        assertTrue(template.contains("\"inputPath\""));
        assertTrue(template.contains("\"profile\""));
    }

    @Test
    void testGeneratedYamlTemplateIsValid() throws IOException {
        String template = ConfigurationLoader.generateYamlTemplate();
        Path yamlFile = tempDir.resolve("template.yaml");
        Files.write(yamlFile, template.getBytes());

        // Should not throw when loading
        assertTrue(loader.validateConfigurationFile(yamlFile.toString()));
    }

    @Test
    void testGeneratedJsonTemplateIsValid() throws IOException {
        String template = ConfigurationLoader.generateJsonTemplate();
        Path jsonFile = tempDir.resolve("template.json");
        Files.write(jsonFile, template.getBytes());

        // Should not throw when loading
        assertTrue(loader.validateConfigurationFile(jsonFile.toString()));
    }

    // ===== Alternative Field Names Tests =====

    @Test
    void testLoadJsonConfigurationWithAlternativeFieldNames() throws IOException {
        String jsonContent = "{\n" +
                "  \"input\": \"/input/test.json\",\n" +
                "  \"output\": \"/output/test.json\",\n" +
                "  \"pattern\": \"qof-*.json\"\n" +
                "}";

        Path jsonFile = tempDir.resolve("config.json");
        Files.write(jsonFile, jsonContent.getBytes());

        assertTrue(loader.loadConfiguration(jsonFile.toString(), config));
        assertEquals("/input/test.json", config.getInputPath());
        assertEquals("/output/test.json", config.getOutputPath());
        assertEquals("qof-*.json", config.getFilePattern());
    }

    @Test
    void testLoadJsonConfigurationWithCustomProperties() throws IOException {
        String jsonContent = "{\n" +
                "  \"customProperties\": {\n" +
                "    \"prop1\": \"value1\",\n" +
                "    \"prop2\": \"value2\"\n" +
                "  }\n" +
                "}";

        Path jsonFile = tempDir.resolve("config.json");
        Files.write(jsonFile, jsonContent.getBytes());

        assertTrue(loader.loadConfiguration(jsonFile.toString(), config));
        assertEquals("value1", config.getCustomProperty("prop1"));
        assertEquals("value2", config.getCustomProperty("prop2"));
    }

    @Test
    void testLoadYamlConfigurationWithAlternativeFieldNames() throws IOException {
        String yamlContent = "input: /input/test.json\n" +
                "output: /output/test.json\n" +
                "pattern: \"*.qof\"\n";

        Path yamlFile = tempDir.resolve("config.yaml");
        Files.write(yamlFile, yamlContent.getBytes());

        assertTrue(loader.loadConfiguration(yamlFile.toString(), config));
        assertEquals("/input/test.json", config.getInputPath());
        assertEquals("/output/test.json", config.getOutputPath());
        assertEquals("*.qof", config.getFilePattern());
    }
}