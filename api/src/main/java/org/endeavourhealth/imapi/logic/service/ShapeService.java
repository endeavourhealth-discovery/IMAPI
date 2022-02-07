package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.ShapeRepository;
import org.endeavourhealth.imapi.logic.cache.CacheLoader;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.logic.reasoner.Reasoner;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShapeService implements ShapeRequest {

	@Override
	public TTBundle getShape(String iri) {
		if (EntityCache.getShape(iri)==null) {
			TTEntityMap shapeMap = ShapeRepository.getShapeAndAncestors(iri);
			if (shapeMap.getEntities() == null)
				return null;
			Reasoner reasoner= new Reasoner();
			reasoner.inheritProperties(shapeMap.getEntity(iri),shapeMap);
			synchronized (EntityCache.lock) {
				CacheLoader.cacheResults(shapeMap);
			}
		}
		return EntityCache.getShape(iri);
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

	private static Set<TTIriRef> addPredicatesFromNode(TTValue subject,Set<TTIriRef> iris){
		if (subject.asNode().getPredicateMap()!=null){
			for (Map.Entry<TTIriRef,TTArray> entry:subject.asNode().getPredicateMap().entrySet()){
				iris.add(entry.getKey());
				for (TTValue v:entry.getValue().getElements()){
					if (v.isNode())
						addPredicatesFromNode(v,iris);
				}
			}
		}
		return iris;
	}

}
