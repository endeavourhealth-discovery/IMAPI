package org.endeavourhealth.dataaccess;


import org.endeavourhealth.dataaccess.repository.SimpleCountRepository;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("ReportService")
public class ReportService implements IReportService{

    @Autowired
    SimpleCountRepository simpleCountRepository;

    @Override
    public List<SimpleCount> getConceptTypeReport() {
        List<SimpleCount> list = new ArrayList<>();
        for (org.endeavourhealth.dataaccess.entity.SimpleCount t : simpleCountRepository.getConceptTypeReport()) {
            SimpleCount simpleCount = new SimpleCount(t.getLabel(), t.getCount());
            list.add(simpleCount);
        }
        return list;
    }

    @Override
    public List<SimpleCount> getConceptSchemeReport() {

        return simpleCountRepository.getConceptSchemeReport()
            .stream()
            .map(s -> new SimpleCount(s.getLabel(),s.getCount()))
            .collect(Collectors.toList());
    }

    @Override
    public List<SimpleCount> getConceptStatusReport() {

        return simpleCountRepository.getConceptStatusReport()
            .stream()
            .map(s -> new SimpleCount(s.getLabel(),s.getCount()))
            .collect(Collectors.toList());
    }

    @Override
    public List<SimpleCount> getConceptCategoryReport() {
        List<SimpleCount> typeList= getConceptTypeReport();
        List<SimpleCount> result = new ArrayList<>();

        int dmCount = 0;
        int ontCount = 0;
        int vsCount =0;
        for (SimpleCount t:typeList){
            if(IM.VALUESET.getIri().equals(t.getLabel())){
                vsCount = t.getCount();
            }else if(OWL.OBJECTPROPERTY.getIri().equals(t.getLabel()) ||
                OWL.DATAPROPERTY.getIri().equals(t.getLabel()) ||
                IM.RECORD.getIri().equals(t.getLabel())){
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
