package org.endeavourhealth.imapi.filer.rdf4j;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.endeavourhealth.imapi.filer.TCGenerator;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClosureGeneratorRdf4j implements TCGenerator {
	private static final Logger LOG = LoggerFactory.getLogger(ClosureGeneratorRdf4j.class);

	private Repository repo;
	private RepositoryConnection conn;
	private HashMap<String, Set<String>> parentMap;
	private Map<String,Set<String>> replacementMap;
	private HashMap<String, Set<String>> closureMap;
	private static String[] topConcepts={"http://snomed.info/sct#138875005",IM.NAMESPACE+"Concept"};
	private int counter;
	private Set<String> blockingIris = new HashSet<>();


	public ClosureGeneratorRdf4j() throws TTFilerException {
		LOG.info("Connecting");
		repo = new HTTPRepository("http://localhost:7200/", "im");

		try {
			repo = new HTTPRepository("http://localhost:7200/", "im");
			repo.initialize();
			conn = repo.getConnection();
			LOG.info("Connected");
		} catch (RepositoryException e) {
			LOG.info("Failed");
			throw new TTFilerException("Failed to open repository connection", e);
		}

	}

	@Override
	public void generateClosure(String outpath, boolean secure) throws IOException {
		getTctBlockers();
		LOG.info("Clearing out all isAs.. This will take some time....");
		clearIsAs();

		List<TTIriRef> relationships = Arrays.asList(
			RDFS.SUBCLASSOF,
			RDFS.SUBPROPERTYOF,
			SNOMED.REPLACED_BY);

		parentMap = new HashMap<>(1000000);
		replacementMap= new HashMap<>();

		for (TTIriRef rel : relationships) {

					loadRelationships(conn, rel);
			}

		String outFile = outpath + "/closure.ttl";
		try(FileWriter fw = new FileWriter(outFile)) {
		buildClosure();
		buildReverseClosure();
		writeClosureData(fw);
		importClosure(outpath, secure);
	}

	}

	private void getTctBlockers() {
		LOG.debug(String.format("Getting %s ", "top level stoppers"));
		blockingIris.addAll(Arrays.asList(topConcepts));
		//For now, only exclude top snomed and im concept
		/*
		StringBuilder blockers= new StringBuilder();
		for (String blocker:topConcepts){
			blockers.append(",<").append(blocker).append(">");
		}
		blockers = new StringBuilder(blockers.substring(1));
		TupleQuery stmt;
		stmt = conn.prepareTupleQuery(getDefaultPrefixes() + "\nSelect ?child \n" +
			"where {?child <" + RDFS.SUBCLASSOF.getIri()+ "> ?parent." +
			"filter (?parent in ("+blockers+"))}\n");

		try (TupleQueryResult rs = stmt.evaluate()) {
			while (rs.hasNext()) {
				BindingSet bs = rs.next();
				String child= bs.getValue("child").stringValue();
				blockingIris.add(child);
			}

		}

		 */
	}

	private void buildReverseClosure() {
		if (!replacementMap.isEmpty()) {
			for (Map.Entry<String, Set<String>> entry : replacementMap.entrySet()) {
				String replacement = entry.getKey();
				Set<String> replacementAncestors= closureMap.get(replacement);
				replacementAncestors.addAll(entry.getValue());
			}
		}
	}

 private void clearIsAs(){
	 String sql="delete {?c <"+IM.NAMESPACE+"isA> "+"?p }"+
		 "where { ?c <"+IM.NAMESPACE+"isA> "+"?p}";

	 Update upd = conn.prepareUpdate(sql);
	 upd.execute();
 }
	private void loadRelationships(RepositoryConnection conn, TTIriRef relationship) {
			LOG.info("Extracting " + relationship.getIri());
		TupleQuery stmt;
		stmt = conn.prepareTupleQuery(getDefaultPrefixes() + "\nSubSelect ?child ?parent\n" +
				"where {?child <" + relationship.getIri() + "> ?parent }\n");

		try (TupleQueryResult rs = stmt.evaluate()) {
			while (rs.hasNext()) {
				BindingSet bs = rs.next();
				String child = bs.getValue("child").stringValue();
				String parent = bs.getValue("parent").stringValue();
				if (!blockingIris.contains(parent)) {
						Set<String> parents = parentMap.computeIfAbsent(child, k -> new HashSet<>());
						parents.add(parent);
						if (relationship.equals(SNOMED.REPLACED_BY)) {
							Set<String> replacements = replacementMap.computeIfAbsent(parent, k -> new HashSet<>());
							replacements.add(child);
						}
					}
			}
		}
		LOG.debug(String.format("Relationships loaded for %s %d entities", relationship.getIri(), parentMap.size()));
	}

	private void buildClosure() {
		closureMap = new HashMap<>(10000000);
		LOG.debug("Generating closure map");
		int c = 0;
		counter=0;
		for (Map.Entry<String, Set<String>> row : parentMap.entrySet()) {
			c++;
			String child = row.getKey();
			if (closureMap.get(child)==null) {
				if (c % 100000 == 0)
					LOG.debug(String.format("Processed %d entities", c));
				generateClosure(child);
			}
		}
		LOG.debug(String.format("Closure built with  %d triples with  %d keys", counter, closureMap.size()));
	}

	private String getDefaultPrefixes() {
		TTManager manager = new TTManager();
		StringJoiner prefixes = new StringJoiner("\n");
		TTContext context = manager.createDefaultContext();
		for (TTPrefix pref : context.getPrefixes()) {
			prefixes.add("PREFIX " + pref.getPrefix() + ": <" + pref.getIri() + ">");
		}
		return prefixes.toString();
	}

	private Set<String> generateClosure(String child) {
		Set<String> closures = closureMap.computeIfAbsent(child, k -> new HashSet<>());

		// Add self
		closures.add(child);
		counter++;

		Set<String> parents = parentMap.get(child);
		if (parents != null) {
			for (String parent : parents) {
				// Check do we have its closure?
				Set<String> parentClosures = closureMap.get(parent);
				if (parentClosures == null) {
					parentClosures = generateClosure(parent);
				}
				// Add parents closure to this closure
				for (String parentClosure : parentClosures) {
					if (!closures.contains(parentClosure)){
						closures.add(parentClosure);
						counter++;
					}
				}
			}
		}
		return closures;
	}

	private void writeClosureData(FileWriter fw) throws IOException {
		counter=0;
		LOG.info("Writing closure data");
		for (Map.Entry<String, Set<String>> entry : closureMap.entrySet()) {
			for (String closure : entry.getValue()) {
				counter++;
				if (counter % 1000000 == 0)
					LOG.info("Written {} isas ", counter);
				fw.write("<" + entry.getKey() + "> <" + IM.IS_A.getIri() + "> <" + closure + ">.\n");
			}
		}
		fw.close();
		LOG.debug(counter + " Closure triples written");
	}

	private void importClosure(String outpath, boolean secure) throws IOException {
        LOG.info("Importing closure ...");

        StringJoiner sql = new StringJoiner("\n");
				sql.add("PREFIX im: <http://endhealth.info/im#>");
        sql.add("INSERT DATA { GRAPH <"+IM.NAMESPACE+"> {");
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(outpath + "/closure.ttl"))) {
            String triple = reader.readLine();
            while (triple != null) {
                if (!triple.isEmpty()) {
                    lineCount++;
                    sql.add(triple);
                    if (lineCount % 200000== 0) {
                        LOG.info("Importing " + lineCount + " of " + counter + " triples :" + triple);
                        sql.add("} }");
                        Update upd = conn.prepareUpdate(sql.toString());
                        conn.begin();
                        upd.execute();
                        conn.commit();
                        sql = new StringJoiner("\n");
											  sql.add("PREFIX im: <http://endhealth.info/im#>");
                        sql.add("INSERT DATA { GRAPH <"+IM.NAMESPACE+"> { ");
                    }
                }
                triple = reader.readLine();
            }
        }
        LOG.info("Importing " + lineCount + " of " + counter + " triples :");
        if (sql.length() > 20) {
            sql.add("} }");
            Update upd = conn.prepareUpdate(sql.toString());
            conn.begin();
            upd.execute();
            conn.commit();
        }
        conn.close();
    }
}
