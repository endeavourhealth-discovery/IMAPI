package org.endeavourhealth.imapi.transform.qofimq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class IriResolver {
  private static final Logger LOG = LoggerFactory.getLogger(IriResolver.class);
  private final QofImqProperties props;

  public IriResolver(QofImqProperties props) {
    this.props = props;
  }

  /**
   * Resolve a field like "QOF:age" using configured namespace mappings or built-in Namespace enum.
   * Returns a TTIriRef for use in IMQ Path/Where.
   */
  public TTIriRef resolveField(String field) {
    if (field == null || field.isBlank()) {
      throw new IriResolutionException("Field is blank");
    }
    String[] parts = field.split(":", 2);
    if (parts.length != 2) {
      throw new IriResolutionException("Field '" + field + "' is not in prefix:name form");
    }
    String prefix = parts[0];
    String local = parts[1];

    // 1) check configured mappings
    Map<String, String> map = props.getNamespaceMappings();
    if (map != null) {
      String base = map.get(prefix);
      if (base != null && !base.isBlank()) {
        return iri(base + local);
      }
    }
    // 2) try built-in Namespace by enum name
    try {
      Namespace ns = Namespace.valueOf(prefix.toUpperCase(Locale.ROOT));
      return iri(ns.toString() + local);
    } catch (IllegalArgumentException iae) {
      // fall through
    }
    throw new IriResolutionException("Unknown namespace prefix '" + prefix + "' for field '" + field + "'");
  }
}
