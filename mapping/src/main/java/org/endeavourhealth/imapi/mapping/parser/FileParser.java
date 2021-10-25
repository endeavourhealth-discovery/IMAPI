package org.endeavourhealth.imapi.mapping.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.eclipse.rdf4j.model.util.Values.iri;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.R2RML;
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

    public static Model readMapToTriples(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Model mapModel = Rio.parse(inputStream, "http://example.com/ns#", RDFFormat.TURTLE);
        inputStream.close();
        return mapModel;
    }

    public static MappingInstructionWrapper parseMap(MultipartFile file) throws IOException {
        MappingInstructionWrapper wrapper = new MappingInstructionWrapper();

        // Array of maps
        ArrayNode result = JsonNodeFactory.instance.arrayNode();
        // Read the map into RDF4J triples
        Model mapModel = readMapToTriples(file);

        // Get the "TriplesMap" roots (i.e. the maps)
        Iterable<Statement> maps = mapModel.getStatements(null, RDF.TYPE,
                iri("http://www.w3.org/ns/r2rml#", "TriplesMap"));
        for (Statement map : maps) {
            System.out.println("Map: " + map.getSubject().toString());
            ObjectNode mapNode = JsonNodeFactory.instance.objectNode();
            result.add(mapNode);
            mapNode.set(IM.IRI, JsonNodeFactory.instance.textNode(map.getSubject().stringValue()));
            // Recursively add the triples
            addData(map.getSubject().toString(), mapModel, mapNode, map.getSubject(), wrapper);
        }
        return wrapper;
    }

    private static void recurse(String mapName, Model mapModel, Resource subject, String propertyType, String property,
                                MappingInstructionWrapper wrapper) {
        Iterable<Statement> triples = mapModel.getStatements(subject, null, null);
        for (Statement tpl : triples) {
            if (tpl.getObject().isBNode()) {
                recurse(mapName, mapModel, (Resource) tpl.getObject(), propertyType, property, wrapper);
            } else if (!tpl.getPredicate().stringValue().equals(R2RML.PREDICATE.getIri())) {
                if (R2RML.CLASS.getIri().equals(tpl.getPredicate().stringValue())
                        || R2RML.TERM_TYPE.getIri().equals(tpl.getPredicate().stringValue())) {
                    wrapper.addInstruction(mapName,
                            new MappingInstruction(propertyType, org.endeavourhealth.imapi.vocabulary.RDF.TYPE.getIri(),
                                    tpl.getPredicate().stringValue(), tpl.getObject().stringValue()));
                } else if (R2RML.GRAPH.getIri().equals(tpl.getPredicate().stringValue())) {
                    wrapper.addInstruction(mapName, new MappingInstruction(propertyType, tpl.getPredicate().stringValue(),
                            R2RML.CONSTANT.getIri(), tpl.getObject().stringValue()));
                } else {
                    wrapper.addInstruction(mapName, new MappingInstruction(propertyType, property,
                            tpl.getPredicate().stringValue(), tpl.getObject().stringValue()));
                }

            }
        }
    }

    private static Map<String, String> findPredicateObjectMapByObjectId(Iterable<Statement> triples, Value object) {
        Map<String, String> property = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode objectNode = mapper.convertValue(object, JsonNode.class);
        Iterator<Statement> i = triples.iterator();
        boolean found = false;
        while (i.hasNext() && !found) {
            Statement tpl = i.next();
            JsonNode tplNode = mapper.convertValue(tpl.getSubject(), JsonNode.class);
            if (tplNode.has("id") && objectNode.get("id").asText().equals(tplNode.get("id").asText())) {
                JsonNode objectValueNode = mapper.convertValue(tpl.getObject(), JsonNode.class);
                JsonNode predicateValueNode = mapper.convertValue(tpl.getPredicate(), JsonNode.class);
                String propertyType = predicateValueNode.get("namespace").asText() + predicateValueNode.get("localName").asText();
                property.put("propertyType", predicateValueNode.get("namespace").asText() + predicateValueNode.get("localName").asText());
                property.put("propertyValue", objectValueNode.get("label").asText());
                found = true;
            }
        }
        return property;
    }

    private static void addData(String mapName, Model mapModel, ObjectNode mapNode, Resource subject,
                                MappingInstructionWrapper wrapper) {
        Iterable<Statement> triples = mapModel.getStatements(subject, null, null);
        for (Statement tpl : triples) {
            String predicate = tpl.getPredicate().stringValue();
            Value object = tpl.getObject();

            if (predicate.equals(R2RML.PREDICATE_MAP.getIri())) {
                Map<String, String> property = findPredicateObjectMapByObjectId(mapModel.getStatements((Resource) object, null, null), object);
                recurse(mapName, mapModel, subject, property.get("propertyType"), property.get("propertyValue"), wrapper);
            }

            if (predicate.equals(R2RML.PREDICATE.getIri())
                    && !object.stringValue().equals("https://w3id.org/function/ontology#executes")) {
                recurse(mapName, mapModel, subject, R2RML.CONSTANT.getIri(), object.stringValue(), wrapper);
            }

            if (predicate.equals("http://semweb.mmlab.be/ns/rml#iterator")) {
                wrapper.setIterator(object.stringValue());
            }

            if (predicate.equals(R2RML.SUBJECT_MAP.getIri())) {
                recurse(mapName, mapModel, (Resource) object, R2RML.CONSTANT.getIri(), IM.IRI, wrapper);
            }

            JsonNode childNode = null;
            if (tpl.getObject().isLiteral())
                childNode = JsonNodeFactory.instance.textNode(object.stringValue());
            else if (tpl.getObject().isIRI()) {
                childNode = JsonNodeFactory.instance.objectNode();
                ((ObjectNode) childNode).set(IM.IRI, JsonNodeFactory.instance.textNode(object.stringValue()));
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

    public static Set<String> getColumnNamesFromCsv(MultipartFile csvFile, char separator) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.typedSchemaFor(Map.class).withHeader();
        MappingIterator<Map<String, String>> it = csvMapper.readerFor(Map.class)
                .with(csvSchema.withColumnSeparator(separator)).readValues(csvFile.getInputStream());
        return it.next().keySet();
    }

    public static XSSFWorkbook getSchemataFromCsvFiles(List<MultipartFile> csvFiles) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFFont font = workbook.createFont();
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        font.setBold(true);
        headerStyle.setFont(font);
        for (MultipartFile csvFile : csvFiles) {
            // defaults to tab seperated
            String separator = "\t";
            if (csvFile.getOriginalFilename().endsWith(".csv")) {
                separator = ",";
            }
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema csvSchema = csvMapper.typedSchemaFor(Map.class).withHeader();
            MappingIterator<Map<String, String>> it = csvMapper.readerFor(Map.class)
                    .with(csvSchema.withColumnSeparator(separator.charAt(0))).readValues(csvFile.getInputStream());

            Sheet sheet = workbook.createSheet(csvFile.getOriginalFilename());
            Row headerRow = sheet.createRow(0);
            Row row = sheet.createRow(1);
            int i = 0;
            try {
                if (it.hasNext()) {
                    for (Map.Entry<String, String> entry : it.next().entrySet()) {
                        String header = entry.getKey();
                        sheet.setColumnWidth(i, 1);
                        Cell headerCell = headerRow.createCell(i);
                        headerCell.setCellValue(header);
                        headerCell.setCellStyle(headerStyle);

                        String exampleValue = entry.getValue();
                        Cell cell = row.createCell(i);
                        cell.setCellValue(exampleValue);

                        i++;
                    }
                } else {
                    InputStream inputStream = csvFile.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    String[] headers = br.readLine().split(",");

                    for(String header: headers) {
                        sheet.setColumnWidth(i, 1);
                        Cell headerCell = headerRow.createCell(i);
                        headerCell.setCellValue(header);
                        headerCell.setCellStyle(headerStyle);
                        i++;
                    }
                }
            } catch (Exception e) {
                System.out.println("There was an error with file: " + csvFile.getOriginalFilename());
            }
        }

        return workbook;

    }
}
