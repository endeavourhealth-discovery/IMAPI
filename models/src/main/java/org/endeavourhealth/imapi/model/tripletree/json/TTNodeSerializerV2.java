package org.endeavourhealth.imapi.model.tripletree.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.io.IOException;
import java.util.*;

/**
 * Serializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument serializer
 */
public class TTNodeSerializerV2 extends StdSerializer<TTNode> {
    private TTContext contextMap;
    private List<TTIriRef> predicateTemplate;

    public TTNodeSerializerV2() {
        this(null);
    }
    public TTNodeSerializerV2(Class<TTNode> t) {
        super(t);
    }
   /**
    *
    * @param contextMap the context object for the JSON-LD document
    */
   public TTNodeSerializerV2(Class<TTNode> t, TTContext contextMap){
       super(t);
      this.contextMap = contextMap;
   }
   public TTNodeSerializerV2(Class<TTNode> t, TTContext contextMap, List<TTIriRef> predicateTemplate){
       super(t);
      this.contextMap = contextMap;
      this.predicateTemplate= predicateTemplate;
   }

    public void serialize(TTNode node, JsonGenerator gen, SerializerProvider prov) throws IOException {
        if (predicateTemplate == null)
            predicateTemplate = new ArrayList<>();

        gen.writeStartObject();
        serializeTemplatedPredicates(node, gen, prov);
        serializeRemainingPredicates(node, gen, prov);
        gen.writeEndObject();
    }

    private void serializeTemplatedPredicates(TTNode node, JsonGenerator gen, SerializerProvider prov) throws IOException {
        for (TTIriRef predicate : predicateTemplate) {
            if (node.get(predicate) != null)
                prov.defaultSerializeField(prefix(predicate.getIri()), node.get(predicate), gen);
        }
    }

    private void serializeRemainingPredicates(TTNode node, JsonGenerator gen, SerializerProvider prov) throws IOException {
        Map<TTIriRef, TTValue> predicates = node.getPredicateMap();
        if (predicates != null && !predicates.isEmpty()) {
            Set<Map.Entry<TTIriRef, TTValue>> entries = predicates.entrySet();
            for (Map.Entry<TTIriRef, TTValue> entry : entries) {
                if (!predicateTemplate.contains(entry.getKey())) {
                    prov.defaultSerializeField(prefix(entry.getKey().getIri()), entry.getValue(), gen);
                }
            }
        }
    }

    public String prefix(String iri) {
       if (contextMap == null)
        return iri;
       else
           return contextMap.prefix(iri);
   }
}
