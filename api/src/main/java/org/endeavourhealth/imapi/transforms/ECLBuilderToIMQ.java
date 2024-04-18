package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.eclBuilder.BoolGroup;
import org.endeavourhealth.imapi.model.eclBuilder.BuilderComponent;
import org.endeavourhealth.imapi.model.eclBuilder.ExpressionConstraint;
import org.endeavourhealth.imapi.model.eclBuilder.Refinement;
import org.endeavourhealth.imapi.model.imq.Entailment;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Where;

import java.util.ArrayList;

public class ECLBuilderToIMQ {
    public Match getIMQFromEclBuilder(BoolGroup boolGroup) {
        return createRootMatch(boolGroup);
    }

    private Match createRootMatch(BoolGroup boolGroup) {
        Match match = new Match();
        if (null != boolGroup.getConjunction() && boolGroup.getItems().size() > 1) match.setBoolMatch(boolGroup.getConjunction());
        if (null != boolGroup.getItems()) {
            if (boolGroup.getItems().size() == 1) {
                if (boolGroup.getItems().get(0).isConcept()) {
                    processConcept(boolGroup.getItems().get(0).asConcept(), match);
                }
            } else {
                for (BuilderComponent builderComponent : boolGroup.getItems()) {
                    if (builderComponent.isConcept()) {
                        Match subMatch = new Match();
                        processConcept(builderComponent.asConcept(), subMatch);
                        match.addMatch(subMatch);
                    }
                    if (builderComponent.isBoolGroup()) {
                        processBoolGroup(builderComponent.asBoolGroup(), match);
                    }
                }
            }
        }
        return match;
    }

    private void processConcept(ExpressionConstraint expressionConstraint, Match match) {
        if (null != expressionConstraint.getConceptSingle()) {
            Node node = new Node(expressionConstraint.getConceptSingle().getIri());
            setOperator(node,expressionConstraint.getConstraintOperator());
            match.setInstanceOf(node);
        } else if (null != expressionConstraint.getConceptBool()) {
            processBoolGroup(expressionConstraint.getConceptBool(),match);
        }
        if (null != expressionConstraint.getRefinementItems()) {
            if (expressionConstraint.getRefinementItems().size() > 1) match.setBoolWhere(expressionConstraint.getConjunction());
            for (BuilderComponent builderComponent : expressionConstraint.getRefinementItems()) {
                if (builderComponent.isRefinement()) {
                    processRefinement(builderComponent.asRefinement(), match, false);
                }
            }
        }
    }

    private void processRefinement(Refinement refinement, Match match,boolean roleGroup) {
        Where where = new Where();
        if (!roleGroup) where.setAnyRoleGroup(true);
        where.setIri(refinement.getProperty().getConcept().getIri());
        setOperator(where, refinement.getProperty().getConstraintOperator());
        Node value = new Node(refinement.getValue().getConcept().getIri());
        setOperator(value, refinement.getValue().getConstraintOperator());
        if (refinement.getOperator().equals("=")) {
            where.addIs(value);
        } else if (refinement.getOperator().equals("!=")) {
            where.addIsNot(value);
        }
        match.addWhere(where);
    }

    private void processBoolGroup(BoolGroup boolGroup, Match match) {
        Match subMatch = new Match();
        if (null != boolGroup.getConjunction() && boolGroup.getItems().size() > 1) {
            subMatch.setBoolMatch(boolGroup.getConjunction());
        }
        if (null != boolGroup.getExclude()) subMatch.setExclude(boolGroup.getExclude());
        for (BuilderComponent subBuilderComponent : boolGroup.getItems()) {
            if (subBuilderComponent.isBoolGroup()) processBoolGroup(subBuilderComponent.asBoolGroup(), subMatch);
            if (subBuilderComponent.isConcept()) {
                if (boolGroup.getItems().size() > 1) {
                    Match concept = new Match();
                    processConcept(subBuilderComponent.asConcept(), concept);
                    subMatch.addMatch(concept);
                } else processConcept(subBuilderComponent.asConcept(), subMatch);
            }
        }
        match.addMatch(subMatch);
    }

    private void setOperator(Entailment entailment, String operator) {
        switch (operator) {
            case "<<": {
                entailment.setDescendantsOrSelfOf(true);
                break;
            }
            case "<": {
                entailment.setDescendantsOf(true);
                break;
            }
            case ">": {
                entailment.setAncestorsOf(true);
                break;
            }
            case "^": {
                entailment.setMemberOf(true);
                break;
            }
            default: break;
        }
    }
}
