package org.endeavourhealth.imapi.transformation.util;

import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Provides default value handling and initialization for QOF documents
 * and their components. Ensures safe access to potentially null fields.
 */
public class QOFDocumentDefaults {

  private static final Logger log = LoggerFactory.getLogger(QOFDocumentDefaults.class);
  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * Initializes a QOFDocument with safe defaults for null collections.
   *
   * @param document The document to initialize
   */
  public static void initializeSafeDefaults(QOFDocument document) {
    if (document == null) {
      return;
    }

    if (document.getSelections() == null) {
      document.setSelections(new ArrayList<>());
    }

    if (document.getRegisters() == null) {
      document.setRegisters(new ArrayList<>());
    }

    if (document.getExtractionFields() == null) {
      document.setExtractionFields(new ArrayList<>());
    }

    if (document.getIndicators() == null) {
      document.setIndicators(new ArrayList<>());
    }

    log.debug("Initialized safe defaults for QOFDocument: {}", document.getName());
  }

  /**
   * Gets the document name, using a generated default if not set.
   *
   * @param document The document
   * @return The document name
   */
  public static String getSafeName(QOFDocument document) {
    if (document == null) {
      return generateDefaultName("UnknownDocument");
    }

    if (document.getName() == null || document.getName().trim().isEmpty()) {
      String defaultName = generateDefaultName("QOFDocument");
      log.debug("Generated default name for QOFDocument: {}", defaultName);
      return defaultName;
    }

    return document.getName();
  }

  /**
   * Generates a default name with timestamp.
   *
   * @param prefix The name prefix
   * @return Generated name with timestamp
   */
  public static String generateDefaultName(String prefix) {
    return prefix + "_" + TIMESTAMP_FORMATTER.format(LocalDateTime.now())
        .replace(" ", "T")
        .replace(":", "-");
  }

  /**
   * Gets the safe selection count for a document.
   *
   * @param document The document
   * @return Number of selections (0 if document is null or empty)
   */
  public static int getSafeSelectionCount(QOFDocument document) {
    return document == null || document.getSelections() == null
        ? 0
        : document.getSelections().size();
  }

  /**
   * Gets the safe register count for a document.
   *
   * @param document The document
   * @return Number of registers (0 if document is null or empty)
   */
  public static int getSafeRegisterCount(QOFDocument document) {
    return document == null || document.getRegisters() == null
        ? 0
        : document.getRegisters().size();
  }

  /**
   * Gets the safe extraction field count for a document.
   *
   * @param document The document
   * @return Number of extraction fields (0 if document is null or empty)
   */
  public static int getSafeExtractionFieldCount(QOFDocument document) {
    return document == null || document.getExtractionFields() == null
        ? 0
        : document.getExtractionFields().size();
  }

  /**
   * Gets the safe indicator count for a document.
   *
   * @param document The document
   * @return Number of indicators (0 if document is null or empty)
   */
  public static int getSafeIndicatorCount(QOFDocument document) {
    return document == null || document.getIndicators() == null
        ? 0
        : document.getIndicators().size();
  }

  /**
   * Logs safe summary of a QOFDocument.
   *
   * @param document The document to summarize
   */
  public static void logDocumentSummary(QOFDocument document) {
    if (document == null) {
      log.debug("QOFDocument summary: null document");
      return;
    }

    log.debug("QOFDocument summary: name={}, selections={}, registers={}, fields={}, indicators={}",
        getSafeName(document),
        getSafeSelectionCount(document),
        getSafeRegisterCount(document),
        getSafeExtractionFieldCount(document),
        getSafeIndicatorCount(document)
    );
  }
}