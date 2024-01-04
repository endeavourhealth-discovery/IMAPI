package org.endeavourhealth.imapi.model.tripletree;

import java.util.HashMap;
import java.util.Map;

public class TTEntityMap {
	private Map<String,TTEntity> entities= new HashMap<>();
	private Map<String, String> predicates = new HashMap<>();


	public Map<String,TTEntity> getEntities() {
		return entities;
	}

	public TTEntityMap setEntities(Map<String,TTEntity> entityResources) {
		this.entities = entityResources;
		return this;
	}

	public TTEntityMap addEntity(String iri, TTEntity entity){
		entities.put(iri,entity);
		return this;
	}

	public TTEntity getEntity(String iri){
		return entities.get(iri);
	}


	public Map<String, String> getPredicates() {
		return predicates;
	}

	public TTEntityMap setPredicates(Map<String, String> predicates) {
		this.predicates = predicates;
		return this;
	}

	public TTEntityMap addPredicate(TTIriRef predicate) {
		predicates.put(predicate.getIri(), predicate.getName());
		return this;
	}

	public TTEntityMap addPredicate(String predicateIri,String name) {
		predicates.put(predicateIri, name);
		return this;
	}
}