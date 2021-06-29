package org.endeavourhealth.imapi.model.tripletree.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TTLiteralDeserializer extends StdDeserializer<TTLiteral> {
    private TTNodeDeserializer helper;

    public TTLiteralDeserializer() {
        this(null);
    }

    public TTLiteralDeserializer(Class<TTLiteral> t) {
        super(t);
    }
    public TTLiteralDeserializer(Class<TTLiteral> t, TTNodeDeserializer helper) {
        super(t);
        this.helper = helper;
    }

    @Override
    public TTLiteral deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if (!node.has("@type")) {
            if (node.isValueNode())
                return literal(node);
            else
                return literal(node.get("@value").textValue());
        }

        TTIriRef type = iri(helper == null ? node.get("@type").asText() : helper.expand(node.get("@type").asText()));
        if (XSD.STRING.equals(type))
            return literal(node.get("@value").textValue());
        else if (XSD.BOOLEAN.equals(type)) {
            return literal(Boolean.valueOf(node.get("@value").asText()));
        }
        else if (XSD.INTEGER.equals(type)) {
            return literal(Integer.valueOf(node.get("@value").asText()));
        }
        else if (XSD.PATTERN.equals(type))
            return literal(Pattern.compile(node.get("@value").textValue()));
        else
            throw new IOException("Unhandled literal type ["+type.getIri()+"]");
    }
}
