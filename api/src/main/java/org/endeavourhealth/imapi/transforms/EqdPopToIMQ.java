package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;

public class EqdPopToIMQ {

  private EqdResources resources;


  public Query convertPopulation(EQDOCReport eqReport, Query query, EqdResources resources) throws IOException, QueryException, EQDException {
    String activeReport = eqReport.getId();
    this.resources = resources;
    query.setTypeOf(IM.NAMESPACE + "Patient");


    if (eqReport.getParent().getParentType() == VocPopulationParentType.ACTIVE) {
      Match rootMatch = new Match();
      query.addMatch(rootMatch);
      rootMatch
        .addInstanceOf(new Node().setIri(IM.NAMESPACE + "Q_RegisteredGMS").setMemberOf(true))
        .setName("Registered with GP for GMS services on the reference date");
      if (eqReport.getPopulation().getCriteriaGroup().isEmpty()){
        EqdToIMQ.gmsPatients.add(activeReport);
        EqdToIMQ.gmsPatients.add("urn:uuid:"+ activeReport);
        return null;
      }
    } else if (eqReport.getParent().getParentType() == VocPopulationParentType.POP) {
      String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
      Match rootMatch = new Match();
      query.addMatch(rootMatch);
      if (EqdToIMQ.gmsPatients.contains(id)){
        rootMatch
         .addInstanceOf(new Node().setIri(IM.NAMESPACE + "Q_RegisteredGMS").setMemberOf(true))
          .setName("Registered with GP for GMS services on the reference date");
      }
      else {
        rootMatch
          .addInstanceOf(new Node().setIri("urn:uuid:" + id).setMemberOf(true))
          .setName("in the population " + resources.reportNames.get(id));
      }
    }


    Match topOr = null;
    int group=0;
    int size= eqReport.getPopulation().getCriteriaGroup().size();
    for (EQDOCCriteriaGroup eqGroup : eqReport.getPopulation().getCriteriaGroup()) {
      boolean must = false;
      boolean select = false;
      group++;
      Match groupMatch = new Match();
      VocRuleAction ifTrue = eqGroup.getActionIfTrue();
      VocRuleAction ifFalse = eqGroup.getActionIfFalse();
      if (eqGroup.getDefinition().getParentPopulationGuid() != null)
        throw new EQDException("parent population at definition level");
      if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.REJECT) {
        must = true;
        select = true;
      }
      else if (ifTrue == VocRuleAction.REJECT && ifFalse == VocRuleAction.SELECT) {
        must = true;
        select = true;
        groupMatch.setExclude(true);
      }
      else if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.NEXT) {
        select = true;
      }else if (ifTrue == VocRuleAction.REJECT && ifFalse == VocRuleAction.NEXT) {
        must = true;
        groupMatch.setExclude(true);
      } else if (ifTrue == VocRuleAction.NEXT && ifFalse == VocRuleAction.REJECT) {
        must = true;
      } else if (ifTrue == VocRuleAction.NEXT && ifFalse == VocRuleAction.SELECT) {
        select = true;
        groupMatch.setExclude(true);
      } else
        throw new EQDException("unrecognised action rule combination : " + activeReport + " " + ifTrue.value() + " / " + ifFalse.value());
      if (must && select) {
        if (group == size) {
          query.addMatch(groupMatch);
        }
        else
          throw new EQDException(("Select reject rule should be the last rule? "+ activeReport));
        }
      else if (select){
        if (topOr==null){
          topOr= new Match();
          topOr.setBoolMatch(Bool.or);
          query.addMatch(topOr);
        }
        topOr.addMatch(groupMatch);
      }
      else if (must){
        if (topOr!=null) {
          topOr.addMatch(groupMatch);
          topOr=null;
        }
        else
          query.addMatch(groupMatch);
      }
      convertGroup(eqGroup, groupMatch);
    }
    return query;

  }


  private void convertGroup(EQDOCCriteriaGroup eqGroup, Match groupMatch) throws IOException, QueryException, EQDException {
    VocMemberOperator memberOp = eqGroup.getDefinition().getMemberOperator();
    if (memberOp == VocMemberOperator.AND) {
      groupMatch.setBoolMatch(Bool.and);
    } else
      groupMatch.setBoolMatch(Bool.or);
    for (EQDOCCriteria eqCriteria : eqGroup.getDefinition().getCriteria()) {
      groupMatch.addMatch(resources.convertCriteria(eqCriteria));
    }
  }


}
