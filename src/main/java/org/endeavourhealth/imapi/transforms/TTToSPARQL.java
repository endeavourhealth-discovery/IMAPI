package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.endeavourhealth.imapi.model.tripletree.TTValueJava;

import java.util.Map;

/**
 * Transforms an entity in the Triple tree node based form to Spqarql triples.
 * It does not include line feeds.
 * The default serializations of TT Classes is JSON-LD. Turtle provides a more easily readable format
 */

public class TTToSPARQL {

  private StringBuilder turtle;
  private int level;

  public String transformEntity(TTEntityJava entity) {
    turtle = new StringBuilder();
    appendEntity(entity);
    append("\n");
    return turtle.toString();
  }

  private void nl() {
    turtle.append(" ");
  }

  private void appendEntity(TTEntityJava entity) {
    level = 0;
    nl();
    append("<" + entity.getIri() + "> ");
    if (entity.getPredicateMap() != null) {
      level = level + 3;
      nl();
      setPredicateObjects(entity);
      append(" .");
      level = level - 3;
    }

  }

  private void setPredicateObjects(TTNodeJava node) {
    int nodeCount = 1;
    Map<TTIriRef, TTArrayJava> predicateObjectList = node.getPredicateMap();
    if (predicateObjectList != null) {
      for (Map.Entry<TTIriRef, TTArrayJava> entry : predicateObjectList.entrySet()) {
        TTIriRef predicate = entry.getKey();
        TTArrayJava value = entry.getValue();
        if (value != null && !value.isEmpty()) {
          outputPredicateObject(predicate, entry.getValue(), nodeCount);
          nodeCount++;
        }
      }
    }
  }

  private void outputPredicateObject(TTIriRef predicate, TTArrayJava object, int nodeCount) {
    if (nodeCount > 1) {
      append(";");
      nl();
    }
    String pred = "<" + predicate.getIri() + "> ";
    append(pred);
    int olevel = level;
    setObject(object);
    level = olevel;
  }


  private void setObject(TTArrayJava value) {
    int firstIn = 1;
    if (value.size() > 1) {
      level = level + 6;
      nl();
    }
    for (TTValueJava entry : value.iterator()) {
      if (firstIn > 1) {
        append(" , ");
        nl();
      }
      firstIn++;
      setObject(entry);
    }
  }

  private void setObject(TTValueJava value) {
    if (value.isIriRef())
      append("<" + value.asIriRef().getIri() + ">");
    else if (value.isLiteral()) {
      String data = value.asLiteral().getValue();
      data = data.replace("\\", "\\\\");
      data = data.replace("\n", "\\n").replace("\r", "\\r").replace("\"", "");
      if (value.asLiteral().getType() == null)
        append("\"" + data + "\"");
      else {
        append("\"" + data + "\"^^<" + value.asLiteral().getType().getIri() + ">");

      }
    } else {
      append("[");
      setPredicateObjects(value.asNode());
      append("]");

    }
  }


  private StringBuilder append(String aString) {
    turtle.append(aString);
    return turtle;
  }


}
