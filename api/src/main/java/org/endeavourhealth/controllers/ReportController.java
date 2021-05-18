package org.endeavourhealth.controllers;

import org.endeavourhealth.dataaccess.ReportService;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.tripletree.TTInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/report")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("concept/type")
    public List<SimpleCount> getConceptTypeReport() throws SQLException {
        long startTime = System.nanoTime();
        for (int i = 1; i < 10; i++) {
            List<SimpleCount> instance = reportService.getConceptTypeReportSring();
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("Spring " + duration / 1000000);

        startTime = System.nanoTime();
        for (int i = 1; i < 10; i++) {
            List<SimpleCount> instance = reportService.getConceptTypeReportNative();
        }
        endTime = System.nanoTime();

        duration = (endTime - startTime);
        System.out.println("Native " + duration / 1000000);

        return reportService.getConceptTypeReport();
    }

    @GetMapping("concept/scheme")
    public List<SimpleCount> getConceptSchemeReport() {

        return reportService.getConceptSchemeReport();
    }

    @GetMapping("concept/status")
    public List<SimpleCount> getConceptStatusReport() {

        return reportService.getConceptStatusReport();
    }

    @GetMapping("concept/category")
    public List<SimpleCount> getConceptCategoryReport() {

        return reportService.getConceptCategoryReport();

    }
}
