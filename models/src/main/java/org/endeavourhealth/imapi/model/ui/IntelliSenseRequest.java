package org.endeavourhealth.imapi.model.ui;

import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntelliSenseRequest {
    private List<TTValue> index = new ArrayList<>();
    private String action;
    private TTConcept concept;

    public List<TTValue> getIndex() {
        return index;
    }

    public IntelliSenseRequest setIndex(List<TTValue> index) {
        this.index = index;
        return this;
    }

    public IntelliSenseRequest setIndex(TTValue ...index) {
        this.index = Arrays.asList(index);
        return this;
    }

    public IntelliSenseRequest addIndex(TTValue index) {
        if (this.index == null) {
            this.index = new ArrayList<>();
        }
        this.index.add(index);
        return this;
    }

    public String getAction() {
        return action;
    }

    public IntelliSenseRequest setAction(String action) {
        this.action = action;
        return this;
    }

    public TTConcept getConcept() {
        return concept;
    }

    public IntelliSenseRequest setConcept(TTConcept concept) {
        this.concept = concept;
        return this;
    }
}
