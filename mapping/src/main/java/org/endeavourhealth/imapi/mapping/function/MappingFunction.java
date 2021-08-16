package org.endeavourhealth.imapi.mapping.function;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

public class MappingFunction {

	public static String generateIri(JsonNode contentObject) throws Exception {
		String contentName = contentObject.get("name").get(0).get("#text").asText();

		String iri = "http://endhealth.info/ref#";
		String name = "";
		String[] parts = contentName.split(" ");
		for (String part : parts) {
			String camelCasePart = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
			name += camelCasePart;
		}
		if ("http://www.w3.org/2002/07/owl#ObjectProperty".equals(getType(contentObject))) {
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		return iri + name;
	}

	public static String getType(JsonNode contentObject) {
		String contentType = contentObject.get("type").asText();
		switch (contentType) {
		case "group":
			return "http://endhealth.info/im#Folder";
		case "item":
			return "http://www.w3.org/2002/07/owl#ObjectProperty";
		}
		return "http://www.w3.org/2002/07/owl#Class";
	}
}
