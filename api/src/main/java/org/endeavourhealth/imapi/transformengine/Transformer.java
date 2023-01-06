package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.*;
import org.endeavourhealth.imapi.model.map.MapProperty;
import org.endeavourhealth.imapi.model.map.MapObject;

import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Transformer engine to mapObject a collection of source objects to target objects using a transformation map
 * All source objects must be able to support graph like property paths (e.g. json, xml, object reflection.
 * <p> The transformation engine does not include extracting data from csv or tables.
 * However, the format dependent plugins may be extended to retrieve or cache data</p>
 */
public class Transformer {

	private final String sourceFormat;
	private final String targetFormat;
	private final Set<Object> targetObjects= new HashSet<>();
	private Map<String,Object> varToObject= new HashMap<>();
	private final SyntaxTranslator sourceTranslator;
	private final SyntaxTranslator targetTranslator;

	public Transformer(String sourceFormat, String targetFormat) throws DataFormatException {
		this.sourceFormat= sourceFormat;
		this.targetFormat= targetFormat;
		this.sourceTranslator = TransformFactory.createConverter(sourceFormat);
		this.targetTranslator = TransformFactory.createConverter(targetFormat);
	}

	/**
	 * Transforms a collection of typed objects to a set of objects.
	 * At least one types should match the source list in the transformation map
	 * The objects are object references in a logical form that matches the type
	 * Note that the actual format of source and target (e.g. json) are used as arguments to this function so that the engine could mapObject
	 * The same logical source in different formats if the plugins have been developed
	 *
	 * @param source a source object to transform mapObject for example, derived from the typed source map from the mapObject request.
	 * @param objectMap  a Map object declaring the relatoinships between a source object class and target object class
	 * @param varToObject a map mappking variables to its values passed in by parent transforms.
	 * @return a set of target objects in the target format
	 */

	public Set<Object> transformObject(Object source, MapObject objectMap, Map<String, Object> varToObject) throws Exception {
		this.varToObject= varToObject;
		if (this.varToObject==null)
			this.varToObject= new HashMap<>();
		if (source instanceof List) {
			for (Object sourceItem : (List<?>) source) {
				transformObject(sourceItem, objectMap, varToObject);
			}
			return targetObjects;
		}
		else {
			if (objectMap.getWhere()!=null){
				if (!where(objectMap.getWhere(), source))
					return null;
			}
			Object targetObject;
			if (objectMap.getTargetType()!=null){
				 targetObject= targetTranslator.createEntity(objectMap.getTargetType());
				targetObjects.add(targetObject);
			}
			else
				throw new DataFormatException("Object map has no target object to create");
			if (objectMap.getPropertyMap() != null) {
				for (MapProperty propertyMap : objectMap.getPropertyMap())
					transformProperty(source,targetObject,propertyMap,this.varToObject);
			}
			return targetObjects;
		}
	}








	public void transformProperty(Object sourceObject,Object targetObject,MapProperty rule,Map<String, Object> varToObject) throws Exception {
		if (sourceObject instanceof List){
			for (Object sourceItem:(List) sourceObject){
				transformProperty(sourceItem,targetObject,rule,varToObject);
			}
		}
		else {
			this.varToObject= varToObject;
			if (rule.getSource() != null) {
				if (targetObject == null)
					throw new DataFormatException("Data map or value map has not created or retrieved a target entity to populate ");
				Where where = rule.getWhere();
				if (where != null) {
					if (!where(where, sourceObject))
						return;
				}
				String source = rule.getSource();
				String variable = rule.getSourceVariable();
				if (source != null) {
					Object sourceValue;
					sourceValue = sourceTranslator.getPropertyValue(sourceObject, source);
					if (rule.getListMode() != null) {
						if (sourceValue instanceof List) {
							sourceValue = getListItems((List<?>) sourceValue, rule.getListMode());
						}
					}
					if (variable != null)
						this.varToObject.put(variable, sourceValue);
					if (rule.getPropertyMap() != null) {
						Transformer flatTransformer = new Transformer(sourceFormat, targetFormat);
						for (MapProperty propertyMap : rule.getPropertyMap()) {
								flatTransformer.transformProperty(sourceValue, targetObject, propertyMap, this.varToObject);
						}
					}
					else if (rule.getObjectMap() != null) {
						Transformer nestedTransformer = new Transformer(sourceFormat, targetFormat);
						if (rule.getTarget() != null) {
							Object targetValue = targetTranslator.convertToTarget(nestedTransformer
								.transformObject(sourceValue, rule.getObjectMap(), this.varToObject));
							targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), targetValue);
						}
						else
							throw new DataFormatException("Map has an object map for processing property "+ source+" but no target property to assien the aobjec to. This should be a property map list instead");
					}
					else if (rule.getFunction() != null) {
						Object targetValue = targetTranslator.convertToTarget(runFunction(rule.getFunction()));
						if (rule.getTarget() != null)
							targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), targetValue);
					}
					else if (rule.getTarget() != null) {
						if (rule.getValueData() != null) {
							targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), rule.getValueData());
						}
						else if (rule.getValueVariable() != null) {
							targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), targetTranslator.convertToTarget(varToObject.get(rule.getValueVariable())));
						}
						else {
							targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), targetTranslator.convertToTarget(sourceValue));
						}
					}
					else
						throw new DataFormatException("no target property, property map or object map for source " + source + "property");
				}
			}
			else if (rule.getTarget()!=null){
				if (rule.getObjectMap()!=null){
					Transformer nestedTransformer = new Transformer(sourceFormat, targetFormat);
					Object targetValue = targetTranslator.convertToTarget(nestedTransformer
						.transformObject(sourceObject, rule.getObjectMap(), this.varToObject));
					targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), targetValue);
				}
				else if (rule.getValueData()!=null){
					targetTranslator.setPropertyValue(rule,targetObject,rule.getTarget(),rule.getValueData());
				}
				else if (rule.getValueVariable()!=null){
					targetTranslator.setPropertyValue(rule,targetObject,rule.getTarget(),varToObject.get(rule.getValueVariable()));
				}
				else
					throw new DataFormatException("Property map has a target property of "+ rule.getTarget()+" but no source property and no object map for the target property.");
			}
			else
				throw new DataFormatException("unrecognised property map. No source property");
		}

	}



	private Object runFunction(FunctionClause functionClause) throws DataFormatException {
			Map<String,Object> args= getFunctionArguments(functionClause);
			return TransformFunctions.runFunction(functionClause.getIri(),args);
	}




	private Map<String, Object> getFunctionArguments(FunctionClause functionClause) throws DataFormatException {
		if (functionClause.getArgument() != null) {
				return getArguments (functionClause.getArgument());
		}
			else
				return null;
	}

	private Map<String,Object> getArguments(List<Argument> arguments) throws DataFormatException {
			Map<String, Object> result = new HashMap<>();
			int argIndex = 0;
				for (Argument argument : arguments) {
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
						result.put(parameter,varToObject.get(argument.getValueVariable()));
					}

				}
			return result;
		}

	private Object query(Object sourceEntity, String path,Where where) throws DataFormatException, JsonProcessingException {
		if (where.getPathTo() != null) {
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
		Object sourceValue = sourceTranslator.getPropertyValue(sourceNode, where.getPathTo());
		if (where.getValue() != null) {
			if (where.getValue().getValue().equals(sourceValue)) {
				return true;
			}
			return false;
		}
		return false;
	}

	public Object getListItems(List source, ListMode listMode){
			if (listMode== ListMode.FIRST)
				return source.get(0);
			else if (listMode==ListMode.REST) {
				source.remove(0);
				return source;
			}
			else
				return source;
		}
}

