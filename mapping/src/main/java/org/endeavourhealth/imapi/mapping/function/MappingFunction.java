package org.endeavourhealth.imapi.mapping.function;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.PRSB;

import com.fasterxml.jackson.databind.JsonNode;

public class MappingFunction {

	public static String generateUUID(JsonNode contentObject, JsonNode parent) {
		String uniqueID = UUID.randomUUID().toString();
		return IM.NAMESPACE + uniqueID;
	}

	public static String generateIri(JsonNode contentObject, JsonNode parent) throws Exception {
		String contentName = contentObject.get("name").get(0).get("#text").asText();

		String name = "";
		String[] parts = contentName.split(" ");
		for (String part : parts) {
			String camelCasePart = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
			name += camelCasePart;
		}
		if (RDF.PROPERTY.getIri().equals(getType(contentObject, parent))) {
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		return (PRSB.NAMESPACE + name).replace("'s", "");
	}

	public static String getType(JsonNode contentObject, JsonNode parent) {
		String contentType = contentObject.get("type").asText();
		switch (contentType) {
		case "group":
			if (!contentObject.has("concept")) {
				return IM.FOLDER.getIri();
			} else {
				Iterator<JsonNode> it = contentObject.get("concept").elements();
				Stream<JsonNode> stream = StreamSupport
						.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
				boolean hasObjectPropertiesAsChildren = stream
						.filter(child -> child.get("type").asText().equals("item") && child.has("valueDomain"))
						.findFirst().isPresent();
				return hasObjectPropertiesAsChildren ? IM.RECORD.getIri() : IM.FOLDER.getIri();
			}

		case "item":
			if (contentObject.has("valueSet")) {
				return IM.VALUESET.getIri();
			} else if (contentObject.has("valueDomain")) {
				return OWL.OBJECTPROPERTY.getIri();
			}
			return IM.RECORD.getIri();

		}
		return OWL.CLASS.getIri();
	}

}
