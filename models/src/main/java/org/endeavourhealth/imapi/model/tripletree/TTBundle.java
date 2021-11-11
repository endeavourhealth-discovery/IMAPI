package org.endeavourhealth.imapi.model.tripletree;

import java.util.HashMap;
import java.util.Map;

public class TTBundle {
    private TTEntity entity;
    private Map<String, String> predicates = new HashMap<>();

    public TTEntity getEntity() {
        return entity;
    }

    public TTBundle setEntity(TTEntity entity) {
        this.entity = entity;
        return this;
    }

    public Map<String, String> getPredicates() {
        return predicates;
    }

    public TTBundle setPredicates(Map<String, String> predicates) {
        this.predicates = predicates;
        return this;
    }

    public TTBundle addPredicate(TTIriRef predicate) {
        if (null == this.predicates)
            this.predicates = new HashMap<>();

        if(predicate != null)
            predicates.put(predicate.getIri(), predicate.getName());

        return this;
    }
}
