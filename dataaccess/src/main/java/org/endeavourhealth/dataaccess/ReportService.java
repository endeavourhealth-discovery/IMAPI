package org.endeavourhealth.dataaccess;


import org.endeavourhealth.dataaccess.repository.SimpleCountRepository;
import org.endeavourhealth.imapi.model.report.SimpleCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("ReportService")
public class ReportService implements IReportService{

    @Autowired
    SimpleCountRepository simpleCountRepository;

    @Override
    public List<SimpleCount> getConceptTypeReport() {
         return simpleCountRepository.getConceptTypeReport()
             .stream()
             .map(t -> new SimpleCount(t.getLabel(),t.getCount()))
             .collect(Collectors.toList());
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
}
