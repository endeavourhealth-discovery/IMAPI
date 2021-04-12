package org.endeavourhealth.imapi.model;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class ConceptReferenceNode extends TTIriRef {
    private List<ConceptReferenceNode> parents;
    private List<ConceptReferenceNode> children;
    private String moduleId;
    private boolean hasChildren;
    private TTArray type;
    
    public ConceptReferenceNode() {
    }

    public ConceptReferenceNode(String iri) {
        super(iri);
    }

    public ConceptReferenceNode(String iri, String name) {
        super(iri, name);
    }

    public List<ConceptReferenceNode> getParents() {
        return parents;
    }

    public ConceptReferenceNode setParents(List<ConceptReferenceNode> parents) {
        this.parents = parents;
        return this;
    }

    public ConceptReferenceNode addParent(ConceptReferenceNode parent) {
        if (this.parents == null)
            this.parents = new ArrayList<>();

        this.parents.add(parent);

        return this;
    }

    public List<ConceptReferenceNode> getChildren() {
        return children;
    }

    public ConceptReferenceNode setChildren(List<ConceptReferenceNode> children) {
        this.children = children;
        return this;
    }

    public ConceptReferenceNode addChild(ConceptReferenceNode parent) {
        if (this.children == null)
            this.children = new ArrayList<>();

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

	public ConceptReferenceNode setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
		return this;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public TTArray getType() {
		return type;
	}

	public void setType(TTArray type) {
		this.type = type;
	}
    
    
}
