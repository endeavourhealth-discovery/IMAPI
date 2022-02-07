package org.endeavourhealth.imapi.logic.cache;

import org.endeavourhealth.imapi.logic.service.ShapeService;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that holds the IM schema as a cache of static maps, including shapes, predicate display ordeers,
 * predicate names, domains and ranges
 */
public class EntityCache {

	public static final Object lock= new Object();
	static final Map<String, TTEntity> shapes = new HashMap<>();
	static final Map<String, List<TTIriRef>> predicateOrder= new HashMap<>();
	static final Map<String,String> predicateNames= new HashMap<>();


 /** Gets a shape from the cache. If not present returns null.
		* @param iri the iri of the shape
	 * @return a TTEntity representing the shape
	 */
	public static TTBundle getShape(String iri) {
		synchronized (shapes) {
			TTEntity shape= shapes.get(iri);
			if (shape==null)
				return null;
			Set<TTIriRef> predicates= ShapeService.getPredicatesFromNode(shape);
			TTBundle bundle= new TTBundle().setEntity(shape);
			predicates.forEach(i-> bundle.getPredicates().put(i.getIri(),predicateNames.get(i.getIri())));
			return bundle;
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


}
