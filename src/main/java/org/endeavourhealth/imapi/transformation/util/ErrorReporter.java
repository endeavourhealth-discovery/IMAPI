package org.endeavourhealth.imapi.transformation.util;

import org.endeavourhealth.imapi.transformation.core.TransformationError;
import org.endeavourhealth.imapi.transformation.core.TransformationErrorCollector;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Generates formatted error reports from transformation errors.
 * Provides multiple output formats for different use cases.
 */
public class ErrorReporter {

  /**
   * Generates a detailed text report of all errors.
   *
   * @param errors List of errors to report
   * @return Formatted error report
   */
  public static String generateDetailedReport(List<TransformationError> errors) {
    StringBuilder sb = new StringBuilder();
    sb.append("=== TRANSFORMATION ERROR REPORT ===\n");
    sb.append("Total Errors: ").append(errors.size()).append("\n");
    sb.append("Generated: ").append(java.time.Instant.now()).append("\n\n");

    for (TransformationError error : errors) {
      sb.append("---\n");
      sb.append("Error ID: ").append(error.getErrorId()).append("\n");
      sb.append("Message: ").append(error.getMessage()).append("\n");
      sb.append("Phase: ").append(error.getPhase()).append("\n");

      if (error.getField() != null) {
        sb.append("Field: ").append(error.getField()).append("\n");
      }

      if (error.getReason() != null) {
        sb.append("Reason: ").append(error.getReason()).append("\n");
      }

      if (error.getCorrelationId() != null) {
        sb.append("Correlation ID: ").append(error.getCorrelationId()).append("\n");
      }

      if (error.getCause() != null) {
        sb.append("Cause:\n");
        sb.append(getStackTrace(error.getCause())).append("\n");
      }
    }

    sb.append("===\n");
    return sb.toString();
  }

  /**
   * Generates a summary report grouped by phase.
   *
   * @param errorCollector The error collector
   * @return Summary report
   */
  public static String generateSummaryReport(TransformationErrorCollector errorCollector) {
    return errorCollector.generateReport();
  }

  /**
   * Generates a CSV-formatted error report.
   *
   * @param errors List of errors to report
   * @return CSV formatted report
   */
  public static String generateCsvReport(List<TransformationError> errors) {
    StringBuilder sb = new StringBuilder();
    sb.append("Error ID,Message,Phase,Field,Reason,Correlation ID\n");

    for (TransformationError error : errors) {
      sb.append(escapeCSV(error.getErrorId())).append(",");
      sb.append(escapeCSV(error.getMessage())).append(",");
      sb.append(escapeCSV(error.getPhase())).append(",");
      sb.append(escapeCSV(error.getField() != null ? error.getField() : "")).append(",");
      sb.append(escapeCSV(error.getReason() != null ? error.getReason() : "")).append(",");
      sb.append(escapeCSV(error.getCorrelationId() != null ? error.getCorrelationId() : ""));
      sb.append("\n");
    }

    return sb.toString();
  }

  /**
   * Gets stack trace from a throwable.
   *
   * @param throwable The exception
   * @return Stack trace as string
   */
  private static String getStackTrace(Throwable throwable) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    throwable.printStackTrace(pw);
    return sw.toString();
  }

  /**
   * Escapes a value for CSV format.
   */
  private static String escapeCSV(String value) {
    if (value == null) {
      return "";
    }
    if (value.contains(",") || value.contains("\"")) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
  }
}