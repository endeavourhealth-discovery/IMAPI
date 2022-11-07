package org.endeavourhealth.imapi.model.maps;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.function.Consumer;

/**
 * A map from one source schema to one target schema. Schema being a collection of tyoed resources such as FHIR/R4 or DSTU2 ets.
 * <p> A Map graph contains many entity maps</p>
 */
public class GraphMap extends TTEntity {


	public TTArray getEntityMap() {
		return this.get(IM.ENTITY_MAP);
	}

	public GraphMap setEntityMap(TTArray map) {
		this.set(IM.ENTITY_MAP,map);
		return this;
	}

	public GraphMap addEntityMap(EntityMap map){
		this.addObject(IM.ENTITY_MAP,map);
		return this;
	}

	public GraphMap entityMap(Consumer<EntityMap> builder){
		EntityMap map= new EntityMap();
		addEntityMap(map);
		builder.accept(map);
		return this;
	}



	@Override
	public GraphMap setIri(String iri) {
		super.setIri(iri);
		return this;
	}

	@Override
	public GraphMap setName(String name) {
		super.setName(name);
		return this;
	}



	public GraphMap setDescription(String description) {
		super.setDescription(description);
		return this;
	}


}
