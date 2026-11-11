package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.imq.Node;

import java.util.*;
import java.util.stream.Collectors;

public class SetDefinitionCompressor {
  private SetRepository setRepository= new SetRepository();

public String getSetDefinition(String iri) {
  Pageable<Node> memberNodes= setRepository.getMembers(iri,false,1,10000);
  if (memberNodes.getResult()==null)
    return "";
  Set<String> members= memberNodes.getResult().stream().map(Node::getIri).collect(Collectors.toSet());
  level0(iri,members);
  return "";
}

  private void level0(String iri,Set<String> members) {
    Set<String> inclusionAncestors = new HashSet<>();
    Set<String> exclusions = new HashSet<>();
    Set<String> newMembers = new HashSet<>();
    Map<String, Map<String, Set<String>>> level0Parents = setRepository.getLevel0ParentInclusions(iri);
    setRepository.addExclusions(iri, level0Parents);
    for (String parentIri : level0Parents.keySet()) {
      members.remove(parentIri);
      inclusionAncestors.add(parentIri);
      Map<String, Set<String>> InOrOut = level0Parents.get(parentIri);
      if (InOrOut.get("inclusion") != null) {
        for (String include : InOrOut.get("inclusion")) {
          members.remove(include);
        }
      }
      if (InOrOut.get("exclusion") != null) {
        for (String exclude : InOrOut.get("exclusion")) {
          exclusions.add(exclude);
          members.remove(exclude);
        }
      }
    }
    int beforeSize = members.size() + 1;
    while (members.size() != beforeSize) {
      levelN(members,inclusionAncestors,exclusions);
      beforeSize = members.size();
    }
  }
  private void levelN(Set<String> members,Set<String> inclusionAncestors,Set<String> exclusions) {
  Map<String,Map<String,Set<String>>> levelNParents= setRepository.getLevelNParentInclusions(members);
   setRepository.addNExclusions(members,levelNParents);
    for (String parentIri:levelNParents.keySet()){
      Map<String,Set<String>> InOrOut= levelNParents.get(parentIri);
      if (InOrOut.get("exclusion")==null){
        members.remove(parentIri);
        inclusionAncestors.add(parentIri);
      }
      else if (InOrOut.get("inclusion")!=null){
        int inSize= InOrOut.get("inclusion").size();
        int outSize= InOrOut.get("exclusion").size();
        double ratio = (double) outSize / inSize;
        if (ratio <0.5){
          members.remove(parentIri);
          inclusionAncestors.add(parentIri);
          exclusions.addAll(InOrOut.get("exclusion"));
        }
      }
    }
  }



}
