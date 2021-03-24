package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

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

        populatePrefixesFromJson(result, node);
        populateConceptFromJson(node, result);

        return result;
    }

    private void populateConceptFromJson(JsonNode node, TTConcept result) throws IOException {
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode value = field.getValue();
            if ("@id".equals(key))
                result.setIri(expand(value.textValue()));
            else if (!"@context".equals(key))
                result.set(iri(expand(key)),getJsonNodeAsValue(value));
        }
    }

    private void populatePrefixesFromJson(TTConcept concept, JsonNode document) {
        JsonNode context= document.get("@context");
        if (context!=null){
            Iterator<Map.Entry<String, JsonNode>> fields = context.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key= field.getKey();
                JsonNode value= field.getValue();
                if (value.isTextual())
                    if (value.textValue().startsWith("http:")) {
                        concept.addPrefix(new TTPrefix(value.textValue(), key));
                        prefixMap.put(key,value.asText());
                    }
            }
        }
    }

    private void populateTTNodeFromJson(TTNode result, JsonNode node) throws IOException {
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> field = iterator.next();
            String key = field.getKey();
            if (!"@context".equals(key)) {
                JsonNode value = field.getValue();
                if (value.isArray()) {
                    result.set(iri(expand(key)), getArrayNodeAsTripleTreeArray((ArrayNode) value));
                } else {
                    result.set(iri(expand(key)), getJsonNodeAsValue(value));
                }
            }
        }
    }

    private TTArray getArrayNodeAsTripleTreeArray(ArrayNode arrayNode) throws IOException {
        TTArray result = new TTArray();

        Iterator<JsonNode> iterator = arrayNode.elements();
        while(iterator.hasNext()) {
            JsonNode value = iterator.next();
            result.add(getJsonNodeAsValue(value));
        }

        return result;
    }

    private TTValue getJsonNodeAsValue(JsonNode node) throws IOException {
        if (node.isTextual())
            return literal(node.asText());
        else if (node.isObject()) {
            if (node.has("@id")) {
                if (node.has("name"))
                    return iri(expand(node.get("@id").asText()), node.get("name").asText());
                else
                    return iri(expand(node.get("@id").asText()));
            } else {
                if (node.has("@value")){
                    return getJsonNodeAsLiteral(node);
                } else {
                    TTNode result = new TTNode();
                    populateTTNodeFromJson(result, node);
                    return result;
                }
            }
        } else if (node.isArray()) {
            return  getArrayNodeAsTripleTreeArray((ArrayNode) node);
        } else {
            System.err.println("Unhandled node type!");
            return null;
        }
    }

    private TTLiteral getJsonNodeAsLiteral(JsonNode node) throws IOException {
        if (!node.has("@type"))
            return literal(node.get("@value").textValue());

        TTIriRef type = iri(node.get("@type").asText());
        if (XSD.STRING.equals(type))
            return literal(node.get("@value").textValue());
        else if (XSD.BOOLEAN.equals(type))
            return literal(node.get("@value").booleanValue());
        else if (XSD.INTEGER.equals(type))
            return literal(node.get("@value").intValue());
        else if (XSD.LONG.equals(type))
            return literal(node.get("@value").longValue());
        else if (XSD.PATTERN.equals(type))
            return literal(Pattern.compile(node.get("@value").textValue()));
        else
            throw new IOException("Unhandled literal type ["+type.getIri()+"]");
    }

    private String expand(String iri) {
        int colonPos = iri.indexOf(":");
        String prefix = iri.substring(0, colonPos);
        String path = prefixMap.get(prefix);
        if (path == null)
            return iri;
        else
            return path + iri.substring(colonPos + 1);
    }
}
