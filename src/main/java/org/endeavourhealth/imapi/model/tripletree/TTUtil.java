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
  public static Object get(TTNodeJava node, TTIriRef predicate, Class clazz) {
    if (node.get(predicate) == null)
      return null;
    TTArrayJava value = node.get(predicate);
    if (value.isIriRef())
      return clazz.cast(value.asIriRef());
    else if (value.isLiteral())
      return clazz.cast(value.asLiteral().getValue());
    else
      return clazz.cast(value.asNode());
  }

  public static void add(TTNodeJava node, TTIriRef predicate, TTValueJava value) {
    if (!value.isIriRef() && !value.isLiteral()) {
      int order = 0;
      if (node.get(predicate) != null)
        order = node.get(predicate).size();
      value.asNode().set(TTIriRefExtensionsKt.iri(new TTIriRef(), ShaclVocab.ORDER), TTLiteralJava.literal(order));

    }
    node.addObject(predicate, value);

  }

  public static List<TTIriRef> getIriList(TTNodeJava node, TTIriRef predicate) {
    if (node.get(predicate) == null)
      return null;
    List<TTIriRef> result = new ArrayList<>();
    for (TTValueJava v : node.get(predicate).getElements()) {
      if (v.isIriRef())
        result.add(v.asIriRef());
    }
    return result;
  }

  public static TTContextJava getDefaultContext() {
    TTContextJava ctx = new TTContextJava();
    ctx.add(NamespaceVocab.IM, "");
    ctx.add(NamespaceVocab.RDFS, "rdfs");
    ctx.add(NamespaceVocab.RDF, "rdf");
    ctx.add(NamespaceVocab.SNOMED, "sn");
    return ctx;
  }

}
