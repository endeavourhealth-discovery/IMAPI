package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.iml.*;
import org.endeavourhealth.imapi.model.maps.EntityMap;
import org.endeavourhealth.imapi.model.maps.MapPath;
import org.endeavourhealth.imapi.model.maps.SourceTargetMap;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

/**
 * Transformer engine to transform a collection of source objects to target objects using a transformation map
 * All source objects must be able to support graph like property paths (e.g. json, xml, object reflection.
 * <p> The transformation engine does not include extracting data from csv or tables.
 * However, the format dependent plugins may be extended to retrieve or cache data</p>
 */
public class Transformer {

	private String sourceFormat;
	private String targetFormat;
	private EntityMap entityMap;
	private Map<String, Set<Object>> typedTargets;
	private Map<String, String> sourceVarMap;
	private Map<String,List<Object>> typedResources;
	private Set<Object> targetObjects;
	private Map<String,Object> variables= new HashMap<>();
	private Object entitySource;

	public Transformer(EntityMap entityMap,String sourceFormat, String targetFormat) {
		this.sourceFormat= sourceFormat;
		this.targetFormat= targetFormat;
		this.entityMap= entityMap;
	}

	/**
	 * Transforms a collection of typed objects to a set of objects.
	 * At least one types should match the source list in the transformation map
	 * The objects are object references in a logical form that matches the type
	 * Note that the actual format of source and target (e.g. json) are used as arguments to this function so that the engine could transform
	 * The same logical source in different formats if the plugins have been developed
	 *
	 * @param sources a list of source objects to transform for example, derived from the typed source map from the transform request.
	 * @return a set of target objects in the target format
	 */

	public Set<Object> transform(List<Object> sources) throws Exception {
		return transform(sources,null,null);
	}

	/**
	 * Transforms a collection of typed objects to a set of objects.
	 * At least one types should match the source list in the transformation map
	 * This method is used when the target resource already exists
	 * The objects are object references in a logical form that matches the type
	 * Note that the actual format of source and target (e.g. json) are used as arguments to this function so that the engine could transform
	 * The same logical source in different formats if the plugins have been developed
	 *
	 * @param sources a list of source objects to transform for example, derived from the typed source map from the transform request.
	 * @param targets a map of type to target object that already exists i.e. to be further populated
	 * @return a set of target objects in the target format
	 */

	public Set<Object> transform(List<Object> sources,Map<String,Object> targets) throws Exception {
		return transform(sources,null,null);
	}
	/**
	 * Transforms a collection of typed objects to a set of objects.
	 * At least one types should match the source list in the transformation map
	 * The objects are object references in a logical form that matches the type
	 * Note that the actual format of source and target (e.g. json) are used as arguments to this function so that the engine could transform
	 * The same logical source in different formats if the plugins have been developed
	 *
	 * @param sources a list of source objects to transform for example, derived from the typed source map from the transform request.
	 * @param typedResources a map of type to a list of resources used in the map, where required.
	 * @return a set of target objects in the target format
	 */

	public Set<Object> transform(List<Object> sources, Map<String,Object> targets,Map<String,List<Object>> typedResources) throws DataFormatException, JsonProcessingException {
		this.typedResources= typedResources;
		this.targetObjects= new HashSet<>();
		for (Object sourceObject:sources) {
			this.entitySource= getObjectFromSource(sourceObject);
			targetObjects.addAll(transformRules());
		}
		return targetObjects;
	}

	private Object getObjectFromSource(Object sourceObject) {
		if (sourceFormat.equalsIgnoreCase("JSON")){
			JsonNode node= new ObjectMapper().valueToTree(sourceObject);
			return node;
		}
		else
			throw new RuntimeException("Source object not compatible with source format : "+ sourceFormat+" source= "+ sourceObject.getClass().getName());
	}


	private Set<Object> transformRules() throws DataFormatException, JsonProcessingException {
		subjectMap();
		return targetObjects;
	}

	private void subjectMap() throws DataFormatException, JsonProcessingException {
		/*
		Object targetEntity= createEntity(subjectMap.getTargetEntity().getIri());
		for (MapPath sourcePath:subjectMap.getSourcePaths()){
			String path= sourcePath.getPath();
			String variable= sourcePath.getVariable();
			variables.put(variable,resolvePath(this.entitySource,path));
			Object targetValue;
			if (subjectMap.getFunction()!=null) {
				targetValue = runFunction(subjectMap.getFunction());
			}
			else
				targetValue= variables.get(variable);
			setTargetValue(targetEntity,subjectMap.getTargetPath(),targetValue);
		}

		 */

	}

	private void setTargetValue(Object targetEntity, String targetPath,Object targetValue) throws JsonProcessingException {
		if (targetFormat.equalsIgnoreCase("JSON-LD")){
			TTTransform.setValueWithPath(targetEntity,targetPath,targetValue);
		}
	}

	private Object runFunction(Function function) throws DataFormatException {
			Map<String,List<Object>> args= getFunctionArguments(function);
			return TransformFunctions.runFunction(function.getIri(),args);
	}

	private Object createEntity(String entityType){
		if (targetFormat.equalsIgnoreCase("JSON-LD")){
			Object target= TTTransform.createEntity(entityType);
			targetObjects.add(target);
			return target;
		}
		else
			throw new RuntimeException("target format unsupported : "+targetFormat);
	}

	private Object resolvePath(Object source,String path){
		if (sourceFormat.equalsIgnoreCase("JSON")){
			return JsonTransform.getValuefromPath(source,path);
		}
		else
			throw new RuntimeException("source format unsupported : "+ sourceFormat);
	}



		private Map<String, List<Object>> getFunctionArguments(Function function) throws DataFormatException {
			Map<String, List<Object>> result = null;
			if (function.getArgument() != null) {
				result = new HashMap<>();
				int i = 0;
				for (Argument argument : function.getArgument()) {
					i++;
					String parameter = argument.getParameter();
					if (parameter == null)
						parameter = String.valueOf(i);
					result.putIfAbsent(parameter,new ArrayList<>());
					if (argument.getValueData() != null)
						result.get(parameter).add(argument.getValueData());
					else if (argument.getValueVariable() != null) {
						result.get(parameter).add(variables.get(argument.getValueVariable()));

					}
				}
			}
			return result;
		}


}