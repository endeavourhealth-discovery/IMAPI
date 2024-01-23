package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.model.codegen.DataModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/DataModel")
@CrossOrigin(origins = "*")
@Tag(name = "Data Model Controller")
@RequestScope
public class DataModelController {

    @GetMapping(value = "/public/getModels")
    @Operation(
            summary = "",
            description = ""
    )
    public List<DataModel> getModels(List<String> iris) {
        List<DataModel> result = new ArrayList<>();



        return result;
    }
}
