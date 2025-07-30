package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.iml.TargetUpdateMode;
import org.endeavourhealth.imapi.model.map.MapProperty;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TTTranslator implements SyntaxTranslator {

  private static void setPropertyValueTTEntity(MapProperty rule, TTNode targetEntity, Object targetValue, String predicate) {
    TTNode nodeValue = (TTNode) targetValue;
    if (((TTEntity) targetValue).getIri() != null)
      nodeValue.setIri(((TTEntity) targetValue).getIri());
    if (rule.getTargetUpdateMode() == TargetUpdateMode.ADDTOLIST) {
      targetEntity.addObject(TTIriRef.iri(predicate), nodeValue);
    }
  }

  private static void setPropertyValueTTValue(MapProperty rule, TTNode targetEntity, TTValue targetValue, String predicate) {
    if (rule.getTargetUpdateMode() == TargetUpdateMode.ADDTOLIST) {
      targetEntity.addObject(TTIriRef.iri(predicate), targetValue);
    } else
      targetEntity.set(TTIriRef.iri(predicate), targetValue);
  }

  public Object createEntity(String type) {
    TTEntity target = new TTEntity();
    target.addType(TTIriRef.iri(type));

    return target;
  }

  @Override
  public Object getPropertyValue(Object source, String property) {
    return null;
  }

  @Override
  public boolean isCollection(Object source) {
    return source instanceof TTArray;
  }

  @Override
  public Object convertToTarget(Object from) {
    if (from instanceof Map<?, ?> fromMap) {
      TTNode result = new TTNode();
      for (Map.Entry<?, ?> entry : fromMap.entrySet()) {
        String key = (String) entry.getKey();
        if (!key.contains(":"))
          key = Namespace.IM + key;
        Object value = convertToTargetSingle(entry.getValue());
        if (value instanceof TTArray valueTTArray)
          result.set(TTIriRef.iri(key), valueTTArray);
        else if (value instanceof TTValue valueTTValue) {
          result.set(TTIriRef.iri(key), valueTTValue);
        } else
          throw new IllegalArgumentException("Unknown sub node type in target map " + value.getClass().getSimpleName());
      }
      return result;
    } else if (from instanceof Collection<?> fromCollection) {
      TTArray result = new TTArray();
      for (Object value : fromCollection) {
        result.add((TTValue) convertToTarget(value));
      }
      return result;
    }
    return convertToTargetSingle(from);
  }

  @Override
  public Object convertFromSource(Object from) {
    return null;
  }

  private Object convertToTargetSingle(Object from) {
    try {
      if (from instanceof String fromString)
        return TTLiteral.literal(fromString);
      else if (from instanceof Number fromNumber)
        return TTLiteral.literal(fromNumber);
      else {
        return from;
      }
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Unknown target value type " + from.getClass().getName());
    }
  }

  @Override
  public void setPropertyValue(MapProperty rule, Object targetEntity, String property, Object targetValue) {
    try {
      if (property.equals("id") || property.equals("iri"))
        ((TTNode) targetEntity).setIri(((TTLiteral) targetValue).getValue());
      else {
        String predicate = property;
        if (!property.contains(":"))
          predicate = Namespace.IM + property;
        switch (targetValue) {
          case List<?> targetValueList -> {
            TTArray array = new TTArray();
            for (Object item : targetValueList) {
              array.add((TTValue) convertToTargetSingle(item));
            }
            ((TTNode) targetEntity).set(TTIriRef.iri(predicate), array);
          }
          case TTArray targetValueTTArray -> ((TTNode) targetEntity).set(TTIriRef.iri(predicate), targetValueTTArray);
          case TTEntity targetValueTTEntity ->
            setPropertyValueTTEntity(rule, (TTNode) targetEntity, targetValueTTEntity, predicate);
          case TTValue targetValueTTValue ->
            setPropertyValueTTValue(rule, (TTNode) targetEntity, targetValueTTValue, predicate);
          case null, default -> ((TTNode) targetEntity).set(TTIriRef.iri(predicate), TTLiteral.literal(targetValue));
        }
      }
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Value of property : " + property + " cannot be set as its class is invalid (" + targetValue.getClass().getSimpleName() + ")");
    }
  }


}
