package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.codegen.CodeGenJava;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.io.StringWriter;

@RestController
@RequestMapping("api/DataModel")
@CrossOrigin(origins = "*")
@Tag(name = "Data Model Controller")
@RequestScope
public class DataModelController {

    @GetMapping(value = "/public/generateJava")
    @Operation(
            summary = "",
            description = ""
    )
    public HttpEntity<String> generateJava() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "force-download"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"DmAutoGen.java\"");
        CodeGenJava codeGen = new CodeGenJava();
        StringWriter result = new StringWriter();
        codeGen.generate(result);
        return new HttpEntity<>(result.toString(), headers);
    }

}
