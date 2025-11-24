package org.endeavourhealth.imapi.utility;


import java.lang.reflect.*;
import java.util.*;

public class IriExtractor {

  private final Set<String> iris = new LinkedHashSet<>();
  private final Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<>());

  public Set<String> extractIris(Object root) {
    iris.clear();
    visited.clear();
    collect(root);
    return iris;
  }

  private void collect(Object obj) {
    if (obj == null) return;
    if (visited.contains(obj)) return;
    visited.add(obj);

    Class<?> clazz = obj.getClass();
    if (isSimpleValue(clazz)) return;

    if (obj instanceof Collection) {
      for (Object item : (Collection<?>) obj) {
        collect(item);
      }
      return;
    }

    if (clazz.isArray()) {
      int len = Array.getLength(obj);
      for (int i = 0; i < len; i++) {
        collect(Array.get(obj, i));
      }
      return;
    }

    if (obj instanceof Map<?, ?> map) {
      for (Map.Entry<?, ?> e : map.entrySet()) {
        if (e.getKey() != null && e.getKey().toString().equalsIgnoreCase("iri")) {
          if (e.getValue() instanceof String) {
            iris.add((String) e.getValue());
          }
        }
        collect(e.getKey());
        collect(e.getValue());
      }
      return;
    }

    for (Field field : getAllFields(clazz)) {
      field.setAccessible(true);
      try {
        Object value = field.get(obj);
        if (field.getName().equalsIgnoreCase("iri")) {
          if (value instanceof String) {
            iris.add((String) value);
          }
        }
        collect(value);
      } catch (IllegalAccessException ignored) {}
    }
  }

  private boolean isSimpleValue(Class<?> clazz) {
    return clazz.isPrimitive()
      || clazz.isEnum()
      || Number.class.isAssignableFrom(clazz)
      || CharSequence.class.isAssignableFrom(clazz)
      || Boolean.class == clazz
      || Character.class == clazz;
  }

  private List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    while (clazz != null && clazz != Object.class) {
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass();
    }
    return fields;
  }
}
