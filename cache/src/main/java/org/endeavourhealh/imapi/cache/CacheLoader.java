package org.endeavourhealh.imapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.dataaccess.CacheRepositoryImpl;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializer;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CacheLoader implements Runnable{

	private static final CacheRepositoryImpl cacheRepository= new CacheRepositoryImpl();
	private static Set<TTIriRef> iris;

	public static void refreshShape(String iri){
		Cache.shapes.putAll(cacheRepository.getShapeAndSupers(iri));
		Set<TTEntity> inferred= new HashSet<>();
		setPredicateOrder(Cache.shapes.get(iri));
		inheritProperties(Cache.shapes.get(iri),inferred);
	}

	/**
	 * Refreshes the node shape cache, predicate orders and domains and ranges
	 */
	public static void refreshShapes(){
		Set<TTEntity> schema= cacheRepository.getSchema();
		iris= new HashSet<>();

		schema.forEach(CacheLoader::setPredicateOrder);
		Set<TTEntity> inferred= new HashSet<>();
		for (TTEntity shape:schema){
			if (!inferred.contains(shape))
				inheritProperties(shape,inferred);
		}
		TTNodeSerializer.predicateOrder= Cache.predicateOrder;
	}


	private static void setPredicateOrder(TTEntity shape) {
		if (Cache.predicateOrder.get(shape.getIri())==null) {
			if (shape.get(RDFS.SUBCLASSOF) != null) {
				boolean hasSuperShape = false;
				for (TTValue superClass : shape.get(RDFS.SUBCLASSOF).getElements()) {
					TTEntity superEntity = Cache.shapes.get(superClass.asIriRef().getIri());
					if (superEntity != null) {
						hasSuperShape = true;
						setPredicateOrder(superEntity);
						if (Cache.predicateOrder.get(superEntity.getIri())!=null){
							int index=0;
							for (TTIriRef pred: Cache.predicateOrder.get(superEntity.getIri())){
								index++;
								Cache.addPredicateOrder(shape.getIri(),pred,index-1);
							}
						}
						int offset = Cache.predicateOrder.get(superEntity.getIri()).size();
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
				TTEntity superEntity = Cache.shapes.get(superClass.asIriRef().getIri());
				if (superEntity != null) {
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
				iris.add(path);
				int insertAt= property.get(SHACL.ORDER)==null ?order :
									property.get(SHACL.ORDER).asLiteral().intValue();
				Cache.addPredicateOrder(shape.getIri(),path,insertAt+offset-1);
				if (property.get(SHACL.CLASS)!=null)
					iris.add(property.get(SHACL.CLASS).asIriRef());
				else if (property.get(SHACL.DATATYPE)!=null)
					iris.add(property.get(SHACL.DATATYPE).asIriRef());

			}
		}
	}



		@Override
	public void run() {
		synchronized (Cache.shapes) {
			CacheLoader.refreshShapes();
		}
	}

}
