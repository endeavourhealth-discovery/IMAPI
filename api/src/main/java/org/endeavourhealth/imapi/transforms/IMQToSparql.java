package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.query.Match;
import org.endeavourhealth.imapi.model.query.Query;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Methods to convert a Query object to its Sparql equivalent
 */
public class IMQToSparql {
	private TTContext prefixes;
	private Map<String,String> varMap= new HashMap<>();

	public String getDefaultPrefixes(){
		prefixes= new TTContext();
		StringBuilder sparql= new StringBuilder();
		prefixes.add(RDFS.NAMESPACE,"rdfs");
		sparql.append("PREFIX rdfs: <"+ RDFS.NAMESPACE+">\n");
		prefixes.add(RDF.NAMESPACE,"rdf");
		sparql.append("PREFIX rdf: <"+ RDF.NAMESPACE+">\n");
		prefixes.add(IM.NAMESPACE,"im");
		sparql.append("PREFIX im: <"+ IM.NAMESPACE+">\n");
		prefixes.add(XSD.NAMESPACE,"xsd");
		sparql.append("PREFIX xsd: <"+ XSD.NAMESPACE+">\n");
		prefixes.add(SNOMED.NAMESPACE,"sn");
		sparql.append("PREFIX sn: <"+ SNOMED.NAMESPACE+">\n\n");
		return sparql.toString();
	}

	public String convert(Query query){
		StringBuilder spq= new StringBuilder();
		spq.append(getDefaultPrefixes());
		spq.append(where(query.getWhere()));

		return spq.toString();
	}
	
	public String where(Match where){
		if (where==null)
			return "";
		StringBuilder spq= new StringBuilder();
		spq.append("WHERE {\n");
		if (where.getGraph()!=null) {
			spq.append(graph(where.getGraph()));
			spq.append(match(where));
			spq.append("}\n");
		}
		else
			spq.append(match(where));
		return spq.toString();
	}

	private boolean match(Match where) {
		return false;
	}

	private String graph(TTIriRef graph) {
		if (graph==null)
			return "";
		return "graph "+ iri(graph.getIri())+" {";
		
	}

	private String iri(String iri){
		return "<"+ iri+">";
	}

}
