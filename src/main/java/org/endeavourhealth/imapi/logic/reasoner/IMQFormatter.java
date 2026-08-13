package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.imq.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMQFormatter {


  public static Map<String, List<Return>> getNodeReturnMap(Query query) {
    Map<String, List<Return>> nodeReturnMap = new HashMap<>();
    if (query.getReturn() == null) return nodeReturnMap;
    for (Return ret : query.getReturn()) {
      nodeReturnMap.computeIfAbsent(ret.getNodeRef(), k -> new ArrayList<>()).add(ret);
    }
    return nodeReturnMap;
  }

}

