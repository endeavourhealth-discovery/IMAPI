package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.endeavourhealth.imapi.model.tripletree.TTValueJava;

/**
 * Uitilities to compare Triple tree objects examning only predicates and values, ignoring entity IRI
 * or private properties
 */
public class TTCompare {

  private TTCompare() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Tests equality the predicate keys and values of a node
   *
   * @param from first node
   * @param to   second node
   * @return if the same or not
   */
  public static boolean equals(TTNodeJava from, TTNodeJava to) {
    if (isNull(from.getPredicateMap(), to.getPredicateMap()))
      return false;
    if (from.getPredicateMap().size() != to.getPredicateMap().size())
      return false;

    return from.getPredicateMap().entrySet().stream()
      .allMatch(e -> equals(e.getValue(), to.getPredicateMap().get(e.getKey())));
  }

  private static boolean isNull(Object from, Object to) {
    if (from == null && to != null)
      return true;
    return to == null && from != null;
  }

  /**
   * Tests equality of two TTArrays recursively checking node predicates
   *
   * @param from first TTArray
   * @param to   second TTArray
   * @return true or false
   */
  public static boolean equals(TTArrayJava from, TTArrayJava to) {
    if (isNull(from, to))
      return false;
    if (from != null && from.size() != to.size())
      return false;
    return !equalsIfFound(from, to);
  }

  private static boolean equalsIfFound(TTArrayJava from, TTArrayJava to) {
    if (from != null) {
      for (TTValueJava fromVal : from.getElements()) {
        boolean found = false;
        for (TTValueJava toVal : to.getElements()) {
          if (equals(fromVal, toVal))
            found = true;
        }
        if (!found)
          return true;
      }
    }
    return false;
  }

  public static boolean equals(TTValueJava from, TTValueJava to) {
    if (from.isIriRef())
      return from.equals(to);
    if (from.isLiteral())
      return from.asLiteral().getValue().equals(to.asLiteral().getValue());
    else if (from.isNode() && to.isNode())
      return equals((TTNodeJava) from, (TTNodeJava) to);
    else
      return false;
  }


}
