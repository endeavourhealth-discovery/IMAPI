package org.endeavourhealth.imapi.transformengine;

import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class TransformFunctions {


	public static Object runFunction(String iri, Map<String, Object> args) throws DataFormatException {
		if (iri.equals(IM.NAMESPACE.iri+"Concatenate"))
			return concatenate(args);
		else if (iri.equals(IM.NAMESPACE.iri+"StringJoin"))
			return stringJoin(args);
		else if (iri.equals(IM.NAMESPACE.iri+"SchemedCodeConceptMap"))
			return schemeCodeConcept(args);
		else
			throw new RuntimeException("FunctionClause not supported : "+ iri);
	}

	private static Object schemeCodeConcept(Map<String, Object> args) {
		return null;
	}


	private static Object stringJoin(Map<String, Object> args) throws DataFormatException {
		try {
			String delimiter = String.valueOf(args.get("delimiter"));
			Object value= args.get("elements");
			if (value instanceof List<?>) {
				List<String> elements = (((List<String>) value).stream().map(String::valueOf).collect(Collectors.toList()));
				return String.join(delimiter, elements);
			}
			else
				return String.valueOf(value);
		}
		catch (Exception e) {
			throw new DataFormatException("String join function must have 'delimiter' as a string and 'elements' as a list");
		}
	}

	private static Object concatenate(Map<String, Object> args) {
		StringBuilder result= new StringBuilder();
		for (Map.Entry<String,Object> entry:args.entrySet()){
			Object value= entry.getValue();
			if (value instanceof Collection) {
				for (Object item : (Collection) value)
					result.append(String.valueOf(value));
			}
			else
				result.append(value);
			}
		return result.toString();
	}
}
