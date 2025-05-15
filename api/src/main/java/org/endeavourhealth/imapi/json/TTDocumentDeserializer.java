package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTDocumentDeserializer extends StdDeserializer<TTDocument> {
  private static final String GRAPH = "@graph";
  private static final String ID = "@id";
  private static final String CRUD = "crud";
  private static final String ENTITIES = "entities";
  private final TTContext context = new TTContext();

  public TTDocumentDeserializer() {
    this(null);
  }

  public TTDocumentDeserializer(Class<TTDocument> vc) {
    super(vc);
  }

  @Override
  public TTDocument deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    TTDocument result = new TTDocument();
    TTNodeDeserializer helper = new TTNodeDeserializer(context);

    List<TTPrefix> prefixes = new ArrayList<>();

    helper.populatePrefixesFromJson(node, prefixes);
    if (!prefixes.isEmpty())
      result.setContext(context);
    if (node.get(GRAPH) != null)
      result.setGraph(iri(helper.expand(node.get(GRAPH).get(ID).asText())));
    if (node.get(CRUD) != null)
      result.setCrud(iri(helper.expand(node.get(CRUD).get(ID).asText())));
    if (node.get(ENTITIES) != null) {
      result.setEntities(getEntities(node.withArray(ENTITIES)));
    }


    return result;
  }


  private List<TTEntity> getEntities(ArrayNode arrayNode) throws IOException {
    List<TTEntity> result = new ArrayList<>();
    Iterator<JsonNode> iterator = arrayNode.elements();
    TTNodeDeserializer helper = new TTNodeDeserializer(context);
    while (iterator.hasNext()) {
      JsonNode entityNode = iterator.next();
      TTEntity entity = new TTEntity();
      result.add(entity);
      Iterator<Map.Entry<String, JsonNode>> fields = entityNode.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> field = fields.next();
        switch (field.getKey()) {
          case ID -> entity.setIri(helper.expand(field.getValue().textValue()));
          case GRAPH -> entity.setGraph(new TTIriRef(helper.expand(field.getValue().get(ID).asText())));
          case CRUD -> entity.setCrud(iri(helper.expand(field.getValue().get(ID).asText())));
          default -> {
            if (field.getValue().isArray())
              entity.set(iri(helper.expand(field.getKey())), helper.getJsonNodeArrayAsValue(field.getValue()));
            else
              entity.set(iri(helper.expand(field.getKey())), helper.getJsonNodeAsValue(field.getValue()));
          }
        }
      }
    }

    return result;
  }
}
