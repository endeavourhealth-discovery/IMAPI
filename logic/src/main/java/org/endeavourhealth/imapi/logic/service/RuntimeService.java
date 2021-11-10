package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.imapi.dataaccess.*;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RuntimeService {
    private static final Logger LOG = LoggerFactory.getLogger(RuntimeService.class);

    private final EntityRepository entityRepository = new EntityRepositoryImpl();
    private final EntityTripleRepository entityTripleRepository = new EntityTripleRepositoryImpl();
    private final TermCodeRepository termCodeRepository = new TermCodeRepositoryImpl();

    private Map<String, String> schemeMap ;

    ConfigService configService = new ConfigService();

    public String getEntityIdForSchemeCode(String scheme, String code) {
        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
            return "";
        return termCodeRepository.findByCodeAndScheme(code, scheme).getIri();

    }

    public String getMappedCoreCodeForSchemeCode(String scheme, String code, boolean snomedOnly) {
        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
            return "";
        ValueSetMember coreEntity = snomedOnly
                ? getMappedCoreSnomedEntity(scheme, code)
                : getMappedCoreEntity(scheme, code);
        if (coreEntity == null)
            return null;
        return coreEntity.getCode();
    }

    private ValueSetMember getMappedCoreSnomedEntity(String scheme, String code) {
        ValueSetMember entity = getMappedCoreEntity(scheme, code);
        if(entity == null)
            return null;
        if(IM.GRAPH_SNOMED.getIri().equals(entity.getEntity().getIri())){
            return entity;
        }else
            return null;
    }

    private ValueSetMember getMappedCoreEntity(String scheme, String code) {
        TTIriRef legacy = termCodeRepository.findByCodeAndScheme(code, scheme);
        Set<ValueSetMember> maps = entityTripleRepository.getObjectBySubjectAndPredicate(legacy.getIri(), IM.MATCHED_TO.getIri());
        if(maps.isEmpty())
            return  null;
        if(maps.size()>1)
            LOG.warn("Multiple maps found for scheme {} and code {}", scheme, code);
        return maps.iterator().next();
    }

    public String getCodeForTypeTerm(String scheme, String context, String term, boolean autoCreate) {
        return null;
    }

    public Integer getEntityDbidForSchemeCode(String scheme, String code) {
        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
            return null;
        return termCodeRepository.findDbidByCodeAndScheme(code, scheme);
    }

    public Integer getMappedCoreEntityDbidForSchemeCode(String scheme, String code ) {
        if(scheme==null || scheme.isEmpty() || code == null || code.isEmpty())
            return null;
        ValueSetMember coreEntity = getMappedCoreEntity(scheme,code);

        if(coreEntity==null)
            return null;

        return null;
    }

    public String getCodeForEntityDbid(Integer dbid) {
        if(dbid==null)
            return "";
        return entityRepository.findCodeByDbid(dbid);
    }

    public Integer getEntityDbidForTypeTerm(String type, String term, Boolean autoCreate) {
        return null;
    }

    public Integer getMappedCoreEntityDbidForTypeTerm(String type, String term) {
        return null;
    }

    public Boolean isInVSet(String code, String v1Scheme, String vSet) throws JsonProcessingException {
        if (code == null || code.isEmpty() || v1Scheme == null || v1Scheme.isEmpty() || vSet == null || vSet.isEmpty())
            return false;

        String scheme = getSchemeMap().get(v1Scheme);

        return included(code, scheme, vSet) && !excluded(code, scheme, vSet);
    }

    private Boolean included(String code, String scheme, String vSet) {
        return entityRepository.isCoreCodeSchemeIncludedInVSet(code, scheme, vSet) != null
                || entityRepository.isLegacyCodeSchemeIncludedInVSet(code, scheme, vSet) != null;
    }

    private Boolean excluded(String code, String scheme, String vSet) {
        return entityRepository.isCoreCodeSchemeExcludedInVSet(code, scheme, vSet) != null
                || entityRepository.isLegacyCodeSchemeExcludedInVSet(code, scheme, vSet) != null;
    }

    private Map<String, String> getSchemeMap() throws JsonProcessingException {
        if(schemeMap==null)
        {
            TypeReference<HashMap<String,String>> ref = new TypeReference<>() {
            };
            schemeMap = configService.getConfig("schemeMap", ref);
        }
        return schemeMap;
    }
}

