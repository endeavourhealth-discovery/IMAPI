package org.endeavourhealth.imapi.mapping.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.endeavourhealth.imapi.mapping.function.MappingFunction;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.mapping.model.MappingInstructionWrapper;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.R2RML;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

public class EntityBuilder {

    public static List<TTEntity> buildEntityListFromJson(JsonNode content, MappingInstructionWrapper instructionWrapper,
                                                         boolean nested) throws Exception {
        ArrayList<TTEntity> entities = new ArrayList<TTEntity>();

        Iterator<JsonNode> elements = instructionWrapper.getIterator() != null
                ? elements = getElementsFromIteratorJsonPath(content, instructionWrapper.getIterator())
                : content.elements();

        while (elements.hasNext()) {
            JsonNode element = elements.next();
            if (nested) {
                addEntity(entities, element, instructionWrapper.getInstructions(),
                        instructionWrapper.getNestedPropName(), null);
            } else {
                for (Entry<String, List<MappingInstruction>> entry : instructionWrapper.getInstructions().entrySet()) {
                    if (!isBNode(instructionWrapper.getInstructions().get(entry.getKey())))
                        entities.add(buildEntity(element, entry.getKey(), instructionWrapper.getInstructions(), null));
                }

            }
        }

        return entities;
    }

    public static List<TTEntity> groupEntities(List<TTEntity> entities) {
        HashMap<String, TTEntity> groupedMap = new HashMap<String, TTEntity>();

        for (TTEntity ungrouped : entities) {
            if (groupedMap.containsKey(ungrouped.getIri())) {
                TTEntity entity = groupedMap.get(ungrouped.getIri());
                Map<TTIriRef, TTValue> map = ungrouped.getPredicateMap();
                for (Entry<TTIriRef, TTValue> entry : map.entrySet()) {
                    if (RDFS.COMMENT.equals(entry.getKey())) {
                        entity.setDescription(entity.has(RDFS.COMMENT) ? getHtmlComment(entity, entry.getValue())
                                : "<p>" + entry.getValue() + "</p>");
                    } else if (entity.has(entry.getKey()) && isToBeAdded(entity, entry)) {
                        entity.addObject(entry.getKey(), entry.getValue());
                    } else {
                        entity.set(entry.getKey(), entry.getValue());
                    }

                }
            } else {
                groupedMap.put(ungrouped.getIri(), ungrouped);
            }
        }

        return new ArrayList<TTEntity>(groupedMap.values());
    }

    private static Iterator<JsonNode> getElementsFromIteratorJsonPath(JsonNode content, String iterator) {
        Configuration JACKSON_CONFIGURATION = Configuration.builder().mappingProvider(new JacksonMappingProvider())
                .jsonProvider(new JacksonJsonNodeJsonProvider()).build();
        JsonNode nodeFromJsonPathJsonNode = JsonPath.using(JACKSON_CONFIGURATION).parse(content).read(iterator);
        return nodeFromJsonPathJsonNode.elements();
    }

    private static String getHtmlComment(TTEntity entity, TTValue value) {
        String startingTag = "<p>";
        String endingTag = "</p>";
        if (!entity.getDescription().startsWith(startingTag)) {
            return startingTag + entity.getDescription() + endingTag + startingTag + value.asLiteral().getValue()
                    + endingTag;
        }
        return entity.getDescription() + startingTag + value.asLiteral().getValue() + endingTag;
    }

    private static boolean isToBeAdded(TTEntity entity, Entry<TTIriRef, TTValue> entry) {

        if (entity.get(entry.getKey()).isIriRef()) {
            String key = entity.get(entry.getKey()).asIriRef().getIri();
            String value = entry.getValue().asIriRef().getIri();
            boolean isAlreadyThere = key.equals(value);
            return !isAlreadyThere;
        }
        if (entity.get(entry.getKey()).isLiteral()) {
            String key = entity.get(entry.getKey()).asLiteral().getValue();
            String value = entry.getValue().asLiteral().getValue();
            boolean isAlreadyThere = key.equals(value);
            return !isAlreadyThere;
        }
        if (entity.get(entry.getKey()).isList()) {
            boolean isAlreadyThere = entity.get(entry.getKey()).asArray().getElements().stream()
                    .anyMatch(element -> (element.isLiteral()
                            && element.asLiteral().getValue().equals(entry.getValue().asLiteral().getValue()))
                            || (element.isIriRef()
                            && element.asIriRef().getIri().equals(entry.getValue().asIriRef().getIri())));
            return !isAlreadyThere;
        }

        return true;
    }

    private static void addEntity(List<TTEntity> entities, JsonNode element, Map<String, List<MappingInstruction>> map,
                                  String nestedProp, JsonNode parent) throws Exception {

        for (Entry<String, List<MappingInstruction>> entry : map.entrySet()) {
            if (!isBNode(map.get(entry.getKey())))
                entities.add(buildEntity(element, entry.getKey(), map, parent));
        }

        if (element.has(nestedProp)) {
            Iterator<JsonNode> subElements = element.get(nestedProp).elements();

            while (subElements.hasNext()) {
                JsonNode nested = subElements.next();
                addEntity(entities, nested, map, nestedProp, element);
            }
        }
    }

    private static TTEntity buildEntity(JsonNode element, String key, Map<String, List<MappingInstruction>> map,
                                        JsonNode parent) throws Exception {
        TTEntity entity = new TTEntity();

        for (MappingInstruction instruction : map.get(key)) {
            if (R2RML.PARENT_TRIPLES_MAP.getIri().equals(instruction.getValueType())) {
                List<MappingInstruction> instructions = map.get(instruction.getValue());

                if (isBNode(instructions)) {
                    TTValue ttvalue = new TTNode();
                    for (MappingInstruction instr : instructions) {
                        setPredicateFromInstruction(element, ttvalue, instr, parent);
                    }
                    String property = getStringTTValue(element, instruction.getPropertyType(), instruction.getProperty(), parent);
                    entity.set(TTIriRef.iri(property), ttvalue);
                } else {
                    TTValue ttvalue = new TTIriRef();
                    List<MappingInstruction> instrList = instructions.stream()
                            .filter(instr -> IM.IRI.equals(instr.getProperty())).collect(Collectors.toList());
                    setPredicateFromInstruction(element, ttvalue, instrList.get(0), parent);
                    String property = getStringTTValue(element, instruction.getPropertyType(), instruction.getProperty(), parent);
                    entity.set(TTIriRef.iri(property), ttvalue);
                }

            } else {
                setPredicateFromInstruction(element, entity, instruction, parent);
            }
        }

        return entity;
    }

    private static boolean isBNode(List<MappingInstruction> instructions) {
        return instructions.stream().anyMatch(instruction -> instruction.getValue().equals(R2RML.BLANK_NODE.getIri()));
    }

    private static void setPredicateFromInstruction(JsonNode element, TTValue entity, MappingInstruction instruction,
                                                    JsonNode parent) throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        String property = getStringTTValue(element, instruction.getPropertyType(), instruction.getProperty(), parent);
        String value = getStringTTValue(element, instruction.getValueType(), instruction.getValue(), parent);

        if (IM.IRI.equals(property) && entity instanceof TTIriRef) {
            ((TTIriRef) entity).setIri(value);
        } else if (IM.IRI.equals(property) && entity instanceof TTEntity) {
            ((TTEntity) entity).setIri(value);
        } else if (RDF.TYPE.getIri().equals(property)
                && !R2RML.BLANK_NODE.getIri().equals(instruction.getValue())) {
            entity.asNode().set(iri(property), new TTIriRef(value));
        } else if (!R2RML.BLANK_NODE.getIri().equals(instruction.getValue())) {
            entity.asNode().set(iri(property), new TTLiteral(value));
        }

    }

    private static String getStringTTValue(JsonNode element, String valueType, String value, JsonNode parent)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        String returnValue = "";

        switch (valueType) {
            case "http://semweb.mmlab.be/ns/rml#reference":
                returnValue = element.at(getPathFromReference(value)).asText();
                break;
            case "http://www.w3.org/ns/r2rml#template":
                StringBuilder templateValue = new StringBuilder();
                String[] parts = value.split("\\{");
                for (String part : parts) {
                    if (part.contains("}")) {
                        String[] nestedParts = part.split("}");
                        templateValue.append(element.at(getPathFromReference(nestedParts[0])).asText());
                        if (nestedParts.length > 1) {
                            templateValue.append(nestedParts[1]);
                        }
                    } else {
                        templateValue.append(part);
                    }
                }
                returnValue = templateValue.toString();
                break;
            default:
                if (isFunction(value)) {
                    String functionName = value.split("#")[1];
                    Class<?> classObj = MappingFunction.class;
                    Method function = classObj.getDeclaredMethod(functionName, JsonNode.class, JsonNode.class);
                    returnValue = (String) function.invoke(classObj, element, parent);
                } else {
                    returnValue = value;
                }
                break;
        }

        return returnValue;
    }

    private static boolean isFunction(String value) {
        if ("@id".equals(value)) {
            return false;
        }

        String valuePart = value.split("#")[1];
        if (valuePart == null) {
            return false;
        }
        Class<?> classObj = MappingFunction.class;
        List<Method> functions = Arrays.asList(classObj.getDeclaredMethods());
        return functions.stream().anyMatch(function -> valuePart.equals(function.getName()));
    }

    private static String getPathFromReference(String reference) {
        if (reference.contains(".")) {
            reference = reference.replaceAll(".", "/");
        }
        if (reference.contains("[")) {
            reference = reference.replaceAll("[\\[\\]]", "/");
        }
        if (reference.contains("//")) {
            reference = reference.replaceAll("//", "/");
        }
        if (reference.contains("'")) {
            reference = reference.replaceAll("'", "");
        }
        if (reference.endsWith("/")) {
            reference = reference.substring(0, reference.length() - 1);
        }
        return "/" + reference;
    }

}
