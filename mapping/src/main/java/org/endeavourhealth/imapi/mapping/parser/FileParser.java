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

public class FileParser {
	
	public static JsonNode parseFile(MultipartFile file) throws IOException {
		String fname = file.getOriginalFilename();
		if (fname.endsWith(".json")) {
			return parseJson(file);
		}
		if (fname.endsWith(".ttl")) {
			return parseTtl(file);
		}
		return null;
	}

	public static JsonNode parseTtl(MultipartFile file)
			throws RDFParseException, UnsupportedRDFormatException, IOException {
		InputStream inputStream = file.getInputStream();
		Model results = Rio.parse(inputStream, RDFFormat.TURTLE);
		inputStream.close();
		
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
	    String json = mapper.writeValueAsString(results);
	    return mapper.readTree(json);
	}

	public static JsonNode parseJson(MultipartFile file) throws IOException {
		JsonFactory factory = new JsonFactory();

		ObjectMapper mapper = new ObjectMapper(factory);
		return mapper.readTree(file.getBytes());
	}

}
