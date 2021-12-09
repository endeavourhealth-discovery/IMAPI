package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.util.*;

/**
 * An object that Generate sparql from various query or object models
 */

public class TTSetToSparql {
	private Map<String,String> prefixMap;
	private StringJoiner spql;
	public static Set<TTIriRef> coreSchemes = new HashSet<>(Arrays.asList(IM.GRAPH_DISCOVERY, SNOMED.GRAPH_SNOMED));

	/**
	 * Generates sparql from a concept set entity
	 * @param setEntity the TT entity that holds the set definition
	 * @param includeLegacy whether or not legacy codes should be included
	 * @return A string of SPARQL
	 */
	public String getExpansionAsGraph(TTEntity setEntity, Boolean includeLegacy){
		initialiseBuilders();
		spql.add("CONSTRUCT {?concept rdfs:label ?name.")
			.add("?concept im:code ?code.")
			.add("?concept im:scheme ?schemeName.");
		if (includeLegacy) {
			spql.add("?legacy rdfs:label ?legacyName.")
				.add("?legacy im:code ?legacyCode.")
				.add("?legacy im:scheme ?legacySchemeName.");
		}
		spql.add("}");
		spql.add("WHERE {");
		addNames(includeLegacy);
		spql.add("{SELECT distinct ?concept");
		whereClause(setEntity,includeLegacy);
		spql.add("}");
		return insertPrefixes()+ spql.toString();
	}


	/**
	 * Returns a set expansion as a select query. Note that if legacy is included the result will be a denormalised list.
	 * @param setEntity iri of set
	 * @param includeLegacy whether to include simple legacy maps
	 * @return
	 */
	public String getExpansionAsSelect(TTEntity setEntity,Boolean includeLegacy){
		initialiseBuilders();;
		spql.add("SELECT ?concept ?name ?code ?schemeName ");
		if (includeLegacy)
			spql.add("?legacy ?legacyName ?legacyCode ?legacySchemeName");
		spql.add("WHERE {");
		addNames(includeLegacy);
		spql.add("{SELECT distinct ?concept");
		whereClause(setEntity,includeLegacy);
		spql.add("}");
		return insertPrefixes()+spql.toString();
	}

	private void whereClause(TTEntity setEntity, Boolean includeLegacy) {
		spql.add("WHERE {");
		TTNode definition= setEntity.get(IM.DEFINITION).asNode();
		graphWherePattern(definition);
		spql.add("}");
	}

	private void graphWherePattern(TTNode definition) {
		if (definition.isIriRef()) {
			simpleSuperClass(definition.asIriRef());
		}
		else if (definition.get(SHACL.OR)!=null) {
			orClause(definition.get(SHACL.OR).asArray());
		
		}
		else if (definition.get(SHACL.AND)!=null) {
			andClause(definition.get(SHACL.AND).asArray(),true);
			andClause(definition.get(SHACL.AND).asArray(),false);
		}
	}

	private void orClause(TTArray ors) {
		//First looks for simple IRIs
		String values="";
		for (TTValue superClass : ors.getElements()) {
			if (superClass.isIriRef())
				values = values + getShort(superClass.asIriRef().getIri()) + " ";
		}
		if (!values.equals("")) {
			spql.add("{");
			spql.add("?concept " + isa() + " ?superClass.");
			values = "VALUES ?superClass {" + values + "}";
			spql.add(values);

			spql.add("}");
		}

		for (TTValue complexClass:ors.getElements()){
			if (complexClass.isNode()) {

				addUnion(complexClass.asNode());
				spql.add("}");
			}
		}
	}

	private void addNames(boolean includeLegacy){
		spql.add("GRAPH ?scheme {?concept " + getShort(RDFS.LABEL.getIri(), "rdfs") + " ?name.\n" +
			"?concept " + getShort(IM.CODE.getIri(), "im") + " ?code");
		spql.add(" OPTIONAL {?scheme rdfs:label ?schemeName}}");
		if (includeLegacy){
			spql.add("GRAPH ?legacyScheme {")
				.add("?legacy im:matchedTo ?concept.")
				.add("?legacy rdfs:label ?legacyName.")
				.add("?legacy im:code ?legacyCode.")
				.add("OPTIONAL {?legacyScheme rdfs:label ?legacySchemeName}}");
		}
	}

	private void simpleSuperClass(TTIriRef superClass) {
		spql.add("?concept " + isa() + " "+ getShort(superClass.asIriRef().getIri())+".");

	}

	private void addUnion(TTNode union) {

		if (union.get(SHACL.AND)!=null){
			spql.add("UNION {");
			andClause(union.get(SHACL.AND).asArray(),true);
			spql.add("}");
			spql.add("UNION {");
			andClause(union.get(SHACL.AND).asArray(),false);
			spql.add("}");
		}
		else {
			spql.add("UNION {");
			roles(union,true); //adds a set of roles from a group.
			spql.add("}");
			spql.add("UNION {");
			roles(union,false);
			spql.add("}");
		}
	}


	private void roles(TTNode node,boolean group) {
		int count = 1;
		for (Map.Entry<TTIriRef, TTValue> entry:node.getPredicateMap().entrySet()) {
			count++;
			String obj = "?o_" + count;
			String pred = "?p_" + count;
			spql.add(obj + " " + isa() + " " + getShort(entry.getValue().asIriRef().getIri()) + ".");
			spql.add(pred + " " + isa() + " " + getShort(entry.getKey().getIri()) + ".");
			if (group) {
				spql.add("?roleGroup " + pred + " " + obj + ".");
				spql.add(" FILTER (isBlank(?roleGroup))");
				spql.add("?superMember " + getShort(IM.ROLE_GROUP.getIri(), "im") + " ?roleGroup.");
			}
			else {
				spql.add("?superMember " + pred + " " + obj + ".");
				spql.add("  FILTER (isIri(?superMember))");
			}
		}
		spql.add("?concept "+getShort(IM.IS_A.getIri(),"im")+" ?superMember.");

	}



	private String getShort(String iri){
		if (iri.contains("#")){
			String prefix= iri.substring(0,iri.lastIndexOf("#"));
			prefix= prefix.substring(prefix.lastIndexOf("/")+1);
			prefixMap.put(iri.substring(0,iri.lastIndexOf("#")+1),prefix);
			return prefix+":"+ iri.substring(iri.lastIndexOf("#")+1);
		}
		return "<"+iri+">";
	}
	private String getShort(String iri,String prefix){
			prefixMap.put(iri.substring(0,iri.lastIndexOf("#")+1),prefix);
			return prefix+":"+ iri.substring(iri.lastIndexOf("#")+1);
	}

	private void andClause(TTArray and,boolean group) {
		for (TTValue inter:and.getElements()) {
			if (inter.isNode()) {
				if (inter.asNode().get(SHACL.NOT) == null) {
					roles(inter.asNode(),group);
				}
			}
		}
		for (TTValue inter:and.getElements()){
			if (inter.isIriRef()){
				simpleSuperClass(inter.asIriRef());
			}
		}
		for (TTValue inter:and.getElements()){
				if (inter.isNode())
					if (inter.asNode().get(SHACL.NOT)!=null)
						notClause(inter.asNode().get(SHACL.NOT).asNode());
			}
	}

	private String isa(){
		//return ("("+ getShort(RDFS.SUBCLASSOF.getIri(),"rdfs")+"|"+ getShort(SNOMED.REPLACED_BY.getIri(),"sct")+")*");
		return getShort(IM.IS_A.getIri());
	}

	private void notClause(TTNode not) {
		spql.add("MINUS {");
		if (not.isIriRef())
			simpleSuperClass(not.asIriRef());
		else if (not.isNode()){
			if (not.asNode().get(SHACL.OR)!=null){
				orClause(not.asNode().get(SHACL.OR).asArray());
			}
			else if (not.asNode().get(SHACL.AND)!=null){
				andClause(not.asNode().get(SHACL.AND).asArray(),true);
				andClause(not.asNode().get(SHACL.AND).asArray(),false);
			}
		}
		spql.add("}");
	}





	private void initialiseBuilders() {
		prefixMap = new HashMap<>();
		spql= new StringJoiner("\n");
	}

	private String insertPrefixes() {
		StringBuilder sb= new StringBuilder();
		for (Map.Entry<String,String> entry:prefixMap.entrySet()){
			sb.append("PREFIX "+entry.getValue()+": <"+ entry.getKey()+">\n");
		}
		return sb.toString();
	}
}

