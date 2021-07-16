package org.endeavourhealth.imapi.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
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
		List<TTEntity> ttentities = new ArrayList<>();
		ttentities.addAll(map(file, map));
		System.out.println(ttentities.size());
		return ttentities;
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
		List<TTEntity> ttentities = getTTEntitiesFromQuads(result.getQuads(null, null, null));

		savedFile.delete();
		savedMap.delete();

		return ttentities;
	}

	private List<TTEntity> getTTEntitiesFromQuads(List<Quad> quads) throws JsonProcessingException {
		List<TTEntity> entities = new ArrayList<>();
		Set<String> uniqIris = new HashSet<String>();
		for (Quad quad : quads) {
			uniqIris.add(quad.getSubject().getValue());
		}

		uniqIris.forEach(iri -> {
			List<Quad> subQuads = quads.stream().filter(quad -> iri.equals(quad.getSubject().getValue()))
					.collect(Collectors.toList());
			entities.add(convertQuadListToTTEntity(iri, subQuads));
		});
		
		return entities;
	}

	private TTEntity convertQuadListToTTEntity(String iri, List<Quad> subQuads) {
		TTEntity entity = new TTEntity().setIri(iri);
		subQuads.forEach(quad -> {
			if(quad.getPredicate().getValue().equals(RDF.TYPE.getIri())) {
				entity.set(new TTIriRef(quad.getPredicate().getValue()), new TTIriRef((quad.getObject().getValue())));
			}
			else if (quad.getPredicate().getValue().equals(IM.IS_A.getIri())) {
				entity.addObject(new TTIriRef(quad.getPredicate().getValue()), new TTIriRef((quad.getObject().getValue())));
			}
			else if (predicateIsArray(quad.getPredicate().getValue(), subQuads)) {
				entity.addObject(new TTIriRef(quad.getPredicate().getValue()),
						new TTLiteral(quad.getObject().getValue()));
			} else {
				entity.set(new TTIriRef(quad.getPredicate().getValue()), new TTLiteral(quad.getObject().getValue()));
			}
		});
		return entity;
	}

	private boolean predicateIsArray(String predicate, List<Quad> subQuads) {
		int predicateNum = subQuads.stream().filter(quad -> quad.getPredicate().getValue().equals(predicate))
				.collect(Collectors.toList()).size();
		return predicateNum > 1;
	}
}
