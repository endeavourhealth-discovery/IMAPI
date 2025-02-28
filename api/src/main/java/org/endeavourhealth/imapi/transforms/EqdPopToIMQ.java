package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.customexceptions.EQDException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EqdPopToIMQ {

  private EqdResources resources;




  public Query convertPopulation(EQDOCReport eqReport, Query query, EqdResources resources) throws IOException, QueryException, EQDException {
    String activeReport = eqReport.getId();
    if (eqReport.getVersionIndependentGUID()!=null) activeReport=eqReport.getVersionIndependentGUID();
    this.resources = resources;
    this.resources.setQueryType(QueryType.POP);
    query.setTypeOf(new Node().setIri(IM.NAMESPACE + "Patient"));

    if (eqReport.getParent().getParentType() == VocPopulationParentType.ACTIVE) {
      query.addIsSubsetOf(
        new IriLD().setIri(IM.NAMESPACE + "Q_RegisteredGMS")
        .setName("Registered with GP for GMS services on the reference date"));
      if (eqReport.getPopulation().getCriteriaGroup().isEmpty()){
        EqdToIMQ.gmsPatients.add(activeReport);
        EqdToIMQ.gmsPatients.add(resources.getNamespace()+  activeReport);
        return null;
      }
    } else if (eqReport.getParent().getParentType() == VocPopulationParentType.POP) {
      String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
      if (EqdToIMQ.gmsPatients.contains(id)){
        query.addIsSubsetOf(
         new IriLD().setIri(IM.NAMESPACE + "Q_RegisteredGMS")
          .setName("Registered with GP for GMS services on the reference date"));
      }
      else {
        query.addIsSubsetOf(new IriLD().setIri(resources.getNamespace() + id)
          .setName(resources.reportNames.get(id)));
      }
    }
    query.setHasRules(true);
    Match topOr = null;
    for (EQDOCCriteriaGroup eqGroup : eqReport.getPopulation().getCriteriaGroup()) {
      Match rule = resources.convertGroup(eqGroup);
      query.addMatch(rule);
      rule.setIsRule(true);
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