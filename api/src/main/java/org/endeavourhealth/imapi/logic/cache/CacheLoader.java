package org.endeavourhealth.imapi.logic.cache;

import org.endeavourhealth.imapi.dataaccess.CacheRepository;
import org.endeavourhealth.imapi.dataaccess.ShapeRepository;
import org.endeavourhealth.imapi.logic.reasoner.Reasoner;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializer;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.*;
import java.util.stream.Collectors;

public class CacheLoader implements Runnable{

	private static final CacheRepository cacheRepository= new CacheRepository();



	/**
	 * Refreshes the node shape cache, predicate orders and domains and ranges
	 */
	public static void refreshCache() {
		synchronized (EntityCache.lock) {
			TTEntityMap shapeMap = ShapeRepository.getShapes();
			Reasoner reasoner = new Reasoner();
			for (Map.Entry<String, TTEntity> entry : shapeMap.getEntities().entrySet()) {
				reasoner.inheritProperties(entry.getValue(), shapeMap);
			}
			TTNodeSerializer.predicateOrder = EntityCache.predicateOrder;
			cacheResults(shapeMap);
		}
	}


	public static void cacheResults(TTEntityMap shapeMap) {
		if (shapeMap.getPredicates() != null)
			shapeMap.getPredicates().entrySet().stream().forEach(entry ->
				EntityCache.addPredicateName(entry.getKey(), entry.getValue()));
		for (Map.Entry<String, TTEntity> entry : shapeMap.getEntities().entrySet()) {
			EntityCache.addShape(entry.getValue());
			if (entry.getValue().get(SHACL.PROPERTY) != null) {
				List<TTIriRef> properties = entry.getValue().get(SHACL.PROPERTY).stream().map(p -> p.asNode().get(SHACL.PATH).asIriRef())
					.collect(Collectors.toList());
				EntityCache.setPredicateOrder(entry.getKey(), properties);
				TTNodeSerializer.addPredicateOrder(entry.getKey(), properties);
				properties.stream().forEach(p->EntityCache.addPredicateName(p.getIri(),p.getName()));
			}
		}

	}



	private static void addPredicateOrder(String iri,TTIriRef predicate, int order){
		EntityCache.predicateOrder.putIfAbsent(iri,new ArrayList<>());
		List<TTIriRef> ordered= EntityCache.predicateOrder.get(iri);
		if (order>ordered.size())
			ordered.add(predicate);
		else
			ordered.add(order-1,predicate);
	}



		@Override
	public void run() {
		synchronized (EntityCache.shapes) {
			CacheLoader.refreshCache();
		}
	}

}
