package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.util.List;
import java.util.zip.DataFormatException;

public class JsonToLDConverter implements ObjectConverter{

	@Override
	public Object convert(Object from) throws DataFormatException {

		try {
			if (from instanceof List) {
				if (((List<?>) from).size()==1)
					return jsonToLDSingle(((List<?>) from).get(0));
				TTArray target = new TTArray();
				for (Object node : (List) from) {
						target.add(jsonToLDSingle(node));
				}
				return target;
			}
			else
				return jsonToLDSingle(from);
		}
		catch (Exception e) {
			throw new DataFormatException("Unknown json source to Triple (LD) object");
		}
	}

	private TTValue jsonToLDSingle(Object node) throws DataFormatException, JsonProcessingException {
		if (node instanceof String)
			return TTLiteral.literal((String) node);
		if (node instanceof Integer)
			return TTLiteral.literal((Integer) node);
		if (node instanceof Float)
			return TTLiteral.literal(node);
		if (node instanceof Long)
			return TTLiteral.literal((Long) node);
		if (node instanceof Double)
			return TTLiteral.literal(node);
		JsonNode jsonNode= (JsonNode) node;
			if (jsonNode.isTextual())
				return TTLiteral.literal(jsonNode.asText());
			else if (jsonNode.isLong())
				return TTLiteral.literal(jsonNode.asLong());
			else if (jsonNode.isInt())
				return TTLiteral.literal(jsonNode.asInt());
			else if (jsonNode.isDouble())
				return TTLiteral.literal(jsonNode.asDouble());
			else if (jsonNode.isFloat())
				return TTLiteral.literal(jsonNode.asDouble());
			else if (jsonNode.isBoolean())
				return TTLiteral.literal(jsonNode.asBoolean());
			else
			throw new DataFormatException("Unknown json source object");
		}
}
