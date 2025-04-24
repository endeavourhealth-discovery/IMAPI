package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class TTToClassObject {
  public <T> T getObject(TTEntity entity, Class<T> classType) throws InstantiationException, IllegalAccessException, JsonProcessingException, NoSuchMethodException, InvocationTargetException {
    T obj = classType.getDeclaredConstructor().newInstance();
    setField(obj, "iri", entity.getIri());
    processNode(entity, obj, classType);
    return obj;
  }

  private List<Field> getAllFields(Class clazz) {
    if (clazz == null) {
      return Collections.emptyList();
    }

    List<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
    List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields())
      .collect(Collectors.toList());
    result.addAll(filteredFields);
    return result;
  }

  private void processNode(TTNode node, Object obj, Class<?> classType) throws InstantiationException, IllegalAccessException, JsonProcessingException, InvocationTargetException, NoSuchMethodException {
    List<Field> fields = getAllFields(classType);
    Map<String, Field> fieldMap = getFieldNames(fields);
    for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
      if (entry.getValue() != null) {
        TTIriRef propertyIri = entry.getKey();
        String fieldName = propertyIri.getIri();
        fieldName = fieldName.substring(fieldName.lastIndexOf("#") + 1);
        Field field = fieldMap.get(fieldName);
        if (field != null) {
          field.setAccessible(true);
          Type type = field.getGenericType();
          if (type instanceof ParameterizedType) {
            processNodeParameterizedType(obj, entry, fieldName, (ParameterizedType) type);
          } else {
            processNodeOtherType(obj, entry, fieldName, type);
          }

        }
      }
    }
  }

  private void processNodeParameterizedType(Object obj, Map.Entry<TTIriRef, TTArray> entry, String fieldName, ParameterizedType pt) throws InstantiationException, IllegalAccessException, JsonProcessingException, NoSuchMethodException, InvocationTargetException {
    if (1 != pt.getActualTypeArguments().length) {
      return;
    }
    List<Object> list = new ArrayList<>();
    setField(obj, fieldName, list);
    Class<?> listClazz = null;
    Type listType = pt.getActualTypeArguments()[0];
    if (listType instanceof Class) {
      listClazz = (Class<?>) listType;
    }
    for (TTValue value : entry.getValue().getElements()) {
      if (value.isNode()) {
        if (listClazz == null)
          throw new InstantiationException("unable to converted entity due to class mismatch");
        Object listItem = listClazz.getDeclaredConstructor().newInstance();
        list.add(listItem);
        processNode(value.asNode(), listItem, listClazz);
      } else if (value.isIriRef()) {
        list.add(value);
      } else {
        if (value.asLiteral().getValue().startsWith("{")) {
          list.add(jsonNodeFromLiteral(value.asLiteral().getValue(), listClazz));
        } else
          addValue(list, value.asLiteral(), pt);
      }
    }
  }

  private void processNodeOtherType(Object obj, Map.Entry<TTIriRef, TTArray> entry, String fieldName, Type type) throws InstantiationException, IllegalAccessException, JsonProcessingException, InvocationTargetException, NoSuchMethodException {
    Class<?> clazz = null;
    if (type instanceof Class) {
      clazz = (Class<?>) type;
    }
    TTArray value = entry.getValue();
    if (value.isNode()) {
      if (clazz == null)
        throw new InstantiationException("UJnable to parse entity due to class mismatch");
      String item = clazz.getName();
      setField(obj, fieldName, item);
      processNode(value.asNode(), item, clazz);
    } else if (value.isIriRef()) {
      setField(obj, fieldName, value.asIriRef());
    } else {
      if (value.asLiteral().getValue().startsWith("{")) {
        setField(obj, fieldName, jsonNodeFromLiteral(value.asLiteral().getValue(), clazz));
      } else
        setValue(obj, fieldName, value.asLiteral(), type);
    }
  }

  private Object jsonNodeFromLiteral(String json, Class<?> classType) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      return om.readValue(json, classType);
    }
  }

  private void setValue(Object object, String fieldName, TTLiteral value, Type type) {

    if (type == String.class) {
      setField(object, fieldName, value.asLiteral().getValue());
    } else if (type == Long.class)
      setField(object, fieldName, value.asLiteral().longValue());
    else if (type == Boolean.class)
      setField(object, fieldName, value.asLiteral().booleanValue());
    else
      setField(object, fieldName, value.asLiteral().intValue());
  }

  private void addValue(List<Object> list, TTLiteral value, Type type) {

    if (type == String.class) {
      list.add(value.asLiteral().getValue());
    } else if (type == Long.class)
      list.add(value.asLiteral().longValue());
    else if (type == boolean.class)
      list.add(value.asLiteral().booleanValue());
    else
      list.add(value.asLiteral().intValue());

  }

  private void setField(Object object, String fieldName, Object fieldValue) {
    Class<?> clazz = object.getClass();
    while (clazz != null) {
      try {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, fieldValue);
        return;
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
  }

  private Map<String, Field> getFieldNames(List<Field> fields) {
    Map<String, Field> fieldMap = new HashMap<>();
    for (Field field : fields) {
      fieldMap.put(field.getName(), field);
    }
    return fieldMap;
  }
}
