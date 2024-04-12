package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.ecl.*;
import org.endeavourhealth.imapi.model.imq.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IMQToECLBuilder {
    private IMQToECL imqToECL = new IMQToECL();
    public ExpressionConstraint getEclBuilderFromQuery(Match query) throws QueryException, EclBuilderException {
        return createBoolGroup(query);
    }

    private ExpressionConstraint createBoolGroup(Match match) throws EclBuilderException {
        ExpressionConstraint boolGroup = new ExpressionConstraint();
        EclType matchType = imqToECL.getEclType(match);
        if (null == matchType) throw new EclBuilderException("Failed to get EclType from match");
        if (matchType == EclType.simple) {
            boolGroup.addItem(createConcept(match));
        } else if (matchType == EclType.refined) {
            boolGroup.addItem(createConcept(match));
        } else if (matchType == EclType.compound) {
            RefinedExpressionConstraint concept = createConcept(match);
            boolGroup.setItems(concept.getConceptItems());
            boolGroup.setConjunction(concept.getConjunction());
        }
        return boolGroup;
    }

    private RefinedExpressionConstraint createConcept(Match imq) throws EclBuilderException {
        RefinedExpressionConstraint concept = new RefinedExpressionConstraint();
        if (null != imq.getInstanceOf()) {
            concept.setConstraintOperator(getOperator(imq.getInstanceOf()));
            concept.setConceptSingle(iri(imq.getInstanceOf().getIri()));
        } else if (null != imq.getBoolMatch()) {
            concept.setConjunction(imq.getBoolMatch());
            for (Match subMatch : imq.getMatch()) {
                if (null != subMatch.getBoolMatch()) {
                    concept.addConceptItem(createBoolGroup(subMatch));
                } else if (null != subMatch.getInstanceOf()) {
                    concept.addConceptItem(createConcept(subMatch));
                }
            }
        }
//        if (null != imq.getWhere())
        return concept;
    }

    private String getOperator(Entailment entailment) throws EclBuilderException {
        if (null == entailment) return "";
        if (entailment.isDescendantsOrSelfOf()) return "<<";
        if (entailment.isDescendantsOf()) return "<";
        if (entailment.isAncestorsOf()) return ">";
        if (entailment.isMemberOf()) return "^";
        else return "";
    }
}
