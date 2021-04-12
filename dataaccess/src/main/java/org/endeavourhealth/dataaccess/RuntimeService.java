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
    public Boolean checkConceptByCodeSchemeInVSet(String code, String scheme, String vSet) {
        Concept concept = conceptRepository.findByCodeAndScheme(code, scheme);
        if (concept.getIri().contains("snomed")) {
            if (conceptRepository.isCoreCodeSchemeIncludedInVSet(code, scheme, vSet) == null)
                return false;
            if (conceptRepository.isCoreCodeSchemeExcludedInVSet(code, scheme, vSet) == null)
                return true;
            else
                return false;
        }
        else{
            if (conceptRepository.isLegacyCodeSchemeIncludedInVSet(code, scheme, vSet) == null)
                return false;
            if (conceptRepository.isLegacyCodeSchemeExcludedInVSet(code, scheme, vSet) == null)
                return true;
            else
                return false;
        }
    }
}

