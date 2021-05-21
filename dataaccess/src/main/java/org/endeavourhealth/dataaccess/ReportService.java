package org.endeavourhealth.dataaccess;


import org.endeavourhealth.dataaccess.repository.SimpleCountRepository;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.model.report.SimpleCountCache;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Qualifier("ReportService")
public class ReportService {

    private interface ReportServiceLambda {
        List<SimpleCount> execute() throws Exception;
    }

    static final Map<String, SimpleCountCache> cache = new HashMap<>();

    SimpleCountRepository simpleCountRepository = new SimpleCountRepository();

    private List<SimpleCount> getAndCacheReport(String reportType, ReportServiceLambda serviceCall) throws Exception {
        SimpleCountCache report = cache.get(reportType);
        if(report!=null&&!report.cacheOlderThan(30L)){
            return report.getData();
        }
        List<SimpleCount> list = serviceCall.execute();

        cache.put(reportType,new SimpleCountCache(list));
        cache.get(reportType).setDateTime(LocalDateTime.now());
        return list;
    }

    public List<SimpleCount> getConceptTypeReport() throws Exception {
      return  getAndCacheReport("Concept Type", () -> simpleCountRepository.getConceptTypeReport());
    }

    public List<SimpleCount> getConceptSchemeReport() throws Exception {
        return  getAndCacheReport("Concept Scheme", () -> simpleCountRepository.getConceptSchemeReport());
    }

    public List<SimpleCount> getConceptStatusReport() throws Exception {
        return  getAndCacheReport("Concept Status", () -> simpleCountRepository.getConceptStatusReport());
    }

    public List<SimpleCount> getConceptCategoryReport() throws Exception {
       return getAndCacheReport("Concept Category", this::getConceptCategoryReportData);
    }

    private List<SimpleCount> getConceptCategoryReportData() throws Exception {
        List<SimpleCount> result = new ArrayList<>();
        int dmCount = 0;
        int ontCount = 0;
        int vsCount =0;
        for (SimpleCount t : getConceptTypeReport()) {
            if(IM.VALUESET.getIri().equals(t.getIri()) ||
            IM.SET.getIri().equals(t.getIri())){
                vsCount = t.getCount();
            }else if(OWL.OBJECTPROPERTY.getIri().equals(t.getIri()) ||
                OWL.DATAPROPERTY.getIri().equals(t.getIri()) ||
                IM.RECORD.getIri().equals(t.getIri())){
                dmCount = dmCount + t.getCount();
            }else {
                ontCount = ontCount + t.getCount();
            }
        }

        result.add(new SimpleCount("Value sets",vsCount));
        result.add(new SimpleCount("Data models",dmCount));
        result.add(new SimpleCount("Ontology",ontCount));
        result.add(new SimpleCount("Total",vsCount+dmCount+ontCount));
        return result;
    }
}
