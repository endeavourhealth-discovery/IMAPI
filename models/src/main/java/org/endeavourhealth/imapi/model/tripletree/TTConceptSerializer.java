package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTConceptSerializer extends StdSerializer<TTConcept> {
    private Map<String, String> contextMap = new HashMap<>();

    public TTConceptSerializer() {
        this(null);
    }

    public TTConceptSerializer(Class<TTConcept> t) {
        super(t);
    }

    @Override
    public void serialize(TTConcept concept, JsonGenerator gen, SerializerProvider prov) throws IOException {

        gen.writeStartObject();
        serializeContexts(concept.getPrefixes(), gen);
        gen.writeStringField("@id",prefix(concept.getIri()));
        serializeNode(concept, gen);
        gen.writeEndObject();
    }

    private void serializeContexts(List<TTPrefix> prefixes, JsonGenerator gen) throws IOException {
        if (prefixes != null && !prefixes.isEmpty()) {
            gen.writeFieldName("@context");
            gen.writeStartObject();

            for(TTPrefix prefix : prefixes) {
                contextMap.put(prefix.getIri(), prefix.getPrefix());
                gen.writeStringField(prefix.getPrefix(),prefix.getIri());
            }

            gen.writeEndObject();
        }
    }

    private void serializeNode(TTNode node, JsonGenerator gen) throws IOException {
        HashMap<TTIriRef, TTValue> predicates = node.getPredicateMap();
        if (predicates != null && !predicates.isEmpty()) {
            Set<Map.Entry<TTIriRef, TTValue>> entries = predicates.entrySet();
            for (Map.Entry<TTIriRef, TTValue> entry : entries) {
                serializeFieldValue(entry.getKey().getIri(), entry.getValue(), gen);
            }
        }
    }

    private void serializeFieldValue(String field, TTValue value, JsonGenerator gen) throws IOException {
        if (value.isList()) {
            gen.writeArrayFieldStart(prefix(field));
            serializeValue(value, gen);
            gen.writeEndArray();
        } else {
            if (value.isLiteral()) {
                if (value.asLiteral().getValue() != null) {
                    gen.writeFieldName(prefix(field));
                    serializeValue(value, gen);
                }
            } else {
                gen.writeFieldName(prefix(field));
                serializeValue(value, gen);
            }
        }
    }

    private void serializeValue(TTValue value, JsonGenerator gen) throws IOException {
        if (value.isIriRef()) {
            TTIriRef ref = value.asIriRef();
            gen.writeStartObject();
            gen.writeStringField("@id", prefix(ref.getIri()));
            if (ref.getName() != null && !ref.getName().isEmpty())
                gen.writeStringField("name", ref.getName());
            gen.writeEndObject();
        } else if (value.isLiteral()) {
            if (value.asLiteral().getType()!=null){
                gen.writeStartObject();
                gen.writeStringField("@value",value.asLiteral().getValue());
                gen.writeStringField("@type",prefix(value.asLiteral().getType().getIri()));
                gen.writeEndObject();
            } else
                gen.writeString(value.asLiteral().getValue());

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
        int end = Integer.max(iri.lastIndexOf("/"), iri.lastIndexOf("#"));
        String path = iri.substring(0, end + 1);
        String prefix = contextMap.get(path);
        if (prefix == null)
            return iri;
        else
            return prefix + ":" + iri.substring(end + 1);
    }
}
