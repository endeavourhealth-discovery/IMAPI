package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.dataaccess.ConceptRepository;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Map;

public class FunctionService {
	private ConceptRepository conceptRepository = new ConceptRepository();

	public ObjectNode callFunction(String iri, Map<String,Object> arguments) throws Exception {
		switch (iri){
			case  (IM.NAMESPACE+"Function_SnomedConceptGenerator") :
				return conceptRepository.createConcept(IM.NAMESPACE);
			case (IM.NAMESPACE+"Function_LocalNameRetriever") :
				return getLocalName(arguments);
			default :
				throw new IllegalArgumentException("No such function");
		}

	}

	private ObjectNode getLocalName(Map<String,Object> arguments){
		if (arguments==null)
			throw new IllegalArgumentException("No arguments, send json property/value pairs in request body");
		String iri= (String) arguments.get("entityIri");
		String fieldName= (String) arguments.get("fieldName");
		if (arguments.get("entityIri")==null)
			throw new IllegalArgumentException("No entity iri property in request body");
		if (arguments.get("fieldName")==null)
			throw new IllegalArgumentException("No 'fieldName' to return the local name in. e.g. \"fieldName\" : \"code\"");
		return new ObjectMapper().createObjectNode().put(fieldName,iri.substring(iri.lastIndexOf("#")+1));
	}




}
