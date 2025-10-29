package org.endeavourhealth.imapi.transform.qofimq;

/**
 * Structured validation error carrying file path, JSON pointer, and message.
 */
public class ValidationError {
  private final String filePath;
  private final String jsonPointer;
  private final String message;

  public ValidationError(String filePath, String jsonPointer, String message) {
    this.filePath = filePath;
    this.jsonPointer = jsonPointer;
    this.message = message;
  }

  public String getFilePath() {
    return filePath;
  }

  public String getJsonPointer() {
    return jsonPointer;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "ValidationError{" +
      "filePath='" + filePath + '\'' +
      ", jsonPointer='" + jsonPointer + '\'' +
      ", message='" + message + '\'' +
      '}';
  }
}
