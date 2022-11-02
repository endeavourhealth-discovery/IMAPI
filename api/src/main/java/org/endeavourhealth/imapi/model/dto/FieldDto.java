package org.endeavourhealth.imapi.model.dto;

public class FieldDto {
    private String name;
    private String firstType;
    private String secondType;

    public FieldDto(String name, String firstType, String secondType) {
        this.name = name;
        this.firstType = firstType;
        this.secondType = secondType;
    }

    public String getName() {
        return name;
    }

    public FieldDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getFirstType() {
        return firstType;
    }

    public FieldDto setFirstType(String firstType) {
        this.firstType = firstType;
        return this;
    }

    public String getSecondType() {
        return secondType;
    }

    public FieldDto setSecondType(String secondType) {
        this.secondType = secondType;
        return this;
    }
}
