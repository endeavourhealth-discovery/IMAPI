package org.endeavourhealh.imapi.cache;

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
public class Cache {

	static final Map<String, TTEntity> shapes = new HashMap<>();
	static final Map<String, List<TTIriRef>> predicateOrder= new HashMap<>();

	/**
	 * Refreshes the entity cache with shapes
	 */
	public static void refreshCache(){
		synchronized (shapes){
			new Thread(new CacheLoader()).start();
		}
	}



	/**
	 * Gets a shape from the cache. If not present gets from IM and may or may not refresh the cache
	 * @param iri the iri of the shape
	 * @return a TTEntity representing the shape
	 */
	public static TTEntity getShape(String iri) {
			if (shapes.get(iri)==null) {
				synchronized (shapes) {
					CacheLoader.refreshShape(iri);
					if (shapes.get(iri) != null) { //shape found, must be missing from cache
						new Thread(new CacheLoader()).start();
					}
				}
			}
			return shapes.get(iri);
	}


	static void addPredicateOrder(String iri,TTIriRef predicate, int order){
		List<TTIriRef> predicates= predicateOrder.computeIfAbsent(iri, key-> new ArrayList<>());
		if (!predicates.contains(predicate)) {
			if (order > predicates.size())
				predicates.add(predicate);
			else
				predicates.add(order, predicate);
		}
	}

	static List<TTIriRef> getPredicateOrder(String iri) {
			if (predicateOrder.get(iri) == null) {
				synchronized (shapes) {
					CacheLoader.refreshShape(iri);
					if (predicateOrder.get(iri) != null) { //order found, must be missing from cache
						new Thread(new CacheLoader()).start();
					}
				}
			}
			return predicateOrder.get(iri);
		}

}
