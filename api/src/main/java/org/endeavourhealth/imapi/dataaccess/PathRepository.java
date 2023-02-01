package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.Value;
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

	public PathDocument pathQuery(QueryRequest request) {
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()){
			this.conn= conn;
			PathQuery pathQuery = request.getPathQuery();
			String targetIri = pathQuery.getTarget().getIri();

			Integer depth = pathQuery.getDepth();
			String source = pathQuery.getSource().getIri();
			List<Path> pathsFromShape= getPathsFromShape(targetIri);
			if (pathsFromShape.isEmpty())
				return null;
			else {
				List<Path> pathsToShape= getPathsToShape(source,depth,pathsFromShape);
				if (pathsToShape.isEmpty()){
					pathsToShape.addAll(pathsFromShape);
				}
				pathsToShape.sort(Comparator.comparing((Path p) -> p.getItems().size()));
				for (Path path:pathsToShape){
					Where where= new Where();
					document.addWhere(where);
					whereFromPath(path,where);
				}
				return document;
				}
			}
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
				subWhere.setId(getLocalName(link.getIri()));
				subWhere.setName(link.getName());
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
						.setAnyRoleGroup(true)));
			}
		}
	}




	private List<Path> getPathsToShape(String source, Integer depth,List<Path> pathsFromShape) {
		source="<"+source+">";
		List<Path> pathsToShape= new ArrayList<>();
		List<Path> allPaths= new ArrayList<>();
		Set<String> shapeList= new HashSet<>();
		for (Path path:pathsFromShape){
			shapeList.add("<"+path.getItems().get(0).getIri()+">");
		}
		String shapes= String.join(",",shapeList);
		StringJoiner sql= new StringJoiner("\n");
		sql.add(setDefaultPrefixes());
		StringBuilder selections=new StringBuilder().append("Select ");
		for (int i=1;i<(depth+1); i++){
			   selections.append("?path").append(i).append(" ").
				append(" ?path").append(i).append("Name ");
				 if (!(i==depth)) {
					 selections.append(" ?shape").append(i);
					 selections.append(" ?shape").append(i).append("Name");
				 }
		}
		sql.add(selections+" ?shape");
		String path="?path"+depth;
		String pathName="?path"+depth+"Name";
		String shape= "?shape";
		String shapeName="?shapeName";
		String superShape= "?super"+depth;
		String prop= "?prop"+depth;
		sql.add("where {")
			.add("	{ select "+shape+" "+shapeName+" "+path+" "+pathName+" "+prop)
			.add("	where {")
			.add("    ?shape im:isA "+superShape+".")
			.add("    ?shape rdfs:label ?shapeName.")
			.add("    filter("+superShape+"!= im:Entity)")
			.add("		?super"+depth+" ^sh:node ?prop"+depth+".")
			.add("		filter (?shape in ("+shapes+"))")
			.add("    "+prop+" sh:path "+path+".")
			.add("		filter ("+path+" not in(im:isComponentOf, im:recordOwner,im:parent))")
			.add("    "+path+" rdfs:label "+ pathName+".")
			.add(" } }")
			.add(" { "+source+" sh:property "+prop+". }");
		for (int i =depth-1;i>0; i--){
			sql.add("union {");
			path="?path"+i;
			pathName="?path"+i+"Name";
			shape= "?shape"+i;
			shapeName= shape+"Name";
			superShape= "?super"+i;
			prop= "?prop"+i;
			sql.add("{ Select "+shape+" "+shapeName+" "+ prop+" "+path+" "+pathName)
				.add("where {");
				sql.add("   " + shape + " sh:property ?prop" + (i + 1) + ".")
					.add("   "+shape+" rdfs:label "+shapeName+".")
					.add("   filter ("+shape+"!="+source+")")
				.add("   "+ shape+" im:isA +"+superShape+".")
					.add("    filter("+superShape+"!= im:Entity)")
				.add("    "+superShape+" ^sh:node "+prop+".")
				.add("    "+prop+" sh:path "+path+".")
				.add("    filter ("+path+" not in(im:isComponentOf, im:recordOwner))")
				.add("    "+path+" rdfs:label "+pathName+".")
				.add("} }")
				.add(" {"+source+" sh:property "+ prop+". }");
		}
		for (int i = 0;i<depth;i++) {
			sql.add("}");
		}
		StringBuilder groupBy= new StringBuilder("group by ");
		for (int i=1;i<(depth+1); i++){
			groupBy.append(" ?path").append(i).append(" ")
				.append(" ?path").append(i).append("Name ")
				.append(" ?shape").append(i)
				.append(" ?shape").append(i).append("Name");
		}
		sql.add(groupBy+" ?shape");
		TupleQuery qry = conn.prepareTupleQuery(sql.toString());
		try (TupleQueryResult rs = qry.evaluate()) {
			while (rs.hasNext()) {
				BindingSet bs = rs.next();
				Path pathList = new Path();
				pathsToShape.add(pathList);
				for (int i = 1; i < (depth + 1); i++) {
					Value pathIri = bs.getValue("path" + i);
					if (pathIri != null) {
						pathList.addItem(new TTTypedRef()
							.setIri(pathIri.stringValue())
							.setName(bs.getValue("path" + i + "Name").stringValue())
							.setType(RDF.PROPERTY));
						if (bs.getValue("shape"+i)!=null) {
							pathList.addItem(new TTTypedRef()
								.setIri(bs.getValue("shape" + i).stringValue())
								.setName(bs.getValue("shape" + i + "Name").stringValue())
								.setType(SHACL.NODESHAPE));
						}
					}
				}
				String toShape = bs.getValue("shape").stringValue();
				for (Path pathFrom : pathsFromShape) {
					if (pathFrom.getItems().get(0).getIri().equals(toShape)) {
						Path fullPath = new Path();
						for (TTTypedRef item : pathList.getItems()) {
							fullPath.addItem(item);
						}
						for (int i=1; i<pathFrom.getItems().size(); i++){
							fullPath.addItem(pathFrom.getItems().get(i));
						}
						allPaths.add(fullPath);
					}
				}
			}
		}
		return allPaths;
	}



	private List<Path> getPathsFromShape(String target) {
		String targetIri = "<" + target + ">";
		List<Path> pathsFromShape= new ArrayList<>();
		//First get the shapes
		StringJoiner sql = new StringJoiner("\n");
		sql.add(setDefaultPrefixes())
			.add("select ?entity ?entityName ?path ?pathName ?conceptProperty ?conceptPropertyName")
			.add("where {")
			.add("{")
			.add(targetIri + " ^im:hasMember ?set.")
			.add("?set ^sh:class ?prop.")
			.add("?prop sh:path ?path.")
			.add("?path rdfs:label ?pathName.")
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
			.add("?prop sh:path ?path.")
			.add("?path rdfs:label ?pathName.")
			.add("?entity sh:property ?prop.")
			.add("?entity rdfs:label ?entityName")
			.add("}")
			.add("union {")
			.add(targetIri + " ^sh:path ?prop.")
			.add("?entity sh:property ?prop.")
			.add("?entity rdfs:label ?entityName }")
			.add("}")
			.add("group by ?entity ?entityName ?path ?pathName ?conceptProperty ?conceptPropertyName");
		TupleQuery qry = conn.prepareTupleQuery(sql.toString());
		try (TupleQueryResult rs = qry.evaluate()) {
			while (rs.hasNext()) {
				BindingSet bs = rs.next();
				Path path = new Path();
				pathsFromShape.add(path);
				String entity = bs.getValue("entity").stringValue();
				path.addItem(new TTTypedRef().setIri(entity).setName(bs.getValue("entityName").stringValue())
					.setType(SHACL.NODESHAPE));
				if (bs.getValue("path") != null) {
					path.addItem(new TTTypedRef().setIri(bs.getValue("path").stringValue())
						.setName(bs.getValue("pathName").stringValue()));
				}
				if (bs.getValue("conceptProperty") != null) {
					path.addItem(new TTTypedRef().setIri(bs.getValue("conceptProperty").stringValue())
						.setName(bs.getValue("conceptPropertyName").stringValue())
						.setType(IM.ROLE_GROUP));
				}
			}
		}
		return pathsFromShape;
	}
	private String setDefaultPrefixes() {
		return "PREFIX xsd: <" + XSD.NAMESPACE + ">\n" +
			"PREFIX rdfs: <" + RDFS.NAMESPACE + ">\n" +
			"PREFIX rdf: <" + RDF.NAMESPACE + ">\n" +
			"PREFIX im: <" + IM.NAMESPACE + ">\n" +
			"PREFIX sn: <" + SNOMED.NAMESPACE + ">\n" +
			"PREFIX sh: <" + SHACL.NAMESPACE + ">\n";
	}


}