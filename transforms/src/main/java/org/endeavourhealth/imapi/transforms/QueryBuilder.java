package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.query.Query;
import org.endeavourhealth.imapi.query.QueryClause;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Map;

/**
 *Utility methods to help build a query
 */
public class QueryBuilder {
	private Query query;

	public Query createQuery(){
		Query query= new Query();
		addPrefix("im",IM.NAMESPACE);

		return query;
	}

	private void addPrefix(String im, String namespace) {
		query.getPrefixes().put("im",IM.NAMESPACE);
		query.getPrefixMap().put(IM.NAMESPACE,"im");
	}

	public Query setMainEntity(TTIriRef mainEntity){
		query.setMainEntity(getShort(mainEntity));
		return query;
	}

	public QueryClause addClause(){
		QueryClause clause= new QueryClause();
		query.addClause(clause);
		return clause;
	}

	public String getShort(TTIriRef iri){
		String siri= iri.getIri();
		int end = Integer.max(siri.lastIndexOf("/"), siri.lastIndexOf("#"));
		String namespace = siri.substring(0, end + 1);
		String prefix= query.getPrefixMap().get(namespace);
		if (prefix!=null)
			return prefix+":"+ siri.substring(end+1);
		else
			return siri;
	}

	public Query getQuery() {
		return query;
	}

	public QueryBuilder setQuery(Query query) {
		this.query = query;
		return this;
	}
}
