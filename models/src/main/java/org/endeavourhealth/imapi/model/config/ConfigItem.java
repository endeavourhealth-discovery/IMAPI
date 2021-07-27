package org.endeavourhealth.imapi.model.config;

public class ConfigItem {
    private String label;
    private String predicate;
    private String type;
    private int size;
    private int order;

    public ConfigItem(String label, String predicate, String type, int size, int order) {
        this.label = label;
        this.predicate = predicate;
        this.type = type;
        this.size = size;
        this.order = order;
    }

    public String getLabel() {
        return label;
    }

    public ConfigItem setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getPredicate() {
        return predicate;
    }

    public ConfigItem setPredicate(String predicate) {
        this.predicate = predicate;
        return this;
    }

    public String getType() {
        return type;
    }

    public ConfigItem setType(String type) {
        this.type = type;
        return this;
    }

    public int getSize() {
        return size;
    }

    public ConfigItem setSize(int size) {
        this.size = size;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public ConfigItem setOrder(int order) {
        this.order = order;
        return this;
    }
}
