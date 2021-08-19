package org.endeavourhealth.imapi.mapping.function;

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
		return (iri + name).replace("'", "");
	}

	public static String getType(JsonNode contentObject) {
		String contentType = contentObject.get("type").asText();
		switch (contentType) {
		case "group":
			return "http://endhealth.info/im#Folder";
		case "item":
			if (contentObject.has("valueDomain")) {
				return "http://www.w3.org/2002/07/owl#ObjectProperty";
			}
			return "http://endhealth.info/im#RecordType";
		}
		return "http://www.w3.org/2002/07/owl#Class";
	}

	public static String getParentPredicate(JsonNode parent) {
		if (getType(parent).equals("http://endhealth.info/im#Folder")) {
			return "http://endhealth.info/im#isContainedIn";
		} else if (getType(parent).equals("http://endhealth.info/im#RecordType")) {
			return "http://endhealth.info/im#isA";
		}
		return "http://endhealth.info/im#isChildOf";
	}
}
