package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.io.IOException;

public class TTEntitySerializer extends StdSerializer<TTEntity> {

  public TTEntitySerializer() {
    this(null);
  }

  public TTEntitySerializer(Class<TTEntity> t) {
    super(t);
  }

  @Override
  public void serialize(TTEntity entity, JsonGenerator gen, SerializerProvider prov) throws IOException {
    Boolean usePrefixes = (Boolean) prov.getAttribute(TTContext.OUTPUT_CONTEXT);
    usePrefixes = (usePrefixes != null && usePrefixes);

    TTContextHelper helper = new TTContextHelper(entity.getContext(), usePrefixes);

    gen.writeStartObject();
    helper.serializeContexts(entity.getPrefixes(), gen);
    gen.writeStringField("iri", helper.prefix(entity.getIri()));

    if (entity.getGraph() != null) {
      outputIri(gen, "graph", entity.getGraph().asIri(), helper);
    }
    if (entity.getCrud() != null) {
      outputIri(gen, "crud", entity.getCrud(), helper);
    }
    TTNodeSerializer nodeSerializer = new TTNodeSerializer(entity.getContext(), usePrefixes);
    nodeSerializer.serializeNode(entity, gen, prov);
    gen.writeEndObject();
  }

  private static void outputIri(JsonGenerator gen, String fieldName, TTIriRef ref, TTContextHelper helper) throws IOException {
    gen.writeFieldName(fieldName);
    gen.writeStartObject();
    gen.writeStringField("iri", helper.prefix(ref.getIri()));
    if (ref.getName() != null && !ref.getName().isEmpty())
      gen.writeStringField("name", ref.getName());
    gen.writeEndObject();
  }
}
