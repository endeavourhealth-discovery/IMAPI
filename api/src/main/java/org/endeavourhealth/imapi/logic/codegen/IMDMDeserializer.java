package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.SneakyThrows;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IMDMDeserializer extends StdDeserializer<IMDMBase> {

    protected IMDMDeserializer() {
        this(null);
    }

    protected IMDMDeserializer(Class<?> vc) {
        super(vc);
    }

    @SneakyThrows
    @Override
    public IMDMBase deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);
        String type = node.get("_type").textValue();

        IMDMBase result = (IMDMBase) Class.forName("org.endeavourhealth.imapi.logic.codegen."+type).getDeclaredConstructor().newInstance();

        for (Iterator<String> it = node.fieldNames(); it.hasNext(); ) {
            String n = it.next();
            if(!"_type".equals(n)) {
                JsonNode value = node.get(n);
                result.setProperty(n, getNodeValue(value, p, ctxt));
            }
        }

        return result;
    }

    private Object getNodeValue(JsonNode value, JsonParser p, DeserializationContext ctxt) throws IOException {

        switch(value.getNodeType()) {
            case ARRAY:
                return getArrayNode((ArrayNode) value, p, ctxt);
            case BINARY:
                return value.binaryValue();
            case BOOLEAN:
                return value.booleanValue();
            case MISSING:
                throw new UnsupportedOperationException("Missing nodes unsupported");
            case NULL:
                return null;
            case NUMBER:
               return getNumberNode(value);
            case OBJECT:
                return ctxt.readValue(value.traverse(p.getCodec()), IMDMBase.class);
            case POJO:
                throw new UnsupportedOperationException("POJO nodes unsupported");
            case STRING:
                return value.textValue();
        }
        return null;
    }

    private List<Object> getArrayNode(ArrayNode value, JsonParser p, DeserializationContext ctxt) throws IOException {
        List<Object> result = new ArrayList<>();

        for (JsonNode node : value) {
            result.add(getNodeValue(node, p, ctxt));
        }

        return result;
    }

    private Object getNumberNode (JsonNode value) {
        if (value.isInt()) {
            return value.intValue();
        } else if (value.isLong()) {
            return value.longValue();
        } else if (value.isDouble()) {
            return value.doubleValue();
        } else {
            throw new  UnsupportedOperationException("Unsupported number type");
        }
    }
}
