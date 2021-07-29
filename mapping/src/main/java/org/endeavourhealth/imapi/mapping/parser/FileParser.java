package org.endeavourhealth.imapi.mapping.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.mapping.model.Node;

public class FileParser {
	
	public static Object parseFile(MultipartFile file) throws IOException {
		String fname = file.getOriginalFilename();
		if (fname.endsWith(".json")) {
			return parseJson(file);
		}
		if (fname.endsWith(".ttl")) {
			return parseTtl(file);
		}
		return null;
	}

	public static Model parseTtl(MultipartFile file)
			throws RDFParseException, UnsupportedRDFormatException, IOException {
		InputStream inputStream = file.getInputStream();
		Model results = Rio.parse(inputStream, RDFFormat.TURTLE);
		inputStream.close();
		return results;
	}

	public static JsonNode parseJson(MultipartFile file) throws IOException {
		JsonFactory factory = new JsonFactory();

		ObjectMapper mapper = new ObjectMapper(factory);
		JsonNode rootNode = mapper.readTree(file.getBytes());

		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
		
		Node node = new Node();
//		while (fieldsIterator.hasNext()) {
//			Map.Entry<String, JsonNode> field = fieldsIterator.next();
//			System.out.println("Key: " + field.getKey() + "\tValue:" + field.getValue());
//		}
		return rootNode;
	}

}
