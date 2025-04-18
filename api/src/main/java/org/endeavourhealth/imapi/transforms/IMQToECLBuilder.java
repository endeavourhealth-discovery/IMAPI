package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.eclBuilder.*;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IMQToECLBuilder {
  private IMQToECL imqToECL = new IMQToECL();

  public BoolGroup getEclBuilderFromQuery(Match query) throws QueryException, EclBuilderException {
    return createBoolGroup(query, true);
  }

  private BoolGroup createBoolGroup(Match match, boolean rootBool) throws EclBuilderException {
    BoolGroup boolGroup = new BoolGroup();
    EclType matchType = imqToECL.getEclType(match);
    if (null == matchType) throw new EclBuilderException("Failed to get EclType from match");
    if (matchType == EclType.simple) {
      boolGroup.addItem(createConcept(match));
    } else if (matchType == EclType.refined) {
      boolGroup.addItem(createConcept(match));
    } else if (matchType == EclType.compound || matchType == EclType.exclusion) {
      if (null != match.getBool()) boolGroup.setConjunction(match.getBool());
      for (Match subMatch : match.getMatch()) {
        if (null != subMatch.getMatch()) {
          BoolGroup subBool = createBoolGroup(subMatch, false);
          boolGroup.addItem(subBool);
        } else {
          if (null != subMatch.getWhere()) {
            boolGroup.addItem(createBoolGroup(subMatch, false));
          } else boolGroup.addItem(createConcept(subMatch));
        }
      }
      if (match.isExclude()) boolGroup.setExclude(true);
    } else if (matchType == EclType.compoundRefined) {
      ExpressionConstraint concept = createConcept(match);
      boolGroup.addItem(concept);
    }
    return boolGroup;
  }

  private BoolGroup createRefinementBoolGroup(Where where) throws EclBuilderException {
    BoolGroup boolGroup = new BoolGroup();
    if (null != where.getBool()) boolGroup.setConjunction(where.getBool());
    for (Where subWhere : where.getWhere()) {
      if (subWhere.getIri()!=null&& subWhere.getIri().equals(IM.ROLE_GROUP) && null != subWhere.getWhere()) {
        BoolGroup attributeGroup = createRefinementBoolGroup(subWhere);
        attributeGroup.setAttributeGroup(true);
        boolGroup.addItem(attributeGroup);
      }
      else if (null != subWhere.getIs()) {
        boolGroup.addItem(createRefinement(subWhere));
      } else if (null != subWhere.getWhere()) boolGroup.addItem(createRefinementBoolGroup(subWhere));
    }
    return boolGroup;
  }

  private BoolGroup createRefinementBoolGroup(Match match) throws EclBuilderException {
    BoolGroup boolGroup = new BoolGroup();
    if (null != match.getBool()) boolGroup.setConjunction(match.getBool());
    for (Where where : match.getWhere()) {
      if (null != where.getIs()) {
        boolGroup.addItem(createRefinement(where));
      } else if (null != where.getWhere()) boolGroup.addItem(createRefinementBoolGroup(where));
      else if (where.getIri().equals(IM.ROLE_GROUP) && null != where.getWhere()) {
        BoolGroup attributeGroup = createRefinementBoolGroup(where);
        attributeGroup.setAttributeGroup(true);
        boolGroup.addItem(attributeGroup);
      }
    }
    return boolGroup;
  }

  private ExpressionConstraint createConcept(Match match) throws EclBuilderException {
    ExpressionConstraint concept = new ExpressionConstraint();
    if (null != match.getInstanceOf()) {
      concept.setConstraintOperator(getOperator(match.getInstanceOf().get(0)));
      concept.setConceptSingle(new ConceptReference(match.getInstanceOf().get(0).getIri()));
    } else if (null != match.getTypeOf() && match.getTypeOf().getIri().equals(IM.CONCEPT)) {
      concept.setConstraintOperator("<<");
      concept.setConceptSingle(new ConceptReference(SNOMED.ANY));
    } else if (null != match.getMatch()) {
      BoolGroup boolGroup = new BoolGroup();
      if (null != match.getBool()) boolGroup.setConjunction(match.getBool());
      for (Match subMatch : match.getMatch()) {
        if (null != subMatch.getBool()) {
          boolGroup.addItem(createBoolGroup(subMatch, false));
        } else if (null != subMatch.getInstanceOf()) {
          boolGroup.addItem(createConcept(subMatch));
        }
      }
      concept.setConceptBool(boolGroup);
    }
    if (null != match.getWhere()) {
      if (null != match.getBool()) {
        concept.setConjunction(match.getBool());
      }
      for (Where where : match.getWhere()) {
        if (where.getIri()!=null&& where.getIri().equals(IM.ROLE_GROUP) && null != where.getWhere()) {
          BoolGroup attributeGroup = createRefinementBoolGroup(where);
          attributeGroup.setAttributeGroup(true);
          concept.addRefinementItem(attributeGroup);
        }
        else if (null != where.getIs()) {
          concept.addRefinementItem(createRefinement(where));
        } else if (null != where.getWhere()) concept.addRefinementItem(createRefinementBoolGroup(where));
      }
    }
    return concept;
  }

  private Refinement createRefinement(Where where) throws EclBuilderException {
    Refinement refinement = new Refinement();
    if (null != where.getIri()) {
      SubExpressionConstraint property = new SubExpressionConstraint().setConcept(new ConceptReference(where.getIri()));
      property.setConstraintOperator(getOperator(where));
      refinement.setProperty(property);
    }
    if (null != where.getIs() && where.getIs().size() == 1) {
      Node whereIs = where.getIs().get(0);
      if (!whereIs.isExclude()) {
        SubExpressionConstraint value = new SubExpressionConstraint().setConcept(new ConceptReference(whereIs.getIri()));
        value.setConstraintOperator(getOperator(whereIs));
        refinement.setOperator("=");
        refinement.setValue(value);
      } else {
        SubExpressionConstraint value = new SubExpressionConstraint().setConcept(new ConceptReference(whereIs.getIri()));
        value.setConstraintOperator(getOperator(whereIs));
        refinement.setOperator("!=");
        refinement.setValue(value);

      }
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
