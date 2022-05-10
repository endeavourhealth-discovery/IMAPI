package org.endeavourhealth.imapi.model.sets;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DataSet of static methods that produce text summaries for shapes
 *
 */
public class Summariser {

	private static final String im=IM.NAMESPACE;
	private static final Map<String,String> verbMap= Map
		.of(im+"patientType", "be",
		im+"age","be",
			"GREATER_THAN_OR_EQUAL",">=",
			"GREATER_THAN",">",
			"LESS_THAN_OR_EQUAL","<=",
			"LESS_THAN",">");

	public static String getSummary(Match match){
		StringBuilder summary= new StringBuilder();
		summary.append(getVerb(match.getProperty().getIri())).append(" ");
		if (match.getValueIn()!=null)
			//summary.append(getValueIn(match.getValueIn()));
		if (match.getValueCompare()!=null)
			summary.append(getCompare(match.getValueCompare()));
		return summary.toString();
	}

	private static String getCompare(Compare valueCompare) {
		StringBuilder compare= new StringBuilder();
		compare.append(verbMap.get(valueCompare.getComparison().name()));
		return compare.toString();
	}

	private static String getVerb(String property){
		String verb=verbMap.get(property);
		if (verb==null)
			verb="have";
		return verb;
	}

	private static String getValueIn(List<TTIriRef> in){
		if (in.size()<3){
			List<String> values= new ArrayList<>();
			for (TTIriRef iri:in){
				values.add(iri.getName());
			}
			return String.join(",",values);
		}
		else
			return "value set";

	}
}
