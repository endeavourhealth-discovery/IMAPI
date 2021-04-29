package org.endeavourhealth.imapi.model.report;

public class SimpleCount {

    String label;
    Integer count;

    public SimpleCount() {
    }

    public SimpleCount(String label, Integer count) {
        this.label = label;
        this.count = count;
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
