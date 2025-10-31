package org.endeavourhealth.imapi.transformation.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Deserializes QOF JSON documents into QOFDocument Java objects.
 * Handles file reading, JSON parsing, and error management.
 */
public class QOFDocumentDeserializer {

  private static final Logger log = LoggerFactory.getLogger(QOFDocumentDeserializer.class);

  private final ObjectMapper objectMapper;

  /**
   * Creates a deserializer with a custom ObjectMapper.
   *
   * @param objectMapper The Jackson ObjectMapper to use
   */
  public QOFDocumentDeserializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * Creates a deserializer with a default ObjectMapper.
   */
  public QOFDocumentDeserializer() {
    this.objectMapper = new ObjectMapper();
  }

  /**
   * Deserializes a QOF JSON file into a QOFDocument object.
   *
   * @param filePath Path to the JSON file
   * @return The deserialized QOFDocument
   * @throws IOException if file cannot be read or JSON is invalid
   */
  public QOFDocument deserializeFromFile(Path filePath) throws IOException {
    log.debug("Deserializing QOF document from file: {}", filePath);

    if (!Files.exists(filePath)) {
      throw new IOException("File not found: " + filePath);
    }

    try {
      String jsonContent = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
      QOFDocument document = objectMapper.readValue(jsonContent, QOFDocument.class);
      log.info("Successfully deserialized QOF document: {}", document.getName());
      return document;
    } catch (IOException e) {
      log.error("Failed to deserialize QOF document from file: {}", filePath, e);
      throw e;
    }
  }

  /**
   * Deserializes a QOF JSON file into a QOFDocument object.
   *
   * @param file The JSON file
   * @return The deserialized QOFDocument
   * @throws IOException if file cannot be read or JSON is invalid
   */
  public QOFDocument deserializeFromFile(File file) throws IOException {
    return deserializeFromFile(file.toPath());
  }

  /**
   * Deserializes a QOF JSON string into a QOFDocument object.
   *
   * @param jsonString The JSON string
   * @return The deserialized QOFDocument
   * @throws IOException if JSON is invalid
   */
  public QOFDocument deserializeFromString(String jsonString) throws IOException {
    log.debug("Deserializing QOF document from JSON string");

    try {
      QOFDocument document = objectMapper.readValue(jsonString, QOFDocument.class);
      log.info("Successfully deserialized QOF document from string");
      return document;
    } catch (IOException e) {
      log.error("Failed to deserialize QOF document from JSON string", e);
      throw e;
    }
  }

  /**
   * Gets the configured ObjectMapper.
   */
  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }
}