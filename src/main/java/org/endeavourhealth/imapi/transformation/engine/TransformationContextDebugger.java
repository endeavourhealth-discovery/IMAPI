package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.transformation.core.TransformationContext;
import org.endeavourhealth.imapi.transformation.core.TransformationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * Debugging utilities for inspecting and reporting on TransformationContext state.
 *
 * Provides methods to:
 * - Generate detailed context reports
 * - Dump context state for debugging
 * - Inspect reference mappings
 * - Analyze error conditions
 * - Export context diagnostics
 */
public class TransformationContextDebugger {

  private static final Logger log = LoggerFactory.getLogger(TransformationContextDebugger.class);

  private final TransformationContext context;

  /**
   * Creates a debugger for the given context.
   *
   * @param context The context to debug
   */
  public TransformationContextDebugger(TransformationContext context) {
    this.context = context;
  }

  /**
   * Generates a detailed diagnostic report of the context state.
   *
   * @return A formatted string containing the diagnostic report
   */
  public String generateDiagnosticReport() {
    StringWriter sw = new StringWriter();
    PrintWriter writer = new PrintWriter(sw);

    writer.println("=== Transformation Context Diagnostic Report ===");
    writer.println();

    // Header information
    writer.println("Context Information:");
    writer.println("  Correlation ID: " + context.getCorrelationId());
    writer.println("  Target Query: " + context.getTargetQuery().getName());
    writer.println();

    // Error information
    reportErrors(writer);

    // Reference mappings
    reportReferenceMappings(writer);

    // Metadata
    reportMetadata(writer);

    writer.println("=== End of Diagnostic Report ===");
    writer.flush();

    return sw.toString();
  }

  /**
   * Reports on errors in the context.
   */
  private void reportErrors(PrintWriter writer) {
    List<TransformationError> errors = context.getErrors();

    writer.println("Errors (" + errors.size() + "):");
    if (errors.isEmpty()) {
      writer.println("  [No errors]");
    } else {
      for (int i = 0; i < errors.size(); i++) {
        TransformationError error = errors.get(i);
        writer.printf("  [%d] Phase: %s%n", i + 1, error.getPhase());
        writer.printf("      Message: %s%n", error.getMessage());
        if (error.getField() != null) {
          writer.printf("      Field: %s%n", error.getField());
        }
        if (error.getReason() != null && !error.getReason().isEmpty()) {
          writer.printf("      Reason: %s%n", error.getReason());
        }
      }
    }
    writer.println();
  }

  /**
   * Reports on reference mappings in the context.
   */
  private void reportReferenceMappings(PrintWriter writer) {
    writer.println("Reference Mappings:");
    // Access internal state through reflection or public methods
    // For now, just report the count
    writer.println("  Total mappings stored in context");
    writer.println();
  }

  /**
   * Reports on metadata in the context.
   */
  private void reportMetadata(PrintWriter writer) {
    writer.println("Metadata Storage:");
    writer.println("  Metadata is stored for internal context tracking");
    writer.println();
  }

  /**
   * Logs the full diagnostic report to the logger at DEBUG level.
   */
  public void logDiagnostics() {
    String report = generateDiagnosticReport();
    log.debug(report);
  }

  /**
   * Generates a summary of context state (one-liner).
   *
   * @return A brief summary string
   */
  public String generateSummary() {
    int errorCount = context.getErrors().size();
    return String.format(
        "Context[%s]: Query=%s, Errors=%d, HasErrors=%s",
        context.getCorrelationId().substring(0, 8),
        context.getTargetQuery().getName(),
        errorCount,
        context.hasErrors()
    );
  }

  /**
   * Checks if the context is in a valid state.
   *
   * A context is considered valid if:
   * - Target query is not null
   * - No errors have been recorded
   * - Correlation ID is not empty
   *
   * @return true if context is in valid state
   */
  public boolean isContextValid() {
    return context.getTargetQuery() != null &&
        !context.hasErrors() &&
        context.getCorrelationId() != null &&
        !context.getCorrelationId().isEmpty();
  }

  /**
   * Gets error statistics for the context.
   *
   * @return Map containing error counts by phase
   */
  public Map<String, Integer> getErrorStatistics() {
    Map<String, Integer> stats = new HashMap<>();
    List<TransformationError> errors = context.getErrors();

    for (TransformationError error : errors) {
      String phase = error.getPhase();
      stats.put(phase, stats.getOrDefault(phase, 0) + 1);
    }

    return stats;
  }

  /**
   * Generates a formatted error summary.
   *
   * @return Formatted string with error details
   */
  public String generateErrorSummary() {
    List<TransformationError> errors = context.getErrors();

    if (errors.isEmpty()) {
      return "No errors detected";
    }

    Map<String, Integer> stats = getErrorStatistics();
    StringWriter sw = new StringWriter();
    PrintWriter writer = new PrintWriter(sw);

    writer.println("Error Summary:");
    writer.printf("  Total Errors: %d%n", errors.size());
    writer.println("  Errors by Phase:");

    for (Map.Entry<String, Integer> entry : stats.entrySet()) {
      writer.printf("    - %s: %d%n", entry.getKey(), entry.getValue());
    }

    writer.println("  Error Details:");
    for (int i = 0; i < Math.min(errors.size(), 10); i++) {
      TransformationError error = errors.get(i);
      writer.printf("    [%d] [%s] %s%n", i + 1, error.getPhase(), error.getMessage());
    }

    if (errors.size() > 10) {
      writer.printf("    ... and %d more errors%n", errors.size() - 10);
    }

    writer.flush();
    return sw.toString();
  }

  /**
   * Exports context state as a structured map for logging/reporting.
   *
   * @return Map containing context state information
   */
  public Map<String, Object> exportContextState() {
    Map<String, Object> state = new LinkedHashMap<>();

    state.put("correlationId", context.getCorrelationId());
    state.put("queryName", context.getTargetQuery().getName());
    state.put("hasErrors", context.hasErrors());
    state.put("errorCount", context.getErrors().size());
    state.put("errorStatistics", getErrorStatistics());

    List<Map<String, String>> errorDetails = new ArrayList<>();
    for (TransformationError error : context.getErrors()) {
      Map<String, String> errorMap = new LinkedHashMap<>();
      errorMap.put("phase", error.getPhase());
      errorMap.put("message", error.getMessage());
      if (error.getField() != null) {
        errorMap.put("field", error.getField());
      }
      if (error.getReason() != null) {
        errorMap.put("reason", error.getReason());
      }
      errorDetails.add(errorMap);
    }
    state.put("errors", errorDetails);

    return state;
  }

  /**
   * Generates a human-readable dump of the entire context state.
   *
   * @return A formatted multi-line string with complete context information
   */
  public String dumpContextState() {
    StringWriter sw = new StringWriter();
    PrintWriter writer = new PrintWriter(sw);

    writer.println();
    writer.println("╔════════════════════════════════════════════════════════════╗");
    writer.println("║       TRANSFORMATION CONTEXT STATE DUMP                    ║");
    writer.println("╚════════════════════════════════════════════════════════════╝");
    writer.println();

    writer.println("📋 CONTEXT METADATA:");
    writer.printf("   Correlation ID:  %s%n", context.getCorrelationId());
    writer.printf("   Query Name:      %s%n", context.getTargetQuery().getName());
    writer.println();

    writer.println("❌ ERROR INFORMATION:");
    List<TransformationError> errors = context.getErrors();
    writer.printf("   Total Errors:    %d%n", errors.size());
    if (!errors.isEmpty()) {
      writer.println("   Error Details:");
      for (int i = 0; i < Math.min(errors.size(), 5); i++) {
        TransformationError error = errors.get(i);
        writer.printf("     [%d] %s: %s%n",
            i + 1,
            error.getPhase(),
            error.getMessage()
        );
      }
      if (errors.size() > 5) {
        writer.printf("     ... and %d more errors%n", errors.size() - 5);
      }
    }
    writer.println();

    writer.println("✓ CONTEXT STATUS:");
    writer.printf("   Valid State:     %s%n", isContextValid() ? "YES" : "NO");
    writer.println();

    writer.println("╔════════════════════════════════════════════════════════════╗");
    writer.println();

    writer.flush();
    return sw.toString();
  }
}