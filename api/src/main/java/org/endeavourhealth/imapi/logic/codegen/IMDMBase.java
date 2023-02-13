package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = IMDMSerializer.class)
@JsonDeserialize(using = IMDMDeserializer.class)
public class IMDMBase<B> {
    Map<String, Object> properties = new HashMap<>();
    String _type;

    public IMDMBase(String type) {
        this._type = type;
    }

    public <T> T getProperty(String name) {
        return (T) properties.get(name);
    }

    public B setProperty(String propertyName, Object value) {
        this.properties.put(propertyName, value);
        return (B) this;
    }

}
