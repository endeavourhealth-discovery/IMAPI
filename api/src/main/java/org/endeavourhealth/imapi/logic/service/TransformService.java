package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.logic.cache.EntityCache;
import org.endeavourhealth.imapi.model.iml.ModelDocument;
import org.endeavourhealth.imapi.model.iml.TransformRequest;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.map.MapObject;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.transformengine.Transformer;
import org.endeavourhealth.imapi.transforms.EqdToIMQ;
import org.endeavourhealth.imapi.transforms.eqd.EnquiryDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@PropertySource("classpath:eqdmap.properties")
public class TransformService {

  public ModelDocument transformEqd(EnquiryDocument eqDoc) throws IOException, DataFormatException, QueryException {
    Properties dataMap = new Properties();
    Properties criteriaLabels = new Properties();

    File file = ResourceUtils.getFile("classpath:eqdmap.properties");
    try (InputStream in = new FileInputStream(file)) {
      dataMap.load(in);
      file = ResourceUtils.getFile("classpath:criterialabels.properties");
    }
    try (InputStream in = new FileInputStream(file)) {
      criteriaLabels.load(in);
    }

    EqdToIMQ converter = new EqdToIMQ();
    return converter.convertEQD(eqDoc, dataMap, criteriaLabels);


  }

  /**
   * Service acting as a client to the transformer to tun the transform. It provides the transform maps from the IM cache
   *
   * @param request A fully formed transform request object from the API containing the typed sources, the map , and the source and target format
   * @return A set of objects which are the target objects transformed from the source
   * @throws Exception With a reference to the underlying reason for failure to transform.
   */

  public Set<Object> runTransform(TransformRequest request) throws Exception {
    validateInputs(request.getSourceFormat(), request.getTargetFormat(), request.getTransformMap(), request.getSource());
    String mapIri = request.getTransformMap().getIri();
    TTEntity mapEntity = EntityCache.getEntity(mapIri).getEntity();

    //Is it a graph map
    if (mapEntity.get(iri(IM.ENTITY_MAP)) != null) {
      return transformGraph(request, mapEntity);
    } else if (mapEntity.get(TTIriRef.iri(IM.DEFINITION)) == null) {
      throw new DataFormatException("IRI sent as graph map is not a graph map or entity map?");
    } else {
      //Must be entity map
      MapObject mapObject = mapEntity.get(TTIriRef.iri(IM.DEFINITION)).asLiteral().objectValue(MapObject.class);
      return transformEntities(request, mapObject);
    }
  }

  private Set<Object> transformEntities(TransformRequest request, MapObject mapData) throws Exception {
    Transformer transform = new Transformer(request.getSourceFormat(), request.getTargetFormat());
    Set<Object> targetObjects = new HashSet<>();
    //Look for the typed source data (String iri, object list) to transform
    for (String sourceIri : request.getSource().keySet()) {
      if (mapData.getSourceType().equals(sourceIri)) {
        List<Object> source = request.getSource().get(sourceIri);
        for (Object sourceItem : source) {
          targetObjects.addAll(transform.transformObject(sourceItem, mapData, null));
        }
      }
    }
    if (targetObjects.isEmpty())
      throw new DataFormatException("transform request source types do not have valid Data maps");
    return targetObjects;
  }

  private Set<Object> transformGraph(TransformRequest request, TTEntity graphMapEntity) throws Exception {
    Transformer transform = new Transformer(request.getSourceFormat(), request.getTargetFormat());
    Set<Object> targetObjects = new HashSet<>();
    for (TTValue map : graphMapEntity.get(TTIriRef.iri(IM.ENTITY_MAP)).getElements()) {
      TTEntity mapEntity = EntityCache.getEntity(map.asIriRef().getIri()).getEntity();
      MapObject mapObject = mapEntity.get(TTIriRef.iri(IM.DEFINITION)).asLiteral().objectValue(MapObject.class);

      //Matches the entity map with the typed source map
      for (String sourceIri : request.getSource().keySet()) {
        if (sourceIri.equals(mapObject.getSourceType())) {
          List<Object> source = request.getSource().get(sourceIri);
          for (Object sourceItem : source) {
            targetObjects.addAll(transform.transformObject(sourceItem, mapObject, null));
          }
        }
      }
    }
    if (targetObjects.isEmpty())
      throw new DataFormatException("transform request source types do not have valid Data maps");
    return targetObjects;
  }


  private void validateInputs(String sourceFormat, String targetFormat, TTIriRef graphMapIri, Map<String, List<Object>> sources) throws DataFormatException {
    if (sourceFormat == null)
      throw new DataFormatException("Source format must be defined in request (e.g. sourceFormat : JSON)");
    if (targetFormat == null)
      throw new DataFormatException("Target format must be defined in request (e.g. targetFormat : JSON-LD)");
    if (graphMapIri == null)
      throw new DataFormatException("Graph Map iri (\"@id\" : \"http...\" must be present in request");
    if (sources == null)
      throw new DataFormatException("No data Sources in request...");

  }


}
