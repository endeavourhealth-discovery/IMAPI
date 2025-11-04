package org.endeavourhealth.imapi.transformation.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.endeavourhealth.imapi.model.imq.Query;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Writes IMQuery output to JSON files.
 * Uses atomic operations (temp file → rename) for safety.
 */
public class QueryOutputWriter {
  private static final ObjectMapper mapper = createObjectMapper();
  
  public enum OverwriteStrategy {
    ALLOW,    // Overwrite existing files
    DENY,     // Throw error if file exists
    BACKUP    // Create backup of existing file
  }

  /**
   * Write a single query to a JSON file.
   */
  public static void writeQuery(Query query, String outputPath, OverwriteStrategy strategy) throws IOException {
    File outputFile = new File(outputPath);
    ensureDirectoryExists(outputFile.getParentFile());
    checkFileExists(outputFile, strategy);

    // Write to temp file first
    File tempFile = File.createTempFile("query_", ".json", outputFile.getParentFile());
    try {
      mapper.writeValue(tempFile, query);
      // Atomic rename
      Files.move(tempFile.toPath(), outputFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      tempFile.delete();
      throw e;
    }
  }

  /**
   * Write multiple queries to individual JSON files in a directory.
   */
  public static void writeQueries(Map<String, Query> queries, String outputDir, OverwriteStrategy strategy) throws IOException {
    Path dirPath = Paths.get(outputDir);
    Files.createDirectories(dirPath);

    for (Map.Entry<String, Query> entry : queries.entrySet()) {
      String queryName = entry.getKey();
      Query query = entry.getValue();
      
      String fileName = sanitizeFileName(queryName) + ".json";
      String filePath = dirPath.resolve(fileName).toString();
      
      writeQuery(query, filePath, strategy);
    }
  }

  /**
   * Write queries to a single combined JSON file.
   */
  public static void writeQueriesCombined(Map<String, Query> queries, String outputPath, OverwriteStrategy strategy) throws IOException {
    File outputFile = new File(outputPath);
    ensureDirectoryExists(outputFile.getParentFile());
    checkFileExists(outputFile, strategy);

    File tempFile = File.createTempFile("queries_", ".json", outputFile.getParentFile());
    try {
      // Create a wrapper object with queries array
      Map<String, Object> wrapper = new java.util.HashMap<>();
      wrapper.put("queries", queries.values());
      wrapper.put("count", queries.size());
      
      mapper.writeValue(tempFile, wrapper);
      Files.move(tempFile.toPath(), outputFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      tempFile.delete();
      throw e;
    }
  }

  /**
   * Write query as JSON string.
   */
  public static String writeAsString(Query query) throws IOException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(query);
  }

  private static ObjectMapper createObjectMapper() {
    ObjectMapper om = new ObjectMapper();
    om.enable(SerializationFeature.INDENT_OUTPUT);
    om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return om;
  }

  private static void ensureDirectoryExists(File directory) throws IOException {
    if (directory != null && !directory.exists()) {
      if (!directory.mkdirs()) {
        throw new IOException("Failed to create directory: " + directory.getAbsolutePath());
      }
    }
  }

  private static void checkFileExists(File file, OverwriteStrategy strategy) throws IOException {
    if (!file.exists()) {
      return;
    }

    switch (strategy) {
      case DENY:
        throw new IOException("File already exists: " + file.getAbsolutePath());
      
      case BACKUP:
        String backupPath = file.getAbsolutePath() + ".backup";
        Files.copy(file.toPath(), Paths.get(backupPath), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        break;
      
      case ALLOW:
        // Continue with overwrite
        break;
    }
  }

  private static String sanitizeFileName(String name) {
    return name.replaceAll("[^a-zA-Z0-9._-]", "_");
  }
}