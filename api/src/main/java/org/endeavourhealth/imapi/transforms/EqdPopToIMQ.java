package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.iml.Where;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
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
		if (eqReport.getParent().getParentType() == VocPopulationParentType.ACTIVE) {
			resources.setFrom(query, TTIriRef.iri(IM.NAMESPACE+"Q_RegisteredGMS").setName("Registered with GP for GMS services on the reference date"));
		}
		else {
			if (eqReport.getParent().getParentType() == VocPopulationParentType.POP) {
				String id = eqReport.getParent().getSearchIdentifier().getReportGuid();
				resources.setFrom(query, TTIriRef.iri("urn:uuid:" + id).setName(resources.reportNames.get(id)));
			}
		}
		Where mainWhere= new Where();
		query.setWhere(mainWhere);
        convertPopulationCriteriaGroup(eqReport, mainWhere);
        flatten(query);
	}

    private void convertPopulationCriteriaGroup(EQDOCReport eqReport, Where mainWhere) throws DataFormatException, IOException {
        boolean lastOr = false;

        for (EQDOCCriteriaGroup eqGroup : eqReport.getPopulation().getCriteriaGroup()) {
            VocRuleAction ifTrue = eqGroup.getActionIfTrue();
            VocRuleAction ifFalse = eqGroup.getActionIfFalse();

            if (ifTrue == VocRuleAction.SELECT && ifFalse == VocRuleAction.NEXT) {
                convertPopulationCriteriaGroupTrueSelectFalseNext(mainWhere, eqGroup);
                lastOr = true;
            } else if ((ifTrue == VocRuleAction.SELECT || ifTrue == VocRuleAction.NEXT) && ifFalse == VocRuleAction.REJECT) {
                convertPopulationCriteriaGroupTrueSelectOrNextFalseReject(mainWhere, lastOr, eqGroup);
                lastOr = false;
            } else if (ifTrue == VocRuleAction.REJECT && (ifFalse == VocRuleAction.SELECT || ifFalse == VocRuleAction.NEXT)) {
                convertPopulationCriteriaGroupTrueRejectFalseSelectOrNext(mainWhere, eqGroup);
            } else
                throw new DataFormatException("unrecognised action rule combination : " + activeReport);
        }
    }

    private void convertPopulationCriteriaGroupTrueSelectFalseNext(Where mainWhere, EQDOCCriteriaGroup eqGroup) throws DataFormatException, IOException {
        Where or = new Where();
        mainWhere.addOr(or);
        convertGroup(eqGroup, or);
    }

    private void convertPopulationCriteriaGroupTrueSelectOrNextFalseReject(Where mainWhere, boolean lastOr, EQDOCCriteriaGroup eqGroup) throws DataFormatException, IOException {
        if (lastOr) {
            Where or = new Where();
            mainWhere.addOr(or);
            convertGroup(eqGroup, or);
        } else {
            Where and = new Where();
            mainWhere.addAnd(and);
            convertGroup(eqGroup, and);
        }
    }

    private void convertPopulationCriteriaGroupTrueRejectFalseSelectOrNext(Where mainWhere, EQDOCCriteriaGroup eqGroup) throws DataFormatException, IOException {
        if (mainWhere.getNotExist() == null) {
            Where not = new Where();
            mainWhere.setNotExist(not);
        }
        Where not = mainWhere.getNotExist();
        Where notOr = new Where();
        not.addOr(notOr);
        convertGroup(eqGroup, notOr);
    }

    private void convertGroup(EQDOCCriteriaGroup eqGroup, Where topWhere) throws DataFormatException, IOException {
		VocMemberOperator memberOp = eqGroup.getDefinition().getMemberOperator();
		if (eqGroup.getDefinition().getCriteria().size()==1){
			resources.convertCriteria(eqGroup.getDefinition().getCriteria().get(0),topWhere);
		}
		else {
			for (EQDOCCriteria eqCriteria : eqGroup.getDefinition().getCriteria()) {
				Where where = new Where();
				if (memberOp == VocMemberOperator.OR) {
					topWhere.addOr(where);
				} else
					topWhere.addAnd(where);
				resources.convertCriteria(eqCriteria, where);
			}
		}
	}

	private void flatten(Query query) {
		Where flatWhere = new Where();
		Where oldWhere = query.getWhere();
		if (oldWhere.getProperty()!=null){
			return;
		}
		if (oldWhere.getWith()!=null){
			return;
		}
		if (oldWhere.getAnd()!=null){
			for (Where oldAnd:oldWhere.getAnd()){
				flattenAnds(flatWhere,oldAnd);
			}
		}
		if (oldWhere.getOr()!=null){
			for (Where oldOr:oldWhere.getOr()) {
				Where flatOr= new Where();
				flattenOrs(flatOr,oldOr);
				flatWhere.addOr(flatOr);
			}
		}
		query.setWhere(flatWhere);
	}

	private void flattenOrs(Where flatWhere, Where oldWhere) {
		if (oldWhere.getWith()!=null|| oldWhere.getProperty()!=null||oldWhere.getFrom()!=null||oldWhere.getPathTo()!=null){
			flatWhere.addOr(oldWhere);
		}
		else if (oldWhere.getNotExist() != null) {
			flatWhere.addOr(oldWhere);
		}
		else if (oldWhere.getAnd() != null) {
			for (Where oldAnd : oldWhere.getAnd()) {
				flattenAnds(flatWhere, oldAnd);
			}
		}
		else {
			for (Where oldOr:oldWhere.getOr())
				flattenOrs(flatWhere,oldOr);
		}
	}

	private void flattenAnds(Where flatWhere,Where oldWhere){
		if (oldWhere.getWith()!=null|| oldWhere.getProperty()!=null||oldWhere.getFrom()!=null||oldWhere.getPathTo()!=null){
			flatWhere.addAnd(oldWhere);
		}
		else if (oldWhere.getNotExist() != null) {
			flatWhere.addAnd(oldWhere);
		}
		else if (oldWhere.getAnd() != null) {
				for (Where oldAnd : oldWhere.getAnd()) {
					flattenAnds(flatWhere, oldAnd);
				}
		} else {
			for (Where oldOr:oldWhere.getOr())
				flatWhere.addOr(oldOr);
			}


		}

}
