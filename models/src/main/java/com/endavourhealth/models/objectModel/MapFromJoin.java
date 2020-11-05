package com.endavourhealth.models.objectModel;

public class MapFromJoin {
    private String joinTable;
    private String childField;
    private String parentField;

    public String getJoinTable() {
        return joinTable;
    }

    public MapFromJoin setJoinTable(String joinTable) {
        this.joinTable = joinTable;
        return this;
    }

    public String getChildField() {
        return childField;
    }

    public MapFromJoin setChildField(String childField) {
        this.childField = childField;
        return this;
    }

    public String getParentField() {
        return parentField;
    }

    public MapFromJoin setParentField(String parentField) {
        this.parentField = parentField;
        return this;
    }
}
