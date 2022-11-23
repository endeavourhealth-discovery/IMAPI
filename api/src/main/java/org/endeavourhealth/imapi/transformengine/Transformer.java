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
	private final DataMap dataMap;
	private Map<String,List<Object>> typeToResources;
	private final Set<Object> targetObjects= new HashSet<>();
	private final Map<String,Object> varToObject= new HashMap<>();
	private Object entitySource;
	private Object workingTarget;
	private final SyntaxTranslator sourceTranslator;
	private final SyntaxTranslator targetTranslator;

	public Transformer(DataMap dataMap, String sourceFormat, String targetFormat) throws DataFormatException {
		this.sourceFormat= sourceFormat;
		this.targetFormat= targetFormat;
		this.dataMap = dataMap;
		this.sourceTranslator = TransformFactory.createConverter(sourceFormat);
		this.targetTranslator = TransformFactory.createConverter(targetFormat);
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
		if (sourceTranslator.isCollection(sourceObject)) {
			for (Object sourceItem : (Iterable<?>) sourceObject) {
				transformSource(sourceItem, targetObject, typedResources);
			}
			return targetObjects;
		}
		else {
			if (targetObject != null) {
				this.targetObjects.add(targetObject);
				this.workingTarget = targetObject;
			}
			this.entitySource = sourceObject;
			if (dataMap.getRules() != null) {
				for (MapRule rule : dataMap.getRules())
					transformRule(rule);
			}
			return targetObjects;
		}
	}






	private void transformRule(MapRule rule) throws DataFormatException, JsonProcessingException {
		if (rule.getCreate()!=null){
			 this.workingTarget = targetTranslator.createEntity(rule.getCreate().getIri());
			 targetObjects.add(workingTarget);
		}
		if (rule.getSourceProperty()!=null) {
			if (this.workingTarget == null)
				throw new DataFormatException("Data map or value map has not created or retrieved a target entity to populate ");
			String path = rule.getSourceProperty();
			String variable = rule.getSourceVariable();
			Where where = rule.getWhere();
			Object sourceValue;
			if (where==null) {
				sourceValue = sourceTranslator.getPropertyValue(this.entitySource, path);
			}
			else
				sourceValue= query(this.entitySource,path,where);
			if (sourceValue != null){
				if (variable!=null)
				 varToObject.put(variable, sourceValue);
				ListMode listMode= rule.getListMode();
				if (listMode!=null)
					sourceValue= sourceTranslator.getListItems(sourceValue,listMode);
				if (rule.getFlatMap()!=null) {
					new Transformer(rule.getFlatMap(), sourceFormat, targetFormat).transformSource(sourceValue, workingTarget, typeToResources);
				}
				else {
					Object targetValue;
					if (rule.getFunction() != null) {
						targetValue = targetTranslator.convertToTarget(runFunction(rule.getFunction()));
				}
				else 	if (rule.getValueMap() != null) {
					targetValue = targetTranslator.convertToTarget(
						new Transformer(rule.getValueMap(), sourceFormat, targetFormat)
							.transformSource(sourceValue, null, typeToResources));
				}
				else {
					targetValue = targetTranslator.convertToTarget(sourceTranslator.convertFromSource(sourceValue));
					}
				targetTranslator.setPropertyValue(rule, workingTarget, rule.getTargetProperty(), targetValue);
			}
			}
		}

	}



	private Object runFunction(Function function) throws DataFormatException {
			Map<String,Object> args= getFunctionArguments(function);
			return TransformFunctions.runFunction(function.getIri(),args);
	}




		private Map<String, Object> getFunctionArguments(Function function) throws DataFormatException {
			Map<String, Object> result = null;
			int argIndex=0;
			if (function.getArgument() != null) {
				result = new HashMap<>();
				for (Argument argument : function.getArgument()) {
					argIndex++;
					String parameter = argument.getParameter();
					if (parameter == null)
						parameter = String.valueOf(argIndex);
					if (argument.getValueData() != null)
						result.put(parameter,argument.getValueData());
					else if (argument.getValueVariable() != null) {
						Object variableValue= varToObject.get(argument.getValueVariable());
						if (variableValue==null)
							throw new DataFormatException("argument : "+ parameter+",  variable: "+ argument.getValueVariable()+"  in function has not been defined");
						result.put(parameter,sourceTranslator.convertFromSource(varToObject.get(argument.getValueVariable())));
					}

				}
			}
			return result;
		}

	private Object query(Object sourceEntity, String path,Where where) throws DataFormatException, JsonProcessingException {
		if (where.getPath() != null) {
			if (where(where, sourceEntity))
				return sourceTranslator.getPropertyValue(sourceEntity,path);
			else
				return null;
		}
		else if (where.getAnd()!=null){
			for (Where and:where.getAnd()){
				if (!where(and,sourceEntity))
					return null;
			}
			return sourceTranslator.getPropertyValue(sourceEntity,path);
		}
		else
			throw new DataFormatException("Where clause for rule on path : "+path+" has clause format not yet supported");
	}

	private boolean where (Where where,Object sourceNode) throws DataFormatException, JsonProcessingException {
		Object sourceValue = sourceTranslator
			.convertFromSource(sourceTranslator.getPropertyValue(sourceNode, where.getPath()));
		if (where.getValue() != null) {
			if (where.getValue().getValue().equals(sourceValue)) {
				return true;
			}
			return false;
		}
		return false;
	}



}