package org.endeavourhealth.imapi.transformation.cli;

import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.transformation.engine.QOFToIMQTransformer;
import org.endeavourhealth.imapi.transformation.output.QOFDocumentLoader;
import org.endeavourhealth.imapi.transformation.output.QueryOutputWriter;
import org.endeavourhealth.imapi.transformation.output.TransformationReport;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Command-line application for QOF to IMQuery transformation.
 * Entry point for batch and single-file transformations.
 */
public class QOFToIMQCliApplication {

  private final CliArgumentsParser parser;
  private final QOFToIMQTransformer transformer;
  private final boolean verbose;

  public QOFToIMQCliApplication(String[] args) {
    this.parser = new CliArgumentsParser(args);
    this.transformer = new QOFToIMQTransformer();
    this.verbose = parser.isVerbose();
  }

  public static void main(String[] args) {
    try {
      if (args.length == 0 || isHelpRequested(args)) {
        showHelp();
        System.exit(0);
      }

      QOFToIMQCliApplication app = new QOFToIMQCliApplication(args);
      int exitCode = app.run();
      System.exit(exitCode);

    } catch (Exception e) {
      System.err.println("ERROR: " + e.getMessage());
      if (args.length > 0 && (contains(args, "-v") || contains(args, "--verbose"))) {
        e.printStackTrace();
      }
      System.exit(1);
    }
  }

  public int run() throws Exception {
    // Validate arguments
    if (!parser.isValid()) {
      System.err.println("ERROR: " + parser.getValidationErrorMessage());
      parser.printHelp();
      return 1;
    }

    String input = parser.getInput();
    String output = parser.getOutput();
    String format = parser.getFormat();
    String overwriteStr = parser.getOverwriteStrategy();

    if (verbose) {
      System.out.println("=== QOF to IMQuery Transformation ===");
      System.out.println("Input: " + input);
      System.out.println("Output: " + output);
      System.out.println("Format: " + format);
      System.out.println("Overwrite Strategy: " + overwriteStr);
      System.out.println();
    }

    QueryOutputWriter.OverwriteStrategy strategy = parseOverwriteStrategy(overwriteStr);

    // Check if input is file or directory
    File inputFile = new File(input);

    if (inputFile.isFile()) {
      return transformSingleFile(inputFile, output, format, strategy);
    } else if (inputFile.isDirectory()) {
      return transformDirectory(inputFile, output, format, strategy);
    } else {
      System.err.println("ERROR: Input path does not exist: " + input);
      return 1;
    }
  }

  private int transformSingleFile(File inputFile, String outputDir, String format, 
                                   QueryOutputWriter.OverwriteStrategy strategy) throws Exception {
    if (verbose) {
      System.out.println("Processing file: " + inputFile.getAbsolutePath());
    }

    TransformationReport report = new TransformationReport();
    report.setSourceFile(inputFile.getAbsolutePath());

    try {
      // Load QOF document
      long loadStart = System.currentTimeMillis();
      QOFDocument qofDoc = QOFDocumentLoader.loadFromFile(inputFile);
      long loadTime = System.currentTimeMillis() - loadStart;
      report.addComponentTime("Load", loadTime);

      if (verbose) {
        System.out.println("Loaded QOF document: " + qofDoc.getName());
      }

      // Transform
      long transformStart = System.currentTimeMillis();
      Map<String, Query> queries = transformer.transform(qofDoc);
      long transformTime = System.currentTimeMillis() - transformStart;
      report.addComponentTime("Transform", transformTime);
      report.setQueriesGenerated(queries.size());

      if (verbose) {
        System.out.println("Generated " + queries.size() + " queries");
      }

      // Write output
      long writeStart = System.currentTimeMillis();
      if ("single".equalsIgnoreCase(format)) {
        String outputPath = Paths.get(outputDir, "queries-" + System.currentTimeMillis() + ".json").toString();
        QueryOutputWriter.writeQueriesCombined(queries, outputPath, strategy);
        System.out.println("Output written to: " + outputPath);
      } else {
        QueryOutputWriter.writeQueries(queries, outputDir, strategy);
        System.out.println("Output written to directory: " + outputDir);
      }
      long writeTime = System.currentTimeMillis() - writeStart;
      report.addComponentTime("Write", writeTime);

      report.markComplete();

      if (verbose) {
        System.out.println(report.generateReport());
      } else {
        System.out.println("Transformation completed successfully");
      }

      return 0;

    } catch (Exception e) {
      report.markFailed(e.getMessage());
      System.err.println(report.generateReport());
      throw e;
    }
  }

  private int transformDirectory(File inputDir, String outputDir, String format,
                                 QueryOutputWriter.OverwriteStrategy strategy) throws Exception {
    if (verbose) {
      System.out.println("Processing directory: " + inputDir.getAbsolutePath());
    }

    File[] jsonFiles = inputDir.listFiles((d, name) -> name.endsWith(".json"));
    if (jsonFiles == null || jsonFiles.length == 0) {
      System.err.println("ERROR: No JSON files found in directory: " + inputDir);
      return 1;
    }

    if (verbose) {
      System.out.println("Found " + jsonFiles.length + " JSON files");
    }

    int successCount = 0;
    int failureCount = 0;
    Map<String, Query> allQueries = new LinkedHashMap<>();

    for (File file : jsonFiles) {
      try {
        if (verbose) {
          System.out.println("Processing: " + file.getName());
        }

        QOFDocument qofDoc = QOFDocumentLoader.loadFromFile(file);
        Map<String, Query> queries = transformer.transform(qofDoc);
        allQueries.putAll(queries);
        successCount++;

        if (verbose) {
          System.out.println("  ✓ Generated " + queries.size() + " queries");
        }

      } catch (Exception e) {
        failureCount++;
        System.err.println("ERROR processing " + file.getName() + ": " + e.getMessage());
      }
    }

    // Write consolidated output
    if (!allQueries.isEmpty()) {
      if ("single".equalsIgnoreCase(format)) {
        String outputPath = Paths.get(outputDir, "all-queries.json").toString();
        QueryOutputWriter.writeQueriesCombined(allQueries, outputPath, strategy);
        System.out.println("All queries written to: " + outputPath);
      } else {
        QueryOutputWriter.writeQueries(allQueries, outputDir, strategy);
        System.out.println("All queries written to directory: " + outputDir);
      }
    }

    System.out.println("\n=== Summary ===");
    System.out.println("Successful: " + successCount);
    System.out.println("Failed: " + failureCount);
    System.out.println("Total Queries: " + allQueries.size());

    return failureCount == 0 ? 0 : 1;
  }

  private QueryOutputWriter.OverwriteStrategy parseOverwriteStrategy(String str) {
    return switch (str.toLowerCase()) {
      case "deny" -> QueryOutputWriter.OverwriteStrategy.DENY;
      case "backup" -> QueryOutputWriter.OverwriteStrategy.BACKUP;
      default -> QueryOutputWriter.OverwriteStrategy.ALLOW;
    };
  }

  private static void showHelp() {
    CliArgumentsParser parser = new CliArgumentsParser(new String[]{});
    parser.printHelp();
  }

  private static boolean isHelpRequested(String[] args) {
    for (String arg : args) {
      if (arg.equals("--help") || arg.equals("-h")) {
        return true;
      }
    }
    return false;
  }

  private static boolean contains(String[] args, String value) {
    for (String arg : args) {
      if (arg.equals(value)) return true;
    }
    return false;
  }
}