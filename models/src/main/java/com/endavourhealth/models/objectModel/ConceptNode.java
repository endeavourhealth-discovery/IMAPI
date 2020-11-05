package com.endavourhealth.models.objectModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConceptNode {
    private String id;
    private Set<ConceptNode> children = new HashSet<>();
    private boolean isAnonymous;

    public String getId() {
        return id;
    }

    public ConceptNode setId(String id) {
        this.id = id;
        return this;
    }

    public Set<ConceptNode> getChildren() {
        return children;
    }

    public ConceptNode setChildren(Set<ConceptNode> children) {
        this.children = children;
        return this;
    }

    public ConceptNode addChild(ConceptNode child) {
        this.children.add(child);
        return this;
    }

    public ConceptNode addChildren(List<ConceptNode> children) {
        this.children.addAll(children);
        return this;
    }


    public boolean isAnonymous() {
        return isAnonymous;
    }

    public ConceptNode setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
        return this;
    }
}
