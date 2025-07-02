package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCCriteriaGroup;
import org.endeavourhealth.imapi.transforms.eqd.EQDOCReport;
import org.endeavourhealth.imapi.transforms.eqd.VocPopulationParentType;
import org.endeavourhealth.imapi.transforms.eqd.VocRuleAction;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;


public class EqdPopToIMQ {

  private EqdResources resources;


  public Query convertPopulation(EQDOCReport eqReport, Query query, EqdResources resources, Graph graph) throws IOException, QueryException, EQDException {
    String activeReport = eqReport.getId();
    if (eqReport.getVersionIndependentGUID() != null) activeReport = eqReport.getVersionIndependentGUID();
    if (eqReport.getName().equals("All currently registered patients"))
      EqdToIMQ.gmsPatients.add(activeReport);
    this.resources = resources;
    this.resources.setQueryType(QueryType.POP);
    query.setTypeOf(new Node().setIri(IM.NAMESPACE + "Patient"));
    if (eqReport.getParent().getParentType() == VocPopulationParentType.ACTIVE) {
      query.addRule(new Match()
        .setIfTrue(RuleAction.NEXT)
        .setIfFalse(RuleAction.REJECT)
        .setBaseRule(true)
        .addInstanceOf(
          new Node().setIri(IM.NAMESPACE + "Q_RegisteredGMS")
            .setName("Registered with GP for GMS services on the reference date")
            .setMemberOf(true)));
      if (eqReport.getPopulation().getCriteriaGroup().isEmpty()) {
        EqdToIMQ.gmsPatients.add(activeReport);
        EqdToIMQ.gmsPatients.add(resources.getNamespace() + activeReport);
        return null;
      }
    } else if (eqReport.getParent().getParentType() == VocPopulationParentType.POP) {
      String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
      if (EqdToIMQ.gmsPatients.contains(id)) {
        query.addRule(new Match()
          .setIfTrue(RuleAction.NEXT)
          .setIfFalse(RuleAction.REJECT)
          .setBaseRule(true)
          .addInstanceOf(
            new Node().setIri(IM.NAMESPACE + "Q_RegisteredGMS")
              .setName("Registered with GP for GMS services on the reference date")
              .setMemberOf(true)));
      } else {
        query.addRule(new Match()
          .setIfTrue(RuleAction.NEXT)
          .setIfFalse(RuleAction.REJECT)
          .setBaseRule(true)
          .addInstanceOf(new Node().setIri(resources.getNamespace() + id)
            .setName(resources.reportNames.get(id))
            .setMemberOf(true)));
      }
    }
    resources.setRule(0);
    resources.setSubRule(0);
    for (EQDOCCriteriaGroup eqGroup : eqReport.getPopulation().getCriteriaGroup()) {
      Match rule = resources.convertGroup(eqGroup, graph);
      query.addRule(rule);
      VocRuleAction ifTrue = eqGroup.getActionIfTrue();
      VocRuleAction ifFalse = eqGroup.getActionIfFalse();
      if (eqGroup.getDefinition().getParentPopulationGuid() != null)
        throw new EQDException("parent population at definition level");
      switch (ifTrue) {
        case SELECT -> rule.setIfTrue(RuleAction.SELECT);
        case REJECT -> rule.setIfTrue(RuleAction.REJECT);
        case NEXT -> rule.setIfTrue(RuleAction.NEXT);
      }
      switch (ifFalse) {
        case SELECT -> rule.setIfFalse(RuleAction.SELECT);
        case REJECT -> rule.setIfFalse(RuleAction.REJECT);
        case NEXT -> rule.setIfFalse(RuleAction.NEXT);
      }


    }
    return query;
  }


}