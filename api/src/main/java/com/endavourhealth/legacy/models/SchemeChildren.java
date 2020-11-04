package com.endavourhealth.legacy.models;

import java.util.ArrayList;
import java.util.List;

public class SchemeChildren {
    private String name;
    private String iri;
    private List<SchemeChild> children;

    public String getName() {
        return name;
    }

    public SchemeChildren setName(String name) {
        this.name = name;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public SchemeChildren setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public List<SchemeChild> getChildren() {
        return children;
    }

    public SchemeChildren setChildren(List<SchemeChild> children) {
        this.children = children;
        return this;
    }

    public SchemeChildren addChild(SchemeChild schemeChild) {
        if (this.children == null)
            this.children = new ArrayList<>();

        this.children.add(schemeChild);

        return this;
    }
}
