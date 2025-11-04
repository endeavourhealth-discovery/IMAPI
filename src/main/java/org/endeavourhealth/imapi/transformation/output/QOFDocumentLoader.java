package org.endeavourhealth.imapi.transformation.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.qof.QOFDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Loads and deserializes QOF JSON documents.
 * Handles file I/O and JSON parsing.
 */
public class QOFDocumentLoader {
  private static final ObjectMapper mapper = new ObjectMapper();

  /**
   * Load a QOF document from a file path.
   */
  public static QOFDocument loadFromFile(String filePath) throws IOException {
    File file = new File(filePath);
    if (!file.exists()) {
      throw new IOException("QOF file not found: " + filePath);
    }
    return mapper.readValue(file, QOFDocument.class);
  }

  /**
   * Load a QOF document from a File object.
   */
  public static QOFDocument loadFromFile(File file) throws IOException {
    if (!file.exists()) {
      throw new IOException("QOF file not found: " + file.getAbsolutePath());
    }
    return mapper.readValue(file, QOFDocument.class);
  }

  /**
   * Load a QOF document from JSON string.
   */
  public static QOFDocument loadFromString(String jsonContent) throws IOException {
    return mapper.readValue(jsonContent, QOFDocument.class);
  }

  /**
   * Load QOF documents from a directory.
   */
  public static java.util.List<QOFDocument> loadFromDirectory(String dirPath) throws IOException {
    java.util.List<QOFDocument> documents = new java.util.ArrayList<>();
    
    Path directory = Paths.get(dirPath);
    if (!Files.isDirectory(directory)) {
      throw new IOException("Path is not a directory: " + dirPath);
    }

    Files.list(directory)
      .filter(path -> path.toString().endsWith(".json"))
      .forEach(path -> {
        try {
          documents.add(loadFromFile(path.toFile()));
        } catch (IOException e) {
          System.err.println("Failed to load QOF file: " + path + " - " + e.getMessage());
        }
      });

    return documents;
  }
}