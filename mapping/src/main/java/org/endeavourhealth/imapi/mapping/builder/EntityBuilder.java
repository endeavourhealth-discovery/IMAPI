package org.endeavourhealth.imapi.mapping.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.endeavourhealth.imapi.mapping.function.MappingFunction;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;

public class EntityBuilder {

	public static List<TTEntity> buildEntityListFromJson(JsonNode content, List<MappingInstruction> instructions,
			String iterator, String nestedProp) {
		ArrayList<TTEntity> entities = new ArrayList<TTEntity>();

//		// $.dataset[*].concept[*]
//		// /dataset/0/concept
		Iterator<JsonNode> elements = null;
		if (iterator != null) {
			elements = content.at(iterator).elements();
		} else {
			elements = content.elements();
		}

		elements.forEachRemaining(element -> {
			try {
				if (!nestedProp.isBlank()) {
					addEntity(entities, element, instructions, nestedProp, null);
				} else {
					addEntity(entities, element, instructions);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return entities;
	}

	private static void addEntity(List<TTEntity> entities, JsonNode element, List<MappingInstruction> instructions)
			throws Exception {
		try {
			entities.add(buildEntity(element, instructions, null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void addEntity(List<TTEntity> entities, JsonNode element, List<MappingInstruction> instructions,
			String nestedProp, JsonNode parent) throws Exception {

		entities.add(buildEntity(element, instructions, parent));

		if (element.has(nestedProp)) {
			element.get(nestedProp).forEach(nested -> {
				try {
					addEntity(entities, nested, instructions, nestedProp, element);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
	}

	private static TTEntity buildEntity(JsonNode element, List<MappingInstruction> instructions, JsonNode parent)
			throws Exception {
		TTEntity entity = new TTEntity();

		for (MappingInstruction instruction : instructions) {
			switch (instruction.getValueTypeString()) {
			case "function":
				executeFunction(element, entity, instruction, parent);
				break;
			case "reference":
				setFromReference(element, entity, instruction);
				break;
			case "template":
				setFromTemplate(element, entity, instruction);
				break;
			case "constant":
				setConstant(element, entity, instruction);
				break;
			}
		}

		return entity;
	}

	private static void setFromTemplate(JsonNode element, TTEntity entity, MappingInstruction instruction) {
		String[] parts = instruction.getTemplate().split("\\{");
		if (parts.length == 2) {
			String second = element.at(instruction.getPathFromReference(parts[1].substring(0, parts[1].length() - 1)))
					.asText();

			if (instruction.getProperty().equals("@id")) {
				entity.setIri(parts[0] + second);
			} else {
				entity.set(TTIriRef.iri(instruction.getProperty()), new TTLiteral(parts[0] + second));
			}
		}
	}

	private static void setFromReference(JsonNode element, TTEntity entity, MappingInstruction instruction) {
		if (instruction.getProperty().equals("@id")) {
			entity.setIri(element.at(instruction.getPathFromReference(null)).asText());
		} else {
			entity.set(TTIriRef.iri(instruction.getProperty()),
					new TTLiteral(element.at(instruction.getPathFromReference(null)).asText()));
		}

	}

	private static void setConstant(JsonNode element, TTEntity entity, MappingInstruction instruction) {
		if (instruction.getProperty().equals("@id")) {
			entity.setIri(instruction.getConstant());
		} else {
			entity.set(TTIriRef.iri(instruction.getProperty()), TTIriRef.iri(instruction.getConstant()));
		}

	}

	private static void executeFunction(JsonNode element, TTEntity entity, MappingInstruction instruction,
			JsonNode parent) throws Exception {
		switch (instruction.getFunction()) {
		case "generateIri":
			entity.setIri(MappingFunction.generateIri(element));
			break;

		case "handleParentRels":
			if (parent != null) {
				String predicate = MappingFunction.getParentPredicate(parent);
				entity.set(TTIriRef.iri(predicate), TTIriRef.iri(MappingFunction.generateIri(parent)));
			}
			break;
			
//		case "getIsas":
//			if (parent != null) {
//				entity.set(TTIriRef.iri(instruction.getProperty()), TTIriRef.iri(MappingFunction.generateIri(parent)));
//			}
//			break;
			
		case "getType":
			entity.set(TTIriRef.iri(instruction.getProperty()), TTIriRef.iri(MappingFunction.getType(element)));
			break;
		}
	}

}
