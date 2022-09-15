package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;

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
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            this.object = om.readValue(json, Class.forName(valueType));
        }
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
