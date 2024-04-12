package org.endeavourhealth.imapi.model.ecl;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface EclValue {
    @JsonIgnore
    default boolean isExpressionConstraint() {return false;}

    default ExpressionConstraint asExpressionConstraint() {return null;}

    @JsonIgnore
    default boolean isRefinedExpressionConstraint() {return false;}

    default RefinedExpressionConstraint asRefinedExpressionConstraint() {return null;}

    @JsonIgnore
    default boolean isCompoundExpressionConstraint() {return false;}

    default CompoundExpressionConstraint asCompoundExpressionConstraint() {return null;}

    @JsonIgnore
    default boolean isSubExpressionConstraint() {return false;}

    default SubExpressionConstraint asSubExpressionConstraint() {return null;}

    @JsonIgnore
    default boolean isRefinement() {return false;}

    default Refinement asRefinement() {return null;}

    @JsonIgnore
    default boolean isCompoundRefinementSet() {return false;}

    default CompoundRefinementSet asCompoundRefinementSet() {return null;}

    @JsonIgnore
    default boolean isSubRefinement() {return false;}

    default SubRefinement asSubRefinement() {return null;}

    @JsonIgnore
    default boolean isAttributeSet() {return false;}

    default AttributeSet asAttributeSet() {return null;}

    @JsonIgnore
    default boolean isSubAttributeSet() {return false;}

    default SubAttributeSet asSubAttributeSet() {return null;}

    @JsonIgnore
    default boolean isCompoundAttributeSet() {return false;}

    default CompoundAttributeSet asCompoundAttributeSet() {return null;}

    @JsonIgnore
    default boolean isAttributeGroup() {return false;}

    default AttributeGroup asAttributeGroup() {return null;}

    @JsonIgnore
    default boolean isAttribute() {return false;}

    default Attribute asAttribute() {return null;}

    default String getType() {return null;}

    default EclValue setType() {return this;}
}
