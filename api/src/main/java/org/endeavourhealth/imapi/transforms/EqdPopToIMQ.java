package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class EqdPopToIMQ {

	private EqdResources resources;
	private String activeReport;



	public void convertPopulation(EQDOCReport eqReport, Query query, EqdResources resources) throws DataFormatException, IOException {
		this.activeReport = eqReport.getId();
		this.resources = resources;
		From rootFrom = new From();
		query.setFrom(rootFrom);

		if (eqReport.getParent().getParentType() == VocPopulationParentType.ACTIVE) {
				rootFrom
				.setSet(IM.NAMESPACE+"Q_RegisteredGMS")
				.setName("Registered with GP for GMS services on the reference date");
		}
		else if (eqReport.getParent().getParentType() == VocPopulationParentType.POP) {
				String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
				rootFrom
					.setSet("urn:uuid:" + id)
					.setName(resources.reportNames.get(id));
			}
			else {
				rootFrom
				.setType(IM.NAMESPACE + "Patient")
				.setName("Patient");
			}


		Where lastOr = null;
		for (EQDOCCriteriaGroup eqGroup : eqReport.getPopulation().getCriteriaGroup()) {
			VocRuleAction ifTrue = eqGroup.getActionIfTrue();
			VocRuleAction ifFalse = eqGroup.getActionIfFalse();
			if (eqGroup.getDefinition().getParentPopulationGuid() != null)
				throw new DataFormatException("parent population at definition level");
			if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.NEXT) {
				lastOr = convertOrGroup(eqGroup, lastOr, rootFrom);
			}
			else if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.REJECT ||
				(ifTrue == VocRuleAction.NEXT && ifFalse == VocRuleAction.REJECT)) {
				convertAndGroup(rootFrom, eqGroup);
			}
			else if (ifTrue == VocRuleAction.REJECT && ifFalse == VocRuleAction.SELECT ||
				ifTrue == VocRuleAction.REJECT && ifFalse == VocRuleAction.NEXT) {
				convertNotGroup(eqGroup, rootFrom);
			}
			else
				throw new DataFormatException("unrecognised action rule combination : " + activeReport);
		}
	}

	private void convertNotGroup(EQDOCCriteriaGroup eqGroup, From rootFrom) throws DataFormatException, IOException {
		VocMemberOperator memberOp = eqGroup.getDefinition().getMemberOperator();
		if (eqGroup.getDefinition().getCriteria().size()==1){
			Where not= new Where();
			rootFrom.addWhere(not);
			resources.convertCriteria(eqGroup.getDefinition().getCriteria().get(0),not);
		}
		else {
			if (memberOp== VocMemberOperator.AND){
				Where not= new Where();
				rootFrom.addWhere(not);
				not.setBool(Bool.not);
				Where and= new Where();
				and.addWhere(and);
				and.setBool(Bool.and);
				for (EQDOCCriteria eqCriteria : eqGroup.getDefinition().getCriteria()) {
					resources.convertCriteria(eqCriteria,and);
				}
			}

		}
	}

	private Where convertOrGroup(EQDOCCriteriaGroup eqGroup, Where lastOr, From rootFrom) throws DataFormatException, IOException {
		VocMemberOperator memberOp = eqGroup.getDefinition().getMemberOperator();
			if (lastOr == null) {
				lastOr = new Where();
				rootFrom.addWhere(lastOr);
				lastOr.setBool(Bool.or);
			}
			if (memberOp == VocMemberOperator.AND) {
				Where and = new Where();
				lastOr.addWhere(and);
				and.setBool(Bool.and);
				for (EQDOCCriteria eqCriteria : eqGroup.getDefinition().getCriteria()) {
					Where match = new Where();
					and.addWhere(match);
					resources.convertCriteria(eqCriteria, match);
				}
			}
			else {
				for (EQDOCCriteria eqCriteria : eqGroup.getDefinition().getCriteria()) {
					Where match = new Where();
					lastOr.addWhere(match);
					resources.convertCriteria(eqCriteria, match);
				}

			}
		return lastOr;
	}

	private void convertAndGroup(From rootFrom, EQDOCCriteriaGroup eqGroup) throws DataFormatException, IOException {
		VocMemberOperator memberOp = eqGroup.getDefinition().getMemberOperator();
		if (memberOp == VocMemberOperator.AND) {
			for (EQDOCCriteria eqCriteria : eqGroup.getDefinition().getCriteria()) {
				Where match = new Where();
				rootFrom.addWhere(match);
				resources.convertCriteria(eqCriteria, match);
			}
		}
		else {
			if (eqGroup.getDefinition().getCriteria().size() == 1) {
				Where match = new Where();
				rootFrom.addWhere(match);
				resources.convertCriteria(eqGroup.getDefinition().getCriteria().get(0), match);
			}
			else {
				Where or = new Where();
				rootFrom.addWhere(or);
				or.setBool(Bool.or);
				for (EQDOCCriteria eqCriteria : eqGroup.getDefinition().getCriteria()) {
					Where match = new Where();
					or.addWhere(match);
					resources.convertCriteria(eqCriteria, match);
				}
			}
		}
	}


}
