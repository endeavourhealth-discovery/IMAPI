package org.endeavourhealth.imapi.model;

import lombok.Getter;

@Getter
public class Context {
    String publisher;
    String system;
    String schema;
    String table;
    String field;

    public Context setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public Context setSystem(String system) {
        this.system = system;
        return this;
    }

    public Context setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public Context setTable(String table) {
        this.table = table;
        return this;
    }

    public Context setField(String field) {
        this.field = field;
        return this;
    }
}
