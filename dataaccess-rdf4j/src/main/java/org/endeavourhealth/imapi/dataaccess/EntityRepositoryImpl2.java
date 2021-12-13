package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class EntityRepositoryImpl2 {
	private Map<String,String> prefixMap;
	private StringJoiner spql;

	public Set<CoreLegacyCode> getSetExpansion(TTEntity set,Boolean includeLegacy){
		String sql= getExpansionAsSelect(set,includeLegacy);
		Set<CoreLegacyCode> result= new HashSet<>();
		try (RepositoryConnection conn = ConnectionManager.getConnection()) {
			TupleQuery qry = conn.prepareTupleQuery(sql);
			try (TupleQueryResult rs = qry.evaluate()) {
				while (rs.hasNext()) {
					BindingSet bs = rs.next();
					CoreLegacyCode cl= new CoreLegacyCode();
					result.add(cl);
					cl.setTerm(bs.getValue("name").stringValue())
						.setCode(bs.getValue("code").stringValue())
						.setScheme(TTIriRef.iri(bs.getValue("scheme").stringValue()));
					if (includeLegacy){
						Value lc= bs.getValue("legacyCode");
						if (lc!=null)
							cl.setLegacyCode(lc.stringValue());
						Value lt= bs.getValue("legacyName");
						if (lt!=null)
							cl.setLegacyTerm(lt.stringValue());
						Value ls= bs.getValue("legacySchemeName");
						if (ls!=null)
							cl.setLegacyTerm(ls.stringValue());
					}

				}
			}
		}
		return result;

	}
	public TTEntity getEntity(String iri, Set<String> predicates){
		StringJoiner sql= new StringJoiner("\n");
		sql.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
		sql.add("CONSTRUCT {")
			.add("  ?entity ?1predicate ?1Level.")
			.add("  ?1Level rdfs:label ?1Name.");
		  for (int i=1;i<5;i++) {
			sql.add("  ?"+i+"Level ?"+(i+1)+"predicate ?"+(i+1)+"Level.")
					.add("  ?"+(i+1)+"predicate rdfs:label ?"+(i+1)+"pName.")
					.add("  ?"+(i+1)+"Level rdfs:label ?"+(i+1)+"Name.");
			}
			sql.add("}");

			sql.add("WHERE {")
			.add("  ?entity ?1predicate ?1Level.");
		if (predicates!=null){
			StringBuilder inPredicates= new StringBuilder();
			for (String pred:predicates) {
				inPredicates.append("<").append(pred).append("> ");
			}
			sql.add("   FILTER ?1 predicate IN ("+inPredicates+")");
		}
		sql.add("  OPTIONAL {?1Level rdfs:label ?1Name.")
			.add("    FILTER (!isBlank(?1Level))}");
		for (int i=1;i<5;i++) {
			sql.add("  OPTIONAL {?" + (i) + "Level ?" + (i + 1) + "predicate ?" + (i + 1) + "Level.")
				.add("    FILTER (isBlank(?" + i + "Level))")
				.add("  OPTIONAL {?" + (i + 1) + "predicate rdfs:label ?" + (i + 1) + "pName}")
				.add("  OPTIONAL {?" + (i + 1) + "Level rdfs:label ?" + (i + 1) + "Name")
				.add("    FILTER (!isBlank(?" + (i + 1) + "Level))}");
		}
			sql.add("}}}}}");
		try (RepositoryConnection conn = ConnectionManager.getConnection()) {
			GraphQuery qry=conn.prepareGraphQuery(sql.toString());
			qry.setBinding("entity",iri(iri));
			try (GraphQueryResult gs = qry.evaluate()) {
				TTEntity entity = null;
				TTNode node;
				Map<Value, TTValue> valueMap = new HashMap<>();
				Resource entityIri = null;
				for (org.eclipse.rdf4j.model.Statement st :gs) {
					Resource subject = st.getSubject();
					TTIriRef predicate= TTIriRef.iri(st.getPredicate().stringValue());
					valueMap.put(st.getPredicate(),predicate);
					if (entity == null) {
						entity = new TTEntity();
						entity.setIri(subject.stringValue());
						valueMap.put(subject, entity);
						entityIri = subject;
					}
					if (subject.isIRI() & (!subject.equals(entityIri))) {
						TTIriRef subjectIri = valueMap.get(subject).asIriRef();
						subjectIri.setName(st.getObject().stringValue());
					}
					else {
						node = valueMap.get(subject).asNode();
						Value value = st.getObject();
						if (value.isLiteral()) {
							node.set(TTIriRef.iri(st.getPredicate().stringValue()), literal(value.stringValue()));
						}
						else if (value.isIRI()) {
							TTIriRef objectIri=null;
							if (valueMap.get(value)!=null)
								objectIri=valueMap.get(value).asIriRef();
							if (objectIri==null)
								objectIri = TTIriRef.iri(value.stringValue());
							if (node.get(predicate)==null)
								node.set(predicate, objectIri);
							else
								node.addObject(predicate,objectIri);
							valueMap.putIfAbsent(value, objectIri);
						}
						else if (value.isBNode()) {
							TTNode ob = new TTNode();
							if (node.get(predicate)==null)
								node.set(predicate, ob);
							else
								node.addObject(predicate,ob);
							valueMap.put(value, ob);
						}
					}
				}
				return entity;
				}
			}
		}

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
			.add("?concept im:scheme ?legacyScheme")
			.add("?concept im:schemeName ?schemeName.");
		if (includeLegacy) {
			spql.add("?legacy rdfs:label ?legacyName.")
				.add("?legacy im:code ?legacyCode.")
				.add("?legacy im:scheme ?legacyScheme")
				.add("?legacy im:legacySchemeName ?legacySchemeName.");
		}
		spql.add("}");
		spql.add("WHERE {");
		addNames(includeLegacy);
		spql.add("{SELECT distinct ?concept");
		whereClause(setEntity);
		spql.add("}");
		return insertPrefixes()+ spql.toString();
	}


	/**
	 * Returns a set expansion as a select query. Note that if legacy is included the result will be a denormalised list.
	 * @param setEntity iri of set
	 * @param includeLegacy whether to include simple legacy maps
	 * @return String containing the sparql query
	 */
	public String getExpansionAsSelect(TTEntity setEntity,Boolean includeLegacy){
		initialiseBuilders();
		spql.add("SELECT ?concept ?name ?code ?scheme ?schemeName ");
		if (includeLegacy)
			spql.add("?legacy ?legacyName ?legacyCode ?legacyScheme ?legacySchemeName");
		spql.add("WHERE {");
		addNames(includeLegacy);
		spql.add("{SELECT distinct ?concept");
		whereClause(setEntity);
		spql.add("}");
		spql.add("}");
		return insertPrefixes()+spql.toString();
	}

	private void whereClause(TTEntity setEntity) {
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
			orClause(definition.get(SHACL.OR));

		}
		else if (definition.get(SHACL.AND)!=null) {
			Boolean hasRoles= andClause(definition.get(SHACL.AND),true);
			if (hasRoles) {
				andClause(definition.get(SHACL.AND), false);
			}
		}
	}


	private void orClause(TTArray ors) {
		spql.add("{");
		StringBuilder values= new StringBuilder();
		for (TTValue superClass : ors.getElements()) {
			if (superClass.isIriRef())
				values.append(getShort(superClass.asIriRef().getIri())).append(" ");
		}
		if (!values.toString().equals("")) {
			spql.add("{");
			spql.add("?concept " + isa() + " ?superClass.");
			values = new StringBuilder("VALUES ?superClass {" + values + "}");
			spql.add(values.toString());

			spql.add("}");
		}

		for (TTValue complexClass:ors.getElements()){
			if (complexClass.isNode()) {

				addUnion(complexClass.asNode());
			}
		}
		spql.add("}");
	}

	private void addNames(boolean includeLegacy){
		spql.add("GRAPH ?scheme {?concept " + getShort(RDFS.LABEL.getIri(), "rdfs") + " ?name.\n" +
			"?concept " + getShort(IM.CODE.getIri(), "im") + " ?code");
		spql.add(" OPTIONAL {?scheme rdfs:label ?schemeName}}");
		if (includeLegacy){
			spql.add("OPTIONAL {GRAPH ?legacyScheme {")
				.add("?legacy im:matchedTo ?concept.")
				.add("OPTIONAL {?legacy rdfs:label ?legacyName.}")
				.add("?legacy im:code ?legacyCode.")
				.add("OPTIONAL {?legacyScheme rdfs:label ?legacySchemeName}}}");
		}
	}

	private void simpleSuperClass(TTIriRef superClass) {
		spql.add("?concept " + isa() + " "+ getShort(superClass.asIriRef().getIri())+".");

	}

	private void addUnion(TTNode union) {

		if (union.get(SHACL.AND)!=null){
			spql.add("UNION {");
			Boolean hasRoles= andClause(union.get(SHACL.AND),true);
			spql.add("}");
			if (hasRoles) {
				spql.add("UNION {");
				andClause(union.get(SHACL.AND), false);
				spql.add("}");
			}
		}
		else {
			spql.add("UNION {");
			roles(union,true); //adds a set of roles from a group.
			spql.add("}");
			spql.add("UNION {");
			roles(union, false);
			spql.add("}");
		}
	}


	private void roles(TTNode node,boolean group) {
		int count = 1;
		for (Map.Entry<TTIriRef, TTArray> entry:node.getPredicateMap().entrySet()) {
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

	private Boolean andClause(TTArray and,boolean group) {
		Boolean hasRoles=false;
		for (TTValue inter:and.getElements()) {
			if (inter.isNode()) {
				if (inter.asNode().get(SHACL.NOT) == null) {
					roles(inter.asNode(),group);
					hasRoles= true;
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
					notClause(inter.asNode().get(SHACL.NOT).asValue());
		}
		return hasRoles;
	}

	private String isa(){
		//return ("("+ getShort(RDFS.SUBCLASSOF.getIri(),"rdfs")+"|"+ getShort(SNOMED.REPLACED_BY.getIri(),"sct")+")*");
		return getShort(IM.IS_A.getIri());
	}

	private void notClause(TTValue not) {
		spql.add("MINUS {");
		if (not.isIriRef())
			simpleSuperClass(not.asIriRef());
		else if (not.isNode()){
			if (not.asNode().get(SHACL.OR)!=null){
				orClause(not.asNode().get(SHACL.OR));
			}
			else if (not.asNode().get(SHACL.AND)!=null){
				Boolean hasRoles= andClause(not.asNode().get(SHACL.AND),true);
				if (hasRoles) {
					andClause(not.asNode().get(SHACL.AND), false);
				}
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
			sb.append("PREFIX ")
				.append(entry.getValue()).append(": <")
				.append(entry.getKey()).append(">\n");
		}
		return sb.toString();
	}
}



