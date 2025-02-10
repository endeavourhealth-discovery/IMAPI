package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.logic.service.CodeGenService;
import org.endeavourhealth.imapi.model.dto.CodeGenDto;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/codeGen")
@CrossOrigin(origins = "*")
@Tag(name = "CodeGenController")
@RequestScope
public class CodeGenController {
  private static final Logger LOG = LoggerFactory.getLogger(CodeGenController.class);
  private final CodeGenService codeGenService = new CodeGenService();

  @Operation(summary = "Get a list of code templates", description = "Retrieve a list of available code templates.")
  @GetMapping(value = "/public/codeTemplates", produces = "application/json")
  public List<String> getCodeTemplateList(HttpServletRequest request) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CodeGen.CodeTemplates.GET")) {
      LOG.debug("getCodeTemplateList");
      return codeGenService.getCodeTemplateList();
    }
  }

  @Operation(summary = "Get a specific code template", description = "Retrieve a specific code template by its name.")
  @GetMapping(value = "/public/codeTemplate", produces = "application/json")
  public CodeGenDto getCodeTemplate(HttpServletRequest request,
                                    @Parameter(description = "The name of the code template to retrieve") @RequestParam("templateName") String templateName) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CodeGen.CodeTemplate.GET")) {
      LOG.debug("getCodeTemplate");
      return codeGenService.getCodeTemplate(templateName);
    }
  }

  @Operation(summary = "Update a code template", description = "Update an existing code template.")
  @PostMapping(value = "/public/codeTemplate", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateCodeTemplate(HttpServletRequest request,
                                 @Parameter(description = "The CodeGenDto object containing the details of the code template to update") @RequestBody CodeGenDto codeGenDto) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CodeGen.CodeTemplate.POST")) {
      LOG.debug("updateCodeTemplate");
      codeGenService.updateCodeTemplate(codeGenDto.getName(), codeGenDto.getExtension(), codeGenDto.getCollectionWrapper(), codeGenDto.getDatatypeMap(), codeGenDto.getTemplate());
    }
  }

  @Operation(summary = "Generate code", description = "Generate code based on the provided IRI, template name, and namespace.")
  @GetMapping(value = "/public/generateCode", produces = "application/json")
  public HttpEntity<Object> generateCode(HttpServletRequest request,
                                         @Parameter(description = "The IRI for which to generate code") @RequestParam(name = "iri", required = false) String iri,
                                         @Parameter(description = "The name of the template to use for generating code") @RequestParam("template") String templateName,
                                         @Parameter(description = "The namespace to use for generating code") @RequestParam("namespace") String namespace) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CodeGen.GenerateCode.GET")) {
      LOG.debug("GenerateCode");

      if (iri == null || iri.isEmpty())
        iri = "http://endhealth.info/im#PatientRecordEntry";

      return codeGenService.generateCode(iri, templateName, namespace);
    }
  }
}