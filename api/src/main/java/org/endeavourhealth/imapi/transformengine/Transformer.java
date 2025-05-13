package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.MapFunction;
import org.endeavourhealth.imapi.model.iml.ListMode;
import org.endeavourhealth.imapi.model.imq.Argument;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Where;
import org.endeavourhealth.imapi.model.map.MapObject;
import org.endeavourhealth.imapi.model.map.MapProperty;


import java.util.*;

/**
 * Transformer engine to mapObject a collection of source objects to target objects using a transformation map
 * All source objects must be able to support graph like property paths (e.g. json, xml, object reflection.
 * <p> The transformation engine does not include extracting data match csv or tables.
 * However, the format dependent plugins may be extended to retrieve or cache data</p>
 */
public class Transformer {

  private final String sourceFormat;
  private final String targetFormat;
  private final Set<Object> targetObjects = new HashSet<>();
  private Map<String, Object> varToObject = new HashMap<>();
  private final SyntaxTranslator sourceTranslator;
  private final SyntaxTranslator targetTranslator;

  public Transformer(String sourceFormat, String targetFormat) {
    this.sourceFormat = sourceFormat;
    this.targetFormat = targetFormat;
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
   * @param source      a source object to transform mapObject for example, derived match the typed source map match the mapObject request.
   * @param objectMap   a Map object declaring the relatoinships between a source object class and target object class
   * @param varToObject a map mappking variables to its values passed in by parent transforms.
   * @return a set of target objects in the target format
   */

  public Set<Object> transformObject(Object source, MapObject objectMap, Map<String, Object> varToObject) throws JsonProcessingException {
    this.varToObject = varToObject;
    if (this.varToObject == null)
      this.varToObject = new HashMap<>();

    if (source instanceof List) {
      transformObjectList((List<?>) source, objectMap, varToObject);
    } else {
      boolean hasTargetObjects = transformObjectValue(source, objectMap);
      if (!hasTargetObjects) return Collections.emptySet();
    }
    return targetObjects;
  }

  private void transformObjectList(List<?> source, MapObject objectMap, Map<String, Object> varToObject) throws JsonProcessingException {
    for (Object sourceItem : source) {
      transformObject(sourceItem, objectMap, varToObject);
    }
  }

  private boolean transformObjectValue(Object source, MapObject objectMap) throws JsonProcessingException {
    if (objectMap.getWhere() != null && !where(objectMap.getWhere(), source))
      return false;

    if (objectMap.getTargetType() == null)
      throw new IllegalArgumentException("Object map has no target object to create");

    Object targetObject = targetTranslator.createEntity(objectMap.getTargetType());
    targetObjects.add(targetObject);
    if (objectMap.getPropertyMap() != null) {
      for (MapProperty propertyMap : objectMap.getPropertyMap())
        transformProperty(source, targetObject, propertyMap, this.varToObject);
    }
    return true;
  }


  public void transformProperty(Object sourceObject, Object targetObject, MapProperty rule, Map<String, Object> varToObject) throws JsonProcessingException {
    if (sourceObject instanceof List<?> sourceObjectList) {
      for (Object sourceItem : sourceObjectList) {
        transformProperty(sourceItem, targetObject, rule, varToObject);
      }
    } else {
      this.varToObject = varToObject;
      if (rule.getSource() != null) {
        transformPropertySource(sourceObject, targetObject, rule, varToObject);
      } else if (rule.getTarget() != null) {
        transformPropertyTarget(sourceObject, targetObject, rule, varToObject);
      } else
        throw new IllegalArgumentException("unrecognised property map. No source property");
    }
  }

  private void transformPropertySource(Object sourceObject, Object targetObject, MapProperty rule, Map<String, Object> varToObject) throws JsonProcessingException {
    if (targetObject == null)
      throw new IllegalArgumentException("Data map or value map has not created or retrieved a target entity to populate ");

    Match where = rule.getWhere();
    if (where != null && !where(where, sourceObject))
      return;

    String source = rule.getSource();
    if (source == null)
      return;

    Object sourceValue = sourceTranslator.getPropertyValue(sourceObject, source);
    if (rule.getListMode() != null && sourceValue instanceof List) {
      sourceValue = getListItems((List<?>) sourceValue, rule.getListMode());
    }

    String variable = rule.getSourceVariable();
    if (variable != null)
      this.varToObject.put(variable, sourceValue);

    if (rule.getPropertyMap() != null) {
      transformPropertySourcePropertyMap(targetObject, rule, sourceValue);
    } else if (rule.getObjectMap() != null) {
      transformPropertySourceObjectMap(targetObject, rule, source, sourceValue);
    } else if (rule.getFunction() != null) {
      transformPropertySourceFunction(targetObject, rule);
    } else if (rule.getTarget() != null) {
      transformPropertySourceTarget(targetObject, rule, varToObject, sourceValue);
    } else
      throw new IllegalArgumentException("no target property, property map or object map for source " + source + "property");
  }

  private void transformPropertySourcePropertyMap(Object targetObject, MapProperty rule, Object sourceValue) throws JsonProcessingException {
    Transformer flatTransformer = new Transformer(sourceFormat, targetFormat);
    for (MapProperty propertyMap : rule.getPropertyMap()) {
      flatTransformer.transformProperty(sourceValue, targetObject, propertyMap, this.varToObject);
    }
  }

  private void transformPropertySourceObjectMap(Object targetObject, MapProperty rule, String source, Object sourceValue) throws JsonProcessingException {
    Transformer nestedTransformer = new Transformer(sourceFormat, targetFormat);
    if (rule.getTarget() != null) {
      Object targetValue = targetTranslator.convertToTarget(nestedTransformer
        .transformObject(sourceValue, rule.getObjectMap(), this.varToObject));
      targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), targetValue);
    } else
      throw new IllegalArgumentException("Map has an object map for processing property " + source + " but no target property to assign the object to. This should be a property map list instead");
  }

  private void transformPropertySourceFunction(Object targetObject, MapProperty rule) {
    Object targetValue = targetTranslator.convertToTarget(runFunction(rule.getFunction()));
    if (rule.getTarget() != null)
      targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), targetValue);
  }

  private void transformPropertySourceTarget(Object targetObject, MapProperty rule, Map<String, Object> varToObject, Object sourceValue) {
    if (rule.getValueData() != null) {
      targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), rule.getValueData());
    } else if (rule.getValueVariable() != null) {
      targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), targetTranslator.convertToTarget(varToObject.get(rule.getValueVariable())));
    } else {
      targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), targetTranslator.convertToTarget(sourceValue));
    }
  }

  private void transformPropertyTarget(Object sourceObject, Object targetObject, MapProperty rule, Map<String, Object> varToObject) throws JsonProcessingException {
    if (rule.getObjectMap() != null) {
      Transformer nestedTransformer = new Transformer(sourceFormat, targetFormat);
      Object targetValue = targetTranslator.convertToTarget(nestedTransformer
        .transformObject(sourceObject, rule.getObjectMap(), this.varToObject));
      targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), targetValue);
    } else if (rule.getValueData() != null) {
      targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), rule.getValueData());
    } else if (rule.getValueVariable() != null) {
      targetTranslator.setPropertyValue(rule, targetObject, rule.getTarget(), varToObject.get(rule.getValueVariable()));
    } else
      throw new IllegalArgumentException("Property map has a target property of " + rule.getTarget() + " but no source property and no object map for the target property.");
  }


  private Object runFunction(MapFunction functionClause) {
    Map<String, Object> args = getFunctionArguments(functionClause);
    return TransformFunctions.runFunction(functionClause.getIri(), args);
  }


  private Map<String, Object> getFunctionArguments(MapFunction functionClause) {
    if (functionClause.getArgument() != null) {
      return getArguments(functionClause.getArgument());
    } else
      return Collections.emptyMap();
  }

  private Map<String, Object> getArguments(List<Argument> arguments) {
    Map<String, Object> result = new HashMap<>();
    int argIndex = 0;
    for (Argument argument : arguments) {
      argIndex++;
      String parameter = argument.getParameter();
      if (parameter == null)
        parameter = String.valueOf(argIndex);
      if (argument.getValueData() != null)
        result.put(parameter, argument.getValueData());
      else if (argument.getValueVariable() != null) {
        Object variableValue = varToObject.get(argument.getValueVariable());
        if (variableValue == null)
          throw new IllegalArgumentException("argument : " + parameter + ",  variable: " + argument.getValueVariable() + "  in function has not been defined");
        result.put(parameter, varToObject.get(argument.getValueVariable()));
      }

    }
    return result;
  }

  private boolean where(Match match, Object sourceNode) throws JsonProcessingException {
    Where property= match.getWhere();
      Object sourceValue = sourceTranslator.getPropertyValue(sourceNode, property.getId());
      if (property.getValue() != null && property.getValue().equals(sourceValue))
        return true;
    return false;
  }

  public Object getListItems(List<?> source, ListMode listMode) {
    if (listMode == ListMode.FIRST)
      return source.get(0);
    else if (listMode == ListMode.REST) {
      source.remove(0);
      return source;
    } else
      return source;
  }
}

