package org.endeavourhealth.imapi.transformation.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransformationConfigurationTest {

    private TransformationConfiguration config;

    @BeforeEach
    void setUp() {
        config = new TransformationConfiguration();
    }

    // ===== Constructor Tests =====

    @Test
    void testDefaultConstructor() {
        assertNotNull(config);
        assertEquals(TransformationConfiguration.Profile.PROD, config.getProfile());
        assertEquals("*.json", config.getFilePattern());
        assertFalse(config.isBatchMode());
        assertFalse(config.isVerbose());
    }

    @Test
    void testConstructorWithProfile() {
        TransformationConfiguration devConfig = new TransformationConfiguration(TransformationConfiguration.Profile.DEV);
        assertEquals(TransformationConfiguration.Profile.DEV, devConfig.getProfile());
        assertEquals("DEBUG", devConfig.getLogLevel());
        assertTrue(devConfig.isEnableMetrics());
    }

    // ===== Profile Tests =====

    @Test
    void testApplyDevProfile() {
        config.applyProfileDefaults(TransformationConfiguration.Profile.DEV);
        assertEquals(TransformationConfiguration.Profile.DEV, config.getProfile());
        assertEquals("DEBUG", config.getLogLevel());
        assertTrue(config.isEnableMetrics());
        assertEquals(1, config.getMaxParallelThreads());
    }

    @Test
    void testApplyProdProfile() {
        config.applyProfileDefaults(TransformationConfiguration.Profile.PROD);
        assertEquals(TransformationConfiguration.Profile.PROD, config.getProfile());
        assertEquals("WARN", config.getLogLevel());
        assertFalse(config.isEnableMetrics());
        assertTrue(config.isParallelProcessing());
    }

    @Test
    void testApplyTestProfile() {
        config.applyProfileDefaults(TransformationConfiguration.Profile.TEST);
        assertEquals(TransformationConfiguration.Profile.TEST, config.getProfile());
        assertEquals("INFO", config.getLogLevel());
        assertFalse(config.isEnableMetrics());
        assertFalse(config.isContinueOnError());
    }

    // ===== Validation Tests =====

    @Test
    void testValidationFailsWithoutInputPath() {
        config.setOutputPath("/output");
        assertFalse(config.validate());
        assertFalse(config.getValidationErrors().isEmpty());
        assertTrue(config.getValidationErrors().stream()
                .anyMatch(e -> e.contains("Input path")));
    }

    @Test
    void testValidationFailsWithoutOutputPath() {
        config.setInputPath("/input");
        assertFalse(config.validate());
        assertFalse(config.getValidationErrors().isEmpty());
        assertTrue(config.getValidationErrors().stream()
                .anyMatch(e -> e.contains("Output path")));
    }

    @Test
    void testValidationSuccessWithPaths() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        assertTrue(config.validate());
        assertTrue(config.isValid());
    }

    @Test
    void testValidationFailsWithInvalidThreadCount() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        config.setMaxParallelThreads(-1);
        assertFalse(config.validate());
        assertTrue(config.getValidationErrors().stream()
                .anyMatch(e -> e.contains("Max parallel threads")));
    }

    @Test
    void testValidationFailsWithInvalidCacheSize() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        config.setCacheSize(-1);
        assertFalse(config.validate());
        assertTrue(config.getValidationErrors().stream()
                .anyMatch(e -> e.contains("Cache size")));
    }

    @Test
    void testValidationFailsWithInvalidBatchSize() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        config.setBatchSize(0);
        assertFalse(config.validate());
        assertTrue(config.getValidationErrors().stream()
                .anyMatch(e -> e.contains("Batch size")));
    }

    @Test
    void testValidationFailsWithInvalidMaxRetries() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        config.setMaxRetries(-1);
        assertFalse(config.validate());
        assertTrue(config.getValidationErrors().stream()
                .anyMatch(e -> e.contains("Max retries")));
    }

    @Test
    void testValidationFailsWithEmptyFilePattern() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        config.setFilePattern("");
        assertFalse(config.validate());
        assertTrue(config.getValidationErrors().stream()
                .anyMatch(e -> e.contains("File pattern")));
    }

    // ===== Environment Variable Tests =====

    @Test
    void testApplyEnvironmentVariablesInput() {
        try {
            // We can't actually set environment variables in tests, but we can test the method doesn't crash
            config.applyEnvironmentVariables();
            // If no env vars set, config should remain unchanged
            assertNotNull(config);
        } catch (Exception e) {
            fail("applyEnvironmentVariables should not throw exceptions");
        }
    }

    // ===== Builder Tests =====

    @Test
    void testBuilder() {
        TransformationConfiguration built = new TransformationConfiguration.Builder()
                .withInputPath("/input")
                .withOutputPath("/output")
                .withBatchMode(true)
                .withFilePattern("qof-*.json")
                .withVerbose(true)
                .build();

        assertEquals("/input", built.getInputPath());
        assertEquals("/output", built.getOutputPath());
        assertTrue(built.isBatchMode());
        assertEquals("qof-*.json", built.getFilePattern());
        assertTrue(built.isVerbose());
    }

    @Test
    void testBuilderWithProfile() {
        TransformationConfiguration built = new TransformationConfiguration.Builder()
                .withProfile(TransformationConfiguration.Profile.DEV)
                .withInputPath("/input")
                .withOutputPath("/output")
                .build();

        assertEquals(TransformationConfiguration.Profile.DEV, built.getProfile());
        assertEquals("DEBUG", built.getLogLevel());
    }

    @Test
    void testBuilderWithMaxThreads() {
        TransformationConfiguration built = new TransformationConfiguration.Builder()
                .withMaxParallelThreads(4)
                .withInputPath("/input")
                .withOutputPath("/output")
                .build();

        assertEquals(4, built.getMaxParallelThreads());
    }

    // ===== Merge Tests =====

    @Test
    void testMergeFromCliConfiguration() {
        CliConfiguration cliConfig = new CliConfiguration();
        cliConfig.setInputPath("/cli-input");
        cliConfig.setOutputPath("/cli-output");
        cliConfig.setBatchMode(true);
        cliConfig.setVerbose(true);

        config.mergeFromCliConfiguration(cliConfig);

        assertEquals("/cli-input", config.getInputPath());
        assertEquals("/cli-output", config.getOutputPath());
        assertTrue(config.isBatchMode());
        assertTrue(config.isVerbose());
    }

    // ===== Error/Warning Tests =====

    @Test
    void testAddError() {
        config.addError("Test error");
        assertFalse(config.isValid());
        assertEquals(1, config.getValidationErrors().size());
        assertTrue(config.getValidationErrors().contains("Test error"));
    }

    @Test
    void testAddWarning() {
        config.addWarning("Test warning");
        assertTrue(config.isValid()); // Warnings don't affect validity
        assertEquals(1, config.getWarnings().size());
        assertTrue(config.getWarnings().contains("Test warning"));
    }

    @Test
    void testGetWarningsReturnsCopy() {
        config.addWarning("Warning 1");
        List<String> warnings1 = config.getWarnings();
        List<String> warnings2 = config.getWarnings();
        
        assertNotSame(warnings1, warnings2);
        assertEquals(warnings1, warnings2);
    }

    // ===== Encoding Validation Tests =====

    @Test
    void testValidationAcceptsValidEncoding() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        config.setOutputEncoding("UTF-8");
        assertTrue(config.validate());
    }

    @Test
    void testValidationAcceptsAlternateEncoding() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        config.setOutputEncoding("ISO-8859-1");
        assertTrue(config.validate());
    }

    @Test
    void testValidationWarnsInvalidEncoding() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        config.setOutputEncoding("INVALID_ENCODING");
        config.validate();
        assertEquals("UTF-8", config.getOutputEncoding()); // Should be corrected
        assertFalse(config.getWarnings().isEmpty());
    }

    // ===== Log Level Validation Tests =====

    @Test
    void testValidationAcceptsValidLogLevels() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        
        for (String level : new String[]{"TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF"}) {
            config = new TransformationConfiguration();
            config.setInputPath("/input");
            config.setOutputPath("/output");
            config.setLogLevel(level);
            assertTrue(config.validate());
        }
    }

    @Test
    void testValidationWarnsInvalidLogLevel() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        config.setLogLevel("INVALID_LEVEL");
        config.validate();
        assertEquals("INFO", config.getLogLevel()); // Should be corrected
        assertFalse(config.getWarnings().isEmpty());
    }

    // ===== Custom Properties Tests =====

    @Test
    void testSetAndGetCustomProperty() {
        config.setCustomProperty("key1", "value1");
        assertEquals("value1", config.getCustomProperty("key1"));
    }

    @Test
    void testGetCustomPropertiesReturnsCopy() {
        config.setCustomProperty("key1", "value1");
        var props1 = config.getCustomProperties();
        var props2 = config.getCustomProperties();
        
        assertNotSame(props1, props2);
        assertEquals(props1, props2);
    }

    // ===== Getters/Setters Tests =====

    @Test
    void testSetAndGetInputPath() {
        config.setInputPath("/test/input");
        assertEquals("/test/input", config.getInputPath());
    }

    @Test
    void testSetAndGetOutputPath() {
        config.setOutputPath("/test/output");
        assertEquals("/test/output", config.getOutputPath());
    }

    @Test
    void testSetAndGetBatchMode() {
        config.setBatchMode(true);
        assertTrue(config.isBatchMode());
    }

    @Test
    void testSetAndGetFilePattern() {
        config.setFilePattern("test-*.json");
        assertEquals("test-*.json", config.getFilePattern());
    }

    @Test
    void testSetAndGetVerbose() {
        config.setVerbose(true);
        assertTrue(config.isVerbose());
    }

    @Test
    void testSetAndGetParallelProcessing() {
        config.setParallelProcessing(true);
        assertTrue(config.isParallelProcessing());
    }

    @Test
    void testSetAndGetMaxParallelThreads() {
        config.setMaxParallelThreads(4);
        assertEquals(4, config.getMaxParallelThreads());
    }

    @Test
    void testSetAndGetCacheSize() {
        config.setCacheSize(5000);
        assertEquals(5000, config.getCacheSize());
    }

    @Test
    void testSetAndGetBatchSize() {
        config.setBatchSize(50);
        assertEquals(50, config.getBatchSize());
    }

    @Test
    void testSetAndGetContinueOnError() {
        config.setContinueOnError(false);
        assertFalse(config.isContinueOnError());
    }

    @Test
    void testSetAndGetMaxRetries() {
        config.setMaxRetries(5);
        assertEquals(5, config.getMaxRetries());
    }

    @Test
    void testSetAndGetRetryDelayMs() {
        config.setRetryDelayMs(2000);
        assertEquals(2000, config.getRetryDelayMs());
    }

    @Test
    void testSetAndGetOverwriteOutput() {
        config.setOverwriteOutput(true);
        assertTrue(config.isOverwriteOutput());
    }

    @Test
    void testSetAndGetCreateBackup() {
        config.setCreateBackup(false);
        assertFalse(config.isCreateBackup());
    }

    @Test
    void testSetAndGetOutputEncoding() {
        config.setOutputEncoding("ISO-8859-1");
        assertEquals("ISO-8859-1", config.getOutputEncoding());
    }

    @Test
    void testSetAndGetLogLevel() {
        config.setLogLevel("DEBUG");
        assertEquals("DEBUG", config.getLogLevel());
    }

    @Test
    void testSetAndGetEnableMetrics() {
        config.setEnableMetrics(true);
        assertTrue(config.isEnableMetrics());
    }

    @Test
    void testSetAndGetMetricsOutput() {
        config.setMetricsOutput("/metrics");
        assertEquals("/metrics", config.getMetricsOutput());
    }

    // ===== ToString Test =====

    @Test
    void testToString() {
        config.setInputPath("/input");
        config.setOutputPath("/output");
        String str = config.toString();
        assertNotNull(str);
        assertTrue(str.contains("inputPath"));
        assertTrue(str.contains("outputPath"));
    }

    // ===== Profile Enum Tests =====

    @Test
    void testProfileFromString() {
        assertEquals(TransformationConfiguration.Profile.DEV, 
                TransformationConfiguration.Profile.fromString("dev"));
        assertEquals(TransformationConfiguration.Profile.PROD, 
                TransformationConfiguration.Profile.fromString("prod"));
        assertEquals(TransformationConfiguration.Profile.TEST, 
                TransformationConfiguration.Profile.fromString("test"));
    }

    @Test
    void testProfileFromStringCaseInsensitive() {
        assertEquals(TransformationConfiguration.Profile.DEV, 
                TransformationConfiguration.Profile.fromString("DEV"));
        assertEquals(TransformationConfiguration.Profile.PROD, 
                TransformationConfiguration.Profile.fromString("PROD"));
    }

    @Test
    void testProfileFromStringThrowsForInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            TransformationConfiguration.Profile.fromString("invalid");
        });
    }
}