package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.TTFilerFactory;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileRepository {

	private Map<String,Map<String,Set<String>>> codeCoreMap= new HashMap<>();
	private Map<String,Map<String,Set<String>>> termCoreMap= new HashMap<>();
	private Map<String,Map<String,Set<String>>> codes= new HashMap<>();
	private Map<String,String> coreTerms= new HashMap<>();
	private static String dataPath;

	public FileRepository(String dataPath){
		this.dataPath=dataPath;
	}

	public void fetchRelationships(Map<String, Set<String>> parentMap,
																				Map<String,Set<String>> replacementMap,
																				Set<String> blockingIris) throws IOException{
		String fileName= getFile("SubTypes");
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line = reader.readLine();
			while (line != null && !line.isEmpty()) {
				String[] fields = line.split("\t");
				String child = fields[0];
				String relationship = fields[1];
				String parent = fields[2];
				if (!blockingIris.contains(parent)) {
					Set<String> parents = parentMap.computeIfAbsent(child, k -> new HashSet<>());
					parents.add(parent);
					if (relationship.equals(SNOMED.REPLACED_BY.getIri())) {
						Set<String> replacements = replacementMap.computeIfAbsent(parent, k -> new HashSet<>());
						replacements.add(child);
					}
				}
				line= reader.readLine();
			}
		}

	}

	/**
	 * Returns A core entity iri and name from a core term
	 * @param term the code or description id or term code
	 * @return iri and name of entity
	 */
	public TTIriRef getReferenceFromCoreTerm(String term,List<String> schemes) throws IOException {
		if (coreTerms.isEmpty()) {
			fetchCoreTerms();
		}
		if (coreTerms.get(term)!=null)
		  return TTIriRef.iri(coreTerms.get(term));
		else
			return null;
		}

	public Map<String, Set<String>> getAllMatchedLegacy() throws IOException{
		Map<String,Set<String>> legacyMap= new HashMap<>();
		String fileName=getFile("LegacyCore");
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line = reader.readLine();
			while (line != null && !line.isEmpty()) {
				String[] fields = line.split("\t");
				String legacy=fields[0];
				if (fields.length<2){
					System.out.println("invalid line "+ line);
					String x= reader.readLine();
					System.out.println(x);
					line=line+x;
					fields= line.split("\t");
				}
				String core = fields[1];
				Set<String> coreSet= legacyMap.computeIfAbsent(legacy, l-> new HashSet<>());
				coreSet.add(core);
				line = reader.readLine();
			}
		}
		return legacyMap;
	}

	private void fetchCoreTerms() throws IOException{
		String fileName=getFile("CoreTerms");
		Map<String,Set<String>> children= new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line = reader.readLine();
			while (line != null && !line.isEmpty()) {
				String[] fields = line.split("\t");
				String term= fields[0];
				String iri = fields[1];
				coreTerms.put(term,iri);
					line = reader.readLine();
				}
			}
	}

	public Set<TTIriRef> getCoreFromCode(String originalCode, List<String> schemes){
		try {
			for (String scheme : schemes) {
				if (codeCoreMap.get(scheme) == null)
					fetchCodeCoreMap(scheme);
				if (codeCoreMap.get(scheme).get(originalCode) != null) {
					return codeCoreMap.get(scheme).get(originalCode).stream().map(TTIriRef::iri).collect(Collectors.toSet());
				}
			}
			return null;
		} catch (Exception e) {
			System.err.println("unable to retrieve core from code : "+ e.getMessage());
			return null;
		}
	}

	public Set<TTIriRef> getCoreFromLegacyTerm(String originalTerm, String scheme) throws IOException {
		if (termCoreMap.get(scheme)==null)
			fetchTermCoreMap(scheme);
		if (termCoreMap.get(scheme).get(originalTerm)!=null)
			return termCoreMap.get(scheme).get(originalTerm).stream().map(TTIriRef::iri).collect(Collectors.toSet());
		else
			return null;
	}
	public Set<String> getCodes(String scheme) throws IOException {
		if (codes.get(scheme)==null)
			fetchCodeMap(scheme);
		return new HashSet<>(codes.get(scheme).keySet());
	}

	public Set<String> getAllEntities() throws IOException{
		Set<String> entities= new HashSet<>();
		String fileName= getFile("Entities");
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line= reader.readLine();
			while(line != null && !line.isEmpty()){
				entities.add(line);
				line= reader.readLine();
			}
		}
		return entities;
	}


	public void fetchCodeMap(String scheme) throws FileNotFoundException, IOException {
		Map<String,Set<String>> codeSet= codes.computeIfAbsent(scheme, s-> new HashMap<>());
		String fileName= getSchemeFile("CodeMap",scheme);
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line= reader.readLine();
			while(line != null && !line.isEmpty()){
				String[] fields=line.split("\t");
				String code=fields[0];
				String iri=fields[1];
				Set<String> coreSet= codeSet.computeIfAbsent(code,c-> new HashSet<>());
				coreSet.add(iri);
				line= reader.readLine();
			}

		}
	}
	public Map<String,Set<String>> getDescendants(String concept) throws IOException {
		return fetchDescendants(concept);
	}

	private Map<String,Set<String>> fetchDescendants(String concept) throws IOException {
		String fileName=getFile("Descendants");
		Map<String,Set<String>> children= new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line = reader.readLine();
			while (line != null && !line.isEmpty()) {
				String[] fields = line.split("\t");
				String parent = fields[0];
				String child = fields[1];
				String childName = fields[2];
				if (parent.equals(concept)) {
					Set<String> childTerms= children.computeIfAbsent(child,c-> new HashSet<>());
					childTerms.add(childName);
					line = reader.readLine();
				}
			}
		}
		return children;

	}

	public Map<String,Set<String>> getCodeCoreMap(String scheme) throws IOException {
		if (codeCoreMap.get(scheme)!=null)
			return codeCoreMap.get(scheme);
		else
			return fetchCodeCoreMap(scheme);
	}

	public Map<String,Set<String>> fetchCodeCoreMap(String scheme) throws  IOException {
		Map<String,Set<String>> coreMap= codeCoreMap.computeIfAbsent(scheme, s-> new HashMap<>());
		String fileName= getSchemeFile("CodeCoreMap",scheme);
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line= reader.readLine();
			while(line != null && !line.isEmpty()){
				String[] fields=line.split("\t");
				String code=fields[0];
				String core=fields[1];
				Set<String> coreSet= coreMap.computeIfAbsent(code,c-> new HashSet<>());
				coreSet.add(core);
				line= reader.readLine();
			}

		}
		return codeCoreMap.get(scheme);
	}

	public void fetchTermCoreMap(String scheme) throws FileNotFoundException, IOException {
		Map<String,Set<String>> coreMap= termCoreMap.computeIfAbsent(scheme, s-> new HashMap<>());
		String fileName= getSchemeFile("TermCoreMap",scheme);
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line= reader.readLine();
			while(line != null && !line.isEmpty()){
				String[] fields=line.split("\t");
				String term=fields[0];
				String core=fields[1];
				Set<String> coreSet= coreMap.computeIfAbsent(term,c-> new HashSet<>());
				coreSet.add(core);
				line= reader.readLine();
			}

		}
	}

	private String getSchemeFile(String fileType,String scheme){
		scheme= scheme.substring(scheme.lastIndexOf("/")+1);
		return dataPath+"\\"+fileType+"-"+scheme+".txt";
	}

	private String getFile(String fileType){
		return dataPath+"\\"+fileType+".txt";
	}

	public static String getDataPath() {
		return dataPath;
	}

	public static void setDataPath(String dataPath) {
		FileRepository.dataPath = dataPath;
	}
}
