package com.endavourhealth.models.objectModel;

import java.util.List;

public interface DataRange {
    public String getExactValue();
    public DataRange setExactValue(String value);
    public List<String> getOneOf();
    public DataRange setOneOf(List<String> oneOf);
    public DataRange addOneOf(String value);
    public String getDataType();
    public DataRange setDataType(String dataType);

}
