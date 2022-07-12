package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class TTToClassObject {
	public <T> T getObject(TTEntity entity, Class<T> classType) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		Object obj = classType.newInstance();
		setField(obj,"iri",entity.getIri());
		processNode(entity, obj, classType);
		return (T) obj;
	}





	private void processNode(TTNode node,Object obj,Class<?> classType) throws InstantiationException, IllegalAccessException {
		Field[] fields = classType.getDeclaredFields();
		Map<String,Field> fieldMap = getFieldNames(fields);
		for (Map.Entry<TTIriRef, TTArray> entry:node.getPredicateMap().entrySet()) {
			if (entry.getValue() != null) {
				TTIriRef propertyIri = entry.getKey();
				String fieldName = propertyIri.getIri();
				fieldName = fieldName.substring(fieldName.lastIndexOf("#") + 1);
				Field field = fieldMap.get(fieldName);
				if (field != null) {
					field.setAccessible(true);
					Type type = field.getGenericType();
					if (type instanceof ParameterizedType) {
						ParameterizedType pt = (ParameterizedType) type;
						if (pt.getActualTypeArguments().length == 1) {
							List list = new ArrayList();
							setField(obj, fieldName, list);
							Class<?> listClazz = null;
							Type listType = pt.getActualTypeArguments()[0];
							if (listType instanceof Class) {
								listClazz = (Class<?>) listType;
							}
							for (TTValue value : entry.getValue().getElements()) {
								if (value.isNode()) {
									Object listItem = listClazz.newInstance();
									list.add(listItem);
									processNode(value.asNode(), listItem, listClazz);
								} else if (value.isIriRef()) {
									list.add(value);
								} else {
									addValue(list, value.asLiteral(), type);
								}
							}

						}
					} else {
						Class<?> clazz = null;
						if (type instanceof Class) {
							clazz = (Class<?>) type;
						}
						TTArray value = entry.getValue();
						if (value.isNode()) {
							Object item = clazz.getName();
							setField(obj, fieldName, item);
							processNode(value.asNode(), item, clazz);
						} else if (value.isIriRef()) {
							setField(obj, fieldName, value.asIriRef());
						} else {
							setValue(obj, fieldName, value.asLiteral(), type);
						}
					}

				}
			}
		}
	}

	private void setValue(Object object,String fieldName, TTLiteral value, Type type){
		if (type==String.class){
			setField(object,fieldName,value.asLiteral().getValue());
		}
		else if (type==Long.class)
			setField(object,fieldName,value.asLiteral().longValue());
		else if (type==boolean.class)
			setField(object,fieldName,value.asLiteral().booleanValue());
		else
			setField(object,fieldName,value.asLiteral().intValue());
	}

	private void addValue(List list,TTLiteral value, Type type){
		if (type==String.class){
			list.add(value.asLiteral().getValue());
		}
		else if (type==Long.class)
			list.add(value.asLiteral().longValue());
		else if (type==boolean.class)
			list.add(value.asLiteral().booleanValue());
		else
			list.add(value.asLiteral().intValue());

	}

	private boolean setField(Object object, String fieldName, Object fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(object, fieldValue);
				return true;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return false;
	}

	private Map<String, Field> getFieldNames(Field[] fields) {
		Map<String,Field> fieldMap= new HashMap<>();
		for (Field field : fields) {
			fieldMap.put(field.getName(), field);
		}
		return fieldMap;
	}
}
