package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTEntityDeserializer extends StdDeserializer<TTEntity> {
  protected TTContext context = new TTContext();
  protected transient TTNodeDeserializer helper;

  public TTEntityDeserializer() {
    this(null);
  }

  public TTEntityDeserializer(Class<TTEntity> vc) {
    super(vc);
  }

  @Override
  public TTEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);


    TTEntity result = new TTEntity();
    helper = new TTNodeDeserializer(context);

    List<TTPrefix> prefixes = new ArrayList<>();

    helper.populatePrefixesFromJson(node, prefixes);
    if (!prefixes.isEmpty())
      result.setContext(context);
    populateEntityFromJson(node, result);

    return result;
  }

  void populateEntityFromJson(JsonNode node, TTEntity result) throws IOException {
    Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> field = fields.next();
      String key = field.getKey();
      JsonNode value = field.getValue();
      if ("iri".equals(key))
        result.setIri(helper.expand(value.textValue()));
      else if (IM.ID.toString().equals(key))
        result.setIri(helper.expand(value.textValue()));
      else if ("crud".equals(key))
        result.setCrud(helper.getJsonNodeAsValue(value).asIriRef());
      else if (!"context".equals(key)) {
        if (value.isArray())
          result.set(iri(helper.expand(key)), helper.getJsonNodeArrayAsValue(value));
        else
          result.set(iri(helper.expand(key)), helper.getJsonNodeAsValue(value));
      }
    }
  }


}
