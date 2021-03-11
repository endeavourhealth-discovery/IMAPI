package org.endeavourhealth.dataaccess.graph.tripletree;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

public class TTConceptDeserializer extends StdDeserializer<TTConcept> {
    private Map<String, String> prefixMap = new HashMap<>();

    public TTConceptDeserializer() {
        this(null);
    }

    public TTConceptDeserializer(Class<TTConcept> vc) {
        super(vc);
    }

    @Override
    public TTConcept deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        TTConcept result = new TTConcept();

        populatePrefixesFromJson(result, (ArrayNode) node.get("prefixes"));
        result.setIri(expand(node.get("iri").asText()));
        populateTTNodeFromJson(result, node);

        return result;
    }

    private void populatePrefixesFromJson(TTConcept result, ArrayNode prefixes) {
        if (prefixes != null && prefixes.elements() != null) {
            Iterator<JsonNode> iterator = prefixes.elements();
            while (iterator.hasNext()) {
                JsonNode prefix = iterator.next();
                result.addPrefix(prefix.get("iri").asText(), prefix.get("prefix").asText());
                prefixMap.put(prefix.get("prefix").asText(), prefix.get("iri").asText());
            }
        }
    }

    private void populateTTNodeFromJson(TTNode result, JsonNode node) {
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> field = iterator.next();
            String key = field.getKey();
            JsonNode value = field.getValue();

            if (!"iri".equals(key) && !"prefixes".equals(key)) {
                if (value.isArray()) {
                    result.set(iri(expand(field.getKey())), getArrayNodeAsTripleTreeArray((ArrayNode) value));
                } else {
                    result.set(iri(expand(field.getKey())), getJsonNodeAsValue(value));
                }
            }
        }
    }

    private TTArray getArrayNodeAsTripleTreeArray(ArrayNode arrayNode) {
        TTArray result = new TTArray();

        Iterator<JsonNode> iterator = arrayNode.elements();
        while(iterator.hasNext()) {
            JsonNode value = iterator.next();
            result.add(getJsonNodeAsValue(value));
        }

        return result;
    }

    private TTValue getJsonNodeAsValue(JsonNode node) {
        if (node.isTextual())
            return new TTLiteral(literal(node.asText()));
/*
        if (node.has("value") && node.has("type") && node.size() == 2)
            return new TripleTreeLiteral(literal(node.get("value").asText(), iri(node.get("type").asText())));
*/
        else if (node.isObject()) {
            if (node.has("iri")) {
                if (node.has("name"))
                    return new TTIriRef(iri(expand(node.get("iri").asText())), node.get("name").asText());
                else
                    return new TTIriRef(iri(expand(node.get("iri").asText())));
            } else {
                TTNode result = new TTNode();
                populateTTNodeFromJson(result, node);
                return result;
            }
        } else if (node.isArray()) {
            return  getArrayNodeAsTripleTreeArray((ArrayNode) node);
        } else {
            System.err.println("Unhandled node type!");
            return null;
        }
    }

    private String expand(String iri) {
        String prefix = iri.substring(0, iri.indexOf(":") + 1);
        String path = prefixMap.get(prefix);
        if (path == null)
            return iri;
        else
            return iri.replace(prefix, path);
    }
}
