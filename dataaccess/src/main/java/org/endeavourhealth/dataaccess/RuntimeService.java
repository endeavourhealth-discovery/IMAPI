package org.endeavourhealth.dataaccess;

import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuntimeService  implements IRuntimeService {

    @Autowired
    ConceptRepository conceptRepository;

    @Override
    public String getConceptIdForSchemeCode(String scheme, String code) {
        return conceptRepository.getConceptIdForSchemeCode(scheme, code);

    }

    @Override
    public String getMappedCoreCodeForSchemeCode(String scheme, String code, boolean snomedOnly) {
        return null;
    }

    @Override
    public String getCodeForTypeTerm(String scheme, String context, String term, boolean autoCreate) {
        return null;
    }

    @Override
    public Integer getConceptDbidForSchemeCode(String scheme, String code) {
        return conceptRepository.getConceptdbidForSchemeCode(scheme, code);
    }

    @Override
    public Integer getMappedCoreConceptDbidForSchemeCode(String scheme, String code) {
        return null;
    }

    @Override
    public String getCodeForConceptDbid(Integer dbid) {

        return conceptRepository.getCodeForConceptDbid(dbid);
    }

    @Override
    public Integer getConceptDbidForTypeTerm(String type, String term, Boolean autoCreate) {
        return null;
    }

    @Override
    public Integer getMappedCoreConceptDbidForTypeTerm(String type, String term) {
        return null;
    }

    @Override
    public Boolean isInVSet(String code, String scheme, String vSet){
        return included(code, scheme, vSet) && !excluded(code, scheme, vSet);
    }

    private Boolean included(String code, String scheme, String vSet) {

        return conceptRepository.isCoreCodeSchemeIncludedInVSet(code, scheme, vSet) != null
                || conceptRepository.isLegacyCodeSchemeIncludedInVSet(code, scheme, vSet) != null;

    }

    private Boolean excluded(String code, String scheme, String vSet) {

        return conceptRepository.isCoreCodeSchemeExcludedInVSet(code, scheme, vSet) != null
                || conceptRepository.isLegacyCodeSchemeExcludedInVSet(code, scheme, vSet) != null;

    }

}

