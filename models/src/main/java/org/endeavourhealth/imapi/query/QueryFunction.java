package org.endeavourhealth.imapi.query;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.RDF;

/**
 * A clause which applies a function to a set of supplied arguments to produce a result
 */
public class QueryFunction extends TTNode {
	public TTIriRef getType() {
		return get(RDF.TYPE).asIriRef();
	}

	public QueryFunction setName(TTIriRef function) {
		this.set(RDF.TYPE,function);
		return this;
	}

	
}
