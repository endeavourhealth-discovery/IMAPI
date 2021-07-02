package org.endeavourhealth.imapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.endeavourhealth.imapi.logic.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/config")
@CrossOrigin(origins = "*")
@Api(value="ConfigController")
@SwaggerDefinition(tags = {
    @Tag(name = "Config Controller", description = "IM application configuration endpoint")
})
public class ConfigController {
    @Autowired
    ConfigService configService;
}
