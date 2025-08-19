package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.Arrays;
import java.util.Map;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTToSCG {
  boolean refinedSet;
  private static final TTIriRef[] corePredicates = {iri(RDF.TYPE), iri(IM.IS_A), iri(IM.HAS_SCHEME), iri(IM.IS_CONTAINED_IN),
    iri(IM.HAS_STATUS), iri(IM.DEFINITIONAL_STATUS)};


  public String getSCG(TTEntity entity, Boolean includeName) throws DataFormatException {
    StringBuilder scg = new StringBuilder();
    if (entity.get(iri(IM.IS_A)) != null) {
      boolean first = true;
      for (TTValue parent : entity.get(iri(IM.IS_A)).iterator()) {
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
    if (node.get(iri(IM.ROLE_GROUP)) != null) {
      scg.append(":");
      this.refinedSet = true;
      boolean first = true;
      for (TTValue group : node.get(iri(IM.ROLE_GROUP)).iterator()) {
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

  private static void addClass(TTIriRef exp, StringBuilder scg, boolean includeName) {
    String iri = checkMember(exp.asIriRef().getIri());
    if (includeName) {
      scg.append(iri + " |" + exp.asIriRef().getName() + " |");
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
}
