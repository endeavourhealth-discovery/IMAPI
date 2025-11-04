package org.endeavourhealth.imapi.transformation.engine;

import org.endeavourhealth.imapi.model.qof.QOFDocument;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.Prefixes;
import org.endeavourhealth.imapi.model.imq.Prefix;
import org.endeavourhealth.imapi.transformation.core.TransformationContext;

/**
 * Handles QOF metadata transformation to IMQuery metadata.
 * Sets up prefixes, namespaces, and query metadata.
 */
public class MetadataTransformer {

  /**
   * Transform QOF document metadata and apply to all generated queries.
   */
  public void transformMetadata(QOFDocument qofDocument, TransformationContext context) {
    try {
      // Get or create prefixes
      Prefixes prefixes = createDefaultPrefixes();

      // Apply metadata to all generated queries
      for (Query query : context.getGeneratedQueries().values()) {
        query.setPrefixes(prefixes);

        // Set document-level metadata if not already set
        if (query.getDescription() == null && qofDocument.getName() != null) {
          query.setDescription("Generated from QOF specification: " + qofDocument.getName());
        }
      }

    } catch (Exception e) {
      context.addError("Error transforming metadata: " + e.getMessage());
    }
  }

  /**
   * Create default QOF and IM prefixes.
   */
  private Prefixes createDefaultPrefixes() {
    Prefixes prefixes = new Prefixes();

    // Add standard prefixes
    addPrefix(prefixes, "qof", "http://endhealth.info/qof#");
    addPrefix(prefixes, "im", "http://endhealth.info/im#");
    addPrefix(prefixes, "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    addPrefix(prefixes, "rdfs", "http://www.w3.org/2000/01/rdf-schema#");
    addPrefix(prefixes, "xsd", "http://www.w3.org/2001/XMLSchema#");
    addPrefix(prefixes, "snomed", "http://snomed.info/sct/");

    return prefixes;
  }

  private void addPrefix(Prefixes prefixes, String prefix, String namespace) {
    if (prefixes != null) {
      Prefix p = new Prefix();
      p.setPrefix(prefix);
      p.setNamespace(namespace);
      prefixes.add(p);
    }
  }

  /**
   * Set query metadata (name, description, IRI).
   */
  public void setQueryMetadata(Query query, String name, String description, String iri) {
    if (name != null) {
      query.setName(name);
    }
    if (description != null) {
      query.setDescription(description);
    }
    if (iri != null) {
      query.setIri(iri);
    }
  }
}