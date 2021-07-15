package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExportValueSet implements Serializable {
    private TTIriRef valueSet;
    private List<ValueSetMember> includedMembers = new ArrayList<>();
    private List<ValueSetMember> excludedMembers = new ArrayList<>();
    private List<ValueSetMember> includedSubsets = new ArrayList<>();
    private boolean limited = false;

    public TTIriRef getValueSet() {
        return valueSet;
    }

    public ExportValueSet setValueSet(TTIriRef valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    public List<ValueSetMember> getIncludedMembers() {
        return includedMembers;
    }

    public ExportValueSet setIncludedMembers(List<ValueSetMember> includedMembers) {
        this.includedMembers = includedMembers;
        return this;
    }

    public ExportValueSet addIncludedMembers(ValueSetMember vsm) {
        if (this.includedMembers == null)
            this.includedMembers = new ArrayList<>();
        this.includedMembers.add(vsm);
        return this;
    }

    public ExportValueSet addAllIncludedMembers(Collection<ValueSetMember> vsm) {
        if (this.includedMembers == null)
            this.includedMembers = new ArrayList<>();
        this.includedMembers.addAll(vsm);
        return this;
    }

    public List<ValueSetMember> getExcludedMembers() {
        return excludedMembers;
    }

    public ExportValueSet setExcludedMembers(List<ValueSetMember> excludedMembers) {
        this.excludedMembers = excludedMembers;
        return this;
    }

    public ExportValueSet addExcludedMembers(ValueSetMember vsm) {
        if (this.excludedMembers == null)
            this.excludedMembers = new ArrayList<>();
        this.excludedMembers.add(vsm);
        return this;
    }

    public ExportValueSet addAllExcludedMembers(Collection<ValueSetMember> vsm) {
        if (this.excludedMembers == null)
            this.excludedMembers = new ArrayList<>();
        this.excludedMembers.addAll(vsm);
        return this;
    }

    public List<ValueSetMember> getIncludedSubsets() {
        return includedSubsets;
    }

    public ExportValueSet setIncludedSubsets(List<ValueSetMember> includedSets) {
        this.includedSubsets = includedSets;
        return this;
    }

    public ExportValueSet addIncludedSubsets(ValueSetMember vsm) {
        if (this.includedSubsets == null)
            this.includedSubsets = new ArrayList<>();
        this.includedSubsets.add(vsm);
        return this;
    }

    public ExportValueSet addAllIncludedSubsets(Collection<ValueSetMember> vsm) {
        if (this.includedSubsets == null)
            this.includedSubsets = new ArrayList<>();
        this.includedSubsets.addAll(vsm);
        return this;
    }

    public boolean isLimited() {
        return limited;
    }

    public ExportValueSet setLimited(boolean limited) {
        this.limited = limited;
        return this;
    }
}
