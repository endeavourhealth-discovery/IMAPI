package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.ConceptReference;

import java.util.ArrayList;
import java.util.List;

public class ValueSet {
    private ConceptReference valueSet;
    private ConceptReference relationship;
    private List<ValueSetMember> included;
    private List<ValueSetMember> excluded;

    public ConceptReference getValueSet() {
        return valueSet;
    }

    public ValueSet setValueSet(ConceptReference valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    public ConceptReference getRelationship() {
        return relationship;
    }

    public ValueSet setRelationship(ConceptReference relationship) {
        this.relationship = relationship;
        return this;
    }

    public List<ValueSetMember> getIncluded() {
        return included;
    }

    public ValueSet setIncluded(List<ValueSetMember> included) {
        this.included = included;
        return this;
    }

    public ValueSet addIncluded(ValueSetMember vsm) {
        if (this.included == null)
            this.included = new ArrayList<>();
        this.included.add(vsm);
        return this;
    }

    public List<ValueSetMember> getExcluded() {
        return excluded;
    }

    public ValueSet setExcluded(List<ValueSetMember> excluded) {
        this.excluded = excluded;
        return this;
    }

    public ValueSet addExcluded(ValueSetMember vsm) {
        if (this.excluded == null)
            this.excluded = new ArrayList<>();
        this.excluded.add(vsm);
        return this;
    }
}
