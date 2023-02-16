package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.codegen.CodeGenJava;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.zip.ZipOutputStream;

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
    public HttpEntity<byte[]> generateJava() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "zip"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"DmAutoGen.zip\"");

        CodeGenJava codeGen = new CodeGenJava();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(baos);
             ZipOutputStream result = new ZipOutputStream(bos)) {

            codeGen.generate(result);
            result.finish();
            result.flush();

            return new HttpEntity<>(baos.toByteArray(), headers);
        }
    }
}
