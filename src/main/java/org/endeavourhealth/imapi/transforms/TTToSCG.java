package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.interfacemanager.model.ImVocab;
import org.endeavourhealth.interfacemanager.model.RdfVocab;

import java.util.Arrays;
import java.util.Map;
import java.util.zip.DataFormatException;


public class TTToSCG {
  private static final TTIriRef[] corePredicates = {TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_A), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_CONTAINED_IN),
    TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_STATUS), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITIONAL_STATUS)};
  boolean refinedSet;

  private static void addClass(TTIriRef exp, StringBuilder scg, boolean includeName) {
    String iri = checkMember(exp.asIriRef().getIri());
    if (includeName) {
      scg.append(iri).append(" |").append(exp.asIriRef().getName()).append(" |");
    } else {
      scg.append(iri);
    }
  }

  private static String checkMember(String iri) {
    if (iri.contains("/sct#") || (iri.contains("/im#")))
      return iri.split("#")[1];
    else
      return iri;
  }

  public String getSCG(TTEntity entity, Boolean includeName) throws DataFormatException {
    StringBuilder scg = new StringBuilder();
    if (entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_A)) != null) {
      boolean first = true;
      for (TTValue parent : entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_A)).iterator()) {
        if (parent.isIriRef()) {
          if (!first)
            scg.append(" +");
          first = false;
          addClass(parent.asIriRef(), scg, includeName);
        } else
          throw new DataFormatException("ecl not supported with complex superclasses");
      }
    }
    convertRoles(entity, scg, includeName);
    return scg.toString();
  }

  private void convertRoles(TTNode node, StringBuilder scg, boolean includeName) {
    if (node.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP)) != null) {
      scg.append(":");
      this.refinedSet = true;
      boolean first = true;
      for (TTValue group : node.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ROLE_GROUP)).iterator()) {
        if (!first)
          scg.append(" ,");
        scg.append("{");
        refined(group.asNode(), scg, includeName);
        scg.append("}");
        first = false;
      }
    } else {
      refined(node, scg, includeName);
    }

  }

  private void refined(TTNode node, StringBuilder scg, Boolean includeName) {
    boolean first = true;
    for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
      if (!excludeCorePredicates(entry.getKey())) {
        if (!entry.getValue().isLiteral() && !refinedSet) {
          scg.append(": ");
          refinedSet = true;
        }
        if (!first)
          scg.append(" , ");
        first = false;
        if (entry.getValue().isIriRef()) {
          addClass(entry.getKey(), scg, includeName);
          scg.append(" = ");
          addClass(entry.getValue().asIriRef(), scg, includeName);
        } else {
          addClass(entry.getKey(), scg, includeName);
          scg.append("(");
          refined(entry.getValue().asNode(), scg, includeName);
          scg.append(")");
        }
      }
    }
  }

  private boolean excludeCorePredicates(TTIriRef predicate) {
    return (Arrays.asList(corePredicates).contains(predicate));
  }
}
