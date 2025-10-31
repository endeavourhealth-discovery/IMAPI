package org.endeavourhealth.imapi.transformation.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.endeavourhealth.imapi.transformation.core.TransformationException;
import org.endeavourhealth.imapi.transformation.util.TransformationLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles loading and deserialization of QOF JSON documents.
 * Supports file reading with UTF-8 encoding and Jackson-based JSON deserialization.
 *
 * Transformation Process:
 * 1. Read JSON file with UTF-8 encoding
 * 2. Deserialize to QOFDocument object
 * 3. Return loaded document
 * 4. Propagate errors with context
 */
public class QOFDocumentLoader {
  private static final Logger log = LoggerFactory.getLogger(QOFDocumentLoader.class);
  private final ObjectMapper objectMapper;
  private final TransformationLogger transformationLogger;

  /**
   * Creates a new QOFDocumentLoader with the given ObjectMapper and logger.
   *
   * @param objectMapper for JSON deserialization
   * @param transformationLogger for structured logging with correlation tracking
   */
  public QOFDocumentLoader(ObjectMapper objectMapper, TransformationLogger transformationLogger) {
    this.objectMapper = objectMapper;
    this.transformationLogger = transformationLogger;
  }

  /**
   * Loads a QOFDocument from a JSON file path.
   *
   * @param filePath path to QOF JSON file
   * @return deserialized QOFDocument
   * @throws TransformationException if file cannot be read or JSON is invalid
   */
  public QOFDocument loadFromFile(String filePath) {
    return loadFromFile(new File(filePath));
  }

  /**
   * Loads a QOFDocument from a JSON file.
   *
   * @param file QOF JSON file
   * @return deserialized QOFDocument
   * @throws TransformationException if file cannot be read or JSON is invalid
   */
  public QOFDocument loadFromFile(File file) {
    transformationLogger.info("Loading QOF document from file: {}", file.getAbsolutePath());
    
    try {
      if (!file.exists()) {
        throw new TransformationException(
            "QOF file not found",
            "FILE_NOT_FOUND",
            new IOException("File does not exist: " + file.getAbsolutePath())
        );
      }

      QOFDocument document = objectMapper.readValue(file, QOFDocument.class);
      transformationLogger.info("Successfully loaded QOF document from file");
      return document;
    } catch (IOException e) {
      transformationLogger.error("Failed to load QOF document from file: {}", e.getMessage());
      throw new TransformationException(
          "Failed to load QOF document from file",
          "FILE_READ_ERROR",
          e
      );
    }
  }

  /**
   * Loads a QOFDocument from a JSON file path.
   *
   * @param path Path object to QOF JSON file
   * @return deserialized QOFDocument
   * @throws TransformationException if file cannot be read or JSON is invalid
   */
  public QOFDocument loadFromPath(Path path) {
    return loadFromFile(path.toFile());
  }

  /**
   * Loads a QOFDocument from a JSON string.
   *
   * @param jsonContent JSON string content
   * @return deserialized QOFDocument
   * @throws TransformationException if JSON is invalid
   */
  public QOFDocument loadFromString(String jsonContent) {
    transformationLogger.info("Loading QOF document from JSON string");
    
    try {
      QOFDocument document = objectMapper.readValue(jsonContent, QOFDocument.class);
      transformationLogger.info("Successfully loaded QOF document from JSON string");
      return document;
    } catch (IOException e) {
      transformationLogger.error("Failed to parse QOF JSON string: {}", e.getMessage());
      throw new TransformationException(
          "Failed to parse QOF JSON content",
          "JSON_PARSE_ERROR",
          e
      );
    }
  }

  /**
   * Loads a QOFDocument from a byte array with UTF-8 encoding.
   *
   * @param content byte array containing JSON content
   * @return deserialized QOFDocument
   * @throws TransformationException if content cannot be read or JSON is invalid
   */
  public QOFDocument loadFromBytes(byte[] content) {
    return loadFromString(new String(content, StandardCharsets.UTF_8));
  }
}