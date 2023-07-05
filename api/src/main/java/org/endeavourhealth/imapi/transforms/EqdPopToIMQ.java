package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class EqdPopToIMQ {

	private EqdResources resources;
	private String activeReport;



	public void convertPopulation(EQDOCReport eqReport, Query query, EqdResources resources) throws DataFormatException, IOException, QueryException {
		this.activeReport = eqReport.getId();
		this.resources = resources;

		query.setType(IM.NAMESPACE+"Patient");


		if (eqReport.getParent().getParentType() == VocPopulationParentType.ACTIVE) {
			Match rootMatch = new Match();
			query.addMatch(rootMatch);
				rootMatch
				.setSet(IM.NAMESPACE+"Q_RegisteredGMS")
				.setName("Registered with GP for GMS services on the reference date");
		}
		else if (eqReport.getParent().getParentType() == VocPopulationParentType.POP) {
				String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
			Match rootMatch = new Match();
			query.addMatch(rootMatch);
				query.addMatch(rootMatch);
				rootMatch
					.setSet("urn:uuid:" + id)
					.setName(resources.reportNames.get(id));
			}


		Match lastOr = null;
		for (EQDOCCriteriaGroup eqGroup : eqReport.getPopulation().getCriteriaGroup()) {
			VocRuleAction ifTrue = eqGroup.getActionIfTrue();
			VocRuleAction ifFalse = eqGroup.getActionIfFalse();
			if (eqGroup.getDefinition().getParentPopulationGuid() != null)
				throw new DataFormatException("parent population at definition level");
			if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.NEXT) {
				if (lastOr==null){
					lastOr= new Match();
					query.addMatch(lastOr);
					lastOr.setBool(Bool.or);
				}
				Match orGroup= new Match();
				lastOr.addMatch(orGroup);
				convertGroup(eqGroup, orGroup);
			}
			else if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.REJECT ||
				(ifTrue == VocRuleAction.NEXT && ifFalse == VocRuleAction.REJECT)) {
				Match andGroup= new Match();
				if (lastOr!=null){
					lastOr.addMatch(andGroup);
					lastOr=null;
				}
				else
					query.addMatch(andGroup);
				convertGroup(eqGroup,andGroup);
			}
			else if (ifTrue == VocRuleAction.REJECT && ifFalse == VocRuleAction.SELECT ||
				ifTrue == VocRuleAction.REJECT && ifFalse == VocRuleAction.NEXT) {
				Match notGroup= new Match();
				if (lastOr!=null) {
					lastOr.addMatch(notGroup);
					lastOr=null;
				}
				else
					query.addMatch(notGroup);
				notGroup.setExclude(true);
				convertGroup(eqGroup, notGroup);
			}
			else
				throw new DataFormatException("unrecognised action rule combination : " + activeReport);
		}
	}


	private void convertGroup(EQDOCCriteriaGroup eqGroup, Match groupMatch) throws DataFormatException, IOException, QueryException {
		VocMemberOperator memberOp = eqGroup.getDefinition().getMemberOperator();
		if (memberOp == VocMemberOperator.AND) {
			groupMatch.setBool(Bool.and);
				for (EQDOCCriteria eqCriteria : eqGroup.getDefinition().getCriteria()) {
					Match match = new Match();
					groupMatch.addMatch(match);
					resources.convertCriteria(eqCriteria, match);
				}
			}
			else {
				groupMatch.setBool(Bool.or);
				for (EQDOCCriteria eqCriteria : eqGroup.getDefinition().getCriteria()) {
					Match match = new Match();
					groupMatch.addMatch(match);
					resources.convertCriteria(eqCriteria, match);
				}

			}
	}



}
