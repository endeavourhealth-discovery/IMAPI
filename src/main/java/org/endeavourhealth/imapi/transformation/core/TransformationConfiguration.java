package org.endeavourhealth.imapi.transformation.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration settings for transformation operations.
 * Supports various options for controlling transformation behavior.
 */
public class TransformationConfiguration {

  private boolean continueOnError = true;
  private boolean preserveQofMetadata = true;
  private boolean validateOutput = true;
  private int maxErrorsToCollect = 1000;
  private boolean enableDetailedLogging = false;
  private Map<String, String> customMappings = new HashMap<>();

  // Getters and setters
  public boolean isContinueOnError() {
    return continueOnError;
  }

  public TransformationConfiguration setContinueOnError(boolean continueOnError) {
    this.continueOnError = continueOnError;
    return this;
  }

  public boolean isPreserveQofMetadata() {
    return preserveQofMetadata;
  }

  public TransformationConfiguration setPreserveQofMetadata(boolean preserveQofMetadata) {
    this.preserveQofMetadata = preserveQofMetadata;
    return this;
  }

  public boolean isValidateOutput() {
    return validateOutput;
  }

  public TransformationConfiguration setValidateOutput(boolean validateOutput) {
    this.validateOutput = validateOutput;
    return this;
  }

  public int getMaxErrorsToCollect() {
    return maxErrorsToCollect;
  }

  public TransformationConfiguration setMaxErrorsToCollect(int maxErrorsToCollect) {
    this.maxErrorsToCollect = maxErrorsToCollect;
    return this;
  }

  public boolean isEnableDetailedLogging() {
    return enableDetailedLogging;
  }

  public TransformationConfiguration setEnableDetailedLogging(boolean enableDetailedLogging) {
    this.enableDetailedLogging = enableDetailedLogging;
    return this;
  }

  public Map<String, String> getCustomMappings() {
    return customMappings;
  }

  public TransformationConfiguration addCustomMapping(String key, String value) {
    this.customMappings.put(key, value);
    return this;
  }

  public TransformationConfiguration setCustomMappings(Map<String, String> customMappings) {
    this.customMappings = customMappings;
    return this;
  }

  /**
   * Creates a default configuration suitable for production use.
   */
  public static TransformationConfiguration createDefault() {
    return new TransformationConfiguration();
  }

  /**
   * Creates a strict configuration that validates everything.
   */
  public static TransformationConfiguration createStrict() {
    return new TransformationConfiguration()
        .setContinueOnError(false)
        .setValidateOutput(true)
        .setEnableDetailedLogging(true);
  }

  /**
   * Creates a lenient configuration that continues on errors.
   */
  public static TransformationConfiguration createLenient() {
    return new TransformationConfiguration()
        .setContinueOnError(true)
        .setValidateOutput(false)
        .setEnableDetailedLogging(false);
  }

  @Override
  public String toString() {
    return "TransformationConfiguration{" +
        "continueOnError=" + continueOnError +
        ", preserveQofMetadata=" + preserveQofMetadata +
        ", validateOutput=" + validateOutput +
        ", maxErrorsToCollect=" + maxErrorsToCollect +
        ", enableDetailedLogging=" + enableDetailedLogging +
        '}';
  }
}