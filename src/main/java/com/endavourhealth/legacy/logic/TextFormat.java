package com.endavourhealth.legacy.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.endavourhealth.legacy.dal.ViewerDAL;
import com.endavourhealth.legacy.dal.ViewerJDBCDAL;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class TextFormat {
    private final ViewerDAL dal;

    public TextFormat() {
        this.dal = new ViewerJDBCDAL();
    }

    public String get(String iri) throws Exception {
        List<JsonNode> axioms = dal.getAxioms(iri);
        List<String> text = new ArrayList<>();

        for(JsonNode node: axioms) {
            text.add(getDefinition(node, ""));
        }

        return String.join("\n", text);
    }

    private String getDefinition(JsonNode definition, String indent) throws JsonProcessingException {
        if (definition.isArray()) {
            return getArrayDefinition((ArrayNode) definition, indent);
        } else if (definition.isObject()){
            List<String> text = new ArrayList<>();
            for (Iterator<Map.Entry<String, JsonNode>> fields = definition.fields(); fields.hasNext();) {
                Map.Entry<String, JsonNode> field = fields.next();
                text.add(indent + field.getKey() + " (\n" + getDefinition(field.getValue(), indent + "\t") + "\n" + indent + ")");
            }
            return String.join("\n", text);
        } else {
            return indent + definition.toString();
        }
    }

    private String getArrayDefinition(ArrayNode nodes, String indent) throws JsonProcessingException {
        List<String> text = new ArrayList<>();
        text.add(indent + "[");
        for (Iterator<JsonNode> it = nodes.iterator(); it.hasNext(); ) {
            JsonNode node = it.next();
            text.add(getDefinition(node, indent + "\t"));
        }
        text.add(indent + "]");

        return String.join("\n", text);
    }
}
