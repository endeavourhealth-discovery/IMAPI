package com.endavourhealth.models.objectModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MapFrom {
    private String table;
    private String clazz;
    private String field;
    private String property;
    private String graphPath;
    private MapFromJoin join;

    public String getTable() {
        return table;
    }

    public MapFrom setTable(String table) {
        this.table = table;
        return this;
    }

    @JsonProperty("class")
    public String getClazz() {
        return clazz;
    }

    public MapFrom setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getField() {
        return field;
    }

    public MapFrom setField(String field) {
        this.field = field;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public MapFrom setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getGraphPath() {
        return graphPath;
    }

    public MapFrom setGraphPath(String graphPath) {
        this.graphPath = graphPath;
        return this;
    }

    public MapFromJoin getJoin() {
        return join;
    }

    public MapFrom setJoin(MapFromJoin join) {
        this.join = join;
        return this;
    }
}
