package org.endeavourhealth.imapi.model;

import java.util.HashSet;
import java.util.Set;

public class ConceptNode extends Concept{
    private Set<ConceptNode> parents;
    private Set<ConceptNode> children;
    private String moduleId;

    public Set<ConceptNode> getParents() {
        return parents;
    }

    public ConceptNode setParents(Set<ConceptNode> parents) {
        this.parents = parents;
        return this;
    }

    public ConceptNode addParent(ConceptNode parent) {
        if (this.parents == null)
            this.parents = new HashSet<>();

        this.parents.add(parent);

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
        if (this.children == null)
            this.children = new HashSet<>();
        children.add(child);
        return this;
    }

    public String getModuleId() {
        return moduleId;
    }

    public ConceptNode setModuleId(String moduleId) {
        this.moduleId = moduleId;
        return this;
    }
}

