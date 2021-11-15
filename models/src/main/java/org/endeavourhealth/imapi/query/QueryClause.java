package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IMQ;
import java.util.zip.DataFormatException;

/**
 * A specialised RDF node that provides methods to populate a query clause such as where,union,minus.
 * Provides the node as a TTNode thus serializable as JSON-LD or Turtle
 */
public class QueryClause extends TTNode {


	/**
	 * Adds a simple triple to  clause
	 * @param entity iri or variable without ? or $
	 * @param property iri or variable without ? or $
	 * @param value iri variable or data value or blank node
	 * @return the clause containing the triple
	 */
	public QueryClause addTriple(Object entity, TTIriRef pathType,TTValue property, TTValue value) throws DataFormatException {
		if (get(IMQ.TRIPLE)==null) {
			set(IMQ.TRIPLE, new TTArray());
			if (entity==null)
				throw new DataFormatException("first triple block must have a subject");
		}
			QueryTriple tpl = new QueryTriple();
			get(IMQ.TRIPLE).asArray().add(tpl);
			if (entity!=null) {
				if (entity instanceof TTIriRef)
					tpl.set(IMQ.ENTITY, (TTIriRef) entity);
				else {
					if (entity instanceof String)
						tpl.set(IMQ.ENTITY, TTLiteral.literal((String) entity));
					else
						throw new IllegalArgumentException("Invalid token in triple subject position");
				}
			}
			Path path= new Path(pathType,property);
			tpl.set(IMQ.PROPERTY,path);
			tpl.set(IMQ.VALUE,value);
			return this;

	}


	/**
	 * Adds a union
	 * @return the union
	 */
	public QueryClause addUnion() {
		if (get(IMQ.UNION) == null)
			set(IMQ.UNION, new TTArray());
		QueryClause union= new QueryClause();
		get(IMQ.UNION).asArray().add(union);
		return union;
	}

	/**
	 * Adds a minus clause to a where or union clause
	 * @return the minus clause
	 */

	public QueryClause setMinus() {
		QueryClause minus= new QueryClause();
		set(IMQ.MINUS,minus);
		return minus;
	}
	/**
	 * Creates and adds a filter to a clause clause
	 * @return the filter object created
	 */
	public Filter addFilter(TTIriRef filterType) {
		if (get(IMQ.FILTER) == null)
			set(IMQ.FILTER, new TTArray());
		Filter filter = new Filter(filterType);
		get(IMQ.FILTER).asArray().add(filter);
		return filter;
	}


}
