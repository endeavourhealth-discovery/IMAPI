package org.endeavourhealth.dataaccess;


import org.endeavourhealth.dataaccess.repository.SimpleCountRepository;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.report.SimpleCountCache;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier("ReportService")
public class ReportService implements IReportService{

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

    @Override
    public List<SimpleCount> getConceptTypeReport() {
      return  getAndCacheReport("Concept Type", () -> simpleCountRepository.getConceptTypeReport()
              .stream()
              .map(sc -> new SimpleCount(sc.getLabel(), sc.getCount()))
              .collect(Collectors.toList()));
    }

    @Override
    public List<SimpleCount> getConceptSchemeReport() {
        return  getAndCacheReport("Concept Scheme", () -> simpleCountRepository.getConceptSchemeReport().stream()
                .map(sc -> new SimpleCount(sc.getLabel(), sc.getCount()))
                .collect(Collectors.toList()));
    }

    @Override
    public List<SimpleCount> getConceptStatusReport() {
        return  getAndCacheReport("Concept Status", () -> simpleCountRepository.getConceptStatusReport().stream()
                .map(sc -> new SimpleCount(sc.getLabel(), sc.getCount()))
                .collect(Collectors.toList()));
    }

    @Override
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
