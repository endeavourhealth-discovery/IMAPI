package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.*;

/**
 * static utilities to handle templated display orders of RDF nodes
 */
public class TTDisplay {
	private static TTIriRef[] query = {IMQ.SELECT,IMQ.WHERE};
	private static TTIriRef[] clause = {IMQ.TRIPLE,IMQ.FILTER,IMQ.UNION,IMQ.MINUS};
	private static TTIriRef[] triple = {IMQ.ENTITY,IMQ.PROPERTY,IMQ.VALUE};
	private static TTIriRef[] filter = {RDF.TYPE,IMQ.VAR,IMQ.EXPRESSION};
	private static TTIriRef[] entity = {RDF.TYPE,RDFS.LABEL,IM.DEFINITION,IM.NOT_MEMBER};

	public static TTIriRef[] getTemplate(TTNode node){
		return entity;
	}
	public static TTIriRef[] getTemplate(TTIriRef predicate){
		if (predicate.equals(IMQ.HAS_QUERY))
			return query;
		if (predicate.equals(IMQ.WHERE))
			return clause;
		if (predicate.equals(IMQ.UNION))
			return clause;
		if (predicate.equals(IMQ.MINUS))
			return clause;
		if (predicate.equals(IMQ.TRIPLE))
			return triple;
		if (predicate.equals(IMQ.FILTER))
			return filter;
		return null;
	}

}
