package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.logic.service.FunctionService;
import org.endeavourhealth.imapi.model.iml.FunctionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("api/status")
@CrossOrigin(origins = "*")
@Tag(name="StatusController")
@RequestScope
public class StatusController {
    private static final Logger LOG = LoggerFactory.getLogger(StatusController.class);
    @GetMapping( "/public/healthCheck")
    public ResponseEntity<String> healthCheck() {
        LOG.debug("healthCheck");
        return ResponseEntity.ok("OK");
    }

}
