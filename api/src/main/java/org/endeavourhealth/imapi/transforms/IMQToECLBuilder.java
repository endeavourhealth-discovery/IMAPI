package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.eclBuilder.*;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IMQToECLBuilder {
    private IMQToECL imqToECL = new IMQToECL();
    public BoolGroup getEclBuilderFromQuery(Match query) throws QueryException, EclBuilderException {
        return createBoolGroup(query);
    }

    private BoolGroup createBoolGroup(Match match) throws EclBuilderException {
        BoolGroup boolGroup = new BoolGroup();
        EclType matchType = imqToECL.getEclType(match);
        if (null == matchType) throw new EclBuilderException("Failed to get EclType from match");
        if (matchType == EclType.simple) {
            boolGroup.addItem(createConcept(match));
        } else if (matchType == EclType.refined) {
            boolGroup.addItem(createConcept(match));
        } else if (matchType == EclType.compound || matchType == EclType.exclusion) {
            boolGroup.setConjunction(match.getBoolMatch());
            for (Match subMatch : match.getMatch()) {
                if (null != subMatch.getMatch()) boolGroup.addItem(createBoolGroup(subMatch));
                else {
                    if (null != subMatch.getWhere()) {
                        boolGroup.addItem(createBoolGroup(subMatch));
                    } else boolGroup.addItem(createConcept(subMatch));
                }
            }
            if (match.isExclude()) boolGroup.setExclude(true);
        } else if (matchType == EclType.compoundRefined) {
            Concept concept = createConcept(match);
            boolGroup.addItem(concept);
        }
        return boolGroup;
    }

    private BoolGroup createRefinementBoolGroup(Where where) throws EclBuilderException {
        BoolGroup boolGroup = new BoolGroup();
        boolGroup.setConjunction(where.getBoolWhere());
        for (Where subWhere : where.getWhere()) {
            boolGroup.addItem(createRefinement(subWhere));
        }
        return boolGroup;
    }

    private BoolGroup createRefinementBoolGroup(Match match) throws EclBuilderException {
        BoolGroup boolGroup = new BoolGroup();
        boolGroup.setConjunction(match.getBoolMatch());
        for (Where where : match.getWhere()) {
            boolGroup.addItem(createRefinement(where));
        }
        return boolGroup;
    }

    private Concept createConcept(Match match) throws EclBuilderException {
        Concept concept = new Concept();
        if (null != match.getInstanceOf()) {
            concept.setConstraintOperator(getOperator(match.getInstanceOf()));
            concept.setConceptSingle(iri(match.getInstanceOf().getIri()));
        } else if (null != match.getBoolMatch()) {
            BoolGroup boolGroup = new BoolGroup();
            if (null != match.getWhere()) {
                boolGroup.setConjunction(match.getBoolMatch());
                for (Match subMatch : match.getMatch()) {
                    if (null != subMatch.getBoolMatch()) {
                        boolGroup.addItem(createBoolGroup(subMatch));
                    } else if (null != subMatch.getInstanceOf()) {
                        boolGroup.addItem(createConcept(subMatch));
                    }
                }
                concept.addConceptItem(boolGroup);
            } else {
                boolGroup.setConjunction(match.getBoolMatch());
                for (Match subMatch : match.getMatch()) {
                    if (null != subMatch.getBoolMatch()) {
                        concept.addConceptItem(createBoolGroup(subMatch));
                    } else if (null != subMatch.getInstanceOf()) {
                        concept.addConceptItem(createConcept(subMatch));
                    }
                }
            }
        }
        if (null != match.getWhere()) {
            if (null != match.getBoolWhere()) {
                concept.setConjunction(match.getBoolWhere());
            }
            for (Where where : match.getWhere()) {
                if (null != where.getIs() || null != where.getIsNot()) {concept.addRefinementItem(createRefinement(where));}
                else if (null != where.getWhere()) concept.addRefinementItem(createRefinementBoolGroup(where));
                else if (where.getIri().equals(IM.ROLE_GROUP) && null != where.getMatch()) {
                    BoolGroup attributeGroup = createRefinementBoolGroup(where.getMatch());
                    attributeGroup.setAttributeGroup(true);
                    concept.addRefinementItem(attributeGroup);
                }
            }
        }
        return concept;
    }

    private Refinement createRefinement(Where where) throws EclBuilderException {
        Refinement refinement = new Refinement();
        if (null != where.getIri()) {
            SubExpressionConstraint property = new SubExpressionConstraint().setConcept(iri(where.getIri()));
            property.setConstraintOperator(getOperator(where));
            refinement.setProperty(property);
        }
        if (null != where.getIs() && where.getIs().size() == 1) {
            Node whereIs = where.getIs().get(0);
            SubExpressionConstraint value = new SubExpressionConstraint().setConcept(iri(whereIs.getIri()));
            value.setConstraintOperator(getOperator(whereIs));
            refinement.setOperator("=");
            refinement.setValue(value);
        }
        else if (null != where.getIsNot() && where.getIsNot().size() == 1) {
            Node whereIsNot = where.getIsNot().get(0);
            SubExpressionConstraint value = new SubExpressionConstraint().setConcept(iri(whereIsNot.getIri()));
            value.setConstraintOperator(getOperator(whereIsNot));
            refinement.setOperator("!=");
            refinement.setValue(value);
        } else throw new EclBuilderException("Where is not valid refinement.");
        return refinement;
    }

    private String getOperator(Entailment entailment) {
        if (null == entailment) return "";
        if (entailment.isDescendantsOrSelfOf()) return "<<";
        if (entailment.isDescendantsOf()) return "<";
        if (entailment.isAncestorsOf()) return ">";
        if (entailment.isMemberOf()) return "^";
        else return "";
    }
}
