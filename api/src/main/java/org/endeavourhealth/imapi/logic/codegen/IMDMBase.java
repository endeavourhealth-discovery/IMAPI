package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@JsonSerialize(using = IMDMSerializer.class)
@JsonDeserialize(using = IMDMDeserializer.class)
public class IMDMBase<B> {
    Map<String, Object> properties = new HashMap<>();
    String _type;

    UUID _id;

    public IMDMBase(String type, UUID id) {
        this._type = type;
        this._id = id;
    }

    public <T> T getProperty(String name) {
        return (T) properties.get(name);
    }

    public B setProperty(String propertyName, Object value) {
        this.properties.put(propertyName, value);
        return (B) this;
    }

    public UUID getId() {
        return _id;
    }
}
