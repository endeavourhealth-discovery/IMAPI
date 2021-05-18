package org.endeavourhealth.dataaccess;


import org.endeavourhealth.dataaccess.repository.SimpleCountRepository;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.report.SimpleCountCache;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier("ReportService")
public class ReportService {

    private interface ReportServiceLambda {
        List<SimpleCount> execute();
    }

    private static final Map<String, SimpleCountCache>  cache = new HashMap<>();

    @Autowired
    SimpleCountRepository simpleCountRepository;

    private List<SimpleCount> getAndCacheReport(String reportType, ReportServiceLambda serviceCall) {
        SimpleCountCache report = cache.get(reportType);
        if(report!=null&&!report.cacheOlderThan(30L)){
            return report.getData();
        }
        List<SimpleCount> list = serviceCall.execute();

        cache.put(reportType,new SimpleCountCache(list));
        cache.get(reportType).setDateTime(LocalDateTime.now());
        return list;
    }

    public List<SimpleCount> getConceptTypeReportSring() {
        List<SimpleCount> result;

        result = simpleCountRepository.getConceptTypeReport()
            .stream()
            .map(sc -> new SimpleCount(sc.getLabel(), sc.getCount()))
            .collect(Collectors.toList());

        return result;
    }

    public List<SimpleCount> getConceptTypeReportNative() throws SQLException {
        List<SimpleCount> result = new ArrayList<>();
        try (Connection conn = ConnectionPool.get()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT t.dbid, t.iri as iri, t.name as label, count(1) as `count`\n" +
                "FROM concept c \n" +
                "JOIN concept_type ct ON ct.concept = c.dbid \n" +
                "JOIN concept t on t.iri = ct.type \n" +
                "GROUP BY t.dbid , t.iri , t.name \n" +
                "ORDER BY `count` DESC  ")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while(rs.next())
                        result.add(new SimpleCount(rs.getString("label"), rs.getInt("count")));
                }
            }
        }
        return result;
    }

    public List<SimpleCount> getConceptTypeReport() {
      return  getAndCacheReport("Concept Type", () -> simpleCountRepository.getConceptTypeReport()
              .stream()
              .map(sc -> new SimpleCount(sc.getLabel(), sc.getCount()))
              .collect(Collectors.toList()));
    }

    public List<SimpleCount> getConceptSchemeReport() {
        return  getAndCacheReport("Concept Scheme", () -> simpleCountRepository.getConceptSchemeReport().stream()
                .map(sc -> new SimpleCount(sc.getLabel(), sc.getCount()))
                .collect(Collectors.toList()));
    }

    public List<SimpleCount> getConceptStatusReport() {
        return  getAndCacheReport("Concept Status", () -> simpleCountRepository.getConceptStatusReport().stream()
                .map(sc -> new SimpleCount(sc.getLabel(), sc.getCount()))
                .collect(Collectors.toList()));
    }

    public List<SimpleCount> getConceptCategoryReport() {
       return getAndCacheReport("Concept Category", this::getConceptCategoryReportData);
    }

    private List<SimpleCount> getConceptCategoryReportData() {
        List<SimpleCount> result = new ArrayList<>();
        int dmCount = 0;
        int ontCount = 0;
        int vsCount =0;
        for (org.endeavourhealth.dataaccess.entity.SimpleCount t : simpleCountRepository.getConceptTypeReport()) {
            if(IM.VALUESET.getIri().equals(t.getIri())){
                vsCount = t.getCount();
            }else if(OWL.OBJECTPROPERTY.getIri().equals(t.getIri()) ||
                OWL.DATAPROPERTY.getIri().equals(t.getIri()) ||
                IM.RECORD.getIri().equals(t.getIri())){
                dmCount = dmCount + t.getCount();
            }else {
                ontCount = ontCount + t.getCount();
            }
        }

        result.add(new SimpleCount("Value Sets",vsCount));
        result.add(new SimpleCount("Data Models",dmCount));
        result.add(new SimpleCount("Ontology",ontCount));
        result.add(new SimpleCount("Total",vsCount+dmCount+ontCount));
        return result;
    }
}
