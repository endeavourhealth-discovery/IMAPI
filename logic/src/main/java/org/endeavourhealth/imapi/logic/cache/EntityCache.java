package org.endeavourhealth.imapi.logic.cache;

import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that holds the IM schema as a cache of static maps, including shapes, predicate display ordeers,
 * predicate names, domains and ranges
 */
public class EntityCache {

	static final Map<String, TTBundle> shapes = new HashMap<>();
	static final Map<String, List<TTIriRef>> predicateOrder= new HashMap<>();
	static final Map<String,String> predicateNames= new HashMap<>();



	/**
	 * Gets a shape from the cache. If not present gets from IM and may or may not refresh the cache
	 * @param iri the iri of the shape
	 * @return a TTEntity representing the shape
	 */
	public static TTBundle getShape(String iri) {
		synchronized (shapes) {
			return shapes.get(iri);
		}
	}

	public static void addShape(TTBundle shape){
		shapes.put(shape.getEntity().getIri(),shape);
	}



	public static List<TTIriRef> getPredicateOrder(String iri){
			synchronized (shapes) {
				return predicateOrder.get(iri);
			}
		}

}
