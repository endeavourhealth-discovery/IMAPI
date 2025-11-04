package org.endeavourhealth.imapi.transformation.cli;

import java.util.*;

/**
 * Parses command-line arguments for QOF to IMQuery transformation.
 * Supports:
 * --input <file|directory>  - Input QOF JSON file or directory
 * --output <directory>      - Output directory for generated queries
 * --config <file>           - Configuration file (YAML/JSON)
 * --format <single|batch>   - Output format (single combined file or batch)
 * --overwrite <allow|deny|backup> - File overwrite strategy
 * --verbose                 - Verbose output
 */
public class CliArgumentsParser {
  private final Map<String, String> arguments = new HashMap<>();
  private final List<String> positionalArgs = new ArrayList<>();

  public CliArgumentsParser(String[] args) {
    parseArguments(args);
  }

  private void parseArguments(String[] args) {
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      
      if (arg.startsWith("--")) {
        String key = arg.substring(2);
        String value = null;
        
        if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
          value = args[++i];
        }
        
        arguments.put(key, value);
      } else if (arg.startsWith("-")) {
        // Single char flag
        String key = arg.substring(1);
        arguments.put(key, "true");
      } else {
        positionalArgs.add(arg);
      }
    }
  }

  public String getArgument(String key) {
    return arguments.get(key);
  }

  public String getArgument(String key, String defaultValue) {
    return arguments.getOrDefault(key, defaultValue);
  }

  public boolean hasArgument(String key) {
    return arguments.containsKey(key);
  }

  public String getInput() {
    return getArgument("input");
  }

  public String getOutput() {
    return getArgument("output", "./output");
  }

  public String getConfig() {
    return getArgument("config");
  }

  public String getFormat() {
    return getArgument("format", "batch");
  }

  public String getOverwriteStrategy() {
    return getArgument("overwrite", "allow");
  }

  public boolean isVerbose() {
    return hasArgument("verbose") || hasArgument("v");
  }

  public boolean isValid() {
    return getInput() != null && !getInput().isEmpty();
  }

  public String getValidationErrorMessage() {
    if (getInput() == null || getInput().isEmpty()) {
      return "Input file or directory is required (--input <path>)";
    }
    return null;
  }

  public void printHelp() {
    System.out.println("QOF to IMQuery Transformer - Command Line Interface");
    System.out.println();
    System.out.println("Usage: java -cp imapi.jar org.endeavourhealth.imapi.transformation.cli.QOFToIMQCliApplication [options]");
    System.out.println();
    System.out.println("Options:");
    System.out.println("  --input <file|directory>        Input QOF JSON file or directory (required)");
    System.out.println("  --output <directory>            Output directory for queries (default: ./output)");
    System.out.println("  --config <file>                 Configuration file");
    System.out.println("  --format <format>               Output format: single|batch (default: batch)");
    System.out.println("  --overwrite <strategy>          Overwrite strategy: allow|deny|backup (default: allow)");
    System.out.println("  --verbose, -v                   Verbose output");
    System.out.println("  --help, -h                      Show this help message");
    System.out.println();
    System.out.println("Examples:");
    System.out.println("  java -cp imapi.jar org.endeavourhealth.imapi.transformation.cli.QOFToIMQCliApplication \\");
    System.out.println("    --input QOF-Diabetes.json --output ./output");
    System.out.println();
    System.out.println("  java -cp imapi.jar org.endeavourhealth.imapi.transformation.cli.QOFToIMQCliApplication \\");
    System.out.println("    --input ./qof-files --output ./queries --format single");
  }

  public Map<String, String> getAllArguments() {
    return new HashMap<>(arguments);
  }

  public List<String> getPositionalArgs() {
    return new ArrayList<>(positionalArgs);
  }
}