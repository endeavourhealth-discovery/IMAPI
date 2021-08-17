package org.endeavourhealth.imapi.mapping.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class FileParser {

	public static JsonNode parseFile(MultipartFile file) throws IOException {
		String fname = file.getOriginalFilename();
		if (fname.endsWith(".json")) {
			return parseJson(file);
		}
		if (fname.endsWith(".ttl")) {
			return parseTtl(file);
		}
		if (fname.endsWith(".csv")) {
			return parseCsv(file);
		}
		return null;
	}

	public static JsonNode parseCsv(MultipartFile file) throws IOException {		
		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		CsvMapper mapper = new CsvMapper();
		ObjectReader with = mapper.readerFor(ArrayNode.class).with(schema);
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		return with.readTree(file.getInputStream());
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
