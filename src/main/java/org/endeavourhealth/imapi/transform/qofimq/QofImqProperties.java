package org.endeavourhealth.imapi.transform.qofimq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Configuration for the QOF→IMQ transformer.
 *
 * Exposes defaults and allows overrides via application.yml, environment variables, or CLI
 * (e.g., --qofimq.input-path=... --qofimq.emit-json=true).
 */
@Component
@ConfigurationProperties(prefix = "qofimq")
public class QofImqProperties {
  /** Root folder to search for input JSON files. */
  private String inputPath = "Z:\\Data\\QOF";

  /** Output folder for emitted artifacts (e.g., IMQ JSON), when enabled. */
  private String outputPath = "Z:\\Data\\QOF\\_imq_out";

  /** Whether to emit IMQ JSON artifacts per input file. */
  private boolean emitJson = false;

  /** Namespace mappings (alias → IRI). */
  private Map<String, String> namespaceMappings = new LinkedHashMap<>();

  public String getInputPath() {
    return inputPath;
  }

  public void setInputPath(String inputPath) {
    this.inputPath = inputPath;
  }

  public String getOutputPath() {
    return outputPath;
  }

  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }

  public boolean isEmitJson() {
    return emitJson;
  }

  public void setEmitJson(boolean emitJson) {
    this.emitJson = emitJson;
  }

  public Map<String, String> getNamespaceMappings() {
    return namespaceMappings;
  }

  public void setNamespaceMappings(Map<String, String> namespaceMappings) {
    this.namespaceMappings = namespaceMappings;
  }
}