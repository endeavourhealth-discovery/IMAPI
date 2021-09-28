package org.endeavourhealth.imapi.controllers;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.endeavourhealth.imapi.mapping.model.MapDocument;
import org.endeavourhealth.imapi.mapping.model.MapDocumentError;
import org.endeavourhealth.imapi.mapping.repository.MapDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.swagger.annotations.Api;
import org.endeavourhealth.imapi.mapping.parser.FileParser;
import org.endeavourhealth.imapi.mapping.validator.MapDocumentValidator;
import org.endeavourhealth.imapi.mapping.builder.EntityBuilder;
import org.endeavourhealth.imapi.mapping.model.MappingInstructionWrapper;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

@RestController
@RequestMapping("api/mapping")
@CrossOrigin(origins = "*")
@Api(value = "MappingController")
@SwaggerDefinition(tags = {
        @Tag(name = "Mapping Controller", description = "Mapping endpoint")
})
public class MappingController {

    MapDocumentRepository mapDocumentRepository = new MapDocumentRepository();

    @Autowired
    MapDocumentValidator mapDocumentValidator;

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping
    public List<TTDocument> map(@RequestParam MultipartFile contentFile, @RequestParam MultipartFile mappingFile, @RequestParam boolean nested) throws Exception {
        JsonNode content = FileParser.parseFile(contentFile);
        MappingInstructionWrapper map = FileParser.parseMap(mappingFile);
        writeToFile("content", content, "Content files loaded.");
        writeToFile("map", map, "Map files loaded.");
        return mapFromJsonNodes(content, map, nested);
    }

    @GetMapping("/mapDocument")
    public List<MapDocument> getMapDocuments() throws SQLException {
        return mapDocumentRepository.findAllMapDocuments();
    }

    @GetMapping("/mapDocument/{id}")
    public MapDocument getMapDocumentsById(@PathVariable int id) throws SQLException {
        return mapDocumentRepository.findMapDocumentById(id);
    }

    @PostMapping("/newPredicates")
    public Set<String> getNewPredicatesFromMapDocument(@RequestParam MultipartFile mapDocument) throws Exception {
        MappingInstructionWrapper map = FileParser.parseMap(mapDocument);
        return mapDocumentValidator.getNewPredicates(map);
    }

    @PostMapping("/mapDocument/errors")
    public MapDocumentError validateMapDocument(@RequestParam MultipartFile mapDocument) {
       return mapDocumentValidator.getError(mapDocument);
    }

    @PostMapping("/references")
    public Set<String> getReferencesFromContentFile(@RequestParam MultipartFile contentFile) throws IOException {
        if ("text/csv".equals(contentFile.getContentType())) {
            return FileParser.getColumnNamesFromCsv(contentFile);
        }
//      addition of diff
        return null;
    }

    public List<TTDocument> mapFromJsonNodes(JsonNode content, MappingInstructionWrapper map, boolean nested)
            throws Exception {
        List<TTEntity> entities = EntityBuilder.buildEntityListFromJson(content, map, nested); // Step 1: map content to entities
        writeToFile("entities", entities, "Content mapped to " + entities.size() + " entities.");

        entities = EntityBuilder.groupEntities(entities); // Step 2: group entities with same IRI
        writeToFile("grouped", entities, "Grouped to " + entities.size() + " entities.");

        List<TTDocument> ttdocuments = new ArrayList<>();
        for (String uniqueGraph : map.getGraphs()) {
            List<TTEntity> graphEntities = entities.stream().filter(ttEntity -> uniqueGraph.equals(ttEntity.getGraph())).collect(Collectors.toList());
            TTDocument ttdocument = new TTDocument().setEntities(graphEntities).setGraph(TTIriRef.iri(uniqueGraph)).setCrud(IM.REPLACE);
            ttdocuments.add(ttdocument);
            writeToFile("ttdocument", ttdocument, "TTDocument populated.");
        }

        return ttdocuments;
    }

    private void writeToFile(String filename, Object object, String message) throws IOException {
        File file = new File("api/src/main/resources/" + filename + ".json");
        file.createNewFile();
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + " : " + message);
        mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValue(file, object);
    }

}
