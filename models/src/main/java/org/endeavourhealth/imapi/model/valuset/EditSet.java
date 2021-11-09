package org.endeavourhealth.imapi.model.valuset;

import org.endeavourhealth.imapi.model.EntitySummary;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EditSet {
    private Set<EntitySummary> incs;
    private Set<EntitySummary> excs;

    public Set<EntitySummary> getIncs() {
        return incs;
    }

    public EditSet setIncs(Set<EntitySummary> incs) {
        this.incs = incs;
        return this;
    }

    public EditSet addInc(EntitySummary inc) {
        if (incs == null)
            incs = new HashSet<>();

        incs.add(inc);
        return this;
    }

    public EditSet addAllIncs(Collection<EntitySummary> inc) {
        if (incs == null)
            incs = new HashSet<>();

        incs.addAll(inc);
        return this;
    }

    public Set<EntitySummary> getExcs() {
        return excs;
    }

    public EditSet setExcs(Set<EntitySummary> excs) {
        this.excs = excs;
        return this;
    }

    public EditSet addExc(EntitySummary exc) {
        if (excs == null)
            excs = new HashSet<>();

        excs.add(exc);
        return this;
    }

    public EditSet addAllExcs(Collection<EntitySummary> exc) {
        if (excs == null)
            excs = new HashSet<>();

        excs.addAll(exc);
        return this;
    }
}
