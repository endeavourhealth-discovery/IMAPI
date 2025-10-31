package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.FunctionService;
import org.endeavourhealth.imapi.model.requests.FunctionRequest;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("api/function")
@CrossOrigin(origins = "*")
@Tag(name = "FunctionController")
@RequestScope
@Slf4j
public class FunctionController {

  @PostMapping("/public/callFunction")
  @Operation(
    summary = "function",
    description = "Runs a function IM passing in the iri of the function and a list (map) parameter name/ value arguments"
  )
  public JsonNode callFunction(HttpServletRequest request, @RequestBody FunctionRequest function) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Function.CallFunction.POST")) {
      log.debug("callFunction");
      return new FunctionService().callFunction(request, function.getFunctionIri(), function.getArguments());
    }
  }
}
