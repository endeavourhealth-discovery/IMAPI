package org.endeavourhealth.imapi.model.tripletree;

import java.util.HashSet;
import java.util.Set;

public class TTBundle {
    private TTEntity entity;
    private Set<TTIriRef> predicates = new HashSet<>();

    public TTEntity getEntity() {
        return entity;
    }

    public TTBundle setEntity(TTEntity entity) {
        this.entity = entity;
        return this;
    }

    public Set<TTIriRef> getPredicates() {
        return predicates;
    }

    public TTBundle setPredicates(Set<TTIriRef> predicates) {
        this.predicates = predicates;
        return this;
    }

    public TTBundle addPredicate(TTIriRef predicate) {
        if (null == this.predicates)
            this.predicates = new HashSet<>();

        predicates.add(predicate);

        return this;
    }
}
