package org.endeavourhealth.imapi.transformengine;

import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class TransformFunctions {


	public static Object runFunction(String iri, Map<String, List<Object>> args) throws DataFormatException {
		if (iri.equals(IM.NAMESPACE+"Concatenate"))
			return concatenate(args);
		else if (iri.equals(IM.NAMESPACE+"StringJoin"))
			return stringJoin(args);
			throw new RuntimeException("Function not supported : "+ iri);
	}

	private static Object stringJoin(Map<String, List<Object>> args) throws DataFormatException {
		try {
			String delimiter = (String) args.get("delimiter").get(0);
			List<String> elements = args.get("elements").stream().map(String::valueOf).collect(Collectors.toList());
			return String.join(delimiter, elements);
		}
		catch (Exception e) {
			throw new DataFormatException("String join function must have 'delimiter' as a string and 'elements' as a list");
		}
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
