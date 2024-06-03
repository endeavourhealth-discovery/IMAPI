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

	public PathDocument pathQuery(PathQuery pathQuery) {
		try (RepositoryConnection conn = ConnectionManager.getIMConnection()){
			this.conn= conn;
			String targetIri = pathQuery.getTarget().getIri();
			Integer depth = pathQuery.getDepth();
			String source = pathQuery.getSource().getIri();
			List<Match> paths= getPaths(source,targetIri,depth);
			if (paths!=null) {
				document.setMatch(paths);
				}
		}
		return document;
	}


	private List<Match> getPaths(String source, String target, Integer depth) {
		StringJoiner sql = new StringJoiner("\n");
		sql.add(getDefaultPrefixes())
			.add("select ?path ?path1 ?path2 ?recordType")
			.add("where {")
			.add("{")
			.add(" ?target ^sh:path ?property.")
			.add("  ?property sh:path ?path.")
			.add(" ?property ^sh:property ?source.}")
			.add("union {")
			.add("  ?target ^im:hasMember ?valueSet.")
			.add("  ?valueSet ^sh:class ?property.")
			.add("  ?property sh:path ?path1.")
			.add("  ?property ^sh:property ?source.}")
			.add("union {")
			.add("  ?target ^im:hasMember ?valueSet.")
			.add("  ?valueSet ^sh:class ?property.")
			.add("  ?property sh:path ?path2.")
			.add("  ?property ^sh:property ?recordType.")
			.add("  ?recordType sh:property ?recordProperty.")
			.add("  ?recordProperty sh:node ?source.}")
			.add("union {")
			.add("  ?target ^im:hasMember ?conceptSet.")
			.add("  ?conceptSet im:binding ?datamodel.")
			.add("  ?datamodel  sh:node ?source.}")
			.add("union {")
			.add("  ?target ^im:hasMember ?conceptSet.")
			.add("  ?conceptSet im:binding ?datamodel.")
			.add("  ?datamodel sh:node ?recordType.")
			.add("  ?recordType sh:property ?recordProperty.")
			.add("  ?recordProperty sh:node ?source.}")
			.add("}");
		String sparql=sql.toString().replace("?target","<"+ target+">");
		sparql= sparql.replace("?source","<"+ source+">");
		TupleQuery qry = conn.prepareTupleQuery(sparql);
		try (TupleQueryResult rs = qry.evaluate()) {
			while (rs.hasNext()) {
				BindingSet bs = rs.next();
			}
		}
		return null;
	}
	private String getDefaultPrefixes() {
		return "PREFIX xsd: <" + XSD.NAMESPACE + ">\n" +
			"PREFIX rdfs: <" + RDFS.NAMESPACE + ">\n" +
			"PREFIX rdf: <" + RDF.NAMESPACE + ">\n" +
			"PREFIX im: <" + IM.NAMESPACE + ">\n" +
			"PREFIX " + SNOMED.PREFIX + ": <" + SNOMED.NAMESPACE + ">\n" +
			"PREFIX sh: <" + SHACL.NAMESPACE + ">\n";
	}


}
