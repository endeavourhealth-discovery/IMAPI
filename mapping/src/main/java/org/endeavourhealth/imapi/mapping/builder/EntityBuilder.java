package org.endeavourhealth.imapi.mapping.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
				if (nestedProp != null) {
					addEntity(entities, element, instructions, nestedProp);
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
			entities.add(buildEntity(element, instructions));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void addEntity(List<TTEntity> entities, JsonNode element, List<MappingInstruction> instructions,
			String nestedProp) throws Exception {

		if (!element.isArray()) {
			entities.add(buildEntity(element, instructions));
			if (element.has(nestedProp)) {
				addEntity(entities, element.get(nestedProp), instructions, nestedProp);
			}
		}

		element.elements().forEachRemaining(remaining -> {
			try {
				entities.add(buildEntity(remaining, instructions));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (remaining.has(nestedProp)) {
				try {
					addEntity(entities, remaining.get(nestedProp), instructions, nestedProp);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private static TTEntity buildEntity(JsonNode element, List<MappingInstruction> instructions) throws Exception {
		TTEntity entity = new TTEntity();

		for (MappingInstruction instruction : instructions) {
			switch (instruction.getValueTypeString()) {
			case "function":
				executeFunction(element, entity, instruction);
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
			System.out.println(element);
			System.out.println(instruction);
		}

		return entity;
	}

	private static void setFromTemplate(JsonNode element, TTEntity entity, MappingInstruction instruction) {
		String[] parts = instruction.getTemplate().split("\\{");
		if (parts.length == 2) {
			String second = element.at(instruction.getPathFromReference(parts[1].substring(0, parts[1].length() - 1)))
					.asText();
			entity.set(TTIriRef.iri(instruction.getProperty()), new TTLiteral(parts[0] + second));
		}
	}

	private static void setFromReference(JsonNode element, TTEntity entity, MappingInstruction instruction) {
		entity.set(TTIriRef.iri(instruction.getProperty()),
				new TTLiteral(element.at(instruction.getPathFromReference(null)).asText()));
	}

	private static void setConstant(JsonNode element, TTEntity entity, MappingInstruction instruction) {
		entity.set(TTIriRef.iri(instruction.getProperty()), TTIriRef.iri(instruction.getConstant()));
	}

	private static void executeFunction(JsonNode element, TTEntity entity, MappingInstruction instruction)
			throws Exception {
		switch (instruction.getFunction()) {
		case "generateIri":
			entity.setIri(MappingFunction.generateIri(element));
			break;

		case "getType":
			entity.set(TTIriRef.iri(instruction.getProperty()), TTIriRef.iri(MappingFunction.getType(element)));
			break;
		}
	}

}
