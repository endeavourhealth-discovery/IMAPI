package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.*;

/**
 * static utilities to handle templated display orders of RDF nodes
 */
public class TTDisplay {

	private static TTIriRef[] entity = {RDF.TYPE,RDFS.LABEL,IM.DEFINITION,IM.NOT_MEMBER};

	public static TTIriRef[] getTemplate(TTNode node){
		return entity;
	}

}
