package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.model.dto.FieldDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/class")
@CrossOrigin(origins = "*")
@Tag(name = "ClassController")
@RequestScope
public class ClassController {
    private static final Logger LOG = LoggerFactory.getLogger(ClassController.class);

    @GetMapping(value = "/public/classProperties")
    public ResponseEntity<List<Field>> getIMClass(@RequestParam("className") String className) {
        LOG.debug("getClass");
        Class<?> clazz;
        try {
            clazz = ClassLoader.getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException classNotFoundException) {
            return ResponseEntity.notFound().build();
        }
        List<Field> fields = new ArrayList<Field>(List.of(clazz.getDeclaredFields()));
        getDeclaredFieldsRecursively(clazz, fields);
        Collections.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(final Field object1, final Field object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });
        return ResponseEntity.ok().body(fields);
    }

    @GetMapping(value = "/public/classFields")
    public ResponseEntity<List<FieldDto>> getClassFields(@RequestParam("className") String className) {
        LOG.debug("getClass");
        Class<?> clazz;
        try {
            clazz = ClassLoader.getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException classNotFoundException) {
            return ResponseEntity.notFound().build();
        }
        List<Field> fields = new ArrayList<Field>(List.of(clazz.getDeclaredFields()));
        getDeclaredFieldsRecursively(clazz, fields);
        Collections.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(final Field object1, final Field object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });
        List<FieldDto> fieldDtos = fields.stream().map(field -> {
            String firstType = field.getGenericType().getTypeName();
            String secondType = "";
            String fullName = field.getGenericType().getTypeName();
            if(fullName.contains("<")) {
                String[] nameParts = fullName.split("<");
                firstType = nameParts[1].replaceFirst(">", "");
                secondType = nameParts[0];
            }
            return new FieldDto(field.getName(), firstType, secondType);

        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(fieldDtos);
    }

    public void getDeclaredFieldsRecursively(Class<?> clazz, List<Field> fields) {
        Class<?> superclass = clazz.getSuperclass();
        if (null != superclass) {
            fields.addAll(List.of(superclass.getDeclaredFields()));
            getDeclaredFieldsRecursively(superclass, fields);
        }
    }
}
