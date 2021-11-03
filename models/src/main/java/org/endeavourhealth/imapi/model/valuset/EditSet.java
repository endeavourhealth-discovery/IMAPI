package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EditSet {
    private Set<TTIriRef> incs;
    private Set<TTIriRef> excs;

    public Set<TTIriRef> getIncs() {
        return incs;
    }

    public EditSet setIncs(Set<TTIriRef> incs) {
        this.incs = incs;
        return this;
    }

    public EditSet addInc(TTIriRef inc) {
        if (incs == null)
            incs = new HashSet<>();

        incs.add(inc);
        return this;
    }

    public EditSet addAllIncs(Collection<TTIriRef> inc) {
        if (incs == null)
            incs = new HashSet<>();

        incs.addAll(inc);
        return this;
    }

    public Set<TTIriRef> getExcs() {
        return excs;
    }

    public EditSet setExcs(Set<TTIriRef> excs) {
        this.excs = excs;
        return this;
    }

    public EditSet addExc(TTIriRef exc) {
        if (excs == null)
            excs = new HashSet<>();

        excs.add(exc);
        return this;
    }

    public EditSet addAllExcs(Collection<TTIriRef> exc) {
        if (excs == null)
            excs = new HashSet<>();

        excs.addAll(exc);
        return this;
    }
}
