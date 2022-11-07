package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.maps.TransformRequest;

import java.util.*;
import java.util.zip.DataFormatException;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.transformengine.Transformer;
import org.endeavourhealth.imapi.vocabulary.IM;

public class TransformService {
	/**
	 * Service acting as a client to the transformer to tun the transform. It provides the transform maps from the IM cache
	 * @param request A fully formed transform request object from the API containing the typed sources, the map , and the source and target format
	 * @return A set of objects which are the target objects transformed from the source
	 * @throws Exception With a reference to the underlying reason for failure to transform.
	 */

	public Set<Object> runTransform(TransformRequest request) throws Exception{
		/*
		if (request.getTransformMap()==null)
			throw new DataFormatException("Transform request must provide the transform map for this transform. The transform map must include all transform maps as imports");
		String mapIri= request.getTransformMap().getIri();
		TTEntity mapEntity = EntityCache.getEntity(mapIri).getEntity();
		if (mapEntity.get(IM.DEFINITION)==null){
			throw new RuntimeException("Transform map with iri : "+ mapIri+" not found in IM or cache.");
		}
		TransformMap transformMap= mapEntity.get(IM.DEFINITION).asLiteral().objectValue(TransformMap.class);
		Map<String,TransformMap> imports=null;
		try {
			if (transformMap.getImports() != null) {
				imports = new HashMap<>();
				Map<String, TransformMap> finalImports = imports;
				transformMap.getImports().forEach(tm -> {
					TTEntity importEntity = EntityCache.getEntity(tm.getIri()).getEntity();
					TransformMap imp = null;
					try {
						imp = importEntity.get(IM.DEFINITION).asLiteral().objectValue(TransformMap.class);
					} catch (JsonProcessingException e) {
						throw new RuntimeException("Transform map "+ importEntity.getIri()+" does not exist or has no map definition");
					}
					finalImports.put(importEntity.getIri(), imp);
				});
			}
		}
		catch (Exception e){
			throw new Exception("Unable to retrieve imported transform maps from "+ mapIri+ " due to "+ e.getMessage());
			}
		Transformer transform = new Transformer();
		Map<String, List<Object>> sources= request.getSource();
		transform.transform(sources,transformMap,request.getSourceFormat(),request.getTargetFormat(),imports);
		*/
		return null;

	}


}
