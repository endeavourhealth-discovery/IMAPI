package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.CodeGenService;
import org.endeavourhealth.imapi.model.dto.CodeGenDto;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@RequestMapping("api/codeGen")
@CrossOrigin(origins = "*")
@Tag(name = "CodeGenController")
@RequestScope
@Slf4j
public class CodeGenController {
  private final CodeGenService codeGenService = new CodeGenService();

  @Operation(summary = "Get a list of code templates", description = "Retrieve a list of available code templates.")
  @GetMapping(value = "/private/codeTemplates", produces = "application/json")
  public List<String> getCodeTemplateList(HttpServletRequest request) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CodeGen.CodeTemplates.GET")) {
      log.debug("getCodeTemplateList");
      return codeGenService.getCodeTemplateList();
    }
  }

  @Operation(summary = "Get a specific code template", description = "Retrieve a specific code template by its name.")
  @GetMapping(value = "/private/codeTemplate", produces = "application/json")
  public CodeGenDto getCodeTemplate(HttpServletRequest request,
                                    @Parameter(description = "The name of the code template to retrieve") @RequestParam("templateName") String templateName) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CodeGen.CodeTemplate.GET")) {
      log.debug("getCodeTemplate");
      return codeGenService.getCodeTemplate(templateName);
    }
  }

  @Operation(summary = "Update a code template", description = "Update an existing code template.")
  @PostMapping(value = "/private/codeTemplate", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateCodeTemplate(
    HttpServletRequest request,
    @Parameter(description = "The CodeGenDto object containing the details of the code template to update") @RequestBody CodeGenDto codeGenDto
  ) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CodeGen.CodeTemplate.POST")) {
      log.debug("updateCodeTemplate");
      codeGenService.updateCodeTemplate(codeGenDto.getName(), codeGenDto.getExtension(), codeGenDto.getCollectionWrapper(), codeGenDto.getDatatypeMap(), codeGenDto.getTemplate(), codeGenDto.getComplexTypes());
    }
  }

  @Operation(summary = "Generate code", description = "Generate code based on the provided IRI, template name, and namespace.")
  @GetMapping(value = "/private/generateCode", produces = "application/json")
  public HttpEntity<Object> generateCode(
    HttpServletRequest request,
    @Parameter(description = "The IRI for which to generate code") @RequestParam(name = "iri", required = false) String iri,
    @Parameter(description = "The name of the template to use for generating code") @RequestParam("template") String templateName,
    @Parameter(description = "The namespace to use for generating code") @RequestParam("namespace") String namespace
  ) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CodeGen.GenerateCode.GET")) {
      log.debug("GenerateCode");
      return codeGenService.generateCode(iri, templateName, namespace);
    }
  }

  @Operation(summary = "Generate code preview", description = "Generate code based on the provided IRI, template name, and namespace.")
  @PostMapping(value = "/private/generateCodePreview", produces = "text/plain")
  public String generateCodePreview(HttpServletRequest request,
                                    @Parameter(description = "The IRI for which to generate code") @RequestParam(name = "iri", required = false) String iri,
                                    @Parameter(description = "The namespace to use for generating code") @RequestParam("namespace") String namespace,
                                    @Parameter(description = "The template data to use") @RequestBody CodeGenDto template
  ) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CodeGen.GenerateCode.GET")) {
      log.debug("GenerateCodePreview");
      return codeGenService.generateCodeForModel(iri, template, namespace);
    }
  }
}