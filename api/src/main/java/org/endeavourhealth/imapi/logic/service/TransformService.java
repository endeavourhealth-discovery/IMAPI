package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.iml.DataMap;
import org.endeavourhealth.imapi.model.iml.TransformRequest;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.transformengine.Transformer;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

public class TransformService {
	/**
	 * Service acting as a client to the transformer to tun the transform. It provides the transform maps from the IM cache
	 * @param request A fully formed transform request object from the API containing the typed sources, the map , and the source and target format
	 * @return A set of objects which are the target objects transformed from the source
	 * @throws Exception With a reference to the underlying reason for failure to transform.
	 */

	public Set<Object> runTransform(TransformRequest request) throws Exception{
		validateInputs(request.getSourceFormat(),request.getTargetFormat(),request.getTransformMap(),request.getSource());
		String mapIri= request.getTransformMap().getIri();
		TTEntity mapEntity = EntityCache.getEntity(mapIri).getEntity();

		//Is it a graph map
		if (mapEntity.get(IM.ENTITY_MAP)!=null) {
			return transformGraph(request, mapEntity);
		}
		else if (mapEntity.get(IM.DEFINITION) == null) {
				throw new DataFormatException("IRI sent as graph map is not a graph map or entity map?");
			}
		else {
			//Must be entity map
			DataMap dataMap = mapEntity.get(IM.DEFINITION).asLiteral().objectValue(DataMap.class);
			return transformEntities(request, dataMap);
			}
		}

	private Set<Object> transformEntities(TransformRequest request, DataMap dataMap) throws Exception {
		Transformer transform = new Transformer(dataMap,request.getSourceFormat(),request.getTargetFormat());
		//Look for the typed source data (String iri, object list) to transform
		for (String sourceIri: request.getSource().keySet()){
				if (dataMap.getSource().getIri().equals(sourceIri))
					 return transform.transform(request.getSource().get(sourceIri));
				}
		throw new DataFormatException("Source types do not match with the entity map ");

	}

	private Set<Object> transformGraph(TransformRequest request, TTEntity graphMapEntity) throws Exception{
		for (TTValue map:graphMapEntity.get(IM.ENTITY_MAP).getElements()){
			TTEntity mapEntity= EntityCache.getEntity(map.asIriRef().getIri()).getEntity();
			DataMap dataMap = mapEntity.get(IM.DEFINITION).asLiteral().objectValue(DataMap.class);
			//Matches the entity map with the typed source map
			for (String source:request.getSource().keySet()){
				if (source.equals(dataMap.getSource().getIri())){
					Transformer transformer= new Transformer(dataMap,request.getSourceFormat(),request.getTargetFormat());
					return transformer.transform(request.getSource().get(source));
				}
			}

		}
		return Collections.emptySet();
	}


	private void validateInputs(String sourceFormat, String targetFormat, TTIriRef graphMapIri,Map<String,List<Object>> sources) throws DataFormatException {
		if (sourceFormat == null)
			throw new DataFormatException("Source format must be defined in request (e.g. sourceFormat : JSON)");
		if (targetFormat == null)
			throw new DataFormatException("Target format must be defined in request (e.g. targetFormat : JSON-LD)");
		if (graphMapIri == null)
			throw new DataFormatException("Graph Map iri (\"@id\" : \"http...\" must be present in request");
		if (sources==null)
			throw new DataFormatException("No data Sources in request...");

	}


}
