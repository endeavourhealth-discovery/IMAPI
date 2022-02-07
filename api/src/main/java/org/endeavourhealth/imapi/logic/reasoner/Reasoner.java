package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Uitliyy class that performs reasoning activities of various kinds such as inferring properties from superclasses
 */
public class Reasoner {

	public void inheritProperties(TTEntity shape, TTEntityMap entityMap) {
		if (shape.get(IM.HAS_INHERITED_PROPERTIES)!=null)
			return;
		List<TTValue> properties = getOrderedProperties(shape);
		int order = 0;
		List<TTValue> mergedProperties = new ArrayList<>();
		if (shape.get(RDFS.SUBCLASSOF) != null) {
			for (TTValue superClass : shape.get(RDFS.SUBCLASSOF).getElements()) {
				TTEntity superEntity = entityMap.getEntity(superClass.asIriRef().getIri());
				if (superEntity != null) {
					inheritProperties(superEntity, entityMap);
					if (superEntity.get(SHACL.PROPERTY) != null) {
						for (TTValue superP : superEntity.get(SHACL.PROPERTY).getElements()) {
							if (!hasProperty(properties,superP.asNode().get(SHACL.PATH).asIriRef())) {
								order++;
								superP.asNode().set(SHACL.ORDER, TTLiteral.literal(order));
								superP.asNode().set(RDFS.DOMAIN, superClass);
								mergedProperties.add(superP);
							}
						}
					}
				}
			}
			if (properties != null) {
				for (TTValue prop : properties) {
					order++;
					prop.asNode().set(SHACL.ORDER, TTLiteral.literal(order));
					mergedProperties.add(prop);
				}
			}
			TTArray newValue = new TTArray();
			mergedProperties.forEach(newValue::add);
			shape.set(SHACL.PROPERTY, newValue);
			shape.set(IM.HAS_INHERITED_PROPERTIES,TTLiteral.literal(true));
		}
	}


	private static List<TTValue> getOrderedProperties(TTEntity shape) {
		if (shape.get(SHACL.PROPERTY) != null) {
			List<TTValue> properties = new ArrayList<>(shape.get(SHACL.PROPERTY).getElements());
			assignMissingOrder(properties);
			return properties.stream()
				.sorted(Comparator.comparingInt((TTValue p) -> p.asNode().get(SHACL.ORDER).asLiteral().intValue()))
				.collect(Collectors.toList());
		}
		return null;
	}


	private static void assignMissingOrder(List<TTValue> properties){
		int order=0;
		for (TTValue node:properties){
			if (node.asNode().get(SHACL.ORDER)==null){
				order++;
				node.asNode().set(SHACL.ORDER,TTLiteral.literal(order));
			}
		}
	}

	private static boolean hasProperty(List<TTValue> subProperties, TTIriRef path) {
		if (subProperties!=null){
			for (TTValue prop: subProperties) {
				if (prop.asNode().get(SHACL.PATH).asIriRef().equals(path))
					return true;
			}
		}
		return false;
	}

}