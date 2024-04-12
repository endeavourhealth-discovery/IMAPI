package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.ecl.ExpressionConstraint;
import org.endeavourhealth.imapi.model.ecl.RefinedExpressionConstraint;
import org.endeavourhealth.imapi.model.ecl.EclBuilderException;
import org.endeavourhealth.imapi.model.ecl.SubExpressionConstraint;
import org.endeavourhealth.imapi.model.imq.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class IMQToECLBuilderTest {
    IMQToECLBuilder imqToECLBuilder = new IMQToECLBuilder();
    @Test
    public void convertsDescendantsAndSelf() throws QueryException, EclBuilderException {
        Match match = new Match().setInstanceOf(new Node("http://snomed.info/sct#29857009").setDescendantsOrSelfOf(true));
        SubExpressionConstraint subExpressionConstraint = new SubExpressionConstraint().setConstraintOperator("<<").setFocusConcept(iri("http://snomed.info/sct#29857009"));
        ExpressionConstraint boolGroup = new ExpressionConstraint().addItem(new RefinedExpressionConstraint().setSubExpressionConstraint(subExpressionConstraint));
        assertThat(imqToECLBuilder.getEclBuilderFromQuery(match)).usingRecursiveComparison().isEqualTo(boolGroup);
    }

    @Test
    public void convertsDescendantsNotSelf() throws QueryException, EclBuilderException {
        Match match = new Match().setInstanceOf(new Node("http://snomed.info/sct#29857009").setDescendantsOf(true));
        ExpressionConstraint boolGroup = new ExpressionConstraint().addItem(new RefinedExpressionConstraint().setConceptSingle(iri("http://snomed.info/sct#29857009")).setConstraintOperator("<"));
        assertThat(imqToECLBuilder.getEclBuilderFromQuery(match)).usingRecursiveComparison().isEqualTo(boolGroup);
    }

    @Test
    public void convertsSimpleAndDescendants() throws QueryException, EclBuilderException {
        Match match = new Match();
        match.setBoolMatch(Bool.and);
        Match subMatch1 = new Match().setInstanceOf(new Node("http://snomed.info/sct#298705000").setDescendantsOf(true));
        Match subMatch2 = new Match().setInstanceOf(new Node("http://snomed.info/sct#301366005").setDescendantsOf(true));
        match.addMatch(subMatch1).addMatch(subMatch2);
        RefinedExpressionConstraint subConcept1 = new RefinedExpressionConstraint().setConceptSingle(iri("http://snomed.info/sct#298705000")).setConstraintOperator("<");
        RefinedExpressionConstraint subConcept2 = new RefinedExpressionConstraint().setConceptSingle(iri("http://snomed.info/sct#301366005")).setConstraintOperator("<");
        ExpressionConstraint boolGroup = new ExpressionConstraint().addItem(subConcept1).addItem(subConcept2).setConjunction(Bool.and);
        assertThat(imqToECLBuilder.getEclBuilderFromQuery(match)).usingRecursiveComparison().isEqualTo(boolGroup);
    }

//    @Test
    public void simpleRefinement() throws QueryException, EclBuilderException {
        Match match = new Match().setInstanceOf(new Node("http://snomed.info/sct#763158003").setDescendantsOrSelfOf(true));
        Where where = new Where().setIri("http://snomed.info/sct#127489000").setDescendantsOrSelfOf(true).addIs(new Node("http://snomed.info/sct#387207008").setDescendantsOrSelfOf(true));
        match.addWhere(where);
        RefinedExpressionConstraint concept = new RefinedExpressionConstraint().setConceptSingle(iri("http://snomed.info/sct#763158003")).setConstraintOperator("<<");
        ExpressionConstraint boolGroup = new ExpressionConstraint().addItem(concept);
        assertThat(imqToECLBuilder.getEclBuilderFromQuery(match)).usingRecursiveComparison().isEqualTo(boolGroup);
    }
}
