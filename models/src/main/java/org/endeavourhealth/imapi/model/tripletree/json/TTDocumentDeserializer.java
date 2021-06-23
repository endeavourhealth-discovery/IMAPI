package org.endeavourhealth.imapi.model.tripletree.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.io.IOException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TTDocumentDeserializer extends StdDeserializer<TTDocument> {
    private TTContext context = new TTContext();
    private TTNodeDeserializer helper;

    public TTDocumentDeserializer() {
        this(null);
    }

    public TTDocumentDeserializer(Class<TTDocument> vc) {
        super(vc);
    }

    @Override
    public TTDocument deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        TTDocument result = new TTDocument();
        helper = new TTNodeDeserializer(context);

        List<TTPrefix> prefixes = new ArrayList<>();

        helper.populatePrefixesFromJson(node, prefixes);
        if (!prefixes.isEmpty())
            result.setContext(context);
        if (node.get("@graph") != null)
            result.setGraph(iri(helper.expand(node.get("@graph").get("@id").asText())));
        if (node.get("crud") != null)
            result.setCrud(iri(helper.expand(node.get("crud").get("@id").asText())));
        if (node.get("entities") != null) {
            result.setEntities(getEntities(node.withArray("entities")));
        }


        return result;
    }


    private List<TTEntity> getEntities(ArrayNode arrayNode) throws IOException {
        List<TTEntity> result = new ArrayList<>();
        Iterator<JsonNode> iterator = arrayNode.elements();
        while (iterator.hasNext()) {
            JsonNode entityNode = iterator.next();
            TTEntity entity = new TTEntity();
            result.add(entity);
            Iterator<Map.Entry<String, JsonNode>> fields = entityNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (field.getKey().equals("@id")) {
                    entity.setIri(helper.expand(field.getValue().textValue()));
                    // System.out.println(entity.getIri());
                } else
                    entity.set(iri(helper.expand(field.getKey())), helper.getJsonNodeAsValue(field.getValue()));
            }
        }

        return result;
    }
}
