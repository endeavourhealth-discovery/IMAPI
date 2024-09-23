package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.eclBuilder.*;
import org.endeavourhealth.imapi.model.imq.Entailment;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Where;
import org.endeavourhealth.imapi.vocabulary.IM;

public class ECLBuilderToIMQ {
  public Match getIMQFromEclBuilder(BoolGroup boolGroup) throws EclBuilderException {
    return createRootMatch(boolGroup);
  }

  private Match createRootMatch(BoolGroup boolGroup) throws EclBuilderException {
    Match match = new Match();
    if (null != boolGroup.getItems()) {
      if (null != boolGroup.getConjunction() && boolGroup.getItems().size() > 1)
        match.setBoolMatch(boolGroup.getConjunction());
      if (boolGroup.getItems().size() == 1) {
        if (boolGroup.getItems().get(0).isConcept()) {
          processConcept(boolGroup.getItems().get(0).asConcept(), match);
        } else if (boolGroup.getItems().get(0).isBoolGroup()) {
          processBoolGroup(boolGroup.getItems().get(0).asBoolGroup(), match);
        }
      } else {
        for (BuilderComponent builderComponent : boolGroup.getItems()) {
          processBoolGroupItems(builderComponent, match);
        }
      }
    }
    return match;
  }

  private void processBoolGroupItems(BuilderComponent builderComponent, Match match) throws EclBuilderException {
    if (builderComponent.isConcept()) {
      Match subMatch = new Match();
      processConcept(builderComponent.asConcept(), subMatch);
      match.addMatch(subMatch);
    }
    if (builderComponent.isBoolGroup()) {
      processBoolGroup(builderComponent.asBoolGroup(), match);
    }
  }

  private void processConcept(ExpressionConstraint expressionConstraint, Match match) throws EclBuilderException {
    if (null != expressionConstraint.getConceptSingle()) {
      Node node = new Node(expressionConstraint.getConceptSingle().getIri());
      setOperator(node, expressionConstraint.getConstraintOperator());
      match.addInstanceOf(node);
    } else if (null != expressionConstraint.getConceptBool()) {
      match.setBoolMatch(expressionConstraint.getConceptBool().getConjunction());
      for (BuilderComponent builderComponent : expressionConstraint.getConceptBool().getItems()) {
        Match subMatch = new Match();
        if (builderComponent.isConcept()) {
          processConcept(builderComponent.asConcept(), subMatch);
        } else if (builderComponent.isBoolGroup()) {
          processBoolGroup(builderComponent.asBoolGroup(), subMatch);
        }
        match.addMatch(subMatch);
      }
    }
    if (null != expressionConstraint.getRefinementItems()) {
      processConceptRefinementItems(expressionConstraint, match);
    }
  }

  private void processConceptRefinementItems(ExpressionConstraint expressionConstraint, Match match) throws EclBuilderException {
    if (expressionConstraint.getRefinementItems().size() > 1)
      match.setBoolWhere(expressionConstraint.getConjunction());
    for (BuilderComponent builderComponent : expressionConstraint.getRefinementItems()) {
      if (builderComponent.isRefinement()) {
        processRefinement(builderComponent.asRefinement(), match, false);
      } else if (builderComponent.isBoolGroup()) {
        processRefinementBoolGroup(builderComponent.asBoolGroup(), match);
      }
    }
  }

  private void processRefinement(Refinement refinement, Match match, boolean roleGroup) throws EclBuilderException {
    Where where = new Where();
    if (!roleGroup) where.setAnyRoleGroup(true);
    if (null == refinement.getProperty().getConcept())
      throw new EclBuilderException("Missing iri for property concept");
    where.setIri(refinement.getProperty().getConcept().getIri());
    if (null == refinement.getValue().getConcept()) throw new EclBuilderException("Missing iri for property value");
    Node value = new Node(refinement.getValue().getConcept().getIri());
    setOperator(value, refinement.getValue().getConstraintOperator());
    if (refinement.getOperator().equals("=")) {
      where.addIs(value);
    } else if (refinement.getOperator().equals("!=")) {
      where.addIs(value.setExclude(true));
    }
    setOperator(where, refinement.getProperty().getConstraintOperator());
    match.addWhere(where);
  }

  private void processRefinement(Refinement refinement, Where where) {
    Where subWhere = new Where();
    if (null != refinement.getProperty().getConcept() && null != refinement.getProperty().getConcept().getIri()) {
      subWhere.setIri(refinement.getProperty().getConcept().getIri());
      Node value = new Node(refinement.getValue().getConcept().getIri());
      setOperator(value, refinement.getValue().getConstraintOperator());
      if (refinement.getOperator().equals("=")) {
        subWhere.addIs(value);
      } else if (refinement.getOperator().equals("!=")) {
        subWhere.addIs(value.setExclude(true));
      }
    }
    setOperator(subWhere, refinement.getProperty().getConstraintOperator());
    where.addWhere(subWhere);
  }

  private void processBoolGroup(BoolGroup boolGroup, Match match) throws EclBuilderException {
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

  private void processRefinementBoolGroup(BoolGroup boolGroup, Match match) throws EclBuilderException {
    if (null != boolGroup.getAttributeGroup()) {
      Where attributeGroup = createAttributeGroup(boolGroup);
      match.addWhere(attributeGroup);
    } else {
      Where where = createSubWhere(boolGroup);
      match.addWhere(where);
    }
  }

  private Where createAttributeGroup(BoolGroup boolGroup) throws EclBuilderException {
    Where attributeGroup = new Where();
    attributeGroup.setIri(IM.ROLE_GROUP);
    Match attributeMatch = new Match();
    if (boolGroup.getItems().size() > 1 && null != boolGroup.getConjunction()) {
      attributeMatch.setBoolWhere(boolGroup.getConjunction());
    }
    for (BuilderComponent subBuilderComponent : boolGroup.getItems()) {
      if (subBuilderComponent.isRefinement())
        processRefinement(subBuilderComponent.asRefinement(), attributeMatch, true);
      else if (subBuilderComponent.isBoolGroup())
        processRefinementBoolGroup(subBuilderComponent.asBoolGroup(), attributeMatch);
    }
    attributeGroup.setMatch(attributeMatch);
    return attributeGroup;
  }

  private Where createSubWhere(BoolGroup boolGroup) throws EclBuilderException {
    Where subWhere = new Where();
    if (null != boolGroup.getConjunction() && boolGroup.getItems().size() > 1) {
      subWhere.setBoolWhere(boolGroup.getConjunction());
    }
    for (BuilderComponent subBuilderComponent : boolGroup.getItems()) {
      if (subBuilderComponent.isRefinement()) processRefinement(subBuilderComponent.asRefinement(), subWhere);
      else if (subBuilderComponent.isBoolGroup())
        processRefinementBoolGroup(subBuilderComponent.asBoolGroup(), subWhere);
    }
    return subWhere;
  }

  private void processRefinementBoolGroup(BoolGroup boolGroup, Where where) throws EclBuilderException {
    if (null != boolGroup.getAttributeGroup()) {
      Where attributeGroup = createAttributeGroup(boolGroup);
      where.addWhere(attributeGroup);
    } else {
      Where subWhere = createSubWhere(boolGroup);
      where.addWhere(subWhere);
    }
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
      default:
        break;
    }
  }
}
