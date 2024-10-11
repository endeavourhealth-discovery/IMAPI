package org.endeavourhealth.imapi.model.sql;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Table {
    private String table;
    private String condition;
    private HashMap<String, Field> fields = new HashMap<>();
    private HashMap<String, Relationship> relationships = new HashMap<>();

    public Table() {}
}
