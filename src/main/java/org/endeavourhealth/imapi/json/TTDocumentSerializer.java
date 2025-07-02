package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.io.IOException;

/**
 * JSON LD- Serializer for TTDocument (triple tree node with collection of entities)
 * <p>Uses context for prefixes and common annotation elements</p>
 */
public class TTDocumentSerializer extends StdSerializer<TTDocument> {

  public TTDocumentSerializer() {
    this(null);
  }

  public TTDocumentSerializer(Class<TTDocument> t) {
    super(t);
  }

  private static void processGraph(TTDocument document, JsonGenerator gen, TTNodeSerializer helper) throws IOException {
    if (document.getNamespace() != null) {
      outputIri(gen, "graph", document.getNamespace(), helper);
    }
  }

  private static void processCrud(TTDocument document, JsonGenerator gen, TTNodeSerializer helper) throws IOException {
    if (document.getCrud() != null) {
      outputIri(gen, "crud", document.getCrud().asIriRef(), helper);
    }
  }

  private static void outputIri(JsonGenerator gen, String fieldName, TTIriRef ref, TTNodeSerializer helper) throws IOException {
    gen.writeFieldName(fieldName);
    gen.writeStartObject();
    gen.writeStringField("iri", helper.prefix(ref.getIri()));
    if (ref.getName() != null && !ref.getName().isEmpty())
      gen.writeStringField("name", ref.getName());
    gen.writeEndObject();
  }

  private static void processEntities(TTDocument document, JsonGenerator gen, SerializerProvider prov, TTNodeSerializer helper) throws IOException {
    if (document.getEntities() != null && !document.getEntities().isEmpty()) {
      gen.writeArrayFieldStart("entities");
      for (TTEntity entity : document.getEntities()) {
        gen.writeStartObject();
        if (entity.getIri() != null)
          gen.writeStringField("iri", helper.prefix(entity.getIri()));
        if (entity.getGraph() != null) {
          outputIri(gen, "graph", entity.getGraph().asIri(), helper);
        }
        if (entity.getCrud() != null) {
          outputIri(gen, "crud", entity.getCrud(), helper);
        }
        helper.serializeNode(entity, gen, prov);
        gen.writeEndObject();
      }
      gen.writeEndArray();
    }
  }

  @Override
  public void serialize(TTDocument document, JsonGenerator gen, SerializerProvider prov) throws IOException {
    Boolean usePrefixes = (Boolean) prov.getAttribute(TTContext.OUTPUT_CONTEXT);
    usePrefixes = (usePrefixes != null && usePrefixes);

    TTNodeSerializer helper = new TTNodeSerializer(document.getContext(), usePrefixes);
    gen.writeStartObject();
    helper.serializeContexts(document.getPrefixes(), gen);
    processGraph(document, gen, helper);
    processCrud(document, gen, helper);
    processEntities(document, gen, prov, helper);


    gen.writeEndObject();
  }

}

