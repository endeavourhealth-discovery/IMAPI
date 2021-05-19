package org.endeavourhealth.controllers;

import org.endeavourhealth.dataaccess.ReportService;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/report")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("concept/type")
    public List<SimpleCount> getConceptTypeReport() throws Exception {
        return reportService.getConceptTypeReport();
    }

    @GetMapping("concept/scheme")
    public List<SimpleCount> getConceptSchemeReport() throws Exception {

        return reportService.getConceptSchemeReport();
    }

    @GetMapping("concept/status")
    public List<SimpleCount> getConceptStatusReport() throws Exception {

        return reportService.getConceptStatusReport();
    }

    @GetMapping("concept/category")
    public List<SimpleCount> getConceptCategoryReport() throws Exception {

        return reportService.getConceptCategoryReport();

    }
}
