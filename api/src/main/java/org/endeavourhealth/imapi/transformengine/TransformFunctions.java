package org.endeavourhealth.imapi.transformengine;

import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransformFunctions {


	public static Object runFunction(String iri, Map<String, List<Object>> args) {
		if (iri.equals(IM.NAMESPACE+"Concatenate"))
			return concatenate(args);
		else
			throw new RuntimeException("Function not supported : "+ iri);
	}

	private static Object concatenate(Map<String, List<Object>> args) {
		StringBuilder result= new StringBuilder();
		for (Map.Entry<String,List<Object>> entry:args.entrySet()){
			for (Object value:entry.getValue()){
				result.append((String) value);
			}
		}
		return result.toString();
	}
}
