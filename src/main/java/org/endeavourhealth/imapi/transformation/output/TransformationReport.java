package org.endeavourhealth.imapi.transformation.output;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Generates reports for transformation executions.
 * Includes statistics, errors, warnings, and performance metrics.
 */
public class TransformationReport {
  private final LocalDateTime startTime;
  private LocalDateTime endTime;
  private final Map<String, Long> componentTimes = new LinkedHashMap<>();
  private final List<String> errors = new ArrayList<>();
  private final List<String> warnings = new ArrayList<>();
  private int queriesGenerated = 0;
  private String sourceFile;
  private String status = "IN_PROGRESS";

  public TransformationReport() {
    this.startTime = LocalDateTime.now();
  }

  public void markComplete() {
    this.endTime = LocalDateTime.now();
    this.status = "COMPLETE";
  }

  public void markFailed(String reason) {
    this.endTime = LocalDateTime.now();
    this.status = "FAILED";
    addError(reason);
  }

  public void addComponentTime(String component, long milliseconds) {
    componentTimes.put(component, milliseconds);
  }

  public void addError(String error) {
    errors.add(error);
  }

  public void addWarning(String warning) {
    warnings.add(warning);
  }

  public void setQueriesGenerated(int count) {
    this.queriesGenerated = count;
  }

  public void setSourceFile(String file) {
    this.sourceFile = file;
  }

  public String generateReport() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("=== QOF to IMQuery Transformation Report ===\n");
    sb.append("Status: ").append(status).append("\n");
    sb.append("Start Time: ").append(startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n");
    
    if (endTime != null) {
      sb.append("End Time: ").append(endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n");
      long duration = java.time.temporal.ChronoUnit.MILLIS.between(startTime, endTime);
      sb.append("Duration: ").append(duration).append("ms\n");
    }
    
    if (sourceFile != null) {
      sb.append("Source File: ").append(sourceFile).append("\n");
    }
    
    sb.append("Queries Generated: ").append(queriesGenerated).append("\n");
    
    if (!componentTimes.isEmpty()) {
      sb.append("\nComponent Times:\n");
      for (Map.Entry<String, Long> entry : componentTimes.entrySet()) {
        sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("ms\n");
      }
    }
    
    if (!errors.isEmpty()) {
      sb.append("\nErrors (").append(errors.size()).append("):\n");
      for (String error : errors) {
        sb.append("  ERROR: ").append(error).append("\n");
      }
    }
    
    if (!warnings.isEmpty()) {
      sb.append("\nWarnings (").append(warnings.size()).append("):\n");
      for (String warning : warnings) {
        sb.append("  WARNING: ").append(warning).append("\n");
      }
    }
    
    sb.append("\n=== End Report ===\n");
    return sb.toString();
  }

  public boolean isSuccessful() {
    return "COMPLETE".equals(status) && errors.isEmpty();
  }

  public int getErrorCount() {
    return errors.size();
  }

  public int getWarningCount() {
    return warnings.size();
  }
}