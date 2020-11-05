package com.endavourhealth.models.objectModel;

import java.util.ArrayList;
import java.util.List;

public class MapCreate {
    private String fieldContext;
    private String tableContext;
    private String propertyContext;
    private String objectContext;

    private String object;
    private String table;
    private String property;
    private MapRelationship relationship;

    private MapCreateValue value;
    private String as;
    private List<MapData> map;

    public String getFieldContext() {
        return fieldContext;
    }

    public MapCreate setFieldContext(String fieldContext) {
        this.fieldContext = fieldContext;
        return this;
    }

    public String getTableContext() {
        return tableContext;
    }

    public MapCreate setTableContext(String tableContext) {
        this.tableContext = tableContext;
        return this;
    }

    public String getPropertyContext() {
        return propertyContext;
    }

    public MapCreate setPropertyContext(String propertyContext) {
        this.propertyContext = propertyContext;
        return this;
    }

    public String getObjectContext() {
        return objectContext;
    }

    public MapCreate setObjectContext(String objectContext) {
        this.objectContext = objectContext;
        return this;
    }

    public String getObject() {
        return object;
    }

    public MapCreate setObject(String object) {
        this.object = object;
        return this;
    }

    public String getTable() {
        return table;
    }

    public MapCreate setTable(String table) {
        this.table = table;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public MapCreate setProperty(String property) {
        this.property = property;
        return this;
    }

    public MapRelationship getRelationship() {
        return relationship;
    }

    public MapCreate setRelationship(MapRelationship relationship) {
        this.relationship = relationship;
        return this;
    }

    public MapCreateValue getValue() {
        return value;
    }

    public MapCreate setValue(MapCreateValue value) {
        this.value = value;
        return this;
    }

    public String getAs() {
        return as;
    }

    public MapCreate setAs(String as) {
        this.as = as;
        return this;
    }

    public List<MapData> getMap() {
        return map;
    }

    public MapCreate setMap(List<MapData> map) {
        this.map = map;
        return this;
    }

    public MapCreate addMap(MapData map) {
        if (this.map == null)
            this.map = new ArrayList<>();
        this.map.add(map);
        return this;
    }
}
