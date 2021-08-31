package org.endeavourhealth.imapi.mapping.parser;

import java.io.IOException;
import java.io.InputStream;

import static org.eclipse.rdf4j.model.util.Values.iri;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.mapping.model.MappingInstructionWrapper;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class FileParser {

	public static MappingInstructionWrapper parseMap(MultipartFile file) throws IOException {
		MappingInstructionWrapper wrapper = new MappingInstructionWrapper();

		// Array of maps
		ArrayNode result = JsonNodeFactory.instance.arrayNode();
		// Read the map into RDF4J triples
		InputStream inputStream = file.getInputStream();
		Model mapModel = Rio.parse(inputStream, RDFFormat.TURTLE);
		inputStream.close();

		// Get the "TriplesMap" roots (i.e. the maps)
		Iterable<Statement> maps = mapModel.getStatements(null, RDF.TYPE,
				iri("http://www.w3.org/ns/r2rml#", "TriplesMap"));
		for (Statement map : maps) {
			System.out.println("Map: " + map.getSubject().toString());
			ObjectNode mapNode = JsonNodeFactory.instance.objectNode();
			result.add(mapNode);
			mapNode.set("@id", JsonNodeFactory.instance.textNode(map.getSubject().stringValue()));
			// Recursively add the triples
			addData(map.getSubject().toString(), mapModel, mapNode, map.getSubject(), wrapper);
		}
		return wrapper;
	}

	private static void recurse(String mapName, Model mapModel, Resource subject, String property,
			MappingInstructionWrapper wrapper) {
		Iterable<Statement> triples = mapModel.getStatements(subject, null, null);
		for (Statement tpl : triples) {
			if (tpl.getObject().isBNode()) {
				recurse(mapName, mapModel, (Resource) tpl.getObject(), property, wrapper);
			} else if (!tpl.getPredicate().stringValue().equals("http://www.w3.org/ns/r2rml#predicate")) {
				System.out.println(
						property + " : " + tpl.getPredicate().stringValue() + " : " + tpl.getObject().stringValue());

				if ("http://www.w3.org/ns/r2rml#class".equals(tpl.getPredicate().stringValue())
						|| "http://www.w3.org/ns/r2rml#termType".equals(tpl.getPredicate().stringValue())) {
					wrapper.addInstruction(mapName, new MappingInstruction(mapName, RDFS.SUBCLASSOF.getIri(),
							tpl.getPredicate().stringValue(), tpl.getObject().stringValue()));
				} else {
					wrapper.addInstruction(mapName, new MappingInstruction(mapName, property,
							tpl.getPredicate().stringValue(), tpl.getObject().stringValue()));
				}

			}
		}
	}

	private static void addData(String mapName, Model mapModel, ObjectNode mapNode, Resource subject,
			MappingInstructionWrapper wrapper) {
		Iterable<Statement> triples = mapModel.getStatements(subject, null, null);
		for (Statement tpl : triples) {
			String predicate = tpl.getPredicate().stringValue();
			Value object = tpl.getObject();

			if (predicate.equals("http://www.w3.org/ns/r2rml#predicate")
					&& !object.stringValue().equals("https://w3id.org/function/ontology#executes")) {
				recurse(mapName, mapModel, subject, object.stringValue(), wrapper);
			}

			if (predicate.equals("http://semweb.mmlab.be/ns/rml#iterator")) {
				wrapper.setIterator(object.stringValue());
			}

			if (predicate.equals("http://www.w3.org/ns/r2rml#subjectMap")) {
				recurse(mapName, mapModel, (Resource) object, "@id", wrapper);
			}

			JsonNode childNode = null;
			if (tpl.getObject().isLiteral())
				childNode = JsonNodeFactory.instance.textNode(object.stringValue());
			else if (tpl.getObject().isIRI()) {
				childNode = JsonNodeFactory.instance.objectNode();
				((ObjectNode) childNode).set("@id", JsonNodeFactory.instance.textNode(object.stringValue()));
			} else if (tpl.getObject().isBNode()) {
				childNode = JsonNodeFactory.instance.objectNode();
				// Recurse nested triple via bnode
				addData(mapName, mapModel, (ObjectNode) childNode, (Resource) object, wrapper);
			} else {
				System.err.println("Unknown triple type!");
			}
			if (mapNode.has(predicate)) {
				if (mapNode.get(predicate).isArray()) {
					// Existing predicate which is array, just add it
					((ArrayNode) mapNode.get(predicate)).add(childNode);
				} else {
					// Existing predicate which is node. Set to new array then add original node
					// plus new node
					ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
					arrayNode.add(mapNode.get(predicate));
					arrayNode.add(childNode);
					mapNode.set(predicate, arrayNode);
				}
			} else {
				// New predicate, just add it
				mapNode.set(predicate, childNode);
			}
		}
	}

	public static JsonNode parseFile(MultipartFile file) throws IOException {
		String fname = file.getOriginalFilename();
		if (fname.endsWith(".json")) {
			return parseJson(file);
		}
		if (fname.endsWith(".ttl")) {
			return parseTtl(file);
		}
		if (fname.endsWith(".csv")) {
			return parseCsv(file, ",".charAt(0));
		}
		if (fname.endsWith(".tsv") || fname.endsWith(".txt")) {
			return parseCsv(file, "\t".charAt(0));
		}
		return null;
	}

	public static JsonNode parseCsv(MultipartFile file, char separator) throws IOException {
		CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(separator);
		CsvMapper csvMapper = new CsvMapper();
		MappingIterator<JsonNode> it = csvMapper.readerFor(JsonNode.class).with(schema)
				.readValues(file.getInputStream());
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode arrayNode = mapper.createArrayNode();
		arrayNode.addAll(it.readAll());
		return arrayNode;
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
