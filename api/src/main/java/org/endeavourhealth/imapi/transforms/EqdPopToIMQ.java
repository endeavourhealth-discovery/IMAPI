package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.SourceType;
import org.endeavourhealth.imapi.transforms.eqd.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class EqdPopToIMQ {

	private EqdResources resources;
	private String activeReport;



	public void convertPopulation(EQDOCReport eqReport, Query query, EqdResources resources) throws DataFormatException, IOException {
		this.activeReport= eqReport.getId();
		this.resources= resources;
		From rootFrom= new From();
		query.setFrom(rootFrom);;
		if (eqReport.getParent().getParentType() == VocPopulationParentType.ACTIVE) {
				rootFrom
				.setIri(IM.NAMESPACE+"Q_RegisteredGMS")
					.setSourceType(SourceType.set)
				.setName("Registered with GP for GMS services on the reference date");
		}
		else if (eqReport.getParent().getParentType() == VocPopulationParentType.POP) {
				String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
				rootFrom
					.setIri("urn:uuid:" + id)
					.setSourceType(SourceType.set)
					.setName(resources.reportNames.get(id));
			}
			else {
				rootFrom
				.setIri(IM.NAMESPACE + "Patient")
				.setSourceType(SourceType.type)
				.setName("Patient");
			}
		Where rootWhere= new Where();
		rootFrom.setWhere(rootWhere);
		rootWhere.setBool(Bool.and);

		Where lastOr= null;


		for (EQDOCCriteriaGroup eqGroup : eqReport.getPopulation().getCriteriaGroup()) {
			VocRuleAction ifTrue = eqGroup.getActionIfTrue();
			VocRuleAction ifFalse = eqGroup.getActionIfFalse();
			VocMemberOperator memberOp= eqGroup.getDefinition().getMemberOperator();

			if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.NEXT) {
				if (lastOr==null){
					lastOr= new Where();
					rootWhere.addWhere(lastOr);
					lastOr.setBool(Bool.or);
				}
					convertGroup(eqGroup, lastOr);
			}
			else if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.REJECT||
				(ifTrue == VocRuleAction.NEXT && ifFalse == VocRuleAction.REJECT))
			 {
				if (lastOr!=null) {
					convertGroup(eqGroup, lastOr);
					lastOr = null;
				}
				else {
					Where where= new Where();
					rootWhere.addWhere(where);
					convertGroup(eqGroup, where);
				}
			}
			else if (ifTrue == VocRuleAction.REJECT && ifFalse == VocRuleAction.SELECT||
				ifTrue == VocRuleAction.REJECT && ifFalse == VocRuleAction.NEXT){
					Where not= new Where();
					lastOr=null;
					not.setBool(Bool.not);
					rootWhere.addWhere(not);
					convertGroup(eqGroup, not);
			}
			else
				throw new DataFormatException("unrecognised action rule combination : " + activeReport);
		}
	}

	private void convertGroup(EQDOCCriteriaGroup eqGroup, Where topMatch) throws DataFormatException, IOException {
		VocMemberOperator memberOp = eqGroup.getDefinition().getMemberOperator();
		if (eqGroup.getDefinition().getCriteria().size()>1) {
			if (memberOp == VocMemberOperator.OR) {
				if (topMatch.getBool() != Bool.or) {
					Where or = new Where();
					or.setBool(Bool.or);
					topMatch.addWhere(or);
					topMatch = or;
				}
			}
			if (memberOp == VocMemberOperator.AND) {
				if (topMatch.getBool() != Bool.and) {
					Where and = new Where();
					and.setBool(Bool.and);
					topMatch.addWhere(and);
					topMatch = and;
				}
			}
		}
		if (eqGroup.getDefinition().getCriteria().size()==1){
			resources.convertCriteria(eqGroup.getDefinition().getCriteria().get(0),topMatch);
		}
		else {
			for (EQDOCCriteria eqCriteria : eqGroup.getDefinition().getCriteria()) {
				Where match = new Where();
				topMatch.addWhere(match);
				resources.convertCriteria(eqCriteria, match);
			}
		}
	}


}
