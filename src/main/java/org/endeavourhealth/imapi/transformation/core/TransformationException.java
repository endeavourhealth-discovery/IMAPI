package org.endeavourhealth.imapi.transformation.core;

/**
 * Exception thrown during QOF to IMQuery transformation.
 * Provides detailed error information for debugging and logging.
 */
public class TransformationException extends Exception {
  
  private String componentName;
  private String context;

  public TransformationException(String message) {
    super(message);
  }

  public TransformationException(String message, Throwable cause) {
    super(message, cause);
  }

  public TransformationException withComponent(String componentName) {
    this.componentName = componentName;
    return this;
  }

  public TransformationException withContext(String context) {
    this.context = context;
    return this;
  }

  public String getComponentName() {
    return componentName;
  }

  public String getContext() {
    return context;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("TransformationException: ");
    if (componentName != null) {
      sb.append("[").append(componentName).append("] ");
    }
    sb.append(getMessage());
    if (context != null) {
      sb.append(" (Context: ").append(context).append(")");
    }
    return sb.toString();
  }
}