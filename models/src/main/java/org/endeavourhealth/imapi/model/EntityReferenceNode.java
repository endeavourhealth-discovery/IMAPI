package org.endeavourhealth.imapi.model;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class EntityReferenceNode extends TTIriRef {
    private List<EntityReferenceNode> parents;
    private List<EntityReferenceNode> children;
    private String moduleId;
    private boolean hasChildren;
    private TTArray type;
    
    public EntityReferenceNode() {
    }

    public EntityReferenceNode(String iri) {
        super(iri);
    }

    public EntityReferenceNode(String iri, String name) {
        super(iri, name);
    }

    public List<EntityReferenceNode> getParents() {
        return parents;
    }

    public EntityReferenceNode setParents(List<EntityReferenceNode> parents) {
        this.parents = parents;
        return this;
    }

    public EntityReferenceNode addParent(EntityReferenceNode parent) {
        if (this.parents == null)
            this.parents = new ArrayList<>();

        this.parents.add(parent);

        return this;
    }

    public List<EntityReferenceNode> getChildren() {
        return children;
    }

    public EntityReferenceNode setChildren(List<EntityReferenceNode> children) {
        this.children = children;
        return this;
    }

    public EntityReferenceNode addChild(EntityReferenceNode parent) {
        if (this.children == null)
            this.children = new ArrayList<>();

        this.children.add(parent);

        return this;
    }

    public String getModuleId() {
        return moduleId;
    }

    public EntityReferenceNode setModuleId(String moduleId) {
        this.moduleId = moduleId;
        return this;
    }

	public EntityReferenceNode setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
		return this;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public TTArray getType() {
		return type;
	}

	public EntityReferenceNode setType(TTArray type) {
		this.type = type;
		return this;
	}
    
    
}
