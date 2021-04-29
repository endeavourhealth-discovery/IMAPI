package org.endeavourhealth.dataaccess;

import org.endeavourhealth.imapi.model.report.SimpleCount;

import java.util.List;

public interface IReportService {

    List<SimpleCount> getConceptTypeReport();
    List<SimpleCount> getConceptSchemeReport();
    List<SimpleCount> getConceptStatusReport();
    List<SimpleCount> getConceptCategoryReport();

}
