package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.iml.Path;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.SourceType;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTTypedRef;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;

public class PathRepository {
	private RepositoryConnection conn;
	private PathDocument document= new PathDocument();
	private TupleQuery queryToEntity;

	public PathDocument pathQuery(QueryRequest request) {
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()){
			this.conn= conn;
			PathQuery pathQuery = request.getPathQuery();
			String targetIri = pathQuery.getTarget().getIri();
			Integer depth = pathQuery.getDepth();
			String source = pathQuery.getSource().getIri();
			List<Path> pathsToShape= getAllPaths(source,targetIri,depth);
			if (pathsToShape!=null) {
				pathsToShape.sort(Comparator.comparing((Path p) -> p.getItems().size()));
				for (Path path : pathsToShape) {
					Where where = new Where();
					document.addWhere(where);
					whereFromPath(path, where);
				}
			}
		}
		return document;
	}


	private List<Path> getAllPaths(String source, String target,Integer depth) {
		queryToEntity= conn.prepareTupleQuery(getPathSql());
		List<Path> partial= new ArrayList<>();
		List<Path> full = new ArrayList<>();
		partial = getPathsFromShape(source, target,full);
		if (partial.isEmpty())
			return null;
		for (int tries = 1; tries < depth; tries++) {
			partial = nextPaths(source, target, partial, full);
		}
		return full;
	}

	private List<Path> nextPaths(String source, String target, List<Path> paths, List<Path> full) {
		List<Path> next= new ArrayList<>();
		for (Path path:paths){
					queryToEntity.setBinding("target", Values.iri(path.getSource().getIri()));
					try (TupleQueryResult rs = queryToEntity.evaluate()) {
						while (rs.hasNext()) {
							BindingSet bs = rs.next();
							Path nextPath= new Path();
							nextPath.setSource(new TTTypedRef()
								.setIri(bs.getValue("entity").stringValue())
								.setName(bs.getValue("entityName").stringValue())
									.setType(SHACL.NODESHAPE));
							nextPath.addItem(new TTTypedRef()
								.setIri(bs.getValue("path").stringValue())
								.setName(bs.getValue("pathName").stringValue())
								.setType(RDF.PROPERTY));
							nextPath.addItem(path.getSource());
							if (path.getItems()!=null)
								nextPath.getItems().addAll(path.getItems());
							nextPath.setTarget(path.getTarget());
							if (nextPath.getSource().getIri().equals(source))
								full.add(nextPath);
							else
								next.add(nextPath);
						}
					}
				}
		return next;
	}

	private String getLocalName(String iri){
		if (iri==null)
			return null;
		if (iri.contains("#"))
			return iri.substring(iri.lastIndexOf("#")+1);
		else
			return iri.substring(iri.lastIndexOf(":")+1);
	}

	private void whereFromPath(Path path, Where where) {
		for (TTTypedRef link:path.getItems()){
			if (link.getType().equals(RDF.PROPERTY)){
				where.setId(getLocalName(link.getIri()));
				where.setName(link.getName());
			}
			else if (link.getType().equals(SHACL.NODESHAPE)){
				Where subWhere= new Where();
				subWhere.setType(TTIriRef.iri(link.getIri()).setName(link.getName()));
				where.addWhere(subWhere);
				where= subWhere;
			}
			else {
				where.addIn(new From()
					.setIri(IM.CONCEPT.getIri())
					.setSourceType(SourceType.type)
					.where(w->w
						.setIri(link.getIri())
						.setName(link.getName())
						.setAnyRoleGroup(true)
						.addIn(new From()
							.setIri(path.getTarget().getIri())
							.setName(path.getTarget().getName()))));
			}
		}
		if (path.getTarget().getType().equals(RDF.PROPERTY)) {
				where.setId(getLocalName(path.getTarget().getIri()));
				where.setName(path.getTarget().getName());
		}
	}


  private String getPathSql() {

		StringJoiner sql = new StringJoiner("\n");
		sql.add(getDefaultPrefixes());
		sql.add("Select ?entity ?entityName ?path ?pathName ?target")
			.add("Where {")
			.add("?target ^sh:node ?prop.")
			.add("?prop sh:path ?path.")
			.add("?path rdfs:label ?pathName.")
			.add("?entity sh:property ?prop.")
			.add("?entity rdfs:label ?entityName }");
		return sql.toString();
	}


	private List<Path> getPathsFromShape(String source, String target,List<Path> full) {
		String targetIri = "<" + target + ">";
		List<Path> pathsFromShape = new ArrayList<>();
		//First get the shapes
		StringJoiner sql = new StringJoiner("\n");
		sql.add(getDefaultPrefixes())
			.add("select ?entity ?entityName ?property ?propertyName ?conceptProperty ?conceptPropertyName ?targetType ?targetName ?order")
			.add("where {")
			.add( targetIri+" rdf:type ?targetType.")
			.add(targetIri+" rdfs:label ?targetName")
			.add("{")
			.add(" ?concept ^im:hasMember ?set.")
			.add("filter (?concept= " + targetIri + ").")
			.add("?concept rdfs:label ?conceptName.")
			.add("?set ^sh:class ?prop.")
			.add("?prop sh:path ?property.")
			.add("?property rdfs:label ?propertyName.")
			.add("?entity sh:property ?prop.")
			.add("?entity rdfs:label ?entityName")
			.add("}")
			.add("union")
			.add("{")
			.add("?roleGroup ?conceptProperty ?target.")
			.add("?conceptProperty rdfs:label ?conceptPropertyName.")
			.add("?target im:isA ?superTarget.")
			.add("filter (?superTarget=" + targetIri + ")")
			.add("?concept im:roleGroup ?roleGroup.")
			.add("?set im:hasMember ?concept.")
			.add("?set ^sh:class ?prop.")
			.add("?prop sh:path ?property.")
			.add("?property rdfs:label ?propertyName.")
			.add("?entity sh:property ?prop.")
			.add("?entity rdfs:label ?entityName")
			.add("}")
			.add("union {")
			.add(targetIri+ " ^sh:path ?prop.")
			.add("optional {?prop sh:order ?order}")
			.add("?entity sh:property ?prop.")
			.add("?entity rdfs:label ?entityName }")
			.add("}")
			.add("group by ?entity ?entityName ?property ?propertyName ?conceptProperty ?order"+
					" ?conceptPropertyName ?targetType ?targetName")
			.add("order by ?order");
		TupleQuery qry = conn.prepareTupleQuery(sql.toString());
		try (TupleQueryResult rs = qry.evaluate()) {
			while (rs.hasNext()) {
				BindingSet bs = rs.next();
				Path path = new Path();
				path.setTarget(new TTTypedRef()
					.setIri(target)
					.setName(bs.getValue("targetName").stringValue())
					.setType(TTIriRef.iri(bs.getValue("targetType").stringValue())));
				path.setSource(new TTTypedRef()
					.setIri(bs.getValue("entity").stringValue())
				  .setName(bs.getValue("entityName").stringValue())
					.setType(SHACL.NODESHAPE));
				if (bs.getValue("property")!=null) {
					String propertyIri = bs.getValue("property").stringValue();
					path.addItem(new TTTypedRef()
						.setIri(propertyIri)
						.setName(bs.getValue("propertyName").stringValue())
						.setType(RDF.PROPERTY));
				}
				if (bs.getValue("conceptProperty") != null) {
					path.addItem(new TTTypedRef().setIri(bs.getValue("conceptProperty").stringValue())
						.setName(bs.getValue("conceptPropertyName").stringValue())
						.setType(IM.ROLE_GROUP));
				}
				if (path.getSource().getIri().equals(source)){
					full.add(path);
				}
				else
					pathsFromShape.add(path);
			}
		}
		return pathsFromShape;
	}


	private String getDefaultPrefixes() {
		return "PREFIX xsd: <" + XSD.NAMESPACE + ">\n" +
			"PREFIX rdfs: <" + RDFS.NAMESPACE + ">\n" +
			"PREFIX rdf: <" + RDF.NAMESPACE + ">\n" +
			"PREFIX im: <" + IM.NAMESPACE + ">\n" +
			"PREFIX sn: <" + SNOMED.NAMESPACE + ">\n" +
			"PREFIX sh: <" + SHACL.NAMESPACE + ">\n";
	}


}