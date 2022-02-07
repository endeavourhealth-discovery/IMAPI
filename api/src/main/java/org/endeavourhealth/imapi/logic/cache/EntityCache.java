package org.endeavourhealth.imapi.logic.cache;

import org.endeavourhealth.imapi.dataaccess.PropertyRepository;
import org.endeavourhealth.imapi.dataaccess.ShapeRepository;
import org.endeavourhealth.imapi.logic.reasoner.Reasoner;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that holds the IM schema as a cache of static maps, including shapes, predicate display ordeers,
 * predicate names, domains and ranges
 */

public class EntityCache implements Runnable{

	public static final Object shapeLock= new Object();
	public static final Object propertyLock= new Object();
	static final Map<String, TTEntity> shapes = new HashMap<>();
	static final Map<String, TTEntity> properties = new HashMap<>();
	static final Map<String, List<TTIriRef>> predicateOrder= new HashMap<>();
	static final Map<String,String> predicateNames= new HashMap<>();


	/**
	 * Refreshes the node shape cache, predicate orders and domains and ranges
	 */
	public static void refreshCache() {
		refreshShapes();
		refreshProperties();
	}

	public static void refreshShapes(){
		synchronized (EntityCache.shapeLock) {
			TTEntityMap shapeMap = ShapeRepository.getShapes();
			Reasoner reasoner = new Reasoner();
			for (Map.Entry<String, TTEntity> entry : shapeMap.getEntities().entrySet()) {
				reasoner.inheritProperties(entry.getValue(), shapeMap);
			}
			cacheShapes(shapeMap);
		}
	}

	public static void refreshProperties(){
		synchronized (EntityCache.propertyLock) {
			TTEntityMap propertyMap= PropertyRepository.getProperties();
			Reasoner reasoner = new Reasoner();
			for (Map.Entry<String, TTEntity> entry : propertyMap.getEntities().entrySet()) {
				reasoner.inheritProperties(entry.getValue(), propertyMap);
			}
			cacheProperties(propertyMap);
		}
	}



	/** Gets a shape from the cache. If not present returns null.
		* @param iri the iri of the shape
	 * @return a TTEntity representing the shape
	 */
	public static TTBundle getProperty(String iri) {
			TTEntity property= properties.get(iri);
			if (property==null){
				synchronized (propertyLock) {
				TTEntityMap propertyMap = PropertyRepository.getProperty(iri);
				  if (propertyMap.getEntities() == null)
					return null;
				cacheProperties(propertyMap);
				Reasoner reasoner= new Reasoner();
				reasoner.inheritDomRans(propertyMap.getEntity(iri),propertyMap);
			}
			property= properties.get(iri);
		}

			Set<TTIriRef> predicates= getPredicatesFromNode(property);
			TTBundle bundle= new TTBundle().setEntity(property);
			predicates.forEach(i-> bundle.getPredicates().put(i.getIri(),predicateNames.get(i.getIri())));
			return bundle;
	}

	/**
	 * Returns the full shape map from IM
	 * @return a Map of iri to shapes
	 */
	public static Map<String,TTEntity> getProperties(){
		return properties;
	}

	public static void addProperty(TTEntity property){
		properties.put(property.getIri(),property);
	}



	/** Gets a shape from the cache. If not present gets shape from IM and places in cache.
	 * @param iri the iri of the shape
	 * @return a TTEntity representing the shape
	 */
	public static TTBundle getShape(String iri) {
			TTEntity shape= shapes.get(iri);
			if (shape==null) {
				synchronized (shapeLock) {
					TTEntityMap shapeMap = ShapeRepository.getShapeAndAncestors(iri);
					if (shapeMap.getEntities() == null)
						return null;
					Reasoner reasoner = new Reasoner();
					reasoner.inheritProperties(shapeMap.getEntity(iri), shapeMap);
					synchronized (EntityCache.shapeLock) {
						cacheShapes(shapeMap);
					}
					shape = shapes.get(iri);
				}
			}
			Set<TTIriRef> predicates= getPredicatesFromNode(shape);
			TTBundle bundle= new TTBundle().setEntity(shape);
			predicates.forEach(i-> bundle.getPredicates().put(i.getIri(),predicateNames.get(i.getIri())));
			return bundle;
	}

	public static void cacheShapes(TTEntityMap shapeMap) {
		if (shapeMap.getPredicates() != null)
			shapeMap.getPredicates().forEach(EntityCache::addPredicateName);
		for (Map.Entry<String, TTEntity> entry : shapeMap.getEntities().entrySet()) {
			EntityCache.addShape(entry.getValue());
			if (entry.getValue().get(SHACL.PROPERTY) != null) {
				List<TTIriRef> properties = entry.getValue().get(SHACL.PROPERTY).stream().map(p -> p.asNode().get(SHACL.PATH).asIriRef())
					.collect(Collectors.toList());
				EntityCache.setPredicateOrder(entry.getKey(), properties);
				properties.forEach(p->EntityCache.addPredicateName(p.getIri(),p.getName()));
			}
		}

	}


	public static void cacheProperties(TTEntityMap propertyMap) {
		if (propertyMap.getPredicates() != null)
			propertyMap.getPredicates().forEach(EntityCache::addPredicateName);
		for (Map.Entry<String, TTEntity> entry : propertyMap.getEntities().entrySet()) {
			addProperty(entry.getValue());
		}
	}

	/**
	 * Returns the full shape map from IM
	 * @return a Map of iri to shapes
	 */
	public static Map<String,TTEntity> getShapes(){
		return shapes;
	}

	public static void addShape(TTEntity shape){
		shapes.put(shape.getIri(),shape);
	}



	public static void setPredicateOrder(String iri,List<TTIriRef> properties){
		predicateOrder.put(iri,properties);
	}

	/**
	 * Returns te list of iri to name predicate maps for use by applications.
	 * <p>Generally an entity bundle will hold subset relevant to the entity</p>
	 * @return the iri name map
	 */
	public static Map<String,String> getPredicateNames(){
		return predicateNames;
	}

	/**
	 * Returns the name of a predicate
	 * @param iri of the predicate
	 * @return A string with the predicate name or null
	 */
	public static String getPredicateName(String iri){
		return predicateNames.get(iri);
	}


	/**
	 * adds an iri to name map for a predicate to enable applications to display predicate names
	 * @param iri iri of the predicate
	 * @param name name of the predicate
	 */
	public static void addPredicateName(String iri,String name){
		predicateNames.put(iri,name);
	}

	/**
	 * Returns the predicate ordering for an entity type
	 * @param iri iri of the entity
	 * @return an ordered list of predicates from the cache.
	 */
	public static List<TTIriRef> getPredicateOrder(String iri){
		return predicateOrder.get(iri);
	}


	/**
	 * Retrieves a set of predicate IRIs from a node or array, including nested nodes
	 * @param node to retrieve the IRIs from
	 * @return a set of iris
	 */
	public static Set<TTIriRef> getPredicatesFromNode(TTNode node){
		Set<TTIriRef> iris = new HashSet<>();
		return addPredicatesFromNode(node,iris);
	}

	private static Set<TTIriRef> addPredicatesFromNode(TTValue subject, Set<TTIriRef> iris){
		if (subject.asNode().getPredicateMap()!=null){
			for (Map.Entry<TTIriRef, TTArray> entry:subject.asNode().getPredicateMap().entrySet()){
				iris.add(entry.getKey());
				for (TTValue v:entry.getValue().getElements()){
					if (v.isNode())
						addPredicatesFromNode(v,iris);
				}
			}
		}
		return iris;
	}




	@Override
	public void run() {
			refreshCache();
		}

}
