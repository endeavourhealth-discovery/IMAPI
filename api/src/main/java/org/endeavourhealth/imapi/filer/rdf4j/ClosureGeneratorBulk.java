package org.endeavourhealth.imapi.filer.rdf4j;

import org.endeavourhealth.imapi.dataaccess.FileRepository;
import org.endeavourhealth.imapi.filer.TCGenerator;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClosureGeneratorBulk implements TCGenerator {
	private static final Logger LOG = LoggerFactory.getLogger(ClosureGeneratorBulk.class);
	private HashMap<String, Set<String>> parentMap;
	private HashMap<String, Set<String>> closureMap;
	private static final String[] topConcepts={"http://snomed.info/sct#138875005",IM.NAMESPACE+"Concept"};
	private int counter;
	private final Set<String> blockingIris = new HashSet<>();


	@Override
	public void generateClosure(String outpath, boolean secure) throws IOException {
		getTctBlockers();
		FileRepository repo= new FileRepository(outpath);

		parentMap = new HashMap<>(1000000);
		LOG.info("Getting all subtypes....");
		repo.fetchRelationships(parentMap,blockingIris);


		try(FileWriter isas = new FileWriter(outpath+"/BulkImport.nq",true)) {
			buildClosure();
			writeClosureData(isas);

		}

	}

	private void getTctBlockers() {
		LOG.debug(String.format("Getting %s ", "top level stoppers"));
		blockingIris.addAll(Arrays.asList(topConcepts));
		//For now, only exclude top snomed and im concept
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


	private Set<String> generateClosure(String child) {
		Set<String> closures = closureMap.computeIfAbsent(child, k -> new HashSet<>());

		// Add self
		closures.add(child);
		counter++;

		Set<String> parents = parentMap.get(child);
		if (parents != null) {
			for (String parent : parents) {
				// Check do we have its closure?
				Set<String> parentIsAs = closureMap.get(parent);
				if (parentIsAs == null) {
					parentIsAs = generateClosure(parent);
				}
				// Add parents closure to this closure
				for (String parentIsA : parentIsAs) {
					if (!closures.contains(parentIsA)){
						closures.add(parentIsA);
						counter++;
					}
				}
			}
		}
		return closures;
	}

	private void writeClosureData(FileWriter fw) throws IOException {
		counter=0;
		int max=0;
		LOG.info("Writing closure data");
		for (Map.Entry<String, Set<String>> entry : closureMap.entrySet()) {
			int isas=entry.getValue().size();
			if (isas>max) {
				LOG.info(entry.getKey()+" has "+isas+" isas");
				max=isas;
			}
			for (String closure : entry.getValue()) {
				counter++;
				TTBulkFiler.setStatementCount(TTBulkFiler.getStatementCount()+1);
				if (counter % 1000000 == 0)
					LOG.info("Written {} isas ", counter);
				fw.write("<" + entry.getKey() + "> <" + IM.IS_A.getIri() + "> <" + closure + "> <"+IM.NAMESPACE+">.\n");
			}
		}
		fw.close();
		LOG.debug(counter + " Closure triples written");
		LOG.debug("statement count {}",TTBulkFiler.getStatementCount());
	}

}
