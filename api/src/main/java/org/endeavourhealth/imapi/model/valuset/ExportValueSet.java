package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExportValueSet implements Serializable {
    private TTIriRef valueSet;
    private List<ValueSetMember> members = new ArrayList<>();
    private boolean limited = false;

    public TTIriRef getValueSet() {
        return valueSet;
    }

    public ExportValueSet setValueSet(TTIriRef valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    public List<ValueSetMember> getMembers() {
        return members;
    }

    public ExportValueSet setMembers(List<ValueSetMember> includedMembers) {
        this.members = includedMembers;
        return this;
    }

    public ExportValueSet addMembers(ValueSetMember vsm) {
        if (this.members == null)
            this.members = new ArrayList<>();
        this.members.add(vsm);
        return this;
    }

    public ExportValueSet addAllMembers(Collection<ValueSetMember> vsm) {
        if (this.members == null)
            this.members = new ArrayList<>();
        this.members.addAll(vsm);
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
