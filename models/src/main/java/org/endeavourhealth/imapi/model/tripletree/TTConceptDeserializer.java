package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TTConceptDeserializer extends StdDeserializer<TTConcept> {
    private TTContext context = new TTContext();
    private TTNodeDeserializer helper;

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
        helper= new TTNodeDeserializer(context);

        List<TTPrefix> prefixes = new ArrayList<>();

        helper.populatePrefixesFromJson(node,prefixes);
        if (!prefixes.isEmpty())
            result.setContext(context);
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
                result.setIri(helper.expand(value.textValue()));
            else if (!"@context".equals(key))
                result.set(iri(helper.expand(key)),helper.getJsonNodeAsValue(value));
        }
    }


}
