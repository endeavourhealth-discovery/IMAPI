package org.endeavourhealth.imapi.transformation.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Parses command-line arguments for the QOF to IMQ transformation CLI.
 * 
 * Supports:
 * - Direct command-line parameters (e.g., --input=/path)
 * - Configuration file loading (e.g., --config=/path/to/config.json)
 * - Parameter validation and error reporting
 */
public class CliArgumentsParser {

    private static final Logger logger = LoggerFactory.getLogger(CliArgumentsParser.class);

    private final ApplicationArguments args;
    private final List<String> errors = new ArrayList<>();

    public CliArgumentsParser(ApplicationArguments args) {
        this.args = args;
    }

    /**
     * Parses command-line arguments and returns a validated CliConfiguration.
     */
    public CliConfiguration parse() {
        CliConfiguration config = new CliConfiguration();

        try {
            // First, check if configuration file is provided
            if (args.containsOption("config")) {
                loadConfigurationFromFile(config);
            }

            // Command-line arguments override config file values
            parseDirectArguments(config);

            // Validate the configuration
            validateConfiguration(config);

        } catch (IOException e) {
            logger.error("Error parsing configuration", e);
            errors.add("Configuration loading failed: " + e.getMessage());
        }

        config.setValidationErrors(errors);
        return config;
    }

    /**
     * Loads configuration from a JSON file
     */
    @SuppressWarnings("unchecked")
    private void loadConfigurationFromFile(CliConfiguration config) throws IOException {
        List<String> configPaths = args.getOptionValues("config");
        if (configPaths == null || configPaths.isEmpty()) {
            errors.add("Configuration file path not specified");
            return;
        }

        String configPath = configPaths.get(0);
        Path filePath = Paths.get(configPath);

        if (!Files.exists(filePath)) {
            errors.add("Configuration file not found: " + configPath);
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> configMap = mapper.readValue(filePath.toFile(), Map.class);
            
            // Apply configuration values
            if (configMap.containsKey("input")) {
                config.setInputPath((String) configMap.get("input"));
            }
            if (configMap.containsKey("output")) {
                config.setOutputPath((String) configMap.get("output"));
            }
            if (configMap.containsKey("batch")) {
                config.setBatchMode((Boolean) configMap.get("batch"));
            }
            if (configMap.containsKey("pattern")) {
                config.setFilePattern((String) configMap.get("pattern"));
            }
            if (configMap.containsKey("verbose")) {
                config.setVerbose((Boolean) configMap.get("verbose"));
            }
            if (configMap.containsKey("parallelProcessing")) {
                config.setParallelProcessing((Boolean) configMap.get("parallelProcessing"));
            }

            logger.info("Configuration loaded from: {}", filePath.toAbsolutePath());

        } catch (IOException e) {
            errors.add("Failed to parse configuration file: " + e.getMessage());
        }
    }

    /**
     * Parses direct command-line arguments
     */
    private void parseDirectArguments(CliConfiguration config) {
        // Parse input path
        if (args.containsOption("input")) {
            List<String> inputs = args.getOptionValues("input");
            if (inputs != null && !inputs.isEmpty()) {
                config.setInputPath(inputs.get(0));
            }
        }

        // Parse output path
        if (args.containsOption("output")) {
            List<String> outputs = args.getOptionValues("output");
            if (outputs != null && !outputs.isEmpty()) {
                config.setOutputPath(outputs.get(0));
            }
        }

        // Parse batch flag
        if (args.containsOption("batch")) {
            config.setBatchMode(true);
        }

        // Parse file pattern
        if (args.containsOption("pattern")) {
            List<String> patterns = args.getOptionValues("pattern");
            if (patterns != null && !patterns.isEmpty()) {
                config.setFilePattern(patterns.get(0));
            }
        }

        // Parse verbose flag
        if (args.containsOption("verbose") || args.containsOption("v")) {
            config.setVerbose(true);
        }

        // Parse parallel processing flag
        if (args.containsOption("parallel")) {
            config.setParallelProcessing(true);
        }
    }

    /**
     * Validates the configuration and adds errors to the errors list
     */
    private void validateConfiguration(CliConfiguration config) {
        // Validate input path is provided
        if (config.getInputPath() == null || config.getInputPath().isEmpty()) {
            errors.add("Input path is required (use --input=/path or --config=/path/to/config.json)");
            return;
        }

        // Validate output path is provided
        if (config.getOutputPath() == null || config.getOutputPath().isEmpty()) {
            errors.add("Output path is required (use --output=/path or --config=/path/to/config.json)");
            return;
        }

        // Validate input path exists
        Path inputPath = Paths.get(config.getInputPath());
        if (!Files.exists(inputPath)) {
            errors.add("Input path does not exist: " + config.getInputPath());
            return;
        }

        // Validate input path type matches batch mode
        if (config.isBatchMode()) {
            if (!Files.isDirectory(inputPath)) {
                errors.add("In batch mode, input must be a directory: " + config.getInputPath());
            }
        } else {
            if (Files.isDirectory(inputPath)) {
                errors.add("In single-file mode, input must be a file: " + config.getInputPath());
            } else if (!inputPath.getFileName().toString().endsWith(".json")) {
                errors.add("Input file must be a JSON file: " + config.getInputPath());
            }
        }

        // Validate output path for batch mode (create parent directory if needed)
        Path outputPath = Paths.get(config.getOutputPath());
        if (config.isBatchMode()) {
            // For batch mode, output should be a directory (or will be created)
            try {
                if (!Files.exists(outputPath)) {
                    Files.createDirectories(outputPath);
                    logger.debug("Created output directory: {}", outputPath.toAbsolutePath());
                }
            } catch (IOException e) {
                errors.add("Cannot create output directory: " + e.getMessage());
            }
        } else {
            // For single file mode, ensure parent directory exists
            Path parentDir = outputPath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                try {
                    Files.createDirectories(parentDir);
                    logger.debug("Created parent directory: {}", parentDir.toAbsolutePath());
                } catch (IOException e) {
                    errors.add("Cannot create output parent directory: " + e.getMessage());
                }
            }
        }
    }
}