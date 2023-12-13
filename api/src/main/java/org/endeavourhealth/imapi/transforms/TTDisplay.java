package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.*;

/**
 * static utilities to handle templated display orders of RDF nodes
 */
public class TTDisplay {
	private TTDisplay() {
		throw new IllegalStateException("Utility class");
	}

	private static TTIriRef[] entity = {RDF.TYPE.asTTIriRef(),RDFS.LABEL.asTTIriRef(),IM.DEFINITION.asTTIriRef()};

	public static TTIriRef[] getTemplate(TTNode node){
		return entity;
	}

}
