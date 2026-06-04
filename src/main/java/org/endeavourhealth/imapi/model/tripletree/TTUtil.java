package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Static methods for casting TT classes to business objects for use in builders
 */
public class TTUtil {

  private TTUtil() {
    throw new IllegalStateException("Utility class");
  }

  @JsonGetter
  public static Object get(TTNode node, TTIriRefExtended predicate, Class clazz) {
    if (node.get(predicate) == null)
      return null;
    TTArray value = node.get(predicate);
    if (value.isIriRef())
      return clazz.cast(value.asIriRef());
    else if (value.isLiteral())
      return clazz.cast(value.asLiteral().getValue());
    else
      return clazz.cast(value.asNode());
  }

  public static void add(TTNode node, TTIriRefExtended predicate, TTValue value) {
    if (!value.isIriRef() && !value.isLiteral()) {
      int order = 0;
      if (node.get(predicate) != null)
        order = node.get(predicate).size();
      value.asNode().set(new TTIriRefExtended(ShaclVocab.ORDER), TTLiteral.literal(order));

    }
    node.addObject(predicate, value);

  }

  public static List<TTIriRefExtended> getIriList(TTNode node, TTIriRefExtended predicate) {
    if (node.get(predicate) == null)
      return null;
    List<TTIriRefExtended> result = new ArrayList<>();
    for (TTValue v : node.get(predicate).getElements()) {
      if (v.isIriRef())
        result.add(v.asIriRef());
    }
    return result;
  }

  public static TTContext getDefaultContext() {
    TTContext ctx = new TTContext();
    ctx.add(NamespaceVocab. IM, "");
    ctx.add(NamespaceVocab. RDFS, "rdfs");
    ctx.add(NamespaceVocab. RDF, "rdf");
    ctx.add(NamespaceVocab. SNOMED, "sn");
    return ctx;
  }

}
