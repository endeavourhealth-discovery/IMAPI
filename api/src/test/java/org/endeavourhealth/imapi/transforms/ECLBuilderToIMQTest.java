package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.eclBuilder.*;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ECLBuilderToIMQTest {
    ECLBuilderToIMQ eclBuilderToIMQ = new ECLBuilderToIMQ();

    @Test
    public void convertsDescendantsAndSelf() throws EclBuilderException {
        Match match = new Match().addInstanceOf(new Node("http://snomed.info/sct#29857009").setDescendantsOrSelfOf(true));
        BoolGroup boolGroup = new BoolGroup().addItem(new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#29857009")).setConstraintOperator("<<"));
        assertThat(eclBuilderToIMQ.getIMQFromEclBuilder(boolGroup)).usingRecursiveComparison().isEqualTo(match);
    }

    @Test
    public void descendantsNotSelf() throws EclBuilderException {
        Match match = new Match().addInstanceOf(new Node("http://snomed.info/sct#29857009").setDescendantsOf(true));
        BoolGroup boolGroup = new BoolGroup().addItem(new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#29857009")).setConstraintOperator("<"));
        assertThat(eclBuilderToIMQ.getIMQFromEclBuilder(boolGroup)).usingRecursiveComparison().isEqualTo(match);
    }

    @Test
    public void convertsSimpleAndDescendants() throws QueryException, EclBuilderException {
        Match match = new Match();
        match.setBoolMatch(Bool.and);
        Match subMatch1 = new Match().addInstanceOf(new Node("http://snomed.info/sct#298705000").setDescendantsOf(true));
        Match subMatch2 = new Match().addInstanceOf(new Node("http://snomed.info/sct#301366005").setDescendantsOf(true));
        match.addMatch(subMatch1).addMatch(subMatch2);
        ExpressionConstraint subConcept1 = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#298705000")).setConstraintOperator("<");
        ExpressionConstraint subConcept2 = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#301366005")).setConstraintOperator("<");
        BoolGroup boolGroup = new BoolGroup().addItem(subConcept1).addItem(subConcept2).setConjunction(Bool.and);
        Match actual = eclBuilderToIMQ.getIMQFromEclBuilder(boolGroup);
        assertThat(actual).usingRecursiveComparison().isEqualTo(match);
    }

    @Test
    public void simpleRefinement() throws QueryException, EclBuilderException {
        Match match = new Match().addInstanceOf(new Node("http://snomed.info/sct#19829001").setDescendantsOf(true));
        Where where = new Where().setIri("http://snomed.info/sct#116676008").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#387207008").setDescendantsOrSelfOf(true)).setAnyRoleGroup(true);
        match.addWhere(where);
        ExpressionConstraint concept = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#19829001")).setConstraintOperator("<");
        SubExpressionConstraint property = new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#116676008"));
        SubExpressionConstraint value = new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#387207008"));
        Refinement refinement = new Refinement().setOperator("=").setProperty(property).setValue(value);
        concept.addRefinementItem(refinement);
        BoolGroup boolGroup = new BoolGroup().addItem(concept);
        assertThat(eclBuilderToIMQ.getIMQFromEclBuilder(boolGroup)).usingRecursiveComparison().isEqualTo(match);
    }

    @Test
    public void orRefinement() throws QueryException, EclBuilderException {
        Match match = new Match().addInstanceOf(new Node("http://snomed.info/sct#763158003").setDescendantsOrSelfOf(true));
        match.setBoolWhere(Bool.or);
        Where where1 = new Where().setIri("http://snomed.info/sct#127489000").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#698090000").setDescendantsOrSelfOf(true)).setAnyRoleGroup(true);
        Where where2 = new Where().setIri("http://snomed.info/sct#127489000").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#442031002").setDescendantsOrSelfOf(true)).setAnyRoleGroup(true);
        match.addWhere(where1).addWhere(where2);
        ExpressionConstraint concept = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#763158003")).setConstraintOperator("<<");
        concept.setConjunction(Bool.or);
        Refinement refinement1 = new Refinement().setOperator("=").setProperty(new SubExpressionConstraint().setConcept(new ConceptReference("http://snomed.info/sct#127489000")).setConstraintOperator("<<")).setValue(new SubExpressionConstraint().setConcept(new ConceptReference("http://snomed.info/sct#698090000")).setConstraintOperator("<<"));
        Refinement refinement2 = new Refinement().setOperator("=").setProperty(new SubExpressionConstraint().setConcept(new ConceptReference("http://snomed.info/sct#127489000")).setConstraintOperator("<<")).setValue(new SubExpressionConstraint().setConcept(new ConceptReference("http://snomed.info/sct#442031002")).setConstraintOperator("<<"));
        concept.addRefinementItem(refinement1).addRefinementItem(refinement2);
        BoolGroup boolGroup = new BoolGroup().addItem(concept);
        Match actual = eclBuilderToIMQ.getIMQFromEclBuilder(boolGroup);
        assertThat(actual).usingRecursiveComparison().isEqualTo(match);
    }

    @Test
    public void orGroupMinusOrGroup() throws QueryException, EclBuilderException {
        Match match = new Match().setBoolMatch(Bool.and);
        Match subMatch1 = new Match()
            .setBoolMatch(Bool.or)
            .addMatch(new Match().addInstanceOf(new Node("http://snomed.info/sct#386725007").setDescendantsOrSelfOf(true)))
            .addMatch(new Match().addInstanceOf(new Node("http://snomed.info/sct#431314004").setDescendantsOrSelfOf(true)));
        Match subMatch2 = new Match()
            .setExclude(true)
            .setBoolMatch(Bool.or)
            .addMatch(new Match().addInstanceOf(new Node("http://snomed.info/sct#838441000000103").setDescendantsOrSelfOf(true)))
            .addMatch(new Match().addInstanceOf(new Node("http://snomed.info/sct#838451000000100").setDescendantsOrSelfOf(true)));
        match.addMatch(subMatch1).addMatch(subMatch2);
        BoolGroup rootBool = new BoolGroup().setConjunction(Bool.and);
        BoolGroup subBoolGroup1 = new BoolGroup().setConjunction(Bool.or);
        ExpressionConstraint concept1 = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#386725007")).setConstraintOperator("<<");
        ExpressionConstraint concept2 = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#431314004")).setConstraintOperator("<<");
        subBoolGroup1.addItem(concept1).addItem(concept2);
        BoolGroup subBoolGroup2 = new BoolGroup().setConjunction(Bool.or).setExclude(true);
        ExpressionConstraint concept3 = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#838441000000103")).setConstraintOperator("<<");
        ExpressionConstraint concept4 = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#838451000000100")).setConstraintOperator("<<");
        subBoolGroup2.addItem(concept3).addItem(concept4);
        rootBool.addItem(subBoolGroup1).addItem(subBoolGroup2);
        Match actual = eclBuilderToIMQ.getIMQFromEclBuilder(rootBool);
        assertThat(actual).usingRecursiveComparison().isEqualTo(match);
    }

    @Test
    public void focusGroupWithRefinement() throws QueryException, EclBuilderException {
        Match match = new Match().setBoolMatch(Bool.or);
        Match subMatch1 = new Match().addInstanceOf(new Node("http://snomed.info/sct#91936005").setDescendantsOrSelfOf(true));
        Match subMatch2 = new Match().addInstanceOf(new Node("http://snomed.info/sct#294532003").setDescendantsOrSelfOf(true));
        Where where = new Where().setAnyRoleGroup(true).setDescendantsOrSelfOf(true).setIri("http://snomed.info/sct#246075003").addInstanceOf(new Node("http://snomed.info/sct#771577000").setDescendantsOrSelfOf(true));
        match.addMatch(subMatch1).addMatch(subMatch2).addWhere(where);
        BoolGroup rootBool = new BoolGroup();
        ExpressionConstraint compoundConcept = new ExpressionConstraint();
        BoolGroup conceptBool = new BoolGroup().setConjunction(Bool.or);
        ExpressionConstraint concept1 = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#91936005")).setConstraintOperator("<<");
        ExpressionConstraint concept2 = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#294532003")).setConstraintOperator("<<");
        conceptBool.addItem(concept1).addItem(concept2);
        compoundConcept.setConceptBool(conceptBool);
        Refinement refinement = new Refinement()
            .setOperator("=")
            .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#246075003")))
            .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#771577000")));
        compoundConcept.addRefinementItem(refinement);
        rootBool.addItem(compoundConcept);
        Match actual = eclBuilderToIMQ.getIMQFromEclBuilder(rootBool);
        assertThat(actual).usingRecursiveComparison().isEqualTo(match);
    }

    @Test
    public void focusGroupWithRefinementAttributeGroup() throws QueryException, EclBuilderException {
        Match match = new Match().setBoolMatch(Bool.and);
        Match subMatch1 = new Match().addInstanceOf(new Node("http://snomed.info/sct#298705000").setDescendantsOrSelfOf(true));
        Match subMatch2 = new Match().addInstanceOf(new Node("http://snomed.info/sct#301366005").setDescendantsOrSelfOf(true));
        Where where = new Where()
            .setIri(IM.ROLE_GROUP)
            .setMatch(new Match()
                .addWhere(new Where().setIri("http://snomed.info/sct#363698007").addInstanceOf(new Node("http://snomed.info/sct#51185008").setDescendantsOrSelfOf(true))));
        subMatch2.addWhere(where);
        match.addMatch(subMatch1).addMatch(subMatch2);
        BoolGroup rootBool = new BoolGroup().setConjunction(Bool.and);
        ExpressionConstraint subConcept1 = new ExpressionConstraint().setConceptSingle(new ConceptReference("http://snomed.info/sct#298705000")).setConstraintOperator("<<");
        BoolGroup subBool1 = new BoolGroup();
        Refinement refinement = new Refinement().setOperator("=")
            .setProperty(new SubExpressionConstraint().setConcept(new ConceptReference("http://snomed.info/sct#363698007")).setConstraintOperator(""))
            .setValue(new SubExpressionConstraint().setConcept(new ConceptReference("http://snomed.info/sct#51185008")).setConstraintOperator("<<"));
        BoolGroup attributeGroup = new BoolGroup()
            .setAttributeGroup(true)
            .addItem(refinement);
        ExpressionConstraint subConcept2 = new ExpressionConstraint()
            .setConceptSingle(new ConceptReference("http://snomed.info/sct#301366005"))
            .setConstraintOperator("<<")
            .addRefinementItem(attributeGroup);
        subBool1.addItem(subConcept2);
        rootBool.addItem(subConcept1).addItem(subBool1);
        Match actual = eclBuilderToIMQ.getIMQFromEclBuilder(rootBool);
        assertThat(actual).usingRecursiveComparison().isEqualTo(match);
    }

    @Test
    public void focusGroupWithMultipleRefinementGroups() throws QueryException, EclBuilderException {
        Match match = new Match().setBoolMatch(Bool.or);
        Match subMatch1 = new Match().addInstanceOf(new Node("http://snomed.info/sct#10363801000001108").setDescendantsOrSelfOf(true));
        Match subMatch2 = new Match().addInstanceOf(new Node("http://snomed.info/sct#10363901000001102").setDescendantsOrSelfOf(true));
        Where attributeGroup = new Where()
            .setIri(IM.ROLE_GROUP)
            .setMatch(new Match()
                .setBoolWhere(Bool.or)
                .addWhere(new Where().setIri("http://snomed.info/sct#127489000").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#116601002").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#127489000").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#396458002").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#127489000").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#116571008").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#127489000").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#396012006").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#127489000").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#372584003").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#127489000").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#116593003").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#127489000").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#116602009").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#10363001000001101").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#116601002").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#10363001000001101").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#396458002").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#10363001000001101").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#116571008").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#10363001000001101").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#396012006").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#10363001000001101").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#372584003").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#10363001000001101").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#116602009").setDescendantsOrSelfOf(true)))
                .addWhere(new Where().setIri("http://snomed.info/sct#10363001000001101").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#116593003").setDescendantsOrSelfOf(true)))
            );
        Where refinementGroup = new Where()
            .setBoolWhere(Bool.or)
            .addWhere(new Where().setIri("http://snomed.info/sct#411116001").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#385268001").setDescendantsOrSelfOf(true)))
            .addWhere(new Where().setIri("http://snomed.info/sct#13088501000001100").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#21000001106").setDescendantsOrSelfOf(true)))
            .addWhere(new Where().setIri("http://snomed.info/sct#13088401000001104").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#26643006").setDescendantsOrSelfOf(true)))
            .addWhere(new Where().setIri("http://snomed.info/sct#10362901000001105").setDescendantsOrSelfOf(true).addInstanceOf(new Node("http://snomed.info/sct#385268001").setDescendantsOrSelfOf(true)));
        Where where = new Where().setBoolWhere(Bool.and).addWhere(attributeGroup).addWhere(refinementGroup);
        match.addMatch(subMatch1).addMatch(subMatch2);
        match.addWhere(where);
        BoolGroup rootBool = new BoolGroup();
        ExpressionConstraint concept = new ExpressionConstraint().setConjunction(Bool.or);
        ExpressionConstraint subConcept1 = new ExpressionConstraint().setConstraintOperator("<<").setConceptSingle(new ConceptReference("http://snomed.info/sct#10363801000001108"));
        ExpressionConstraint subConcept2 = new ExpressionConstraint().setConstraintOperator("<<").setConceptSingle(new ConceptReference("http://snomed.info/sct#10363901000001102"));
        BoolGroup refinementBoolGroup = new BoolGroup().setConjunction(Bool.and);
        BoolGroup attributeBoolGroup = new BoolGroup().setConjunction(Bool.or).setAttributeGroup(true)
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#127489000")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#116601002"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#127489000")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#396458002"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#127489000")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#116571008"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#127489000")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#396012006"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#127489000")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#372584003"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#127489000")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#116593003"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#127489000")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#116602009"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#10363001000001101")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#116601002"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#10363001000001101")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#396458002"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#10363001000001101")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#116571008"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#10363001000001101")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#396012006"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#10363001000001101")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#372584003"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#10363001000001101")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#116602009"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#10363001000001101")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#116593003"))))
            ;
        BoolGroup subRefinementBoolGroup = new BoolGroup().setConjunction(Bool.or)
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#411116001")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#385268001"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#13088501000001100")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#21000001106"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#13088401000001104")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#26643006"))))
            .addItem(new Refinement()
                .setOperator("=")
                .setProperty(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#10362901000001105")))
                .setValue(new SubExpressionConstraint().setConstraintOperator("<<").setConcept(new ConceptReference("http://snomed.info/sct#385268001"))))
            ;
        refinementBoolGroup.addItem(attributeBoolGroup).addItem(subRefinementBoolGroup);
        concept.setConceptBool(new BoolGroup().addItem(subConcept1).addItem(subConcept2)).addRefinementItem(refinementBoolGroup);
        rootBool.addItem(concept);
        Match actual = eclBuilderToIMQ.getIMQFromEclBuilder(rootBool);
        assertThat(actual).usingRecursiveComparison().isEqualTo(match);
    }
}
