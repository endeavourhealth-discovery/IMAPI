package org.endeavourhealth.imapi.model.eclBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import org.endeavourhealth.imapi.json.ExpressionConstraintDeserializer;
import org.endeavourhealth.imapi.json.TTEntityDeserializer;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@Getter
@JsonDeserialize(using = ExpressionConstraintDeserializer.class)
public class ExpressionConstraint extends BuilderComponent {
  private String constraintOperator;
  private Bool conjunction;
  private ConceptReference conceptSingle;
  private BoolGroup conceptBool;
  private List<BuilderComponent> refinementItems;

  public ExpressionConstraint() {
    super("ExpressionConstraint");
    this.conjunction = Bool.or;
  }

  public ExpressionConstraint setConstraintOperator(String constraintOperator) {
    this.constraintOperator = constraintOperator;
    return this;
  }

  public ExpressionConstraint setConjunction(Bool conjunction) {
    this.conjunction = conjunction;
    return this;
  }

  public ExpressionConstraint setConceptSingle(ConceptReference concept) {
    this.conceptSingle = concept;
    return this;
  }

  public ExpressionConstraint setRefinementItems(List<BuilderComponent> refinementItems) {
    this.refinementItems = refinementItems;
    return this;
  }

  public ExpressionConstraint addRefinementItem(BuilderComponent refinementItem) {
    if (null == this.refinementItems) this.refinementItems = new ArrayList<>();
    this.refinementItems.add(refinementItem);
    return this;
  }

  public ExpressionConstraint setConceptBool(BoolGroup conceptBool) {
    this.conceptBool = conceptBool;
    return this;
  }

  @Override
  public boolean isConcept() {
    return true;
  }

  @Override
  public ExpressionConstraint asConcept() {
    return this;
  }
}
