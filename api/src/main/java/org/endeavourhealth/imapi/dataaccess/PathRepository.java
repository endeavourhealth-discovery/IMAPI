package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.model.iml.PathQuery;
import org.endeavourhealth.imapi.model.iml.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

public class PathRepository {

	public TTDocument queryIM(QueryRequest request,RepositoryConnection conn) throws DataFormatException {
		PathQuery pathQuery= request.getPathQuery();
		String spq= buildPathSQL(request,pathQuery,conn);
		TupleQuery qry= conn.prepareTupleQuery(spq);
		try (TupleQueryResult rs= qry.evaluate()) {
			return bindResults(rs, request);
		}
	}

	public static String buildPathSQL (QueryRequest request, PathQuery pathQuery,RepositoryConnection conn) throws DataFormatException {
			String source;
			String target;
			if (pathQuery.getTarget()==null)
				target= SparqlConverter.resolveReference("target",request);
			else
				target= pathQuery.getTarget().getIri();
			if (pathQuery.getSource()==null)
				source= SparqlConverter.resolveReference("source",request);
			else
				source= pathQuery.getSource().getIri();
			Integer depth= pathQuery.getDepth();
			if (depth==null) {
				depth= Integer.parseInt(request.getArgumentDataValue("depth").toString());
				pathQuery.setDepth(depth);
			}
			String sql = "select ?type ?superType ?where {<" + target + "> <" + RDF.TYPE.getIri() + "> ?type.\n" +
				"optional {<"+target+"> <" + IM.IS_A.getIri() + "> ?superType.\n" +
				"filter (?superType=<" + IM.NAMESPACE + "dataModelProperty>)}}";

			TupleQuery qry = conn.prepareTupleQuery(sql);

			try (TupleQueryResult rs = qry.evaluate()) {
				List<TTIriRef> types = new ArrayList<>();
				while (rs.hasNext()) {
					BindingSet bs = rs.next();
					if (bs.getValue("superType") != null)
						types.add(TTIriRef.iri(IM.NAMESPACE + "dataModelProperty"));
					types.add(TTIriRef.iri(bs.getValue("type").stringValue()));
				}
				if (types.isEmpty())
					throw new DataFormatException("Unknown entity type " + target);
				if (types.contains(TTIriRef.iri(IM.NAMESPACE + "dataModelProperty")))
					return entityToDMPropertyPathSql(depth, source, target);
				else if (types.contains(RDF.PROPERTY))
					return entityToConceptPropertyPathSql(depth, source, target);
				else if (types.contains(SHACL.NODESHAPE))
					return entityToEntityPathSql(depth, source, target);
				else
					return entityToConceptPathSql(depth, source, target);
			}

	}


	private static String entityToConceptPropertyPathSql(Integer depth, String source, String target) {
		StringBuilder sql= new StringBuilder(setDefaultPrefixes());
		sql.append("Select ?entity ?entityLabel ?path1 ?path1Label");
		for (int i=1; i<(depth); i++){
			sql.append(" ?subEntity").append(i).append(" ?subEntity").append(i).append("Label")
				.append(" ?path").append(i+1)
				.append(" ?path").append(i+1).append("Label").append("\n");
		}
		sql.append("where {\n" + "    {  # First get to a value set\n" + "        Select distinct ?set \n" + "        where {\n" + "           ?roleGroup <")
			.append(target).append("> ?value.\n")
			.append("           ?testConcept im:roleGroup ?roleGroup.\n")
			.append("           ?set im:hasMember ?testConcept.\n").append("          }}\n")
			.append("    #Then get the paths from Main entities to an entity, include  the sub entities\n")
			.append("    ?entity im:isA <").append(source).append(">.\n")
			.append("    ?entity sh:property ?prop1.\n")
			.append("    ?entity rdfs:label ?entityLabel.\n")
			.append("    #Retrieve the path from main entity to entity\n")
			.append("    ?prop1 sh:path ?path1.\n")
			.append("     ?path1 rdfs:label ?path1Label.")
			.append("    {\n")
			.append("        ?prop1 sh:class ?set.\n").append("    }\n");

		for (int i=1; i<(depth); i++){
			sql.append("    union \n" + "    {\n")
				.append("        ?prop").append(i).append(" sh:node ?entity").append(i).append(".\n")
				.append("        ?subEntity").append(i).append(" im:isA ?entity").append(i).append(".\n")
				.append("        filter (?subEntity").append(i).append(" not in(").append(removeRepeat(i)).append("))\n")
				.append("        ?subEntity").append(i).append(" sh:property ?prop").append(i + 1).append(".\n")
				.append("        ?subEntity").append(i).append(" rdfs:label ?subEntity").append(i).append("Label.\n")
				.append("        ?prop").append(i + 1).append(" sh:path ?path").append(i + 1).append(".\n")
				.append("        ?path").append(i + 1).append(" rdfs:label ?path").append(i + 1).append("Label.\n")
				.append("        {\n").append("            ?prop").append(i + 1).append(" sh:class ?set.\n")
				.append("        }\n");
		}
		group(depth, sql);
		sql.append("\n");
		sql.append("order by ?entity ?path1");
		for (int i=1; i<depth; i++){
			sql.append(" ?subEntity").append(i).append(" ?path").append(i + 1);
		}
		return sql.toString();
	}

	private static void group(Integer depth, StringBuilder sql) {
		sql.append("}\n".repeat(Math.max(0, depth)));

		sql.append(
			"group by ?entity ?entityLabel ?path1 ?path1Label");
		for (int i=1; i<depth; i++){
			sql.append(" ?subEntity").append(i).append(" ?subEntity").append(i).append("Label")
				.append(" ?path").append(i + 1).append(" ?path").append(i+1).append("Label");
		}
	}


	private static String entityToConceptPathSql(Integer depth, String source, String target) {
		StringBuilder sql= new StringBuilder(setDefaultPrefixes());
		sql.append("Select ?entity ?entityLabel ?path1 ?path1Label");
		for (int i=1; i<(depth); i++){
			sql.append(" ?subEntity").append(i).append(" ?subEntity").append(i).append("Label")
				.append(" ?path").append(i+1)
				.append(" ?path").append(i+1).append("Label");
		}
		sql.append(" ?conceptProperty ?conceptPropertyLabel\n");
		sql.append("where {\n")
			.append("    {  # First get to a value set\n")
			.append("        Select distinct ?set ?conceptProperty ?conceptPropertyLabel\n")
			.append("        where {\n")
			.append("            ?conceptProperty rdfs:label ?conceptPropertyLabel.\n")
			.append("            filter not exists {\n")
			.append("                ?conceptProperty im:isA ?super.\n")
			.append("                filter (?super!= ?conceptProperty)\n")
			.append("                ?set im:hasMember ?testConcept.\n")
			.append("                ?testConcept im:roleGroup ?rg2.\n")
			.append("                ?rg2 ?super <")
			.append(target).append(">.\n")
			.append("            }\n")
			.append("            { #Either the concept is a value in a role group of a concept that is in a value set\n")
			.append("              #or a concept in the value set expansion  \n")
			.append("                Select distinct ?set ?concept ?conceptProperty\n")
			.append("                where {\n")
			.append("                    {\n")
			.append("                        ?roleGroup ?superProperty <").append(target).append(">.\n")
			.append("                        ?conceptProperty im:isA ?superProperty.\n")
			.append("                        ?roleGroup ^im:roleGroup ?concept.\n")
			.append("                        ?concept ^im:hasMember ?set.\n")
			.append("                        ?set ^sh:class ?property.\n").append("                    }\n")
			.append("                    union \n")
			.append("                    {\n")
			.append("                        <").append(target).append("> ^im:hasMember ?set.\n").append("                    }\n")
			.append("                }\n").append("            }\n").append("        }\n").append("    }\n")
			.append("    #Then get the paths from Main entities to an entity, include  the sub entities\n")
			.append("    ?entity im:isA <").append(source).append(">.\n").append("    ?entity sh:property ?prop1.\n")
			.append("    ?entity rdfs:label ?entityLabel.\n")
			.append("    #Retrieve the path from main entity to entity\n")
			.append("    ?prop1 sh:path ?path1.\n").append("     ?path1 rdfs:label ?path1Label.")
			.append("    {\n").append("        ?prop1 sh:class ?set.\n").append("    }\n");

		for (int i=1; i<(depth); i++){
			sql.append("    union \n")
				.append("    {\n" + "        ?prop").append(i).append(" sh:node ?entity").append(i).append(".\n")
				.append("        ?subEntity").append(i).append(" im:isA ?entity").append(i).append(".\n")
				.append("        filter (?subEntity").append(i).append(" not in(").append(removeRepeat(i)).append("))\n")
				.append("        ?subEntity").append(i).append(" sh:property ?prop").append(i + 1).append(".\n")
				.append("        ?subEntity").append(i).append(" rdfs:label ?subEntity").append(i).append("Label.\n")
				.append("        ?prop").append(i + 1).append(" sh:path ?path").append(i + 1).append(".\n")
				.append("        ?path").append(i + 1).append(" rdfs:label ?path").append(i + 1).append("Label.\n")
				.append("        {\n").append("            ?prop").append(i + 1).append(" sh:class ?set.\n")
				.append("        }\n");
		}
		group(depth, sql);
		sql.append(" ?conceptProperty ?conceptPropertyLabel\n");
		sql.append("order by ?entity ?path1");
		for (int i=1; i<depth; i++){
			sql.append(" ?subEntity").append(i).append(" ?path").append(i + 1);
		}
		return sql.toString();
	}

	private static String entityToEntityPathSql(Integer depth, String source, String target) {
		StringBuilder sql = entitySelectSql(depth);
		sql.append("where {\n" + "   \n")
			.append("    #Then get the paths from Main entity to a  nodeShape, include  the sub entities\n")
			.append("    ?entity im:isA <").append(source).append(">.\n")
			.append("    ?entity sh:property ?prop1.\n")
			.append("    ?entity rdfs:label ?entityLabel.\n")
			.append("    #Retrieve the path from main entity to entity\n");
		for (int i=1; i<(depth+1); i++) {
			sql.append("    ?prop").append(i).append(" sh:path ?path").append(i).append(".\n")
				.append("    ?path").append(i).append(" rdfs:label ?path").append(i).append("Label.\n")
				.append("   {?prop").append(i).append(" sh:node ?target.\n")
				.append("    <").append(target).append("> im:isA ?target.}\n");
			if (i<depth)
				sql.append("    union {\n" + "        ?prop").append(i).append(" sh:node ?entity").append(i).append(".\n")
					.append("        ?subEntity").append(i).append(" im:isA ?entity").append(i).append(".\n")
					.append("        filter (?subEntity").append(i).append(" not in(").append(removeRepeat(i)).append("))\n")
					.append("        ?subEntity").append(i).append(" sh:property ?prop").append(i + 1).append(".\n")
					.append("        ?subEntity").append(i).append(" rdfs:label ?subEntity").append(i).append("Label.\n")
					.append("        ?subEntity").append(i).append(" sh:property ?prop").append(i + 1).append(".\n");
		}

		sql.append("}\n".repeat(Math.max(0, depth)));
		return sql.toString();
	}

	private static StringBuilder entitySelectSql(Integer depth) {
		StringBuilder sql= new StringBuilder(setDefaultPrefixes());
		sql.append("Select ?entity ?entityLabel");
		for (int i=1; i<(depth+1); i++){
			sql.append(" ?path").append(i).append(" ?path").append(i).append("Label")
				.append(" ?subEntity").append(i).append(" ?subEntity").append(i).append("Label");
		}
		return sql;
	}

	private static String entityToDMPropertyPathSql(Integer depth, String source, String target) {
		StringBuilder sql=entitySelectSql(depth);
		sql.append("where {\n" + "   \n")
			.append("    #Then get the paths from entity to a data model property, include  the sub entities\n")
			.append("    ?entity im:isA <").append(source).append(">.\n")
			.append("    ?entity sh:property ?prop1.\n")
			.append("    ?entity rdfs:label ?entityLabel.\n")
			.append("    #Retrieve the path from main entity to entity\n");
		for (int i=1; i<(depth+1); i++) {
			sql.append("   { ?prop").append(i).append(" sh:path <").append(target).append(">.}\n")
				.append("    union {\n")
				.append("        ?prop").append(i).append(" sh:path ?path").append(i).append(".\n")
				.append("        ?path").append(i).append("  rdfs:label ?path").append(i).append("Label.\n")
				.append("        ?prop").append(i).append(" sh:node ?entity").append(i).append(".\n")
				.append("        ?subEntity").append(i).append(" im:isA ?entity").append(i).append(".\n")
				.append("        filter (?subEntity").append(i).append(" not in(").append(removeRepeat(i)).append("))\n")
				.append("        ?subEntity").append(i).append(" sh:property ?prop").append(i + 1).append(".\n")
				.append("        ?subEntity").append(i).append(" rdfs:label ?subEntity").append(i).append("Label.\n")
				.append("        ?subEntity").append(i).append(" sh:property ?prop").append(i + 1).append(".\n");
		}
		sql.append("   ?prop").append(depth).append(" sh:path <").append(target).append(">.");
		sql.append("}\n".repeat(Math.max(0, depth+1)));
		return sql.toString();
	}

	private static String removeRepeat(Integer depth){
		StringBuilder repeat= new StringBuilder("?entity");
		for (int i=1; i<(depth); i++){
			repeat.append(",?subEntity").append(i);
		}
		return repeat.toString();
	}

	private static String setDefaultPrefixes() {
		return "PREFIX xsd: <" + XSD.NAMESPACE + ">\n" +
			"PREFIX rdfs: <" + RDFS.NAMESPACE + ">\n" +
			"PREFIX rdf: <" + RDF.NAMESPACE + ">\n" +
			"PREFIX im: <" + IM.NAMESPACE + ">\n" +
			"PREFIX sn: <" + SNOMED.NAMESPACE + ">\n" +
			"PREFIX sh: <" + SHACL.NAMESPACE + ">\n";
	}

	private static TTNode addNode(TTNode node, TTIriRef predicate){
		TTNode newNode= new TTNode();
		node.addObject(predicate,newNode);
		return newNode;
	}

	private static TTNode getNode(TTNode node, TTIriRef predicate){
		TTNode result;
		if (node.get(predicate)==null) {
			result= addNode(node,predicate);
			return result;
		}
		else return node.get(predicate).asNode();
	}

	public static TTDocument bindResults(TupleQueryResult rs,QueryRequest request) {
		Integer depth = request.getPathQuery().getDepth();
		TTDocument result = new TTDocument();
		if (!rs.hasNext())
			return result;
		TTEntity entity = null;
		while (rs.hasNext()) {
			BindingSet bs = rs.next();
			if (entity == null) {
				entity = new TTEntity();
				result.addEntity(entity);
				String iri = bs.getValue("entity").stringValue();
				entity
					.setIri(iri)
					.setName(bs.getValue("entityLabel").stringValue());

			}
			TTNode shape = entity;
			for (int i = 1; i < (depth + 1); i++) {
				if (bs.getValue("path" + i) != null) {
					String property = bs.getValue("path" + i).stringValue();
					TTNode propertyNode = getPropertyNode(shape, property, bs.getValue("path" + i + "Label").stringValue());
					if (bs.getValue("subEntity" + i) != null) {
						String subEntity = bs.getValue("subEntity" + i).stringValue();
						TTNode subEntityNode = getSubEntityMode(propertyNode, subEntity, bs.getValue("subEntity" + i + "Label").stringValue());
						shape = subEntityNode;
					} else if (bs.getValue("conceptProperty") != null) {
						TTNode conceptNode = new TTNode();
						propertyNode.set(SHACL.CLASS, conceptNode);
						conceptNode.setIri(IM.CONCEPT.getIri());
						conceptNode.set(RDFS.LABEL, TTLiteral.literal("Terminology concept"));
						String conceptProperty = bs.getValue("conceptProperty").stringValue();
						TTNode conceptPropertyNode = new TTNode();
						conceptNode.addObject(SHACL.PROPERTY, conceptPropertyNode);
						conceptPropertyNode.set(SHACL.PATH, TTIriRef.iri(conceptProperty).setName(bs.getValue("conceptPropertyLabel").stringValue()));
						conceptPropertyNode.set(SHACL.CLASS, request.getPathQuery().getTarget());
					} else
						propertyNode.set(SHACL.CLASS, request.getPathQuery().getTarget());
				}
			}
		}
		return result;

	}

	private static TTNode getSubEntityMode(TTNode propertyNode, String subEntity,String label) {
		if (propertyNode.get(SHACL.NODE)==null) {
			TTNode subEntityNode = new TTNode();
			propertyNode.addObject(SHACL.NODE, subEntityNode);
			subEntityNode.setIri(subEntity);
			subEntityNode.set(RDFS.LABEL, TTLiteral.literal((label)));
			return subEntityNode;
		}
		for (TTValue prop:propertyNode.get(SHACL.NODE).getElements()){
			if (prop.asNode().getIri().equals(subEntity))
				return prop.asNode();
		}
		TTNode subEntityNode = new TTNode();
		propertyNode.addObject(SHACL.NODE, subEntityNode);
		subEntityNode.setIri(subEntity);
		subEntityNode.set(RDFS.LABEL, TTLiteral.literal((label)));
		return subEntityNode;

	}

	private static TTNode getPropertyNode(TTNode shape, String property,String label) {
		if (shape.get(SHACL.PROPERTY)==null) {
			TTNode propertyNode = new TTNode();
			shape.addObject(SHACL.PROPERTY, propertyNode);
			propertyNode.set(SHACL.PATH,TTIriRef.iri(property).setName(label));
			return propertyNode;
		}
		TTIriRef check= TTIriRef.iri(property);
		for (TTValue prop:shape.get(SHACL.PROPERTY).getElements()){
			if (prop.asNode().get(SHACL.PATH).equals(check))
				return prop.asNode();
		}
		TTNode propertyNode = new TTNode();
		shape.addObject(SHACL.PROPERTY, propertyNode);
		propertyNode.set(SHACL.PATH,TTIriRef.iri(property).setName(label));
		return propertyNode;
	}
}