package org.endeavourhealth.imapi.mapping.function;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.PRSB;

import com.fasterxml.jackson.databind.JsonNode;

public class MappingFunction {

	public static String generateLocationIri(JsonNode contentObject, JsonNode parent) throws NoSuchAlgorithmException {
//		AddrLn1 AddrLn2 AddrLn3 Town County PostCode Country UPRN
        String address = contentObject.get("AddrLn1").asText();
        address+=contentObject.get("AddrLn2").asText();
        address+=contentObject.get("AddrLn3").asText();
        address+=contentObject.get("Town").asText();
        address+=contentObject.get("County").asText();
        address+=contentObject.get("PostCode").asText();
        address+=contentObject.get("Country").asText();
        address+=contentObject.get("UPRN").asText();
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(address.getBytes(StandardCharsets.UTF_8));
        String hash = Base64.getEncoder().encodeToString(digest);
        return IM.NAMESPACE + hash.substring(0, 16);
	}

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
