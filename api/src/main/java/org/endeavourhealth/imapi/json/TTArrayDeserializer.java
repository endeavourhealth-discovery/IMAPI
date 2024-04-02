package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTNode;

import java.io.IOException;
import java.util.Iterator;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TTArrayDeserializer extends StdDeserializer<TTArray> {
    public TTArrayDeserializer() {
        this(null);
    }

    public TTArrayDeserializer(Class<TTArray> t) {
        super(t);
    }

    @Override
    public TTArray deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
        ArrayNode array = jsonParser.getCodec().readTree(jsonParser);

        return getNodeAsArray(jsonParser, ctx, array);
    }

    private TTArray getNodeAsArray(JsonParser jsonParser, DeserializationContext ctx, ArrayNode array) throws IOException {

        TTArray result = new TTArray();

        Iterator<JsonNode> elements = array.elements();
        while(elements.hasNext()) {
            JsonNode node = elements.next();
            if (node.isTextual())
                result.add(literal(node.asText()));
            else if (node.isArray())
                throw new IOException("Cant deserialize array of arrays");
            else if (node.isObject()) {
                if (node.has("@id"))
                    result.add(iri(node.get("@id").textValue()));
                else
                    result.add(ctx.readValue(node.traverse(jsonParser.getCodec()), TTNode.class));
            } else
                result.add(literal(node.asText()));
        }

        return result;
    }
}
