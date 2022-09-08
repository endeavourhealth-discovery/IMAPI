package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("api/class")
@CrossOrigin(origins = "*")
@Tag(name = "ClassController")
@RequestScope
public class ClassController {
    private static final Logger LOG = LoggerFactory.getLogger(ClassController.class);

    @GetMapping(value = "/public/classProperties")
    public Object getIMClass(@RequestParam String className) {
        LOG.debug("getClass");
        Class<?> clazz;
        try {
            clazz = ClassLoader.getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException classNotFoundException) {
            return ResponseEntity.notFound().build();
        }
        return clazz.getDeclaredFields();
    }
}
