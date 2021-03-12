package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TTConceptSerializer extends StdSerializer<TTConcept> {
    private Map<String, String> prefixMap = new HashMap<>();

    public TTConceptSerializer() {
        this(null);
    }

    public TTConceptSerializer(Class<TTConcept> t) {
        super(t);
    }

    @Override
    public void serialize(TTConcept concept, JsonGenerator gen, SerializerProvider prov) throws IOException {
        gen.writeStartObject();
        serializePrefixes(concept.getPrefixes(), gen);
        gen.writeStringField("iri", prefix(concept.getIri()));
        serializeNode(concept, gen);
        gen.writeEndObject();
    }

    private void serializePrefixes(List<TTPrefix> prefixes, JsonGenerator gen) throws IOException {
        if (prefixes != null && !prefixes.isEmpty()) {
            gen.writeArrayFieldStart("prefixes");

            for(TTPrefix prefix : prefixes) {
                prefixMap.put(prefix.getIri(), prefix.getPrefix());
                gen.writeStartObject();
                gen.writeStringField("prefix", prefix.getPrefix());
                gen.writeStringField("iri", prefix.getIri());
                gen.writeEndObject();
            }

            gen.writeEndArray();
        }
    }

    private void serializeNode(TTNode node, JsonGenerator gen) throws IOException {
        HashMap<String, TTValue> predicates = node.getPredicateMap();

        if (predicates != null && !predicates.isEmpty()) {
            Set<Map.Entry<String, TTValue>> entries = predicates.entrySet();
            for(Map.Entry<String, TTValue> entry : entries) {
                serializeFieldValue(entry.getKey().toString(), entry.getValue(), gen);
            }
        }
    }

    private void serializeFieldValue(String field, TTValue value, JsonGenerator gen) throws IOException {
        if (value.isList()) {
            gen.writeArrayFieldStart(prefix(field));
            serializeValue(value, gen);
            gen.writeEndArray();
        } else {
            gen.writeFieldName(prefix(field));
            serializeValue(value, gen);
        }
    }

    private void serializeValue(TTValue value, JsonGenerator gen) throws IOException {
        if (value.isIriRef()) {
            TTIriRef ref = value.asIriRef();

            gen.writeStartObject();
            gen.writeStringField("iri", prefix(ref.getIri()));
            if (ref.getName() != null && !ref.getName().isEmpty())
                gen.writeStringField("name", ref.getName());
            gen.writeEndObject();
        } else if (value.isLiteral()) {
            gen.writeString(value.asLiteral().getValue());
/*
            gen.writeStartObject();
            gen.writeStringField("value", value.asLiteral().stringValue());
            gen.writeStringField("type", value.asLiteral().getDatatype().toString());
            gen.writeEndObject();
*/
        } else if (value.isNode()) {
            gen.writeStartObject();
            serializeNode((TTNode)value, gen);
            gen.writeEndObject();
        } else if (value.isList()) {
            for (TTValue v : ((TTArray)value).elements) {
                serializeValue(v, gen);
            }
        }
    }

    private String prefix(String iri) {
        String path = iri.substring(0, Integer.max(iri.lastIndexOf("/"), iri.lastIndexOf("#")) + 1);
        String prefix = prefixMap.get(path);
        if (prefix == null)
            return iri;
        else
            return iri.replace(path, prefix);
    }
}
