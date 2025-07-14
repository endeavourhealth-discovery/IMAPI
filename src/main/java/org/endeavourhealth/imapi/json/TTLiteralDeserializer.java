package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class TTLiteralDeserializer extends StdDeserializer<TTLiteral> {
  private transient TTNodeDeserializer helper;

  public TTLiteralDeserializer() {
    this(null);
  }

  public TTLiteralDeserializer(Class<TTLiteral> t) {
    super(t);
  }

  public TTLiteralDeserializer(Class<TTLiteral> t, TTNodeDeserializer helper) {
    super(t);
    this.helper = helper;
  }

  @Override
  public TTLiteral deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    if (!node.has(IM.TYPE.toString())) {
      if (node.isValueNode())
        return literal(node);
      else
        return literal(node.get(IM.VALUE.toString()).textValue());
    }

    TTIriRef type = iri(helper == null ? node.get(IM.TYPE.toString()).asText() : helper.expand(node.get(IM.TYPE.toString()).asText()));
    return switch (XSD.from(type.getIri())) {
      case XSD.STRING -> literal(node.get(IM.VALUE.toString()).textValue());
      case XSD.BOOLEAN -> literal(Boolean.valueOf(node.get(IM.VALUE.toString()).asText()));
      case XSD.INTEGER -> literal(Integer.valueOf(node.get(IM.VALUE.toString()).asText()));
      case XSD.PATTERN -> literal(Pattern.compile(node.get(IM.VALUE.toString()).textValue()));
      case null, default -> throw new IOException("Unhandled literal type [" + type.getIri() + "]");
    };
  }
}
