package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExportValueSet {
    private TTIriRef valueSet;
    // private EntityReference relationship;
    private List<ValueSetMember> included = new ArrayList<>();
    private List<ValueSetMember> excluded = new ArrayList<>();

    public TTIriRef getValueSet() {
        return valueSet;
    }

    public ExportValueSet setValueSet(TTIriRef valueSet) {
        this.valueSet = valueSet;
        return this;
    }

//    public EntityReference getRelationship() {
//        return relationship;
//    }
//
//    public ExportValueSet setRelationship(EntityReference relationship) {
//        this.relationship = relationship;
//        return this;
//    }

    public List<ValueSetMember> getIncluded() {
        return included;
    }

    public ExportValueSet setIncluded(List<ValueSetMember> included) {
        this.included = included;
        return this;
    }

    public ExportValueSet addIncluded(ValueSetMember vsm) {
        if (this.included == null)
            this.included = new ArrayList<>();
        this.included.add(vsm);
        return this;
    }

    public ExportValueSet addAllIncluded(Collection<ValueSetMember> vsm) {
        if (this.included == null)
            this.included = new ArrayList<>();
        this.included.addAll(vsm);
        return this;
    }

    public List<ValueSetMember> getExcluded() {
        return excluded;
    }

    public ExportValueSet setExcluded(List<ValueSetMember> excluded) {
        this.excluded = excluded;
        return this;
    }

    public ExportValueSet addExcluded(ValueSetMember vsm) {
        if (this.excluded == null)
            this.excluded = new ArrayList<>();
        this.excluded.add(vsm);
        return this;
    }

    public ExportValueSet addAllExcluded(Collection<ValueSetMember> vsm) {
        if (this.excluded == null)
            this.excluded = new ArrayList<>();
        this.excluded.addAll(vsm);
        return this;
    }
}
