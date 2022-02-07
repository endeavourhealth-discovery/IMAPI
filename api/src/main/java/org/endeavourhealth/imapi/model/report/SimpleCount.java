package org.endeavourhealth.imapi.model.report;

public class SimpleCount {
    String iri;
    String label;
    Integer count;

    public SimpleCount() {
    }

    public SimpleCount(String label, Integer count) {
        this.label = label;
        this.count = count;
    }

    public SimpleCount(String iri, String label, Integer count) {
        this.iri = iri;
        this.label = label;
        this.count = count;
    }

    public String getIri() {
        return iri;
    }

    public SimpleCount setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public SimpleCount setLabel(String label) {
        this.label = label;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public SimpleCount setCount(Integer count) {
        this.count = count;
        return this;
    }
}
