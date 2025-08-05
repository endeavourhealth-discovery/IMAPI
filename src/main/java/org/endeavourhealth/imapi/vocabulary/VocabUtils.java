package org.endeavourhealth.imapi.vocabulary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class VocabUtils {
  public static HashSet<String> asHashSet(VocabEnum...enums) {
    HashSet<String> result = new HashSet<>(enums.length);
    for(VocabEnum e : enums)
      result.add(e.toString());
    return result;
  }

  public static List<String> asArrayList(VocabEnum...enums) {
    List<String> result = new ArrayList<>(enums.length);
    for(VocabEnum e : enums)
      result.add(e.toString());
    return result;
  }

  public static String[] asArray(VocabEnum...enums) {
    return asArrayList(enums).toArray(new String[enums.length]);
  }
}
