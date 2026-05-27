package org.endeavourhealth.imapi.utility;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EnumUtils {
  private EnumUtils() {
  }

  public static TTIriRef asIri(Enum<?> value) {
    return iri(
      value.toString(),
      Arrays.stream(value.name().split("_"))
        .map(i -> i.substring(0, 1).toUpperCase() + i.substring(1).toLowerCase())
        .collect(Collectors.joining(" "))
    );
  }

  public static IRI asDbIri(Enum<?> value) {
    return Values.iri(value.toString());
  }

  public static HashSet<String> asHashSet(Enum<?>... enums) {
    HashSet<String> result = new HashSet<>(enums.length);
    for (Enum<?> e : enums)
      result.add(e.toString());
    return result;
  }

  public static List<String> asArrayList(Enum<?>... enums) {
    List<String> result = new ArrayList<>(enums.length);
    for (Enum<?> e : enums)
      result.add(e.toString());
    return result;
  }

  public static String[] asArray(Enum<?>... enums) {
    return asArrayList(enums).toArray(new String[enums.length]);
  }
}
