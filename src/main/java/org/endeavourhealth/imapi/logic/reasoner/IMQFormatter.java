package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.imq.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMQFormatter {
  public List<Return> getNestedReturns(Match match) {
    Map<String,List<Return>> nodeReturnMap = new HashMap<>();
    buildNodeReturnMap(match,nodeReturnMap);
    List<Return> returns= new ArrayList<>();
    if (match.getReturn()!=null){
      for (Return ret: match.getReturn()){
        if (ret.getNodeRef()==null){
          returns.add(ret);
        }
      }
    }
    List<Return> nestedReturns= buildNestedReturns(match,nodeReturnMap);
    if (!nestedReturns.isEmpty()) returns.addAll(nestedReturns);
    return returns;
  }



  private List<Return> buildNestedReturns(HasPaths hasPaths,Map<String, List<Return>> nodeReturnMap) {
    List<Return> nestedReturns = new ArrayList<>();
    if (hasPaths.getPath()!=null) {
      for (Path path : hasPaths.getPath()) {
        boolean found = false;
        Return pathReturn = new Return();
        pathReturn.setIri(path.getIri());
        pathReturn.setName(path.getName());
        List<Return> leafReturns = nodeReturnMap.get(path.getNode());
        if (leafReturns != null) {
          nestedReturns.add(pathReturn);
          pathReturn.setReturn(leafReturns);
        }
        List<Return> branchReturns = buildNestedReturns(path, nodeReturnMap);
        if (!branchReturns.isEmpty()) {
          if (pathReturn.getReturn() != null) {
            pathReturn.getReturn().addAll(branchReturns);
          } else pathReturn.setReturn(branchReturns);
        }
      }
    }
    return nestedReturns;
  }


  private void buildNodeReturnMap(Match match,Map<String,List<Return>> nodeReturnMap) {
    if (match.getReturn()==null) return;
    for (Return ret: match.getReturn()){
      nodeReturnMap.computeIfAbsent(ret.getNodeRef(),k-> new ArrayList<>()).add(ret);
    }
  }

}

