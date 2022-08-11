package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.sets.PathTarget;
import org.endeavourhealth.imapi.model.sets.QueryRequest;
import org.endeavourhealth.imapi.model.shapes.NodeShape;
import org.endeavourhealth.imapi.model.shapes.PropertyShape;
import org.endeavourhealth.imapi.model.shapes.ShapeDocument;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;
import java.util.zip.DataFormatException;

public class PathRepository {
	public static String buildPathSQL (QueryRequest request) throws DataFormatException {
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
			PathTarget pathTarget= request.getQuery().getSelect().getPathToTarget();
			String target= pathTarget.getIri();
			if (target==null)
				target= QueryRepository.resolveReference(pathTarget.getAlias(),request);
			String source= request.getQuery().getSelect().getEntityId().getIri();
			if (source==null)
				source= QueryRepository.resolveReference(request.getQuery().getSelect().getEntityId().getAlias(),request);
			Integer depth= pathTarget.getDepth();
			if (depth==null) {
				depth = Integer.parseInt(QueryRepository.resolveReference(pathTarget.getDepthAlias(), request));
				pathTarget.setDepth(depth);
			}
			String sql = "select ?type ?superType ?where {<" + target + "> <" + RDF.TYPE.getIri() + "> ?type.\n" +
				"optional {<"+target+"> <" + IM.IS_A.getIri() + "> ?superType.\n" +
				"filter (?superType=<" + IM.NAMESPACE + "dataModelProperty>)}}";
			TupleQuery qry = conn.prepareTupleQuery(sql);
			TupleQueryResult rs = qry.evaluate();
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
		sql.append(
			"where {\n" +
				"    {  # First get to a value set\n" +
				"        Select distinct ?set \n" +
				"        where {\n" +
				"           ?roleGroup <"+target+"> ?value.\n" +
				"           ?testConcept im:roleGroup ?roleGroup.\n" +
				"           ?set im:hasMember ?testConcept.\n" +
				"          }}\n" +
				"    #Then get the paths from Main entities to an entity, include  the sub entities\n" +
				"    ?entity im:isA <"+source+">.\n"+
				"    ?entity sh:property ?prop1.\n" +
				"    ?entity rdfs:label ?entityLabel.\n"+
				"    #Retrieve the path from main entity to entity\n" +
				"    ?prop1 sh:path ?path1.\n" +
				"     ?path1 rdfs:label ?path1Label."+
				"    {\n" +
				"        ?prop1 sh:class ?set.\n" +
				"    }\n");

		for (int i=1; i<(depth); i++){
			sql.append(
				"    union \n" +
					"    {\n" +
					"        ?prop"+i+" sh:node ?entity"+ i+ ".\n" +
					"        ?subEntity"+i+" im:isA ?entity"+i+".\n" +
					"        filter (?subEntity"+i+" not in("+removeRepeat(i)+"))\n" +
					"        ?subEntity"+i+" sh:property ?prop"+(i+1)+".\n" +
					"        ?subEntity"+i+" rdfs:label ?subEntity"+i+"Label.\n"+
					"        ?prop"+(i+1)+" sh:path ?path"+(i+1)+".\n" +
					"        ?path"+(i+1)+" rdfs:label ?path"+(i+1)+"Label.\n"+
					"        {\n" +
					"            ?prop"+ (i+1)+" sh:class ?set.\n" +
					"        }\n");
		}
		sql.append("}\n".repeat(Math.max(0, depth)));
		sql.append(
			"group by ?entity ?entityLabel ?path1 ?path1Label");
		for (int i=1; i<depth; i++){
			sql.append(" ?subEntity").append(i).append(" ?subEntity").append(i).append("Label").append(" ?path").append(i + 1)
				.append(" ?path").append(i+1).append("Label");
		}
		sql.append("\n");
		sql.append("order by ?entity ?path1");
		for (int i=1; i<depth; i++){
			sql.append(" ?subEntity").append(i).append(" ?path").append(i + 1);
		}
		return sql.toString();
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
		sql.append(
			"where {\n" +
				"    {  # First get to a value set\n" +
				"        Select distinct ?set ?conceptProperty ?conceptPropertyLabel\n" +
				"        where {\n" +
				"            ?conceptProperty rdfs:label ?conceptPropertyLabel.\n" +
				"            filter not exists {\n" +
				"                ?conceptProperty im:isA ?super.\n" +
				"                filter (?super!= ?conceptProperty)\n" +
				"                ?set im:hasMember ?testConcept.\n" +
				"                ?testConcept im:roleGroup ?rg2.\n" +
				"                ?rg2 ?super <"+target+">.\n" +
				"            }\n" +
				"            { #Either the concept is a value in a role group of a concept that is in a value set\n" +
				"              #or a concept in the value set expansion  \n" +
				"                Select distinct ?set ?concept ?conceptProperty\n" +
				"                where {\n" +
				"                    {\n" +
				"                        ?roleGroup ?superProperty <"+target+">.\n" +
				"                        ?conceptProperty im:isA ?superProperty.\n" +
				"                        ?roleGroup ^im:roleGroup ?concept.\n" +
				"                        ?concept ^im:hasMember ?set.\n" +
				"                        ?set ^sh:class ?property.\n" +
				"                    }\n" +
				"                    union \n" +
				"                    {\n" +
				"                        <"+ target+"> ^im:hasMember ?set.\n" +
				"                    }\n" +
				"                }\n" +
				"            }\n" +
				"        }\n" +
				"    }\n" +
				"    #Then get the paths from Main entities to an entity, include  the sub entities\n" +
				"    ?entity im:isA <"+source+">.\n"+
				"    ?entity sh:property ?prop1.\n" +
				"    ?entity rdfs:label ?entityLabel.\n"+
				"    #Retrieve the path from main entity to entity\n" +
				"    ?prop1 sh:path ?path1.\n" +
				"     ?path1 rdfs:label ?path1Label."+
				"    {\n" +
				"        ?prop1 sh:class ?set.\n" +
				"    }\n");

		for (int i=1; i<(depth); i++){
			sql.append(
				"    union \n" +
					"    {\n" +
					"        ?prop"+i+" sh:node ?entity"+ i+ ".\n" +
					"        ?subEntity"+i+" im:isA ?entity"+i+".\n" +
					"        filter (?subEntity"+i+" not in("+removeRepeat(i)+"))\n" +
					"        ?subEntity"+i+" sh:property ?prop"+(i+1)+".\n" +
					"        ?subEntity"+i+" rdfs:label ?subEntity"+i+"Label.\n"+
					"        ?prop"+(i+1)+" sh:path ?path"+(i+1)+".\n" +
					"        ?path"+(i+1)+" rdfs:label ?path"+(i+1)+"Label.\n"+
					"        {\n" +
					"            ?prop"+ (i+1)+" sh:class ?set.\n" +
					"        }\n");
		}
		sql.append("}\n".repeat(Math.max(0, depth)));
		sql.append(
			"group by ?entity ?entityLabel ?path1 ?path1Label");
		for (int i=1; i<depth; i++){
			sql.append(" ?subEntity").append(i).append(" ?subEntity").append(i).append("Label").append(" ?path").append(i + 1)
				.append(" ?path").append(i+1).append("Label");
		}
		sql.append(" ?conceptProperty ?conceptPropertyLabel\n");
		sql.append("order by ?entity ?path1");
		for (int i=1; i<depth; i++){
			sql.append(" ?subEntity").append(i).append(" ?path").append(i + 1);
		}
		return sql.toString();
	}

	private static String entityToEntityPathSql(Integer depth, String source, String target) {
		StringBuilder sql= new StringBuilder(setDefaultPrefixes());
		sql.append("Select ?entity ?entityLabel");
		for (int i=1; i<(depth+1); i++){
			sql.append(" ?path").append(i).append(" ?path").append(i).append("Label")
				.append(" ?subEntity").append(i).append(" ?subEntity").append(i).append("Label");
		}
		sql.append("where {\n" +
			"   \n" +
			"    #Then get the paths from Main entity to a  nodeShape, include  the sub entities\n" +
			"    ?entity im:isA <"+ source+">.\n" +
			"    ?entity sh:property ?prop1.\n" +
			"    ?entity rdfs:label ?entityLabel.\n" +
			"    #Retrieve the path from main entity to entity\n");
		for (int i=1; i<(depth+1); i++) {
			sql.append(
				"    ?prop"+i+" sh:path ?path"+i+".\n"+
					"    ?path" + i + " rdfs:label ?path" + i + "Label.\n" +
					"   {?prop"+i+" sh:node ?target.\n"+
					"    <"+target+"> im:isA ?target.}\n");
			if (i<depth)
				sql.append(
					"    union {\n" +
					"        ?prop" + i + " sh:node ?entity" + i + ".\n" +
					"        ?subEntity" + i + " im:isA ?entity" + i + ".\n" +
					"        filter (?subEntity"+i+" not in("+removeRepeat(i)+"))\n" +
					"        ?subEntity" + i + " sh:property ?prop" + (i + 1) + ".\n" +
					"        ?subEntity" + i + " rdfs:label ?subEntity" + i + "Label.\n"+
					"        ?subEntity" + i + " sh:property ?prop"+ (i+1)+".\n");
		}

		sql.append("}\n".repeat(Math.max(0, depth)));
		return sql.toString();
	}

	private static String entityToDMPropertyPathSql(Integer depth, String source, String target) {
		StringBuilder sql= new StringBuilder(setDefaultPrefixes());
		sql.append("Select ?entity ?entityLabel");
		for (int i=1; i<(depth+1); i++){
			sql.append(" ?path").append(i).append(" ?path").append(i).append("Label")
				.append(" ?subEntity").append(i).append(" ?subEntity").append(i).append("Label");
		}
		sql.append("where {\n" +
			"   \n" +
			"    #Then get the paths from entity to a data model property, include  the sub entities\n" +
			"    ?entity im:isA <"+ source+">.\n" +
			"    ?entity sh:property ?prop1.\n" +
			"    ?entity rdfs:label ?entityLabel.\n" +
			"    #Retrieve the path from main entity to entity\n");
		for (int i=1; i<(depth+1); i++) {
			sql.append(
				"   { ?prop"+i+" sh:path <"+target+">.}\n"+
					"    union {\n" +
					"        ?prop" + i + " sh:path ?path" + i + ".\n" +
					"        ?path"+ i + "  rdfs:label ?path"+i+"Label.\n"+
					"        ?prop" + i + " sh:node ?entity" + i + ".\n" +
					"        ?subEntity" + i + " im:isA ?entity" + i + ".\n" +
					"        filter (?subEntity"+i+" not in("+removeRepeat(i)+"))\n" +
					"        ?subEntity" + i + " sh:property ?prop" + (i + 1) + ".\n" +
					"        ?subEntity" + i + " rdfs:label ?subEntity" + i + "Label.\n"+
					"        ?subEntity" + i + " sh:property ?prop"+ (i+1)+".\n");
		}
		sql.append("   ?prop"+depth+" sh:path <"+target+">.");
		sql.append("}\n".repeat(Math.max(0, depth+1)));
		return sql.toString();
	}

	private static String removeRepeat(Integer depth){
		String repeat= "?entity";
		for (int i=1; i<(depth); i++){
			repeat= repeat+",?subEntity"+i;
		}
		return repeat;
	}

	private static String setDefaultPrefixes() {
		return "PREFIX xsd: <" + XSD.NAMESPACE + ">\n" +
			"PREFIX rdfs: <" + RDFS.NAMESPACE + ">\n" +
			"PREFIX rdf: <" + RDF.NAMESPACE + ">\n" +
			"PREFIX im: <" + IM.NAMESPACE + ">\n" +
			"PREFIX sn: <" + SNOMED.NAMESPACE + ">\n" +
			"PREFIX sh: <" + SHACL.NAMESPACE + ">\n";
	}

	private static ObjectNode addNode(ObjectNode node, String predicate,ObjectMapper om){
		if (node.get(predicate)==null) {
			node.set(predicate, om.createArrayNode());
		}
		ObjectNode newNode= om.createObjectNode();
		((ArrayNode) node.get(predicate)).add(newNode);
		return newNode;
	}

	private static ObjectNode getNode(ObjectNode node, String predicate,String iri, ObjectMapper om){
		ObjectNode result;
		if (node.get(predicate)==null) {
			result= addNode(node,predicate,om);
			result.put("@id",iri);
			return result;
		}
		for (Iterator<JsonNode> it = (node.get(predicate).elements()); it.hasNext(); ) {
			JsonNode on = it.next();
			if (on.get("@id").asText().equals(iri))
				return (ObjectNode) on;
		}
		result = addNode(node,predicate,om);
		result.put("@id",iri);
		return result;
	}

	public static ObjectNode bindResults(TupleQueryResult rs,QueryRequest request) {

		ObjectMapper om = new ObjectMapper();
		Integer depth = request.getQuery().getSelect().getPathToTarget().getDepth();
		Map<String, ObjectNode> sourceEntities = new HashMap<>();
		ObjectNode result = om.createObjectNode();
		if (!rs.hasNext())
			return result;
		while (rs.hasNext()) {
			BindingSet bs = rs.next();
			String entity = bs.getValue("entity").stringValue();
			ObjectNode shape = sourceEntities.get(entity);
			if (shape == null) {
				shape = addNode(result, "entity", om);
				shape.put("@id", entity);
				shape.put(RDFS.LABEL.getIri(), (bs.getValue("entityLabel").stringValue()));
				sourceEntities.put(entity, shape);
			}

			for (int i = 1; i < (depth+1); i++) {
				if (bs.getValue("path" + i) != null) {
					String property = bs.getValue("path" + i).stringValue();
					ObjectNode propertyNode = getNode(shape, "path", property, om);
					propertyNode.put(RDFS.LABEL.getIri(), (bs.getValue("path" + i + "Label").stringValue()));
					String subEntity = bs.getValue("subEntity" + i) != null
						? bs.getValue("subEntity" + i).stringValue()
						: null;

					if (subEntity != null) {
						ObjectNode subEntityNode = getNode(propertyNode,"entity",subEntity,om);
						subEntityNode.put(RDFS.LABEL.getIri(), bs.getValue("subEntity" + i + "Label").stringValue());
						shape = subEntityNode;
					}
					else if (bs.getValue("conceptProperty") != null) {
						String conceptProperty = bs.getValue("conceptProperty").stringValue();
						ObjectNode conceptPropertyNode = addNode(propertyNode, "path", om);
						conceptPropertyNode.put("@id", conceptProperty);
						conceptPropertyNode.put(RDFS.LABEL.getIri(), bs.getValue("conceptPropertyLabel").stringValue());
					}
				}
			}
		}
		return result;
	}


}

