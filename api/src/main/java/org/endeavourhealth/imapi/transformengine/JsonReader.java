package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.iml.Where;

import java.util.ArrayList;
import java.util.List;

public class JsonReader implements ObjectReader {

	public Object getPropertyValue(Object source, String property, Where where) {
		ArrayList<Object> result= new ArrayList<>();
		if (source instanceof List){
			if (((List<?>) source).size()>1)
				throw new RuntimeException("Looking for value of property : "+ property+"  but Source object is list. Either source is wrongly formatted or the map should have a list mode set");
			else
				source= ((List<?>) source).get(0);
		}
			if (((JsonNode) source).has(property)) {
				JsonNode jsonValue = ((JsonNode) source).get(property);
				if (jsonValue instanceof ArrayNode) {
					for (JsonNode valueNode : jsonValue) {
						if (where != null) {
							JsonNode filter = query(valueNode, where);
							if (filter != null) {
									result.add(convertNode(filter));
								}
							}
						else
							result.add(convertNode(valueNode));
						}
					}
				else {
					return convertNode(jsonValue);
				}
			}
		return result;
	}

	private Object convertNode(JsonNode value) {
		if (value.isTextual())
			return value.asText();
		else if (value.isInt())
			return String.valueOf(value.asInt());
		else if (value.isFloat())
			return String.valueOf(value.floatValue());
		else
			return value;
	}


	private JsonNode query(JsonNode sourceNode, Where where){
				if (where.getPath()!=null) {
					if (sourceNode.has(where.getPath())) {
						if (where.getValue() != null)
							if (where.getValue().getValue().equals(sourceNode.get(where.getPath()).asText()))
								return sourceNode;
					}
				}
				return null;
	}


}
