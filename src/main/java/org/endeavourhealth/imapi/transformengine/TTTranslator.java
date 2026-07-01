package org.endeavourhealth.imapi.transformengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.map.MapProperty;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.interfacemanager.model.TargetUpdateMode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TTTranslator implements SyntaxTranslator {

  private static void setPropertyValueTTEntity(MapProperty rule, TTNodeJava targetEntity, Object targetValue, String predicate) {
    TTNodeJava nodeValue = (TTNodeJava) targetValue;
    if (((TTEntityJava) targetValue).getIri() != null)
      nodeValue.setIri(((TTEntityJava) targetValue).getIri());
    if (rule.getTargetUpdateMode() == TargetUpdateMode.ADDTOLIST) {
      targetEntity.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate), nodeValue);
    }
  }

  private static void setPropertyValueTTValue(MapProperty rule, TTNodeJava targetEntity, TTValueJava targetValue, String predicate) {
    if (rule.getTargetUpdateMode() == TargetUpdateMode.ADDTOLIST) {
      targetEntity.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate), targetValue);
    } else
      targetEntity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate), targetValue);
  }

  public Object createEntity(String type) {
    TTEntityJava target = new TTEntityJava();
    target.addType(TTIriRefExtensionsKt.iri(new TTIriRef(), type));

    return target;
  }

  @Override
  public Object getPropertyValue(Object source, String property) {
    return null;
  }

  @Override
  public boolean isCollection(Object source) {
    return source instanceof TTArrayJava;
  }

  @Override
  public Object convertToTarget(Object from) {
    if (from instanceof Map<?, ?> fromMap) {
      TTNodeJava result = new TTNodeJava();
      for (Map.Entry<?, ?> entry : fromMap.entrySet()) {
        String key = (String) entry.getKey();
        if (!key.contains(":"))
          key = NamespaceVocab.
            IM + key;
        Object value = convertToTargetSingle(entry.getValue());
        if (value instanceof TTArrayJava valueTTArrayJava)
          result.set(TTIriRefExtensionsKt.iri(new TTIriRef(), key), valueTTArrayJava);
        else if (value instanceof TTValueJava valueTTValueJava) {
          result.set(TTIriRefExtensionsKt.iri(new TTIriRef(), key), valueTTValueJava);
        } else
          throw new IllegalArgumentException("Unknown sub node type in target map " + value.getClass().getSimpleName());
      }
      return result;
    } else if (from instanceof Collection<?> fromCollection) {
      TTArrayJava result = new TTArrayJava();
      for (Object value : fromCollection) {
        result.add((TTValueJava) convertToTarget(value));
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
        return TTLiteralJava.literal(fromString);
      else if (from instanceof Number fromNumber)
        return TTLiteralJava.literal(fromNumber);
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
        ((TTNodeJava) targetEntity).setIri(((TTLiteralJava) targetValue).getValue());
      else {
        String predicate = property;
        if (!property.contains(":"))
          predicate = NamespaceVocab.
            IM + property;
        switch (targetValue) {
          case List<?> targetValueList -> {
            TTArrayJava array = new TTArrayJava();
            for (Object item : targetValueList) {
              array.add((TTValueJava) convertToTargetSingle(item));
            }
            ((TTNodeJava) targetEntity).set(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate), array);
          }
          case TTArrayJava targetValueTTArrayJava ->
            ((TTNodeJava) targetEntity).set(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate), targetValueTTArrayJava);
          case TTEntityJava targetValueTTEntity ->
            setPropertyValueTTEntity(rule, (TTNodeJava) targetEntity, targetValueTTEntity, predicate);
          case TTValueJava targetValueTTValueJava ->
            setPropertyValueTTValue(rule, (TTNodeJava) targetEntity, targetValueTTValueJava, predicate);
          case null, default ->
            ((TTNodeJava) targetEntity).set(TTIriRefExtensionsKt.iri(new TTIriRef(), predicate), TTLiteralJava.literal(targetValue));
        }
      }
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Value of property : " + property + " cannot be set as its class is invalid (" + targetValue.getClass().getSimpleName() + ")");
    }
  }


}
