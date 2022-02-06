package org.endeavourhealth.imapi.model.config;

import java.util.List;

public class FilterDefault {
    private List<String> schemeOptions;
    private List<String> statusOptions;
    private List<String> typeOptions;

    public FilterDefault() {}

    public FilterDefault(List<String> schemeOptions, List<String> statusOptions, List<String> typeOptions) {
        this.schemeOptions = schemeOptions;
        this.statusOptions = statusOptions;
        this.typeOptions = typeOptions;
    }

    public List<String> getSchemeOptions() {
        return schemeOptions;
    }

    public FilterDefault setSchemeOptions(List<String> schemeOptions) {
        this.schemeOptions = schemeOptions;
        return this;
    }

    public List<String> getStatusOptions() {
        return statusOptions;
    }

    public FilterDefault setStatusOptions(List<String> statusOptions) {
        this.statusOptions = statusOptions;
        return this;
    }

    public List<String> getTypeOptions() {
        return typeOptions;
    }

    public FilterDefault setTypeOptions(List<String> typeOptions) {
        this.typeOptions = typeOptions;
        return this;
    }
}
