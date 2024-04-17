package org.endeavourhealth.imapi.model.eclBuilder;

import lombok.Getter;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Concept extends BuilderComponent{
    private String constraintOperator;
    private Bool conjunction;
    private TTIriRef conceptSingle;
    private List<BuilderComponent> conceptItems;
    private List<BuilderComponent> refinementItems;

    public Concept() {
        super("Concept");
    }

    public Concept setConstraintOperator(String constraintOperator) {
        this.constraintOperator = constraintOperator;
        return this;
    }

    public Concept setConjunction(Bool conjunction) {
        this.conjunction = conjunction;
        return this;
    }

    public Concept setConceptSingle(TTIriRef concept) {
        this.conceptSingle = concept;
        return this;
    }

    public Concept setRefinementItems(List<BuilderComponent> refinementItems) {
        this.refinementItems = refinementItems;
        return this;
    }

    public Concept addRefinementItem(BuilderComponent refinementItem) {
        if (null == this.refinementItems) this.refinementItems = new ArrayList<>();
        this.refinementItems.add(refinementItem);
        return this;
    }

    public Concept setConceptItems(List<BuilderComponent> conceptItems) {
        this.conceptItems = conceptItems;
        return this;
    }

    public Concept addConceptItem(BuilderComponent conceptItem) {
        if (null == this.conceptItems) this.conceptItems = new ArrayList<>();
        this.conceptItems.add(conceptItem);
        return this;
    }

    @Override
    public boolean isConcept() {
        return true;
    }

    @Override
    public Concept asConcept() {
        return this;
    }
}
