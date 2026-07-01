package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.endeavourhealth.imapi.model.tripletree.TTValueJava;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TTToNQuad {
  private static int blank;
  private List<String> quads;

  public List<String> transformEntity(TTEntityJava entity, GRAPH graph) {
    quads = new ArrayList<>();
    appendEntity(entity, graph);
    return quads;
  }

  private void appendEntity(TTEntityJava entity, GRAPH graph) {
    String subject = "<" + entity.getIri() + "> ";
    if (entity.getPredicateMap() != null) {
      setPredicateObjects(subject, entity, graph);
    }

  }

  private void setPredicateObjects(String subject, TTNodeJava node, GRAPH graph) {
    Map<TTIriRef, TTArrayJava> predicateObjectList = node.getPredicateMap();
    if (predicateObjectList != null) {
      for (Map.Entry<TTIriRef, TTArrayJava> entry : predicateObjectList.entrySet()) {
        String predicate = "<" + entry.getKey().getIri() + "> ";
        TTArrayJava value = entry.getValue();
        if ((value != null) && (!value.isEmpty())) {
          for (TTValueJava val : value.getElements()) {
            setObject(subject, predicate, val, graph);
          }
        }
      }
    }
  }

  private void setObject(String subject, String predicate, TTValueJava value, GRAPH graph) {
    if (value.isIriRef())
      quads.add(subject + predicate + "<" + value.asIriRef().getIri() + "> <" + graph + ">.");
    else if (value.isLiteral()) {
      String data = value.asLiteral().getValue();
      data = data.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r").replace("\"", "\\\"");
      if (value.asLiteral().getType() == null)
        quads.add(subject + predicate + "\"" + data + "\" <" + graph + ">.");
      else {
        quads.add(subject + predicate + "\"" + data + "\"^^<" + value.asLiteral().getType().getIri() + "> <" + graph + ">.");

      }
    } else {
      blank++;
      String blankNode = "_:b" + blank;
      quads.add(subject + predicate + blankNode + " <" + graph + ">.");
      setPredicateObjects(blankNode, value.asNode(), graph);

    }
  }
}
