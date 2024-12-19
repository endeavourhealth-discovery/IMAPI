package org.endeavourhealth.imapi.logic.exporters.helpers;

import org.endeavourhealth.imapi.model.iml.Concept;

public class ExporterHelpers {
  private ExporterHelpers() {
    throw new IllegalStateException("Utility class");
  }

  public static String getUsage(Concept concept) {
    return concept.getUsage() != null ? concept.getUsage().toString() : "null";
  }

  public static String getIsExtension(Concept concept) {
    return concept.getScheme() != null ? (concept.getScheme().getIri().contains("sct#") ? "N" : "Y") : "null";
  }

  public static String getSubSet(Concept concept) {
    return concept.getIsContainedIn() != null ? concept.getIsContainedIn().iterator().next().getName() : "null";
  }

  public static String getSubsetIri(Concept concept) {
    return concept.getIsContainedIn() != null ? concept.getIsContainedIn().iterator().next().getIri() : "null";
  }

  public static String setSubsetVersion(Concept concept) {
    return concept.getIsContainedIn() != null ? String.valueOf(concept.getIsContainedIn().iterator().next().getVersion()) : "null";
  }

  public static String getCode(Concept concept) {
    return concept.getAlternativeCode() != null ? concept.getAlternativeCode() : "null";
  }

  public static String getStatus(Concept concept) {
    return concept.getStatus() != null ? concept.getStatus().getName() : "null";
  }
}
