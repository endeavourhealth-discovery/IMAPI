package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.logic.service.CodeGenService;
import org.endeavourhealth.imapi.model.dto.CodeGenDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@RequestMapping("api/codeGen")
@CrossOrigin(origins = "*")
@Tag(name = "CodeGenController")
@RequestScope
public class CodeGenController {
    private static final Logger LOG = LoggerFactory.getLogger(CodeGenController.class);
    private final CodeGenService codeGenService = new CodeGenService();

    @GetMapping(value = "/codeTemplates", produces = "application/json")
    public List<String> getCodeTemplateList(HttpServletRequest request) throws JsonProcessingException {
        LOG.debug("getCodeTemplateList");
        return codeGenService.getCodeTemplateList();
    }

    @GetMapping(value = "/codeTemplate", produces = "application/json")
    public CodeGenDto getCodeTemplate(HttpServletRequest request, @RequestParam("templateName") String templateName) throws JsonProcessingException {
        LOG.debug("getCodeTemplate");
        return codeGenService.getCodeTemplate(templateName);
    }

    @PostMapping(value = "/codeTemplate", produces = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void saveCodeTemplate(HttpServletRequest request, @RequestBody CodeGenDto codeGenDto) throws JsonProcessingException {
        LOG.debug("saveCodeTemplate");
        codeGenService.saveCodeTemplate(codeGenDto.getName(), codeGenDto.getExtension(), codeGenDto.getCollectionWrapper(), codeGenDto.getDatatypeMap(), codeGenDto.getTemplate());
    }
}
