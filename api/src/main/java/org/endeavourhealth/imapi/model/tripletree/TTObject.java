package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TTObject implements TTValue {

    public static TTObject object(Object object) {
        return new TTObject(object);
    }

    public static TTObject object(String json, String valueType) throws ClassNotFoundException, JsonProcessingException {
        return new TTObject(json, valueType);
    }
    private Object object;

    public TTObject() {
    }

    public TTObject(Object object) {
        this.object = object;
    }

    public TTObject(String json, String valueType) throws ClassNotFoundException, JsonProcessingException {
            this.object = new ObjectMapper().readValue(json, Class.forName(valueType));
    }

    public Object getObject() {
        return object;
    }

    public TTObject setObject(Object object) {
        this.object = object;
        return this;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public TTObject asObject() {
        return this;
    }
}
