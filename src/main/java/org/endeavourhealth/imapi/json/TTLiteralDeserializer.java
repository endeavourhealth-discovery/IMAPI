package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.endeavourhealth.interfacemanager.model.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;

import java.io.IOException;
import java.util.regex.Pattern;

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

    if (!node.has(ImVocab.TYPE.toString())) {
      if (node.isValueNode())
        return literal(node);
      else
        return literal(node.get(ImVocab.VALUE.toString()).textValue());
    }

    TTIriRef type = TTIriRefExtensionsKt.iri(new TTIriRef(), helper == null ? node.get(ImVocab.TYPE.toString()).asText() :
      helper.expand(node.get(ImVocab.TYPE.toString()).asText()));
    return switch (XsdVocab.fromValue(type.getIri())) {
      case XsdVocab.STRING -> literal(node.get(ImVocab.VALUE.toString()).textValue());
      case XsdVocab.BOOLEAN -> literal(Boolean.valueOf(node.get(ImVocab.VALUE.toString()).asText()));
      case XsdVocab.INTEGER -> literal(Integer.valueOf(node.get(ImVocab.VALUE.toString()).asText()));
      case XsdVocab.PATTERN -> literal(Pattern.compile(node.get(ImVocab.VALUE.toString()).textValue()));
      case null, default -> throw new IOException("Unhandled literal type [" + type.getIri() + "]");
    };
  }
}
