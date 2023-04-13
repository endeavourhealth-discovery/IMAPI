package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.io.IOException;
import java.util.List;

/**
 * JSON LD- Serializer for TTDocument (triple tree node with collection of entities)
 * <p>Uses @context for prefixes and common annotation elements</p>
 */
public class TTDocumentSerializer extends StdSerializer<TTDocument> {

    public TTDocumentSerializer() {
        this(null);
    }

    public TTDocumentSerializer(Class<TTDocument> t) {
        super(t);
    }


    @Override
    public void serialize(TTDocument document, JsonGenerator gen, SerializerProvider prov) throws IOException {
        Boolean usePrefixes = (Boolean) prov.getAttribute(TTContext.OUTPUT_CONTEXT);
        usePrefixes = (usePrefixes != null && usePrefixes);

        setPredicateOrder();
        TTNodeSerializer helper = new TTNodeSerializer(document.getContext(), usePrefixes);
        gen.writeStartObject();
        helper.serializeContexts(document.getPrefixes(), gen);
        processGraph(document, gen, helper);
        processCrud(document, gen, helper);
        processEntities(document, gen, prov, helper);


        gen.writeEndObject();
    }

    private static void processGraph(TTDocument document, JsonGenerator gen, TTNodeSerializer helper) throws IOException {
        if (document.getGraph() != null) {
            gen.writeFieldName("@graph");
            gen.writeStartObject();
            gen.writeStringField("@id", helper.prefix(document.getGraph().getIri()));
            gen.writeEndObject();
        }
    }

    private static void processCrud(TTDocument document, JsonGenerator gen, TTNodeSerializer helper) throws IOException {
        if (document.getCrud() != null) {
            gen.writeFieldName("crud");
            TTIriRef ref = document.getCrud().asIriRef();
            gen.writeStartObject();
            gen.writeStringField("@id", helper.prefix(ref.getIri()));
            if (ref.getName() != null && !ref.getName().isEmpty())
                gen.writeStringField("name", ref.getName());
            gen.writeEndObject();
        }
    }

    private static void processEntities(TTDocument document, JsonGenerator gen, SerializerProvider prov, TTNodeSerializer helper) throws IOException {
        if (document.getEntities() != null && !document.getEntities().isEmpty()) {
            gen.writeArrayFieldStart("entities");
            for (TTEntity entity : document.getEntities()) {
                gen.writeStartObject();
                if (entity.getIri()!=null)
                    gen.writeStringField("@id", helper.prefix(entity.getIri()));
                if (entity.getGraph() != null) {
                    gen.writeStringField("@graph", helper.prefix(entity.getGraph().getIri()));
                }
                if (entity.getCrud() != null) {
                    gen.writeStringField("crud", helper.prefix(entity.getCrud().getIri()));
                }
                helper.serializeNode(entity, gen, prov);
                gen.writeEndObject();
            }
            gen.writeEndArray();
        }
    }

    private void setPredicateOrder() {
        List<TTIriRef> predicateTemplate = List.of(RDF.TYPE, RDFS.LABEL,
            RDFS.COMMENT, IM.CODE, IM.HAS_SCHEME, IM.HAS_STATUS,
            RDFS.SUBCLASSOF);
    }
}

