package org.endeavourhealth.imapi.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import be.ugent.rml.Executor;
import be.ugent.rml.Utils;
import be.ugent.rml.functions.FunctionLoader;
import be.ugent.rml.functions.lib.IDLabFunctions;
import be.ugent.rml.records.RecordsFactory;
import be.ugent.rml.store.Quad;
import be.ugent.rml.store.QuadStore;
import be.ugent.rml.store.QuadStoreFactory;
import be.ugent.rml.store.RDF4JStore;
import be.ugent.rml.term.NamedNode;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("api/mapping")
@CrossOrigin(origins = "*")
@Api(value = "MappingController", description = "Mapping endpoint")
public class MappingController {

	ObjectMapper mapper = new ObjectMapper();

	@PostMapping
	public List<TTEntity> main(@RequestParam("file") MultipartFile file, @RequestParam("map") MultipartFile map)
			throws Exception {
		List<TTEntity> ttconcepts = new ArrayList<>();
		ttconcepts.addAll(map(file, map));
		return ttconcepts;
	}

	private List<TTEntity> map(MultipartFile file, MultipartFile map) throws Exception {
		// path to the content file
		File savedFile = new File("src/test/resources/" + file.getResource().getFilename());
		if (!savedFile.exists()) {
			file.transferTo(new File(savedFile.getAbsolutePath()));
		}
		
		// path to the mapping file that needs to be executed
		File savedMap = new File("src/test/resources/" + map.getResource().getFilename());
		map.transferTo(new File(savedMap.getAbsolutePath()));

		// Get the mapping string stream
		InputStream mappingStream = new FileInputStream(savedMap);

		// Load the mapping in a QuadStore
		QuadStore rmlStore = QuadStoreFactory.read(mappingStream);

		// Set up the basepath for the records factory, i.e., the basepath for the
		// (local file) data sources
		RecordsFactory factory = new RecordsFactory(savedMap.getParent());

		// Set up the functions used during the mapping
		Map<String, Class> libraryMap = new HashMap<>();
		libraryMap.put("IDLabFunctions", IDLabFunctions.class);

		FunctionLoader functionLoader = new FunctionLoader(null, libraryMap);

		// Set up the outputstore (needed when you want to output something else than
		// nquads
		QuadStore outputStore = new RDF4JStore();

		// Create the Executor
		Executor executor = new Executor(rmlStore, factory, functionLoader, outputStore,
				Utils.getBaseDirectiveTurtle(mappingStream));

		// Execute the mapping
		QuadStore result = executor.executeV5(null).get(new NamedNode("rmlmapper://default.store"));
		result.getQuads(null, null, null).forEach(quad -> {
			String subject = quad.getSubject().getValue();
			String predicate = quad.getPredicate().getValue();
			String object = quad.getObject().getValue();
			System.out.println(subject + " : " + predicate + " : " + object);
		});

		List<TTEntity> ttconcepts = getTTEntitiesFromQuads(result.getQuads(null, null, null));

		savedFile.delete();
		savedMap.delete();
		
		return ttconcepts;
	}

	private List<TTEntity> getTTEntitiesFromQuads(List<Quad> quads) throws JsonProcessingException {
		List<TTEntity> concepts = new ArrayList<>();

		quads.forEach(quad -> {
			List<TTEntity> existingConcepts = concepts.stream()
					.filter(concept -> quad.getSubject().getValue().equals(concept.getIri()))
					.collect(Collectors.toList());

			if (!existingConcepts.isEmpty()) {
				existingConcepts.get(0).addObject(new TTIriRef(quad.getPredicate().getValue()),
						new TTIriRef(quad.getObject().getValue()));
			} else {
				TTEntity concept = new TTEntity().setIri(quad.getSubject().getValue());
				concept.addObject(new TTIriRef(quad.getPredicate().getValue()),
						new TTIriRef(quad.getObject().getValue()));
				concepts.add(concept);
			}
		});

		return concepts;
	}
}
