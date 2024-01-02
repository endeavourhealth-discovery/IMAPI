package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;

public class PathRepository {
	private RepositoryConnection conn;
	private final PathDocument document= new PathDocument();
	private TupleQuery queryToShape;

	public PathDocument pathQuery(QueryRequest request) {
		TTContext context = request.getAsContext();
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()){
			this.conn= conn;
			PathQuery pathQuery = request.getPathQuery();
			String targetIri = context.expand(pathQuery.getTarget().getIri());
			Integer depth = pathQuery.getDepth();
			String source = context.expand(pathQuery.getSource().getIri());
			List<Match> paths= getAllPaths(source,targetIri,depth);
			if (paths!=null) {
				paths.sort(Comparator.comparing(this::getLength));
				document.setMatch(paths);
				}
		}
		return document;
	}

	private int getLength(Match match){
		int count=0;
		if (match==null)
			return 0;
		if (match.getProperty()==null)
			return 0;
		else {
			count++;
			for (Property path:match.getProperty())
				count=count+getLength(path.getMatch());
		}
		return count;
	}


	private List<Match> getAllPaths(String source, String target,Integer depth) {
        switch(source) {
            case USER.USER_THEME: break;
            case SNOMED.ATTRIBUTE: break;
        }

		queryToShape = conn.prepareTupleQuery(getPathSql());
		List<Match> partial;
		List<Match> full = new ArrayList<>();
		partial = getPathsFromShape(source, target,full);
		if (partial.isEmpty())
			return null;
		for (int tries = 1; tries < depth; tries++) {
			partial = nextPaths(source, partial, full);
		}
		return full;
	}

	private List<Match> nextPaths(String source, List<Match> partials, List<Match> full) {
		List<Match> next= new ArrayList<>();
		for (Match partial:partials){
					queryToShape.setBinding("target", Values.iri(partial.getTypeOf().getIri()));
					try (TupleQueryResult rs = queryToShape.evaluate()) {
						while (rs.hasNext()) {
							BindingSet bs = rs.next();
							Match nextPath= new Match();
							next.add(nextPath);
							nextPath
								.setTypeOf(bs.getValue("entity").stringValue())
								.setName(bs.getValue("entityName").stringValue());
							Property path= new Property();
							nextPath.addProperty(path);
							path
								.setIri(bs.getValue("path").stringValue())
								.setName(bs.getValue("pathName").stringValue());
							Match node= new Match();
							path.setMatch(node);
							node.setTypeOf(partial.getTypeOf());
							node.setProperty(partial.getProperty());
							if (nextPath.getTypeOf().getIri().equals(source))
								full.add(nextPath);
							else
								next.add(nextPath);
						}
					}
				}
		return next;
	}


  private String getPathSql() {

		StringJoiner sql = new StringJoiner("\n");
		sql.add(getDefaultPrefixes());
		sql.add("Select ?entity ?entityName ?path ?pathName ?target")
			.add("Where{")
			.add("?target ^sh:node ?prop.")
			.add("?prop sh:path ?path.")
			.add("?path rdfs:label ?pathName.")
			.add("?entity sh:property ?prop.")
			.add("?entity rdfs:label ?entityName }");
		return sql.toString();
	}


	private List<Match> getPathsFromShape(String source, String target,List<Match> full) {
		String targetIri = "<" + target + ">";
		List<Match> pathsFromShape = new ArrayList<>();
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
				Match match= new Match();
				match
					.setTypeOf(bs.getValue("entity").stringValue())
				  .setName(bs.getValue("entityName").stringValue());
				Property where= new Property();
				if (bs.getValue("property")!=null) {
					String propertyIri = bs.getValue("property").stringValue();
					if (bs.getValue("conceptProperty")==null) {
						match.addProperty(where);
						where
							.setIri(propertyIri)
							.setName(bs.getValue("propertyName").stringValue())
							.addIs(new Node()
								.setIri(targetIri)
								.setName(bs.getValue("targetName").stringValue()));
					}
					else {
						Property path= new Property();
						match.addProperty(path);
						path
							.setIri(propertyIri)
							.setName(bs.getValue("propertyName").stringValue())
							.match(mConcept->mConcept
								.addProperty(where));
						where
						.setIri(bs.getValue("conceptProperty").stringValue())
						.setName(bs.getValue("conceptPropertyName").stringValue())
						.setAnyRoleGroup(true)
						.addIs(new Node()
						.setIri(targetIri)
						.setName(bs.getValue("targetName").stringValue()));
					}
				}
				if (match.getTypeOf().getIri().equals(source)){
					full.add(match);
				}
				else
					pathsFromShape.add(match);
			}
		}
		return pathsFromShape;
	}


	private String getDefaultPrefixes() {
		return "PREFIX xsd: <" + XSD.NAMESPACE.iri + ">\n" +
			"PREFIX rdfs: <" + RDFS.NAMESPACE.iri + ">\n" +
			"PREFIX rdf: <" + RDF.NAMESPACE.iri + ">\n" +
			"PREFIX im: <" + IM.NAMESPACE.iri + ">\n" +
			"PREFIX " + SNOMED.PREFIX + ": <" + SNOMED.NAMESPACE + ">\n" +
			"PREFIX sh: <" + SHACL.NAMESPACE.iri + ">\n";
	}


}
