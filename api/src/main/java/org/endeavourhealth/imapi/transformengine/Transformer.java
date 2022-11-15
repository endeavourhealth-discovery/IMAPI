package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.*;
import org.endeavourhealth.imapi.model.iml.MapRule;

import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Transformer engine to transform a collection of source objects to target objects using a transformation map
 * All source objects must be able to support graph like property paths (e.g. json, xml, object reflection.
 * <p> The transformation engine does not include extracting data from csv or tables.
 * However, the format dependent plugins may be extended to retrieve or cache data</p>
 */
public class Transformer {

	private final String sourceFormat;
	private final String targetFormat;
	private DataMap dataMap;
	private Map<String,List<Object>> typeToResources;
	private final Set<Object> targetObjects= new HashSet<>();
	private final Map<String,Object> varToObject= new HashMap<>();
	private Object entitySource;
	private Object targetObject;
	private ObjectReader sourceReader;
	private ObjectFiler targetFiler;

	public Transformer(DataMap dataMap, String sourceFormat, String targetFormat) throws DataFormatException {
		this.sourceFormat= sourceFormat;
		this.targetFormat= targetFormat;
		this.dataMap = dataMap;
		this.sourceReader= TransformFactory.createReader(sourceFormat);
		this.targetFiler= TransformFactory.createFiler(targetFormat);

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
	 * @param target an existing target object passed in i.e. to be further populated
	 * @return a set of target objects in the target format
	 */

	public Set<Object> transform(List<Object> sources,Object target) throws Exception {
		return transform(sources,target,null);
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

	public Set<Object> transform(List<Object> sources, Object targetObject,Map<String,List<Object>> typedResources) throws DataFormatException, JsonProcessingException {


		for (Object sourceObject:sources) {
			transformSource(sourceObject, targetObject, typedResources);
		}
		return targetObjects;

		}

	public Set<Object> transformSource(Object sourceObject, Object targetObject, Map<String, List<Object>> typedResources) throws DataFormatException, JsonProcessingException {
		if (targetObject!=null) {
			this.targetObjects.add(targetObject);
			this.targetObject = targetObject;
		}
		this.entitySource= sourceObject;
		if (dataMap.getRules()!=null){
			for (MapRule rule: dataMap.getRules())
				transformRule(rule);
		}
		return targetObjects;
	}






	private void transformRule(MapRule map) throws DataFormatException, JsonProcessingException {
		if (map.getCreate()!=null){
			 this.targetObject= targetFiler.createEntity(map.getCreate().getIri());
			 targetObjects.add(targetObject);
		}
		if (map.getSourceProperty()!=null){
			String path= map.getSourceProperty();
			String variable= map.getSourceVariable();
			Where where= map.getWhere();
			Object sourceValue= sourceReader.getPropertyValue(this.entitySource, path,where);
			varToObject.put(variable,sourceValue);
			Object targetValue;
			if (map.getFunction()!=null) {
				targetValue = runFunction(map.getFunction());
			}
			else
				targetValue= varToObject.get(variable);
			if (map.getValueMap()!=null){
				if (map.getTargetProperty() == null) {
					new Transformer(map.getValueMap(), sourceFormat, targetFormat).transformSource(sourceValue, targetObject, typeToResources);
				}
				else {
					targetFiler.setPropertyValue(targetObject,map.getTargetProperty(),new Transformer(map.getValueMap(), sourceFormat, targetFormat).transformSource(sourceValue, targetObject, typeToResources));
				}
			}
			else {
				targetFiler.setPropertyValue(targetObject, map.getTargetProperty(), targetValue);
			}
		}

	}



	private Object runFunction(Function function) throws DataFormatException {
			Map<String,List<Object>> args= getFunctionArguments(function);
			return TransformFunctions.runFunction(function.getIri(),args);
	}




		private Map<String, List<Object>> getFunctionArguments(Function function) throws DataFormatException {
			Map<String, List<Object>> result = null;
			if (function.getArgument() != null) {
				result = new HashMap<>();
				for (Argument argument : function.getArgument()) {
					String parameter = argument.getParameter();
					if (parameter == null)
						parameter = "null";
					result.putIfAbsent(parameter,new ArrayList<>());
					if (argument.getValueData() != null)
						result.get(parameter).add(argument.getValueData());
					else if (argument.getValueVariable() != null) {
						result.get(parameter).add(varToObject.get(argument.getValueVariable()));

					}
				}
			}
			return result;
		}


}