package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.IMQ;

/**
 * A specialised RDF shape that provides methods to build a data set definition.
 * Provides the shape as a TTEntity thus serializable as JSON-LD or Turtle
 */
public class Dataset extends TTEntity {

	public Dataset(TTIriRef setType,String iri){
		this.setIri(iri);
		this.set(RDF.TYPE,new TTArray().add(setType));
	}

	/**
	 * Creates and assigns Assigns a query node to the data set
	 * @return the query node created;
	 */
	public Query setQuery() {
		Query query = new Query();
		set(IMQ.HAS_QUERY,query);
		return query;
	}
	/**
	 * Assigns a query node to the data set
	 * @param query The query object when already instantiated
	 * @return the query node created;
	 */
	public Query setQuery(Query query) {
		set(IMQ.HAS_QUERY, query);
		return query;
	}

}