package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import jdk.internal.org.jline.utils.Log;
import org.endeavourhealth.imapi.logic.service.ConfigService;
import org.endeavourhealth.imapi.model.config.ConfigItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

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

    @GetMapping(value = "/getConfig")
    public Object getConfig(
            @RequestParam(name="name") String name
    ) throws SQLException, JsonProcessingException {
        Log.debug("getConfig");
        return configService.getConfig(name, new TypeReference<List<ConfigItem>>(){});
    }
}
