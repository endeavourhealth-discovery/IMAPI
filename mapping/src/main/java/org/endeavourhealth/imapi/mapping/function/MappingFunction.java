package org.endeavourhealth.imapi.mapping.function;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.PRSB;

import com.fasterxml.jackson.databind.JsonNode;

public class MappingFunction {

	public static TTIriRef generateIri(JsonNode contentObject) throws Exception {
		String contentName = contentObject.get("name").get(0).get("#text").asText();

		String name = "";
		String[] parts = contentName.split(" ");
		for (String part : parts) {
			String camelCasePart = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
			name += camelCasePart;
		}
		if ("http://www.w3.org/2002/07/owl#ObjectProperty".equals(getType(contentObject))) {
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		return TTIriRef.iri((PRSB.NAMESPACE + name).replace("'s", ""));
	}

	public static TTIriRef getType(JsonNode contentObject) {
		String contentType = contentObject.get("type").asText();
		switch (contentType) {
		case "group":
			if (contentObject.has("concept")) {
				Iterator<JsonNode> it = contentObject.get("concept").elements();
				Stream<JsonNode> stream = StreamSupport
						.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false);
				boolean hasObjectPropertiesAsChildren = stream
						.filter(child -> child.get("type").asText().equals("item") && child.has("valueDomain"))
						.findFirst().isPresent();
				if (hasObjectPropertiesAsChildren) {
					return IM.RECORD;
				}
			}
			return IM.FOLDER;
		case "item":
			if (contentObject.has("valueSet")) {
				return IM.VALUESET;
			}
			if (contentObject.has("valueDomain")) {
				return OWL.OBJECTPROPERTY;
			}
			return IM.RECORD;
		}
		return OWL.CLASS;
	}

	public static String getOptional(JsonNode contentObject) {
		boolean isMandatory = contentObject.get("isMandatory").asBoolean();
		return String.valueOf(!isMandatory);
	}

	public static TTIriRef getParentPredicate(JsonNode parent) {
		if (getType(parent).equals(IM.FOLDER)) {
			return IM.IS_CONTAINED_IN;
		} else if (getType(parent).equals(IM.RECORD)) {
			return IM.IS_A;
		}
		
		
		return IM.IS_CHILD_OF;
	}
}
