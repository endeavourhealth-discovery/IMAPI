package org.endeavourhealth.imapi.transformation.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Loads configuration from YAML or JSON files.
 * 
 * Supports:
 * - YAML files (*.yaml, *.yml)
 * - JSON files (*.json)
 * - Profile-specific configuration files (e.g., config-prod.yaml)
 */
public class ConfigurationLoader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationLoader.class);

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    /**
     * Load configuration from a file (YAML or JSON).
     *
     * @param configFile Path to configuration file
     * @param config Configuration object to populate
     * @return true if loaded successfully, false otherwise
     */
    public boolean loadConfiguration(String configFile, TransformationConfiguration config) {
        if (configFile == null || configFile.trim().isEmpty()) {
            return true; // No config file specified, use defaults
        }

        try {
            File file = new File(configFile);
            if (!file.exists()) {
                logger.warn("Configuration file not found: {}", configFile);
                return false;
            }

            if (configFile.endsWith(".yaml") || configFile.endsWith(".yml")) {
                return loadYamlConfiguration(file, config);
            } else if (configFile.endsWith(".json")) {
                return loadJsonConfiguration(file, config);
            } else {
                logger.warn("Unknown configuration file type: {}. Supported: .yaml, .yml, .json", configFile);
                return false;
            }
        } catch (IOException e) {
            logger.error("Failed to load configuration file: {}", configFile, e);
            config.addError("Failed to load configuration file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load configuration from a YAML file.
     */
    private boolean loadYamlConfiguration(File file, TransformationConfiguration config) throws IOException {
        logger.info("Loading YAML configuration from: {}", file.getAbsolutePath());
        
        JsonNode root = yamlMapper.readTree(file);
        
        applyJsonNodeToConfiguration(root, config);
        logger.info("Successfully loaded YAML configuration");
        return true;
    }

    /**
     * Load configuration from a JSON file.
     */
    private boolean loadJsonConfiguration(File file, TransformationConfiguration config) throws IOException {
        logger.info("Loading JSON configuration from: {}", file.getAbsolutePath());
        
        JsonNode root = jsonMapper.readTree(file);
        
        applyJsonNodeToConfiguration(root, config);
        logger.info("Successfully loaded JSON configuration");
        return true;
    }

    /**
     * Apply JsonNode values to configuration object.
     */
    private void applyJsonNodeToConfiguration(JsonNode node, TransformationConfiguration config) {
        if (node == null) {
            return;
        }

        // Transformation parameters
        if (node.has("transformation")) {
            JsonNode transform = node.get("transformation");
            
            if (transform.has("inputPath")) {
                config.setInputPath(transform.get("inputPath").asText());
            }
            if (transform.has("input")) {
                config.setInputPath(transform.get("input").asText());
            }
            if (transform.has("outputPath")) {
                config.setOutputPath(transform.get("outputPath").asText());
            }
            if (transform.has("output")) {
                config.setOutputPath(transform.get("output").asText());
            }
            if (transform.has("batchMode")) {
                config.setBatchMode(transform.get("batchMode").asBoolean());
            }
            if (transform.has("batch")) {
                config.setBatchMode(transform.get("batch").asBoolean());
            }
            if (transform.has("filePattern")) {
                config.setFilePattern(transform.get("filePattern").asText());
            }
            if (transform.has("pattern")) {
                config.setFilePattern(transform.get("pattern").asText());
            }
            if (transform.has("verbose")) {
                config.setVerbose(transform.get("verbose").asBoolean());
            }
            if (transform.has("parallelProcessing")) {
                config.setParallelProcessing(transform.get("parallelProcessing").asBoolean());
            }
            if (transform.has("parallel")) {
                config.setParallelProcessing(transform.get("parallel").asBoolean());
            }
        }

        // Top-level transformation parameters (alternative format)
        if (node.has("inputPath")) {
            config.setInputPath(node.get("inputPath").asText());
        }
        if (node.has("input")) {
            config.setInputPath(node.get("input").asText());
        }
        if (node.has("outputPath")) {
            config.setOutputPath(node.get("outputPath").asText());
        }
        if (node.has("output")) {
            config.setOutputPath(node.get("output").asText());
        }
        if (node.has("batchMode")) {
            config.setBatchMode(node.get("batchMode").asBoolean());
        }
        if (node.has("batch")) {
            config.setBatchMode(node.get("batch").asBoolean());
        }
        if (node.has("filePattern")) {
            config.setFilePattern(node.get("filePattern").asText());
        }
        if (node.has("pattern")) {
            config.setFilePattern(node.get("pattern").asText());
        }
        if (node.has("verbose")) {
            config.setVerbose(node.get("verbose").asBoolean());
        }
        if (node.has("parallelProcessing")) {
            config.setParallelProcessing(node.get("parallelProcessing").asBoolean());
        }
        if (node.has("parallel")) {
            config.setParallelProcessing(node.get("parallel").asBoolean());
        }

        // Performance settings
        if (node.has("performance")) {
            JsonNode perf = node.get("performance");
            
            if (perf.has("maxParallelThreads")) {
                config.setMaxParallelThreads(perf.get("maxParallelThreads").asInt());
            }
            if (perf.has("threads")) {
                config.setMaxParallelThreads(perf.get("threads").asInt());
            }
            if (perf.has("cacheSize")) {
                config.setCacheSize(perf.get("cacheSize").asInt());
            }
            if (perf.has("batchSize")) {
                config.setBatchSize(perf.get("batchSize").asInt());
            }
            if (perf.has("maxMemoryUsage")) {
                config.setMaxMemoryUsage(perf.get("maxMemoryUsage").asLong());
            }
        }

        // Top-level performance settings
        if (node.has("maxParallelThreads")) {
            config.setMaxParallelThreads(node.get("maxParallelThreads").asInt());
        }
        if (node.has("threads")) {
            config.setMaxParallelThreads(node.get("threads").asInt());
        }
        if (node.has("cacheSize")) {
            config.setCacheSize(node.get("cacheSize").asInt());
        }
        if (node.has("batchSize")) {
            config.setBatchSize(node.get("batchSize").asInt());
        }

        // Error handling
        if (node.has("errorHandling")) {
            JsonNode errors = node.get("errorHandling");
            
            if (errors.has("continueOnError")) {
                config.setContinueOnError(errors.get("continueOnError").asBoolean());
            }
            if (errors.has("maxRetries")) {
                config.setMaxRetries(errors.get("maxRetries").asInt());
            }
            if (errors.has("retryDelayMs")) {
                config.setRetryDelayMs(errors.get("retryDelayMs").asLong());
            }
        }

        // Top-level error handling
        if (node.has("continueOnError")) {
            config.setContinueOnError(node.get("continueOnError").asBoolean());
        }
        if (node.has("maxRetries")) {
            config.setMaxRetries(node.get("maxRetries").asInt());
        }
        if (node.has("retryDelayMs")) {
            config.setRetryDelayMs(node.get("retryDelayMs").asLong());
        }

        // Output settings
        if (node.has("output")) {
            JsonNode output = node.get("output");
            
            if (output.has("path")) {
                config.setOutputPath(output.get("path").asText());
            }
            if (output.has("overwrite")) {
                config.setOverwriteOutput(output.get("overwrite").asBoolean());
            }
            if (output.has("createBackup")) {
                config.setCreateBackup(output.get("createBackup").asBoolean());
            }
            if (output.has("encoding")) {
                config.setOutputEncoding(output.get("encoding").asText());
            }
        }

        // Output settings (top-level)
        if (node.has("overwriteOutput")) {
            config.setOverwriteOutput(node.get("overwriteOutput").asBoolean());
        }
        if (node.has("overwrite")) {
            config.setOverwriteOutput(node.get("overwrite").asBoolean());
        }
        if (node.has("createBackup")) {
            config.setCreateBackup(node.get("createBackup").asBoolean());
        }
        if (node.has("outputEncoding")) {
            config.setOutputEncoding(node.get("outputEncoding").asText());
        }

        // Logging and monitoring
        if (node.has("logging")) {
            JsonNode logging = node.get("logging");
            
            if (logging.has("level")) {
                config.setLogLevel(logging.get("level").asText());
            }
            if (logging.has("logLevel")) {
                config.setLogLevel(logging.get("logLevel").asText());
            }
        }

        if (node.has("logLevel")) {
            config.setLogLevel(node.get("logLevel").asText());
        }

        if (node.has("monitoring")) {
            JsonNode monitoring = node.get("monitoring");
            
            if (monitoring.has("enableMetrics")) {
                config.setEnableMetrics(monitoring.get("enableMetrics").asBoolean());
            }
            if (monitoring.has("metricsOutput")) {
                config.setMetricsOutput(monitoring.get("metricsOutput").asText());
            }
        }

        if (node.has("enableMetrics")) {
            config.setEnableMetrics(node.get("enableMetrics").asBoolean());
        }

        // Profile
        if (node.has("profile")) {
            try {
                String profileName = node.get("profile").asText();
                config.setProfile(TransformationConfiguration.Profile.fromString(profileName));
            } catch (IllegalArgumentException e) {
                logger.warn("Unknown profile in configuration: {}", node.get("profile").asText());
            }
        }

        // Custom properties
        if (node.has("customProperties")) {
            JsonNode custom = node.get("customProperties");
            custom.fields().forEachRemaining(entry -> {
                config.setCustomProperty(entry.getKey(), entry.getValue().asText());
            });
        }
    }

    /**
     * Load profile-specific configuration (e.g., config-prod.yaml).
     * Falls back to base configuration if profile-specific not found.
     */
    public boolean loadProfileConfiguration(String baseConfigFile, String profile, TransformationConfiguration config) {
        if (baseConfigFile == null || baseConfigFile.isEmpty()) {
            return true;
        }

        // Try profile-specific file first
        String profileConfigFile = getProfileConfigFileName(baseConfigFile, profile);
        if (new File(profileConfigFile).exists()) {
            logger.info("Loading profile-specific configuration: {}", profileConfigFile);
            return loadConfiguration(profileConfigFile, config);
        }

        // Fall back to base configuration
        logger.debug("Profile-specific configuration not found: {}. Using base configuration.", profileConfigFile);
        return loadConfiguration(baseConfigFile, config);
    }

    /**
     * Generate profile-specific configuration filename.
     * E.g., config.yaml → config-prod.yaml
     */
    private String getProfileConfigFileName(String baseFile, String profile) {
        int lastDot = baseFile.lastIndexOf('.');
        if (lastDot == -1) {
            return baseFile + "-" + profile;
        }
        return baseFile.substring(0, lastDot) + "-" + profile + baseFile.substring(lastDot);
    }

    /**
     * Validate that a configuration file is readable and properly formatted.
     */
    public boolean validateConfigurationFile(String configFile) {
        if (configFile == null || configFile.isEmpty()) {
            return true;
        }

        try {
            File file = new File(configFile);
            if (!file.exists()) {
                logger.error("Configuration file not found: {}", configFile);
                return false;
            }

            if (!Files.isReadable(Paths.get(configFile))) {
                logger.error("Configuration file is not readable: {}", configFile);
                return false;
            }

            // Try parsing to check validity
            if (configFile.endsWith(".yaml") || configFile.endsWith(".yml")) {
                yamlMapper.readTree(file);
            } else if (configFile.endsWith(".json")) {
                jsonMapper.readTree(file);
            } else {
                logger.warn("Unknown configuration file format: {}", configFile);
                return false;
            }

            logger.info("Configuration file is valid: {}", configFile);
            return true;
        } catch (IOException e) {
            logger.error("Invalid configuration file: {}", configFile, e);
            return false;
        }
    }

    /**
     * Generate a default YAML configuration template.
     */
    public static String generateYamlTemplate() {
        return "# QOF to IMQ Transformation Configuration\n" +
                "# This is a template configuration file in YAML format\n" +
                "\n" +
                "# Transformation settings\n" +
                "inputPath: /path/to/input.json\n" +
                "outputPath: /path/to/output.json\n" +
                "batch: false\n" +
                "pattern: \"*.json\"\n" +
                "verbose: false\n" +
                "parallel: false\n" +
                "\n" +
                "# Performance tuning\n" +
                "performance:\n" +
                "  maxParallelThreads: 8\n" +
                "  cacheSize: 10000\n" +
                "  batchSize: 100\n" +
                "\n" +
                "# Error handling\n" +
                "errorHandling:\n" +
                "  continueOnError: true\n" +
                "  maxRetries: 3\n" +
                "  retryDelayMs: 1000\n" +
                "\n" +
                "# Output configuration\n" +
                "output:\n" +
                "  overwrite: false\n" +
                "  createBackup: true\n" +
                "  encoding: UTF-8\n" +
                "\n" +
                "# Logging\n" +
                "logging:\n" +
                "  level: INFO\n" +
                "\n" +
                "# Profile: dev, prod, or test\n" +
                "profile: prod\n";
    }

    /**
     * Generate a default JSON configuration template.
     */
    public static String generateJsonTemplate() {
        return "{\n" +
                "  \"transformation\": {\n" +
                "    \"inputPath\": \"/path/to/input.json\",\n" +
                "    \"outputPath\": \"/path/to/output.json\",\n" +
                "    \"batchMode\": false,\n" +
                "    \"filePattern\": \"*.json\",\n" +
                "    \"verbose\": false,\n" +
                "    \"parallelProcessing\": false\n" +
                "  },\n" +
                "  \"performance\": {\n" +
                "    \"maxParallelThreads\": 8,\n" +
                "    \"cacheSize\": 10000,\n" +
                "    \"batchSize\": 100\n" +
                "  },\n" +
                "  \"errorHandling\": {\n" +
                "    \"continueOnError\": true,\n" +
                "    \"maxRetries\": 3,\n" +
                "    \"retryDelayMs\": 1000\n" +
                "  },\n" +
                "  \"output\": {\n" +
                "    \"overwrite\": false,\n" +
                "    \"createBackup\": true,\n" +
                "    \"encoding\": \"UTF-8\"\n" +
                "  },\n" +
                "  \"logging\": {\n" +
                "    \"level\": \"INFO\"\n" +
                "  },\n" +
                "  \"profile\": \"prod\"\n" +
                "}\n";
    }
}