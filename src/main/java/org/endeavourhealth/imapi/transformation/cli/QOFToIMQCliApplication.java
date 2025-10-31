package org.endeavourhealth.imapi.transformation.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.transformation.engine.QOFToIMQTransformer;
import org.endeavourhealth.imapi.transformation.batch.BatchTransformationProcessor;
import org.endeavourhealth.imapi.transformation.batch.BatchTransformationReport;
import org.endeavourhealth.imapi.transformation.batch.ConsoleProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Command-Line Interface for QOF to IMQ transformation.
 * 
 * Supports both single file and batch directory transformations with configurable
 * output paths, verbose logging, and error resilience.
 * 
 * Usage Examples:
 * - Single file: java -jar qofimq-cli.jar --input=/path/to/qof.json --output=/path/to/imq.json
 * - Batch mode: java -jar qofimq-cli.jar --batch --input=/path/to/dir --output=/path/to/output-dir
 * - With verbose logging: java -jar qofimq-cli.jar --input=/path/to/qof.json --output=/path/to/imq.json --verbose
 * - Help: java -jar qofimq-cli.jar --help
 */
@SpringBootApplication
public class QOFToIMQCliApplication implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(QOFToIMQCliApplication.class);

    public static void main(String[] args) {
        try {
            int exitCode = SpringApplication.exit(SpringApplication.run(QOFToIMQCliApplication.class, args));
            System.exit(exitCode);
        } catch (Exception e) {
            logger.error("Fatal error in CLI application", e);
            System.exit(1);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            // Check for help flag first
            if (args.containsOption("help") || args.containsOption("h")) {
                printHelp();
                System.exit(0);
            }

            // Parse command-line arguments
            CliArgumentsParser parser = new CliArgumentsParser(args);
            CliConfiguration config = parser.parse();

            // Validate configuration
            if (!config.isValid()) {
                System.err.println("Error: Invalid configuration");
                printUsageError(config.getValidationErrors());
                System.exit(1);
            }

            // Enable verbose logging if requested
            if (config.isVerbose()) {
                logger.info("Verbose logging enabled");
                logger.debug("Configuration: {}", config);
            }

            // Execute transformation
            int exitCode = executeTransformation(config);
            System.exit(exitCode);

        } catch (Exception e) {
            logger.error("Error during CLI execution", e);
            System.exit(1);
        }
    }

    /**
     * Executes the appropriate transformation based on configuration.
     * Returns exit code: 0 for success, 1 for error
     */
    private int executeTransformation(CliConfiguration config) {
        try {
            if (config.isBatchMode()) {
                return executeBatchTransformation(config);
            } else {
                return executeSingleFileTransformation(config);
            }
        } catch (Exception e) {
            logger.error("Transformation failed", e);
            return 1;
        }
    }

    /**
     * Executes single file transformation
     */
    private int executeSingleFileTransformation(CliConfiguration config) throws IOException {
        Path inputPath = Paths.get(config.getInputPath());
        Path outputPath = Paths.get(config.getOutputPath());

        logger.info("Starting single file transformation");
        logger.info("Input: {}", inputPath.toAbsolutePath());
        logger.info("Output: {}", outputPath.toAbsolutePath());

        try {
            QOFToIMQTransformer transformer = new QOFToIMQTransformer();
            
            // Perform transformation from file
            Query resultQuery = transformer.transformFromFile(inputPath.toAbsolutePath().toString());
            
            // Write result to output file
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputPath.toFile(), resultQuery);
            
            logger.info("Transformation completed successfully");
            logger.info("Output written to: {}", outputPath.toAbsolutePath());
            return 0;

        } catch (Exception e) {
            logger.error("Single file transformation failed: {}", e.getMessage(), e);
            return 1;
        }
    }

    /**
     * Executes batch transformation of multiple files
     */
    private int executeBatchTransformation(CliConfiguration config) throws IOException {
        Path inputDir = Paths.get(config.getInputPath());
        Path outputDir = Paths.get(config.getOutputPath());
        String filePattern = config.getFilePattern();

        logger.info("Starting batch transformation");
        logger.info("Input directory: {}", inputDir.toAbsolutePath());
        logger.info("Output directory: {}", outputDir.toAbsolutePath());
        logger.info("File pattern: {}", filePattern);

        try {
            // Define transformation function that handles both transformation and output writing
            ObjectMapper mapper = new ObjectMapper();
            BatchTransformationProcessor.FileTransformer transformer = filePath -> {
                try {
                    QOFToIMQTransformer qofTransformer = new QOFToIMQTransformer();
                    Query resultQuery = qofTransformer.transformFromFile(filePath);
                    
                    // Generate output filename from input filename
                    String inputFilename = new java.io.File(filePath).getName();
                    String outputFilename = inputFilename.replaceAll("\\..*$", "") + "_query.json";
                    Path outputFilePath = outputDir.resolve(outputFilename);
                    
                    // Write result to output file
                    mapper.writerWithDefaultPrettyPrinter().writeValue(outputFilePath.toFile(), resultQuery);
                    
                    return resultQuery;
                } catch (Exception e) {
                    throw new RuntimeException("Transformation failed for " + filePath, e);
                }
            };

            // Create batch processor with transformation function
            BatchTransformationProcessor processor = new BatchTransformationProcessor(transformer);

            // Add progress listener for console output
            processor.addProgressListener(new ConsoleProgressListener());

            // Execute batch processing
            boolean useParallelProcessing = config.isParallelProcessing();
            BatchTransformationReport report = processor.processBatch(
                inputDir, 
                outputDir, 
                filePattern, 
                useParallelProcessing
            );

            // Print report
            printBatchReport(report);

            // Return success if all transformations succeeded
            return report.getTotalSuccessful() > 0 && report.getTotalFailed() == 0 ? 0 : 1;

        } catch (Exception e) {
            logger.error("Batch transformation failed: {}", e.getMessage(), e);
            return 1;
        }
    }

    /**
     * Prints the batch transformation report
     */
    private void printBatchReport(BatchTransformationReport report) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("BATCH TRANSFORMATION REPORT");
        System.out.println("=".repeat(60));
        System.out.println(report.getSummary());
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Prints help message
     */
    private void printHelp() {
        System.out.println("""
            
            QOF to IMQ Transformation CLI
            ==============================
            
            Usage:
              java -jar qofimq-cli.jar [OPTIONS]
            
            Options:
              --input=<path>           Path to input QOF JSON file or directory (batch mode)
              --output=<path>          Path to output IMQ JSON file or directory (batch mode)
              --batch                  Enable batch mode for directory processing
              --pattern=<pattern>      Glob pattern for batch file selection (default: *.json)
              --verbose, -v            Enable verbose logging output
              --config=<path>          Path to configuration file (JSON format)
              --help, -h               Display this help message
            
            Exit Codes:
              0                        Transformation completed successfully
              1                        Transformation failed
            
            Examples:
            
              # Single file transformation:
              java -jar qofimq-cli.jar \\
                --input=/data/qof-document.json \\
                --output=/data/imq-query.json
            
              # Batch transformation with verbose logging:
              java -jar qofimq-cli.jar \\
                --batch \\
                --input=/data/qof-documents \\
                --output=/data/imq-queries \\
                --verbose
            
              # Batch with custom file pattern:
              java -jar qofimq-cli.jar \\
                --batch \\
                --input=/data/qof-documents \\
                --output=/data/imq-queries \\
                --pattern="qof-*.json"
            
              # Using configuration file:
              java -jar qofimq-cli.jar \\
                --config=/etc/qofimq.json
            
            Configuration File Format (JSON):
            {
              "input": "/path/to/input",
              "output": "/path/to/output",
              "batch": true,
              "pattern": "*.json",
              "verbose": true,
              "parallelProcessing": false
            }
            
            For more information, visit: https://github.com/endeavourhealth-discovery/IMAPI
            """);
    }

    /**
     * Prints usage error message
     */
    private void printUsageError(java.util.List<String> errors) {
        System.err.println("Validation errors:");
        for (String error : errors) {
            System.err.println("  - " + error);
        }
        System.err.println("\nUse --help for usage information");
    }
}