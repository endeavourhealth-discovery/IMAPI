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
        List<Match> result= new ArrayList<>();
        StringJoiner sql = new StringJoiner("\n");
        //The logic is to look for a target as a record types, properties, value sets or concepts linked to the source.
        sql.add(getDefaultPrefixes())
            .add("select ?where ?whereLabel ?recordType ?recordTypeLabel ?path ?pathLabel ?where1 ?where1Label ")
            .add("where {")
            .add("{ ?target sh:property ?property.")
            .add("  ?property sh:path ?path.")
            .add("  ?path rdfs:label ?pathLabel.")
            .add(" ?property sh:node ?source.")
            .add(" ?property ^sh:property ?recordType.")
            .add(" ?recordType rdfs:label ?recordTypeLabel.}")
            .add("union {")
            .add(" ?target ^sh:path ?property.")
            .add("  ?property sh:path ?where.")
            .add("  ?where rdfs:label ?whereLabel.")
            .add(" ?property ^sh:property ?source.}")
            .add(" union {")
            .add(" ?target ^sh:path ?subProperty.")
            .add("  ?subProperty sh:path ?where.")
            .add("  ?where rdfs:label ?whereLabel.")
            .add(" ?subProperty ^sh:property ?recordType.")
            .add("  ?recordType rdfs:label ?recordTypeLabel.")
            .add("  ?recordType sh:property ?recordProperty.")
            .add("  ?recordProperty sh:path ?path.")
            .add("  ?path rdfs:label ?pathLabel.")
            .add("  ?recordProperty sh:node ?source.}")
            .add(" union {")
            .add(" ?target ^sh:path ?subProperty.")
            .add("  ?subProperty sh:path ?where.")
            .add("  ?where rdfs:label ?whereLabel.")
            .add(" ?subProperty ^sh:property ?recordType.")
            .add("  ?recordType rdfs:label ?recordTypeLabel.")
            .add("  ?recordType ^sh:node ?recordProperty.")
            .add("  ?recordProperty sh:path ?where1.")
            .add("  ?where1 rdfs:label ?where1Label.")
            .add("  ?recordProperty ^sh:property ?source.}")
            .add("union {")
            .add("  ?target ^im:hasMember ?valueSet.")
            .add("  ?valueSet ^sh:class ?property.")
            .add("  ?property sh:path ?where.")
            .add("  ?where rdfs:label ?whereLabel.")
            .add("  ?property ^sh:property ?source.}")
            .add("union {")
            .add("  ?target ^im:hasMember ?valueSet.")
            .add("  ?valueSet ^sh:class ?property.")
            .add("  ?property sh:path ?where.")
            .add("  ?where rdfs:label ?whereLabel.")
            .add("  ?property ^sh:property ?recordType.")
            .add("  ?recordType rdfs:label ?recordTypeLabel.")
            .add("  ?recordType sh:property ?recordProperty.")
            .add("  ?recordProperty sh:path ?path.")
            .add("  ?path rdfs:label ?pathLabel.")
            .add("  ?recordProperty sh:node ?source.}")
            .add("union {")
            .add("  ?target ^sh:class ?property.")
            .add("  ?property sh:path ?where.")
            .add("  ?where rdfs:label ?whereLabel.")
            .add("  ?property ^sh:property ?recordType.")
            .add("  ?recordType rdfs:label ?recordTypeLabel.")
            .add("  ?recordType sh:property ?recordProperty.")
            .add("  ?recordProperty sh:path ?path.")
            .add("  ?path rdfs:label ?pathLabel.")
            .add("  ?recordProperty sh:node ?source.}")
            .add("union {")
            .add("  ?target im:binding ?datamodel.")
            .add("  ?datamodel sh:path ?where.")
            .add("  ?where rdfs:label ?whereLabel.")
            .add("  ?datamodel sh:node ?recordType.")
            .add("  ?recordType rdfs:label ?recordTypeLabel.")
            .add("  ?recordType sh:property ?recordProperty.")
            .add("  ?recordProperty sh:path ?path.")
            .add("  ?path rdfs:label ?pathLabel.")
            .add("  ?recordProperty sh:node ?source.}")
            .add("}")
            .add("group by ?where ?whereLabel  ?recordType ?recordTypeLabel ?path ?pathLabel ?where1 ?where1Label ");
        String sparql=sql.toString().replace("?target","<"+ target+">");
        sparql= sparql.replace("?source","<"+ source+">");
        TupleQuery qry = conn.prepareTupleQuery(sparql);
        try (TupleQueryResult rs = qry.evaluate()) {
            while (rs.hasNext()) {
                BindingSet bs = rs.next();
                Match match= new Match();
                if (bs.getValue("where1")!=null) {
                    Match superMatch= new Match();
                    result.add(superMatch);
                    superMatch.addWhere (new Where().setIri(bs.getValue("where1").stringValue())
                        .setName(bs.getValue("where1Label").stringValue())
                        .setMatch(match));
                }
                else
                    result.add(match);
                if (bs.getValue("recordType")!=null){
                    match.setTypeOf(new Node().setIri(bs.getValue("recordType").stringValue())
                        .setName(bs.getValue("recordTypeLabel").stringValue()));
                }
                if (bs.getValue("where")!=null){
                    match.addWhere(new Where()
                        .setIri(bs.getValue("where").stringValue())
                        .setName(bs.getValue("whereLabel").stringValue()));
                }
                if (bs.getValue("path")!=null){
                    match.addPath(new IriLD().setIri(bs.getValue("path").stringValue())
                        .setName(bs.getValue("pathLabel").stringValue()));
                }

            }
        }
        sortWherePaths(result);
        return result;
    }

    private void sortWherePaths(List<Match> result) {
        Set<Match> remove= new HashSet<>();
        for (int i =0; i<result.size(); i++){
            Match pathMatch= result.get(i);
            if (pathMatch.getPath()!=null){
                for (int q= i+1; q<result.size();q++){
                    Match hasWhere= result.get(q);
                    if (hasWhere.getWhere()!=null)
                        if (hasWhere.getWhere().get(0).getMatch()!=null)
                            if (hasWhere.getWhere().get(0).getMatch().getTypeOf().equals(pathMatch.getTypeOf()))
                                remove.add(pathMatch);
                }
            }
        }
        if (!remove.isEmpty())
            for (Match match:remove)
                result.remove(match);
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