package org.endeavourhealth.imapi.logic.cache;

import org.endeavourhealth.imapi.dataaccess.CacheRepositoryImpl;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializer;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CacheLoader implements Runnable{

	private static final CacheRepositoryImpl cacheRepository= new CacheRepositoryImpl();



	/**
	 * Refreshes the node shape cache, predicate orders and domains and ranges
	 */
	public static void refreshCache(){
		synchronized (EntityCache.shapes) {
			Set<TTBundle> schema = cacheRepository.getSchema();
			schema.forEach(EntityCache::addShape);
			schema.forEach(b-> setPredicateOrder(b.getEntity()));
			Set<TTEntity> inferred = new HashSet<>();
			for (TTBundle shape : schema) {
				if (!inferred.contains(shape.getEntity()))
					inheritProperties(shape.getEntity(), inferred);
			}
			TTNodeSerializer.predicateOrder = EntityCache.predicateOrder;
		}
	}


	private static void setPredicateOrder(TTEntity shape) {
		if (EntityCache.predicateOrder.get(shape.getIri())==null) {
			if (shape.get(RDFS.SUBCLASSOF) != null) {
				boolean hasSuperShape = false;
				for (TTValue superClass : shape.get(RDFS.SUBCLASSOF).getElements()) {
					TTBundle superBundle= EntityCache.shapes.get(superClass.asIriRef().getIri());
					if (superBundle != null) {
						hasSuperShape = true;
						TTEntity superEntity= superBundle.getEntity();
						setPredicateOrder(superEntity);
						if (EntityCache.predicateOrder.get(superEntity.getIri())!=null){
							int index=0;
							for (TTIriRef pred: EntityCache.predicateOrder.get(superEntity.getIri())){
								index++;
								addPredicateOrder(shape.getIri(),pred,index);
							}
						}
						int offset = EntityCache.predicateOrder.get(superEntity.getIri()).size();
						addOrder(shape, offset);
					}
				}
				if (!hasSuperShape)
					addOrder(shape, 0);
			} else
				addOrder(shape, 0);
		}
	}

	private static void inheritProperties(TTEntity shape,
																				Set<TTEntity> inferred) {
		if (shape.get(RDFS.SUBCLASSOF)!=null) {
			for (TTValue superClass : shape.get(RDFS.SUBCLASSOF).getElements()) {
				TTBundle superBundle = EntityCache.shapes.get(superClass.asIriRef().getIri());
				if (superBundle != null) {
					TTEntity superEntity= superBundle.getEntity();
					if (!inferred.contains(superEntity))
						inheritProperties(superEntity,inferred);
					inferred.add(shape);
					if (superEntity.get(SHACL.PROPERTY) != null) {
						for (TTValue value : superEntity.get(SHACL.PROPERTY).getElements()) {
							TTIriRef path= value.asNode().get(SHACL.PATH).asIriRef();
							if (!hasProperty(shape,path))
								shape.addObject(SHACL.PROPERTY, value);
						}
					}
				}
			}
		}
	}

	private static boolean hasProperty(TTEntity shape, TTIriRef path) {
		if (shape.get(SHACL.PROPERTY)!=null) {
			for (TTValue value : shape.get(SHACL.PROPERTY).getElements()) {
				if (value.asNode().get(SHACL.PATH).asIriRef().equals(path))
					return true;
			}
		}
		return false;
	}


	private static void addOrder(TTEntity shape, int offset) {
		int order=0;
		if (shape.get(SHACL.PROPERTY)!=null){
			for (TTValue value:shape.get(SHACL.PROPERTY).getElements()) {
				order++;
				TTNode property=value.asNode();
				TTIriRef path= property.get(SHACL.PATH).asIriRef();
				int insertAt= property.get(SHACL.ORDER)==null ?order :
									property.get(SHACL.ORDER).asLiteral().intValue();
				addPredicateOrder(shape.getIri(),path,insertAt+offset);

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
