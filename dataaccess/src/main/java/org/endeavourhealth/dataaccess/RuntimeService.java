package org.endeavourhealth.dataaccess;

import org.endeavourhealth.dataaccess.entity.Concept;
import org.endeavourhealth.dataaccess.entity.Tpl;
import org.endeavourhealth.dataaccess.repository.ConceptRepository;
import org.endeavourhealth.dataaccess.repository.ConceptTripleRepository;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RuntimeService  implements IRuntimeService {
    private static final Logger LOG = LoggerFactory.getLogger(RuntimeService.class);

    @Autowired
    ConceptRepository conceptRepository;

    @Autowired
    ConceptTripleRepository conceptTripleRepository;

    @Override
    public String getConceptIdForSchemeCode(String scheme, String code) {
        return conceptRepository.findByCodeAndScheme(scheme, code).getIri();

    }

    @Override
    public String getMappedCoreCodeForSchemeCode(String scheme, String code, boolean snomedOnly) {
        Concept coreConcept = snomedOnly
                ? getMappedCoreSnomedConcept(scheme, code)
                : getMappedCoreConcept(scheme, code);
        if (coreConcept == null)
            return null;
        return coreConcept.getCode();
    }

    private Concept getMappedCoreSnomedConcept(String scheme, String code) {
        Concept concept = getMappedCoreConcept(scheme, code);
        if(concept == null)
            return null;
        if(IM.CODE_SCHEME_SNOMED.getIri().equals(concept.getIri())){
            return concept;
        }else
            return null;
    }

    @Override
    public String getCodeForTypeTerm(String scheme, String context, String term, boolean autoCreate) {
        return null;
    }

    @Override
    public Integer getConceptDbidForSchemeCode(String scheme, String code) {
        return conceptRepository.findByCodeAndScheme(scheme, code).getDbid();
    }

    @Override
    public Integer getMappedCoreConceptDbidForSchemeCode(String scheme, String code ) {
        Concept coreConcept = getMappedCoreConcept(scheme,code);
        if(coreConcept==null)
            return null;
        return  coreConcept.getDbid();
    }

    @Override
    public String getCodeForConceptDbid(Integer dbid) {

        return conceptRepository.findByDbid(dbid).getCode();
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

    private Concept getMappedCoreConcept(String scheme, String code){
        Concept legacy = conceptRepository.findByCodeAndScheme(code,scheme);
        Set<Tpl> maps = conceptTripleRepository.findAllBySubject_Iri_AndPredicate_Iri(legacy.getIri(), IM.MATCHED_TO.getIri());
        if(maps.size()==0)
            return  null;
        if(maps.size()>1)
            LOG.warn("Multiple maps found for scheme "+ scheme + " and code "+ code);
        Tpl map = maps.stream().findFirst().get();
        return map.getObject();
    }

    private Boolean snomedOnlyFalse(Boolean snomedOnly, String iri){
        return snomedOnly && !IM.CODE_SCHEME_SNOMED.getIri().equals(iri);
    }

}

