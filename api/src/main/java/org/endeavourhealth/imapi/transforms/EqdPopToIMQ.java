package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;

public class EqdPopToIMQ {

  private EqdResources resources;


  public void convertPopulation(EQDOCReport eqReport, Query query, EqdResources resources) throws IOException, QueryException, EQDException {
    String activeReport = eqReport.getId();
    this.resources = resources;
    query.setTypeOf(IM.NAMESPACE + "Patient");


    if (eqReport.getParent().getParentType() == VocPopulationParentType.ACTIVE) {
      Match rootMatch = new Match();
      query.addMatch(rootMatch);
      rootMatch
        .addInstanceOf(new Node().setIri(IM.NAMESPACE + "Q_RegisteredGMS").setMemberOf(true))
        .setName("Registered with GP for GMS services on the reference date");
    } else if (eqReport.getParent().getParentType() == VocPopulationParentType.POP) {
      String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
      Match rootMatch = new Match();
      query.addMatch(rootMatch);
      rootMatch
        .addInstanceOf(new Node().setIri("urn:uuid:" + id).setMemberOf(true))
        .setName("in the population " + resources.reportNames.get(id));
    }


    Match lastOr = null;
    for (EQDOCCriteriaGroup eqGroup : eqReport.getPopulation().getCriteriaGroup()) {
      lastOr = processCriteriaGroup(eqGroup, lastOr, query, activeReport);
    }
  }

  private Match processCriteriaGroup(EQDOCCriteriaGroup eqGroup, Match match, Query query, String activeReport) throws QueryException, IOException, EQDException {
    Match lastOr = match;
    VocRuleAction ifTrue = eqGroup.getActionIfTrue();
    VocRuleAction ifFalse = eqGroup.getActionIfFalse();
    if (eqGroup.getDefinition().getParentPopulationGuid() != null)
      throw new EQDException("parent population at definition level");
    if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.NEXT) {
      if (lastOr == null) {
        lastOr = new Match();
        query.addMatch(lastOr);
        lastOr.setBoolMatch(Bool.or);
      }
      Match orGroup = new Match();
      lastOr.addMatch(orGroup);
      convertGroup(eqGroup, orGroup);
    } else if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.REJECT ||
      (ifTrue == VocRuleAction.NEXT && ifFalse == VocRuleAction.REJECT)) {
      Match andGroup = new Match();
      if (lastOr != null) {
        lastOr.addMatch(andGroup);
      } else
        query.addMatch(andGroup);
      convertGroup(eqGroup, andGroup);
    } else if (ifTrue == VocRuleAction.REJECT && ifFalse == VocRuleAction.SELECT ||
      ifTrue == VocRuleAction.REJECT && ifFalse == VocRuleAction.NEXT) {
      Match notGroup = new Match();
      if (lastOr != null) {
        lastOr.addMatch(notGroup);
      } else
        query.addMatch(notGroup);
      notGroup.setExclude(true);
      convertGroup(eqGroup, notGroup);
    } else
      throw new EQDException("unrecognised action rule combination : " + activeReport);
    return lastOr;
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
