package org.endeavourhealth.imapi.filer.rdf4j;

import org.endeavourhealth.imapi.filer.TTDocumentFiler;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTToNQuad;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TTBulkFiler  extends TTDocumentFiler {
	private static final Logger LOG = LoggerFactory.getLogger(TTBulkFiler.class);
	private static String dataPath;
	private static String configTTl;
	private static String preload;
	private FileWriter codeMap;
	private FileWriter termCoreMap;
	private FileWriter coreTerms;
	private FileWriter quads;
	private FileWriter subtypes;
	private FileWriter codeCoreMap;
	private FileWriter descendants;
	private FileWriter legacyCore;
	private FileWriter allEntities;
	private FileWriter codeIds;
	private FileWriter coreIris;
	private static int privacyLevel=0;
	private static int statementCount;
	private static final Set<String> specialChildren= new HashSet<>(Arrays.asList(SNOMED.NAMESPACE+"92381000000106"));

	@Override
	protected void startTransaction() throws TTFilerException {

	}

	@Override
	protected void commit() throws TTFilerException {

	}

	@Override
	protected void rollback() {
	}


	public void fileDocument(TTDocument document) throws TTFilerException {
		if (document.getEntities() == null)
			return;
		if (document.getEntities().isEmpty())
			return;
		writeGraph(document);

	}




	private void writeGraph(TTDocument document) throws TTFilerException {

		String graph=null;
		if (document.getGraph()!=null) {
			graph = document.getGraph().getIri();
		}
		String scheme = graph.substring(graph.lastIndexOf("/") + 1);
		String path = dataPath;
		try {
			try {
				quads = new FileWriter(path + "\\BulkImport" + ".nq",true);
				//quads = new FileWriter(path + "\\BulkImport-" + fileNumber + ".nq");
				codeMap = new FileWriter(path + "\\CodeMap-" + scheme + ".txt",true);
				termCoreMap = new FileWriter(path + "\\TermCoreMap-" + scheme + ".txt",true);
				subtypes = new FileWriter(path + "\\SubTypes" + ".txt",true);
				allEntities = new FileWriter(path + "\\Entities" + ".txt", true);
				codeCoreMap = new FileWriter(path + "\\CodeCoreMap-" + scheme+ ".txt",true);
				codeIds= new FileWriter(path+"\\CodeIds-"+scheme+".txt",true);
				descendants = new FileWriter(path + "\\Descendants" + ".txt",true);
				coreTerms = new FileWriter(path + "\\CoreTerms" + ".txt",true);
				legacyCore = new FileWriter(path + "\\LegacyCore" + ".txt",true);
				coreIris= new FileWriter(path+"\\coreIris.txt",true);


				int counter = 0;
				TTToNQuad converter = new TTToNQuad();
				LOG.info("Writing out graph data for " + graph);
				for (TTEntity entity : document.getEntities()) {
					counter++;
					String entityGraph= entity.getGraph()!=null ? entity.getGraph().getIri() : graph;
					if (entity.get(IM.PRIVACY_LEVEL)!=null)
						if (entity.get(IM.PRIVACY_LEVEL).asLiteral().intValue()>getPrivacyLevel())
							continue;

				//	if (entity.getIri().equals("http://endhealth.info/emis#_ESCTMA381305"))
				//		System.out.println(entity.getIri());
					allEntities.write(entity.getIri() + "\n");
					if (graph.equals(IM.NAMESPACE))
						coreIris.write(entity.getIri()+"\t"+ entity.getName()+"\n");
					addToMaps(entity,entityGraph);
					addSubtypes(entity);
					addTerms(entity,entityGraph);

					if (counter % 100000 == 0)
						LOG.info("Written {} entities for " + document.getGraph().getIri(), counter);
					if (entity.get(RDFS.LABEL) != null) {
						if (entity.get(IM.HAS_STATUS) == null)
							entity.set(IM.HAS_STATUS, IM.ACTIVE);
						if (entity.get(IM.HAS_SCHEME)==null)
							entity.set(IM.HAS_SCHEME, TTIriRef.iri(graph));
					}

					List<String> quadList = converter.transformEntity(entity, entityGraph);
					for (String quad : quadList) {
						quads.write(quad + "\n");
						statementCount++;
					}
				}
				LOG.debug(counter + "Document written to file");
				LOG.info("Finished - total of {} statements,  {}", statementCount,new Date());
			} catch (Exception e) {
				e.printStackTrace();
				throw new TTFilerException(e.getMessage());
			} finally {
				quads.close();
				codeMap.close();
				termCoreMap.close();
				subtypes.close();
				codeCoreMap.close();
				descendants.close();
				coreTerms.close();
				legacyCore.close();
				allEntities.close();
				codeIds.close();
				coreIris.close();
			}
		} catch (Exception e)  {
		e.printStackTrace();
		throw new TTFilerException("Unable to bulk file");
	}

}

	private void addTerms(TTEntity entity, String graph) throws IOException {
		boolean isCoreGraph= graph.equals(IM.NAMESPACE)||graph.equals(SNOMED.NAMESPACE);
		if (isCoreGraph)
			if (entity.getName()!=null)
				coreTerms.write(entity.getName()+"\t"+ entity.getIri()+"\n");
	}

	private void addSubtypes(TTEntity entity) throws IOException {
		if (entity.get(RDFS.SUBCLASSOF)!=null)
			for (TTValue parent:entity.get(RDFS.SUBCLASSOF).getElements()) {
				subtypes.write(entity.getIri() + "\t" + RDFS.SUBCLASSOF.getIri()+"\t"+ parent.asIriRef().getIri() + "\n");
				if (specialChildren.contains(parent.asIriRef().getIri()))
					descendants.write(parent.asIriRef().getIri()+"\t"+ entity.getIri()+"\t"+ entity.getName()+"\n");
			}
		if (entity.get(RDFS.SUBPROPERTYOF)!=null)
			for (TTValue parent:entity.get(RDFS.SUBPROPERTYOF).getElements())
				subtypes.write(entity.getIri()+"\t"+ RDFS.SUBCLASSOF.getIri()+"\t"+ parent.asIriRef().getIri()+"\n");
		if (entity.get(SNOMED.REPLACED_BY)!=null)
			for (TTValue parent:entity.get(SNOMED.REPLACED_BY).getElements())
				subtypes.write(entity.getIri()+"\t"+ SNOMED.REPLACED_BY.getIri()+"\t"+ parent.asIriRef().getIri()+"\n");
	}

	private void addToMaps(TTEntity entity,String graph) throws IOException {
		boolean isCoreGraph= graph.equals(IM.NAMESPACE)||graph.equals(SNOMED.NAMESPACE);
		if (entity.getCode()!=null){
			codeMap.write(entity.getCode()+"\t"+ entity.getIri()+"\n");
			if (graph.equals(IM.NAMESPACE)|| (graph.equals(SNOMED.NAMESPACE)))
				codeCoreMap.write(entity.getCode()+"\t"+ entity.getIri()+"\n");
		}
		if (entity.get(IM.CODE_ID)!=null)
			codeIds.write(entity.get(IM.CODE_ID).asLiteral().getValue()+"\t"+
				entity.getIri()+"\n");
		if (entity.get(IM.HAS_TERM_CODE)!=null){
				for (TTValue tc:entity.get(IM.HAS_TERM_CODE).getElements()) {
					if (tc.asNode().get(IM.CODE) != null) {
						String code = tc.asNode().get(IM.CODE).asLiteral().getValue();
						if (graph.equals(IM.NAMESPACE)|| (graph.equals(SNOMED.NAMESPACE)))
							codeCoreMap.write(code + "\t" + entity.getIri() + "\n");
					}
				}
		}


		if (entity.get(IM.MATCHED_TO)!=null) {
			for (TTValue core : entity.get(IM.MATCHED_TO).getElements()) {
				if (!isCoreGraph) {
					legacyCore.write(entity.getIri() + "\t" + core.asIriRef().getIri() + "\n");
					if (entity.get(IM.CODE_ID)!=null)
						codeIds.write(entity.get(IM.CODE_ID).asLiteral().getValue()+"\t"+
							                           core.asIriRef().getIri()+"\n");
				}
				codeCoreMap.write(entity.getCode()+"\t"+ core.asIriRef().getIri()+"\n");
				writeTermCoreMap(entity.getName(),core.asIriRef().getIri());
				if (entity.get(IM.HAS_TERM_CODE) != null) {
					for (TTValue tc : entity.get(IM.HAS_TERM_CODE).getElements()) {
						TTNode termCode = tc.asNode();
						if (termCode.get(IM.CODE) != null) {
							String code = termCode.get(IM.CODE).asLiteral().getValue();
							codeCoreMap.write(code+"\t"+core.asIriRef().getIri());
						}
						if (termCode.get(RDFS.LABEL) != null) {
							String term = termCode.get(RDFS.LABEL).asLiteral().getValue();

							writeTermCoreMap(term, core.asIriRef().getIri()+"\n");
						}
					}
				}
			}
		}
	}

	private void writeTermCoreMap(String term,String core) throws IOException {
		if (term== null)
			return;

		termCoreMap.write(term+"\t"+ core+"\n");
		byte[] arr=term.getBytes(StandardCharsets.UTF_8);
		if (!(arr.length== term.length())){
			StringBuilder newTerm= new StringBuilder();
			for (byte b : arr) {
				if (b > 0)
					newTerm.append((char) b);
			}
			termCoreMap.write(newTerm+"\t"+ core+"\n");
		}
	}

 public static void createRepository() throws TTFilerException {
	 LOG.info("Fast import of {} quad data", new Date());
		try {
				String config = configTTl;
				String data = dataPath;
				String preloadPath =preload;
				Process process = Runtime.getRuntime()
					.exec("cmd /c " + "preload --stopOnFirstError --configFile " + config + "\\Config.ttl " +"--queue.folder "+data + " "+ data + "\\BulkImport*.nq",
						null, new File(preloadPath));
				BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while (true) {
					line = r.readLine();
					if (line == null) {
						break;
					}
					System.out.println(line);
				}
			File directory= new File(data+"\\");
			for(File file: Objects.requireNonNull(directory.listFiles()))
				if (!file.isDirectory())
					file.delete();
			} catch (IOException e) {
				e.printStackTrace();
				throw new TTFilerException(e.getMessage());
		}
 }


	@Override
	public void close() throws Exception {

	}

	public static String getDataPath() {
		return dataPath;
	}

	public static void setDataPath(String dataPath) {
		TTBulkFiler.dataPath = dataPath;
	}

	public static String getConfigTTl() {
		return configTTl;
	}

	public static void setConfigTTl(String configTTl) {
		TTBulkFiler.configTTl = configTTl;
	}

	public static String getPreload() {
		return preload;
	}

	public static void setPreload(String preload) {
		TTBulkFiler.preload = preload;
	}

	public static int getPrivacyLevel() {
		return privacyLevel;
	}

	public static void setPrivacyLevel(int privacyLevel) {
		TTBulkFiler.privacyLevel = privacyLevel;
	}

	public static int getStatementCount() {
		return statementCount;
	}

	public static void setStatementCount(int statementCount) {
		TTBulkFiler.statementCount = statementCount;
	}
}
