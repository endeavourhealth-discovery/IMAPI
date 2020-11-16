package org.endeavourhealth.imapi.model;

import java.util.HashSet;
import java.util.Set;

public class ConceptReferenceNode extends ConceptReference {
    private Set<ConceptReferenceNode> parents;
    private Set<ConceptReferenceNode> children;
    private String moduleId;

    public ConceptReferenceNode() {
    }

    public ConceptReferenceNode(String iri) {
        super(iri);
    }

    public ConceptReferenceNode(String iri, String name) {
        super(iri, name);
    }

    public Set<ConceptReferenceNode> getParents() {
        return parents;
    }

    public ConceptReferenceNode setParents(Set<ConceptReferenceNode> parents) {
        this.parents = parents;
        return this;
    }

    public ConceptReferenceNode addParent(ConceptReferenceNode parent) {
        if (this.parents == null)
            this.parents = new HashSet<>();

        this.parents.add(parent);

        return this;
    }

    public Set<ConceptReferenceNode> getChildren() {
        return children;
    }

    public ConceptReferenceNode setChildren(Set<ConceptReferenceNode> children) {
        this.children = children;
        return this;
    }

    public ConceptReferenceNode addChild(ConceptReferenceNode parent) {
        if (this.children == null)
            this.children = new HashSet<>();

        this.children.add(parent);

        return this;
    }

    public String getModuleId() {
        return moduleId;
    }

    public ConceptReferenceNode setModuleId(String moduleId) {
        this.moduleId = moduleId;
        return this;
    }
}
