package org.endeavourhealth.imapi.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ABNFGenerator {
	private StringBuilder table = new StringBuilder();
	public <T> void generateTable(Class<T> classType) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, JsonProcessingException {
		table.append("{| class=\"wikitable\"\n" +
			"|+\n" +
			"!Rule\n" +
			"!Subrules or lexical pattern\n" +
			"!Explanation\n" +
			"|-\n");
		processClass(classType,classType.getSimpleName());
		table.append("\n|}");
		System.out.println(table.toString());
	}
	private <T> void processClass(Class<T> classType,String name) throws InstantiationException, IllegalAccessException {
		table.append("|").append("<span id=\"rule_").append(name).append("\">").append(name).append("</span>");
		table.append("\n|");
		List<Field> fields= getAllFields(classType);
	  for (Field field:fields){
			field.setAccessible(true);
			String fieldName= field.getName();
			Type type = field.getGenericType();
			String cardinality="";
			Class<?> clazz;
			if (type instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) type;
				if (pt.getActualTypeArguments().length == 1) {
					Type listType = pt.getActualTypeArguments()[0];
					clazz = (Class<?>) listType;
					cardinality="*";
				}
			}
			else
				clazz= (Class<?>) type;
			table.append("[[#rule_").append(fieldName).append("|").append(fieldName)
				.append(cardinality).append("]]   ");
		}
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
}
