package org.endeavourhealth.imapi.logic.codegen;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class DataModelProperty {

    private String name;
    private TTIriRef dataType;
    private boolean isModel;
    private String comment;
    private Integer maxCount;
    private Integer minCount;

    public String getName() {
        return name;
    }

    public DataModelProperty setName(String name) {
        this.name = name;
        return this;
    }

    public TTIriRef getDataType() {
        return dataType;
    }

    public DataModelProperty setDataType(TTIriRef dataType) {
        this.dataType = dataType;
        return this;
    }

    public boolean isModel() {
        return isModel;
    }

    public String getComment() {
        return comment;
    }

    public DataModelProperty setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public DataModelProperty setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public Integer getMinCount() {
        return minCount;
    }

    public DataModelProperty setMinCount(Integer minCount) {
        this.minCount = minCount;
        return this;
    }

    public DataModelProperty setModel(boolean model) {
        isModel = model;
        return this;
    }
}
