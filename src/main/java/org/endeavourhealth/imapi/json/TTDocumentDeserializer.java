package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.tripletree.TTContextJava;
import org.endeavourhealth.imapi.model.tripletree.TTDocumentJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TTDocumentDeserializer extends StdDeserializer<TTDocumentJava> {
  private static final String DEFAULT_SCHEME = "defaultScheme";
  private static final String GRAPH = "graph";
  private static final String ID = "iri";
  private static final String CRUD = "crud";
  private static final String ENTITIES = "entities";
  private final TTContextJava context = new TTContextJava();

  public TTDocumentDeserializer() {
    this(null);
  }

  public TTDocumentDeserializer(Class<TTDocumentJava> vc) {
    super(vc);
  }

  @Override
  public TTDocumentJava deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    TTDocumentJava result = new TTDocumentJava();
    TTNodeDeserializer helper = new TTNodeDeserializer(context);

    List<TTPrefix> prefixes = new ArrayList<>();

    helper.populatePrefixesFromJson(node, prefixes);
    if (!prefixes.isEmpty())
      result.setContext(context);
    if (node.get(CRUD) != null)
      result.setCrud(TTIriRefExtensionsKt.iri(new TTIriRef(), helper.expand(node.get(CRUD).get(ID).asText())));
    if (node.get(ENTITIES) != null) {
      result.setEntities(getEntities(node.withArray(ENTITIES)));
    }


    return result;
  }


  private List<TTEntityJava> getEntities(ArrayNode arrayNode) throws IOException {
    List<TTEntityJava> result = new ArrayList<>();
    Iterator<JsonNode> iterator = arrayNode.elements();
    TTNodeDeserializer helper = new TTNodeDeserializer(context);
    while (iterator.hasNext()) {
      JsonNode entityNode = iterator.next();
      TTEntityJava entity = new TTEntityJava();
      result.add(entity);
      Iterator<Map.Entry<String, JsonNode>> fields = entityNode.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> field = fields.next();
        switch (field.getKey()) {
          case ID -> entity.setIri(helper.expand(field.getValue().textValue()));
          case CRUD ->
            entity.setCrud(TTIriRefExtensionsKt.iri(new TTIriRef(), helper.expand(field.getValue().get(ID).asText())));
          default -> {
            if (field.getValue().isArray())
              entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), helper.expand(field.getKey())), helper.getJsonNodeArrayAsValue(field.getValue()));
            else
              entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), helper.expand(field.getKey())), helper.getJsonNodeAsValue(field.getValue()));
          }
        }
      }
    }

    return result;
  }
}
